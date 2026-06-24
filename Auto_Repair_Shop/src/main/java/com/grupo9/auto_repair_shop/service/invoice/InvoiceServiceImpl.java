package com.grupo9.auto_repair_shop.service.invoice;

import com.grupo9.auto_repair_shop.dto.response.common.PageResponse;
import com.grupo9.auto_repair_shop.dto.response.invoice.InvoiceResponse;
import com.grupo9.auto_repair_shop.entity.budget.Budget;
import com.grupo9.auto_repair_shop.entity.invoice.Invoice;
import com.grupo9.auto_repair_shop.entity.warranty.Warranty;
import com.grupo9.auto_repair_shop.entity.workorder.WorkOrder;
import com.grupo9.auto_repair_shop.enums.BudgetStatus;
import com.grupo9.auto_repair_shop.enums.InvoiceStatus;
import com.grupo9.auto_repair_shop.enums.WorkOrderStatus;
import com.grupo9.auto_repair_shop.exception.BusinessRuleException;
import com.grupo9.auto_repair_shop.exception.ConflictException;
import com.grupo9.auto_repair_shop.exception.ResourceNotFoundException;
import com.grupo9.auto_repair_shop.mapper.invoice.InvoiceMapper;
import com.grupo9.auto_repair_shop.repository.budget.BudgetRepository;
import com.grupo9.auto_repair_shop.repository.invoice.InvoiceRepository;
import com.grupo9.auto_repair_shop.repository.warranty.WarrantyRepository;
import com.grupo9.auto_repair_shop.repository.workorder.WorkOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final WorkOrderRepository workOrderRepository;
    private final BudgetRepository budgetRepository;
    private final WarrantyRepository warrantyRepository;
    private final InvoiceMapper invoiceMapper;

    @Override
    public InvoiceResponse generate(UUID workOrderId) {

        WorkOrder workOrder = workOrderRepository.findById(workOrderId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Orden de trabajo no encontrada con id: " + workOrderId
                ));

        if (workOrder.getStatus() != WorkOrderStatus.DONE) {
            throw new BusinessRuleException(
                    "Solo se puede generar una factura si la orden está en estado DONE"
            );
        }

        Budget budget = budgetRepository.findByWorkOrderId(workOrderId)
                .orElseThrow(() -> new BusinessRuleException(
                        "La orden no tiene un presupuesto asociado"
                ));

        if (budget.getStatus() != BudgetStatus.APPROVED) {
            throw new BusinessRuleException(
                    "Solo se puede generar una factura si el presupuesto fue APPROVED"
            );
        }

        if (invoiceRepository.findByWorkOrderId(workOrderId).isPresent()) {
            throw new ConflictException(
                    "Esta orden de trabajo ya tiene una factura generada"
            );
        }

        BigDecimal subtotal = calculateSubtotal(budget);
        BigDecimal taxAmount = subtotal.multiply(
                budget.getTaxRate().divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP)
        ).setScale(2, RoundingMode.HALF_UP);
        BigDecimal total = subtotal.add(taxAmount);

        String invoiceNumber = generateInvoiceNumber();

        Invoice invoice = Invoice.builder()
                .workOrder(workOrder)
                .invoiceNumber(invoiceNumber)
                .subtotal(subtotal)
                .tax(taxAmount)
                .total(total)
                .status(InvoiceStatus.ISSUED)
                .issuedAt(LocalDateTime.now())
                .build();

        Invoice saved = invoiceRepository.save(invoice);

        createWarranty(workOrder);

        return invoiceMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<InvoiceResponse> findAll(int page, int size) {

        PageRequest pageRequest = PageRequest.of(
                page, size, Sort.by(Sort.Direction.DESC, "issuedAt")
        );

        Page<InvoiceResponse> result = invoiceRepository.findAll(pageRequest)
                .map(invoiceMapper::toResponse);

        return PageResponse.from(result);
    }

    @Override
    @Transactional(readOnly = true)
    public InvoiceResponse findById(UUID id) {

        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Factura no encontrada con id: " + id
                ));

        return invoiceMapper.toResponse(invoice);
    }

    @Override
    @Transactional(readOnly = true)
    public InvoiceResponse findByWorkOrder(UUID workOrderId) {

        Invoice invoice = invoiceRepository.findByWorkOrderId(workOrderId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Factura no encontrada para la orden: " + workOrderId
                ));

        return invoiceMapper.toResponse(invoice);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<InvoiceResponse> findByClient(UUID clientId, int page, int size) {

        PageRequest pageRequest = PageRequest.of(
                page, size, Sort.by(Sort.Direction.DESC, "issuedAt")
        );

        Page<InvoiceResponse> result = invoiceRepository
                .findByWorkOrderVehicleClientId(clientId, pageRequest)
                .map(invoiceMapper::toResponse);

        return PageResponse.from(result);
    }

    @Override
    public InvoiceResponse pay(UUID id) {

        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Factura no encontrada con id: " + id
                ));

        if (invoice.getStatus() != InvoiceStatus.ISSUED) {
            throw new ConflictException(
                    "Solo se puede marcar como PAID una factura en estado ISSUED"
            );
        }

        invoice.setStatus(InvoiceStatus.PAID);
        invoice.setPaidAt(LocalDateTime.now());

        Invoice updated = invoiceRepository.save(invoice);

        return invoiceMapper.toResponse(updated);
    }

    @Override
    public InvoiceResponse cancel(UUID id) {

        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Factura no encontrada con id: " + id
                ));

        if (invoice.getStatus() != InvoiceStatus.ISSUED) {
            throw new ConflictException(
                    "Solo se puede cancelar una factura en estado ISSUED"
            );
        }

        invoice.setStatus(InvoiceStatus.CANCELLED);

        Invoice updated = invoiceRepository.save(invoice);

        return invoiceMapper.toResponse(updated);
    }

    private BigDecimal calculateSubtotal(Budget budget) {
        if (budget.getItems() == null || budget.getItems().isEmpty()) {
            return BigDecimal.ZERO;
        }

        return budget.getItems().stream()
                .map(item -> {
                    BigDecimal qty = BigDecimal.valueOf(item.getQuantity());
                    BigDecimal price = item.getUnitPrice();
                    BigDecimal disc = item.getDiscount() != null
                            ? item.getDiscount() : BigDecimal.ZERO;
                    BigDecimal factor = BigDecimal.ONE.subtract(
                            disc.divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP)
                    );
                    return qty.multiply(price).multiply(factor);
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }

    private String generateInvoiceNumber() {
        String candidate;
        do {
            candidate = "INV-" + System.currentTimeMillis();
        } while (invoiceRepository.existsByInvoiceNumber(candidate));
        return candidate;
    }

    private void createWarranty(WorkOrder workOrder) {
        if (warrantyRepository.findByWorkOrderId(workOrder.getId()).isPresent()) {
            return;
        }

        LocalDate start = LocalDate.now();

        Warranty warranty = Warranty.builder()
                .workOrder(workOrder)
                .startDate(start)
                .endDate(start.plusDays(30))
                .coverage("Garantía de 30 días sobre los servicios realizados")
                .active(true)
                .build();

        warrantyRepository.save(warranty);
    }
}
