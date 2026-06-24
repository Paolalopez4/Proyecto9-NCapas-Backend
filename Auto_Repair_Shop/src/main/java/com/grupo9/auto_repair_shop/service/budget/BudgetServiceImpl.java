package com.grupo9.auto_repair_shop.service.budget;

import com.grupo9.auto_repair_shop.dto.request.budget.BudgetItemRequest;
import com.grupo9.auto_repair_shop.dto.request.budget.BudgetRequest;
import com.grupo9.auto_repair_shop.dto.response.budget.BudgetItemResponse;
import com.grupo9.auto_repair_shop.dto.response.budget.BudgetResponse;
import com.grupo9.auto_repair_shop.entity.budget.Budget;
import com.grupo9.auto_repair_shop.entity.budget.BudgetItem;
import com.grupo9.auto_repair_shop.entity.workorder.WorkOrder;
import com.grupo9.auto_repair_shop.enums.BudgetStatus;
import com.grupo9.auto_repair_shop.enums.WorkOrderStatus;
import com.grupo9.auto_repair_shop.exception.BusinessRuleException;
import com.grupo9.auto_repair_shop.exception.ConflictException;
import com.grupo9.auto_repair_shop.exception.ForbiddenException;
import com.grupo9.auto_repair_shop.exception.ResourceNotFoundException;
import com.grupo9.auto_repair_shop.mapper.budget.BudgetItemMapper;
import com.grupo9.auto_repair_shop.mapper.budget.BudgetMapper;
import com.grupo9.auto_repair_shop.repository.budget.BudgetItemRepository;
import com.grupo9.auto_repair_shop.repository.budget.BudgetRepository;
import com.grupo9.auto_repair_shop.repository.workorder.WorkOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class BudgetServiceImpl implements BudgetService {

    private final BudgetRepository budgetRepository;
    private final BudgetItemRepository budgetItemRepository;
    private final WorkOrderRepository workOrderRepository;
    private final BudgetMapper budgetMapper;
    private final BudgetItemMapper budgetItemMapper;

    @Override
    public BudgetResponse create(UUID workOrderId, BudgetRequest request) {

        WorkOrder workOrder = workOrderRepository.findById(workOrderId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Orden de trabajo no encontrada con id: " + workOrderId
                ));

        if (workOrder.getStatus() != WorkOrderStatus.IN_PROGRESS) {
            throw new ConflictException(
                    "Solo se puede crear un presupuesto si la orden está en estado IN_PROGRESS"
            );
        }

        if (budgetRepository.existsByWorkOrderId(workOrderId)) {
            throw new ConflictException(
                    "Esta orden de trabajo ya tiene un presupuesto asociado"
            );
        }

        Budget budget = Budget.builder()
                .workOrder(workOrder)
                .taxRate(request.getTaxRate())
                .total(BigDecimal.ZERO)
                .status(BudgetStatus.PENDING)
                .build();

        Budget saved = budgetRepository.save(budget);

        return budgetMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public BudgetResponse findByWorkOrder(UUID workOrderId) {

        Budget budget = budgetRepository.findByWorkOrderId(workOrderId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Presupuesto no encontrado para la orden: " + workOrderId
                ));

        return budgetMapper.toResponse(budget);
    }

    @Override
    public BudgetResponse update(UUID workOrderId, BudgetRequest request) {

        Budget budget = budgetRepository.findByWorkOrderId(workOrderId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Presupuesto no encontrado para la orden: " + workOrderId
                ));

        budget.setTaxRate(request.getTaxRate());
        recalculateTotal(budget);

        Budget updated = budgetRepository.save(budget);

        return budgetMapper.toResponse(updated);
    }

    @Override
    public BudgetResponse send(UUID workOrderId) {

        Budget budget = budgetRepository.findByWorkOrderId(workOrderId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Presupuesto no encontrado para la orden: " + workOrderId
                ));

        if (budget.getItems() == null || budget.getItems().isEmpty()) {
            throw new BusinessRuleException(
                    "El presupuesto debe tener al menos un ítem antes de enviarse"
            );
        }

        budget.setSentAt(LocalDateTime.now());

        Budget updated = budgetRepository.save(budget);

        return budgetMapper.toResponse(updated);
    }

    @Override
    public BudgetResponse approve(UUID workOrderId, String clientEmail) {

        Budget budget = budgetRepository.findByWorkOrderId(workOrderId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Presupuesto no encontrado para la orden: " + workOrderId
                ));

        validateClientOwnership(budget, clientEmail);

        if (budget.getStatus() != BudgetStatus.PENDING) {
            throw new ConflictException(
                    "Solo se puede aprobar un presupuesto en estado PENDING"
            );
        }

        budget.setStatus(BudgetStatus.APPROVED);
        budget.setRespondedAt(LocalDateTime.now());

        Budget updated = budgetRepository.save(budget);

        return budgetMapper.toResponse(updated);
    }

    @Override
    public BudgetResponse reject(UUID workOrderId, String clientEmail) {

        Budget budget = budgetRepository.findByWorkOrderId(workOrderId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Presupuesto no encontrado para la orden: " + workOrderId
                ));

        validateClientOwnership(budget, clientEmail);

        if (budget.getStatus() != BudgetStatus.PENDING) {
            throw new ConflictException(
                    "Solo se puede rechazar un presupuesto en estado PENDING"
            );
        }

        budget.setStatus(BudgetStatus.REJECTED);
        budget.setRespondedAt(LocalDateTime.now());

        Budget updated = budgetRepository.save(budget);

        return budgetMapper.toResponse(updated);
    }

    @Override
    public BudgetItemResponse addItem(UUID workOrderId, BudgetItemRequest request) {

        Budget budget = budgetRepository.findByWorkOrderId(workOrderId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Presupuesto no encontrado para la orden: " + workOrderId
                ));

        if (budget.getSentAt() != null) {
            throw new BusinessRuleException(
                    "No se pueden modificar ítems de un presupuesto ya enviado"
            );
        }

        BudgetItem item = BudgetItem.builder()
                .budget(budget)
                .itemType(request.getItemType())
                .description(request.getDescription())
                .quantity(request.getQuantity())
                .unitPrice(request.getUnitPrice())
                .discount(request.getDiscount())
                .build();

        BudgetItem saved = budgetItemRepository.save(item);

        recalculateTotal(budget);
        budgetRepository.save(budget);

        return budgetItemMapper.toResponse(saved);
    }

    @Override
    public BudgetItemResponse updateItem(UUID workOrderId, UUID itemId, BudgetItemRequest request) {

        Budget budget = budgetRepository.findByWorkOrderId(workOrderId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Presupuesto no encontrado para la orden: " + workOrderId
                ));

        if (budget.getSentAt() != null) {
            throw new BusinessRuleException(
                    "No se pueden modificar ítems de un presupuesto ya enviado"
            );
        }

        BudgetItem item = budgetItemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Ítem no encontrado con id: " + itemId
                ));

        if (!item.getBudget().getId().equals(budget.getId())) {
            throw new ResourceNotFoundException(
                    "El ítem no pertenece al presupuesto indicado"
            );
        }

        item.setItemType(request.getItemType());
        item.setDescription(request.getDescription());
        item.setQuantity(request.getQuantity());
        item.setUnitPrice(request.getUnitPrice());
        item.setDiscount(request.getDiscount());

        BudgetItem updated = budgetItemRepository.save(item);

        recalculateTotal(budget);
        budgetRepository.save(budget);

        return budgetItemMapper.toResponse(updated);
    }

    @Override
    public void deleteItem(UUID workOrderId, UUID itemId) {

        Budget budget = budgetRepository.findByWorkOrderId(workOrderId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Presupuesto no encontrado para la orden: " + workOrderId
                ));

        if (budget.getSentAt() != null) {
            throw new BusinessRuleException(
                    "No se pueden modificar ítems de un presupuesto ya enviado"
            );
        }

        BudgetItem item = budgetItemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Ítem no encontrado con id: " + itemId
                ));

        if (!item.getBudget().getId().equals(budget.getId())) {
            throw new ResourceNotFoundException(
                    "El ítem no pertenece al presupuesto indicado"
            );
        }

        budgetItemRepository.delete(item);

        recalculateTotal(budget);
        budgetRepository.save(budget);
    }

    private void validateClientOwnership(Budget budget, String clientEmail) {
        String ownerEmail = budget.getWorkOrder()
                .getVehicle()
                .getClient()
                .getUser()
                .getEmail();

        if (!ownerEmail.equals(clientEmail)) {
            throw new ForbiddenException(
                    "Solo el cliente dueño del vehículo puede aprobar o rechazar el presupuesto"
            );
        }
    }

    private void recalculateTotal(Budget budget) {
        if (budget.getItems() == null || budget.getItems().isEmpty()) {
            budget.setTotal(BigDecimal.ZERO);
            return;
        }

        BigDecimal subtotal = budget.getItems().stream()
                .map(item -> {
                    BigDecimal qty = BigDecimal.valueOf(item.getQuantity());
                    BigDecimal price = item.getUnitPrice();
                    BigDecimal disc = item.getDiscount() != null
                            ? item.getDiscount() : BigDecimal.ZERO;
                    BigDecimal factor = BigDecimal.ONE
                            .subtract(disc.divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP));
                    return qty.multiply(price).multiply(factor);
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal taxFactor = BigDecimal.ONE.add(
                budget.getTaxRate().divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP)
        );

        budget.setTotal(subtotal.multiply(taxFactor).setScale(2, RoundingMode.HALF_UP));
    }
}
