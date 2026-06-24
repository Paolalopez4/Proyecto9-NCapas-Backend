package com.grupo9.auto_repair_shop.service.workorder;

import com.grupo9.auto_repair_shop.dto.request.workorder.WorkOrderPartRequest;
import com.grupo9.auto_repair_shop.dto.request.workorder.WorkOrderRequest;
import com.grupo9.auto_repair_shop.dto.request.workorder.WorkOrderServiceRequest;
import com.grupo9.auto_repair_shop.dto.response.workorder.WorkOrderResponse;
import com.grupo9.auto_repair_shop.entity.appointment.Appointment;
import com.grupo9.auto_repair_shop.entity.branch.Branch;
import com.grupo9.auto_repair_shop.entity.inventory.BranchInventory;
import com.grupo9.auto_repair_shop.entity.mechanic.Mechanic;
import com.grupo9.auto_repair_shop.entity.part.Part;
import com.grupo9.auto_repair_shop.entity.repairhistory.RepairHistory;
import com.grupo9.auto_repair_shop.entity.service.Service;
import com.grupo9.auto_repair_shop.entity.vehicle.Vehicle;
import com.grupo9.auto_repair_shop.entity.workorder.WorkOrder;
import com.grupo9.auto_repair_shop.entity.workorder.WorkOrderPart;
import com.grupo9.auto_repair_shop.enums.OrderType;
import com.grupo9.auto_repair_shop.enums.WorkOrderStatus;
import com.grupo9.auto_repair_shop.exception.BusinessRuleException;
import com.grupo9.auto_repair_shop.exception.ConflictException;
import com.grupo9.auto_repair_shop.exception.ResourceNotFoundException;
import com.grupo9.auto_repair_shop.mapper.workorder.WorkOrderMapper;
import com.grupo9.auto_repair_shop.repository.appointment.AppointmentRepository;
import com.grupo9.auto_repair_shop.repository.branch.BranchRepository;
import com.grupo9.auto_repair_shop.repository.inventory.BranchInventoryRepository;
import com.grupo9.auto_repair_shop.repository.mechanic.MechanicRepository;
import com.grupo9.auto_repair_shop.repository.part.PartRepository;
import com.grupo9.auto_repair_shop.repository.repairhistory.RepairHistoryRepository;
import com.grupo9.auto_repair_shop.repository.service.ServiceRepository;
import com.grupo9.auto_repair_shop.repository.vehicle.VehicleRepository;
import com.grupo9.auto_repair_shop.repository.warranty.WarrantyRepository;
import com.grupo9.auto_repair_shop.repository.workorder.WorkOrderPartRepository;
import com.grupo9.auto_repair_shop.repository.workorder.WorkOrderRepository;
import com.grupo9.auto_repair_shop.repository.workorder.WorkOrderServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class WorkOrderServiceImpl implements WorkOrderService {
    private final AppointmentRepository appointmentRepository;
    private final WorkOrderRepository workOrderRepository;
    private final WorkOrderServiceRepository workOrderServiceRepository;
    private final WorkOrderPartRepository workOrderPartRepository;
    private final VehicleRepository vehicleRepository;
    private final MechanicRepository mechanicRepository;
    private final BranchRepository branchRepository;
    private final ServiceRepository serviceRepository;
    private final PartRepository partRepository;
    private final BranchInventoryRepository branchInventoryRepository;
    private final RepairHistoryRepository repairHistoryRepository;
    private final WarrantyRepository warrantyRepository;
    private final WorkOrderMapper workOrderMapper;

    //GET paginado con filtros opcionales
    @Override
    public Page<WorkOrderResponse> findAll(WorkOrderStatus status, OrderType orderType,
                                           UUID branchId, UUID mechanicId, Pageable pageable) {
        if (status != null && branchId != null) {
            return workOrderRepository.findByStatusAndBranchId(status, branchId, pageable)
                    .map(workOrderMapper::toResponse);
        } else if (status != null) {
            return workOrderRepository.findByStatus(status, pageable)
                    .map(workOrderMapper::toResponse);
        } else if (branchId != null) {
            return workOrderRepository.findByBranchId(branchId, pageable)
                    .map(workOrderMapper::toResponse);
        } else if (mechanicId != null) {
            return workOrderRepository.findByMechanicId(mechanicId, pageable)
                    .map(workOrderMapper::toResponse);
        } else if (orderType != null) {
            return workOrderRepository.findByOrderType(orderType, pageable)
                    .map(workOrderMapper::toResponse);
        }
        return workOrderRepository.findAll(pageable).map(workOrderMapper::toResponse);
    }

    // GET por id
    @Override
    public WorkOrderResponse findById(UUID id) {
        return workOrderMapper.toResponse(findOrThrow(id));
    }

    // GET work orders de un cliente
    @Override
    public Page<WorkOrderResponse> findByClientId(UUID clientId, Pageable pageable) {
        return workOrderRepository.findByClientId(clientId, pageable)
                .map(workOrderMapper::toResponse);
    }

    // POST crear orden de trabajo
    @Override
    public WorkOrderResponse create(WorkOrderRequest request) {
        Vehicle vehicle = vehicleRepository.findById(request.getVehicleId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Vehicle not found with id: " + request.getVehicleId()));

        Mechanic mechanic = mechanicRepository.findById(request.getMechanicId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Mechanic not found with id: " + request.getMechanicId()));

        Branch branch = branchRepository.findById(request.getBranchId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Branch not found with id: " + request.getBranchId()));

        if (request.getOrderType() == OrderType.POST_WARRANTY) {
            List<WorkOrder> activeWarranties = workOrderRepository
                    .findActiveWarrantiesByVehicleId(vehicle.getId());
            if (activeWarranties.isEmpty()) {
                throw new BusinessRuleException(
                        "A POST_WARRANTY order requires an active and non-expired warranty for this vehicle.");
            }
        }

        WorkOrder workOrder = WorkOrder.builder()
                .vehicle(vehicle)
                .mechanic(mechanic)
                .branch(branch)
                .orderType(request.getOrderType())
                .diagnosis(request.getDiagnosis())
                .status(WorkOrderStatus.OPEN)
                .openedAt(LocalDateTime.now())
                .build();

        if (request.getAppointmentId() != null) {
            Appointment appointment = appointmentRepository.findById(request.getAppointmentId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Appointment not found with id: " + request.getAppointmentId()));
            workOrder.setAppointment(appointment);
        }

        return workOrderMapper.toResponse(workOrderRepository.save(workOrder));
    }

    // PUT actualizar datos de la orden
    @Override
    public WorkOrderResponse update(UUID id, WorkOrderRequest request) {
        WorkOrder workOrder = findOrThrow(id);

        validateNotTerminal(workOrder);

        Mechanic mechanic = mechanicRepository.findById(request.getMechanicId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Mechanic not found with id: " + request.getMechanicId()));

        Branch branch = branchRepository.findById(request.getBranchId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Branch not found with id: " + request.getBranchId()));

        workOrder.setDiagnosis(request.getDiagnosis());
        workOrder.setOrderType(request.getOrderType());
        workOrder.setMechanic(mechanic);
        workOrder.setBranch(branch);

        return workOrderMapper.toResponse(workOrderRepository.save(workOrder));
    }

    // PATCH /start
    @Override
    public WorkOrderResponse start(UUID id) {
        WorkOrder workOrder = findOrThrow(id);

        if (workOrder.getStatus() != WorkOrderStatus.OPEN) {
            throw new ConflictException(
                    "Work order can only be started from OPEN status. Current status: "
                            + workOrder.getStatus());
        }

        workOrder.setStatus(WorkOrderStatus.IN_PROGRESS);
        return workOrderMapper.toResponse(workOrderRepository.save(workOrder));
    }

    // PATCH /request-approval
    @Override
    public WorkOrderResponse requestApproval(UUID id) {
        WorkOrder workOrder = findOrThrow(id);

        if (workOrder.getStatus() != WorkOrderStatus.IN_PROGRESS) {
            throw new ConflictException(
                    "Work order can only request approval from IN_PROGRESS status. Current status: "
                            + workOrder.getStatus());
        }

        workOrder.setStatus(WorkOrderStatus.AWAITING_APPROVAL);
        return workOrderMapper.toResponse(workOrderRepository.save(workOrder));
    }

    // PATCH /close
    //requiere presupuesto APPROVED
    //genera RepairHistory automaticamente
    //genera Warranty de 30 dias automaticamente
    //envia notificacion al cliente
    @Override
    public WorkOrderResponse close(UUID id) {
        WorkOrder workOrder = findOrThrow(id);

        //solo desde IN_PROGRESS o AWAITING_APPROVAL
        if (workOrder.getStatus() != WorkOrderStatus.IN_PROGRESS
                && workOrder.getStatus() != WorkOrderStatus.AWAITING_APPROVAL) {
            throw new ConflictException(
                    "Work order can only be closed from IN_PROGRESS or AWAITING_APPROVAL status. Current status: "
                            + workOrder.getStatus());
        }

        //presupuesto APPROVED
        if (workOrder.getBudget() == null
                || workOrder.getBudget().getStatus() != com.grupo9.auto_repair_shop.enums.BudgetStatus.APPROVED) {
            throw new BusinessRuleException(
                    "Work order cannot be closed without an approved budget.");
        }

        //sin factura emitida
        if (workOrder.getInvoice() != null) {
            throw new ConflictException(
                    "Work order already has an issued invoice and cannot be cancelled.");
        }

        workOrder.setStatus(WorkOrderStatus.DONE);
        workOrder.setClosedAt(LocalDateTime.now());
        WorkOrder saved = workOrderRepository.save(workOrder);

        //generar RepairHistory automaticamente
        if (repairHistoryRepository.findByWorkOrderId(saved.getId()).isEmpty()) {
            RepairHistory repairHistory = RepairHistory.builder()
                    .vehicle(saved.getVehicle())
                    .workOrder(saved)
                    .summary(saved.getDiagnosis() != null ? saved.getDiagnosis() : "Repair completed.")
                    .repairDate(LocalDate.now())
                    .mileageAtRepair(saved.getVehicle().getMileage())
                    .build();
            repairHistoryRepository.save(repairHistory);
        }
        return workOrderMapper.toResponse(saved);
    }

    // PATCH /cancel
    @Override
    public WorkOrderResponse cancel(UUID id) {
        WorkOrder workOrder = findOrThrow(id);

        validateNotTerminal(workOrder);

        if (workOrder.getInvoice() != null) {
            com.grupo9.auto_repair_shop.enums.InvoiceStatus invoiceStatus =
                    workOrder.getInvoice().getStatus();
            if (invoiceStatus == com.grupo9.auto_repair_shop.enums.InvoiceStatus.ISSUED
                    || invoiceStatus == com.grupo9.auto_repair_shop.enums.InvoiceStatus.PAID) {
                throw new BusinessRuleException(
                        "Work order cannot be cancelled because it has an active invoice.");
            }
        }

        workOrder.setStatus(WorkOrderStatus.CANCELLED);
        return workOrderMapper.toResponse(workOrderRepository.save(workOrder));
    }

    // POST /services
    @Override
    public WorkOrderResponse addService(UUID workOrderId, WorkOrderServiceRequest request) {
        WorkOrder workOrder = findOrThrow(workOrderId);
        validateNotTerminal(workOrder);

        Service service = serviceRepository.findById(request.getServiceId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Service not found with id: " + request.getServiceId()));

        com.grupo9.auto_repair_shop.entity.workorder.WorkOrderService workOrderService =
                com.grupo9.auto_repair_shop.entity.workorder.WorkOrderService.builder()
                        .workOrder(workOrder)
                        .service(service)
                        .quantity(request.getQuantity())
                        .unitPrice(service.getBasePrice())
                        .discount(request.getDiscount())
                        .build();

        workOrderServiceRepository.save(workOrderService);
        return workOrderMapper.toResponse(findOrThrow(workOrderId));
    }

    // PUT /services/{serviceId} actualizar servicio en la orden
    @Override
    public WorkOrderResponse updateService(UUID workOrderId, UUID serviceId,
                                           WorkOrderServiceRequest request) {
        WorkOrder workOrder = findOrThrow(workOrderId);
        validateNotTerminal(workOrder);

        var pivotService = workOrderServiceRepository
                .findByWorkOrderIdAndServiceId(workOrderId, serviceId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Service not found in this work order."));

        pivotService.setQuantity(request.getQuantity());
        pivotService.setDiscount(request.getDiscount());
        workOrderServiceRepository.save(pivotService);

        return workOrderMapper.toResponse(findOrThrow(workOrderId));
    }

    // DELETE /services/{serviceId} borrar servicio
    @Override
    public WorkOrderResponse removeService(UUID workOrderId, UUID serviceId) {
        WorkOrder workOrder = findOrThrow(workOrderId);
        validateNotTerminal(workOrder);

        var pivotService = workOrderServiceRepository
                .findByWorkOrderIdAndServiceId(workOrderId, serviceId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Service not found in this work order."));

        workOrderServiceRepository.delete(pivotService);
        return workOrderMapper.toResponse(findOrThrow(workOrderId));
    }

    // POST /parts agregar repuesto a la orden
    @Override
    public WorkOrderResponse addPart(UUID workOrderId, WorkOrderPartRequest request) {
        WorkOrder workOrder = findOrThrow(workOrderId);
        validateNotTerminal(workOrder);

        Part part = partRepository.findById(request.getPartId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Part not found with id: " + request.getPartId()));

        BranchInventory inventory = branchInventoryRepository
                .findByBranchIdAndPartId(workOrder.getBranch().getId(), part.getId())
                .orElseThrow(() -> new BusinessRuleException(
                        "Part '" + part.getName() + "' is not available in this branch."));

        if (inventory.getStock() < request.getQuantity()) {
            throw new BusinessRuleException(
                    "Insufficient stock for part '" + part.getName() + "'. Available: "
                            + inventory.getStock() + ", requested: " + request.getQuantity());
        }

        inventory.setStock(inventory.getStock() - request.getQuantity());
        branchInventoryRepository.save(inventory);

        WorkOrderPart workOrderPart = WorkOrderPart.builder()
                .workOrder(workOrder)
                .part(part)
                .quantity(request.getQuantity())
                .unitPrice(part.getUnitPrice())
                .discount(request.getDiscount())
                .build();

        workOrderPartRepository.save(workOrderPart);
        return workOrderMapper.toResponse(findOrThrow(workOrderId));
    }

    // PUT /parts/{partId}
    @Override
    public WorkOrderResponse updatePart(UUID workOrderId, UUID partId,
                                        WorkOrderPartRequest request) {
        WorkOrder workOrder = findOrThrow(workOrderId);
        validateNotTerminal(workOrder);

        WorkOrderPart workOrderPart = workOrderPartRepository
                .findByWorkOrderIdAndPartId(workOrderId, partId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Part not found in this work order."));

        int oldQuantity = workOrderPart.getQuantity();
        int newQuantity = request.getQuantity();
        int diff = newQuantity - oldQuantity;

        if (diff != 0) {
            BranchInventory inventory = branchInventoryRepository
                    .findByBranchIdAndPartId(workOrder.getBranch().getId(), partId)
                    .orElseThrow(() -> new BusinessRuleException(
                            "Part inventory not found in this branch."));

            if (diff > 0 && inventory.getStock() < diff) {
                throw new BusinessRuleException(
                        "Insufficient stock to increase quantity. Available: "
                                + inventory.getStock() + ", needed: " + diff);
            }

            inventory.setStock(inventory.getStock() - diff);
            branchInventoryRepository.save(inventory);
        }

        workOrderPart.setQuantity(newQuantity);
        workOrderPart.setDiscount(request.getDiscount());
        workOrderPartRepository.save(workOrderPart);

        return workOrderMapper.toResponse(findOrThrow(workOrderId));
    }

    // DELETE /parts/{partId} borrar repuesto
    @Override
    public WorkOrderResponse removePart(UUID workOrderId, UUID partId) {
        WorkOrder workOrder = findOrThrow(workOrderId);
        validateNotTerminal(workOrder);

        WorkOrderPart workOrderPart = workOrderPartRepository
                .findByWorkOrderIdAndPartId(workOrderId, partId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Part not found in this work order."));

        branchInventoryRepository
                .findByBranchIdAndPartId(workOrder.getBranch().getId(), partId)
                .ifPresent(inventory -> {
                    inventory.setStock(inventory.getStock() + workOrderPart.getQuantity());
                    branchInventoryRepository.save(inventory);
                });

        workOrderPartRepository.delete(workOrderPart);
        return workOrderMapper.toResponse(findOrThrow(workOrderId));
    }

    //helpers
    private WorkOrder findOrThrow(UUID id) {
        return workOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Work order not found with id: " + id));
    }

    //valida que la orden no este en estado terminal
    private void validateNotTerminal(WorkOrder workOrder) {
        if (workOrder.getStatus() == WorkOrderStatus.DONE
                || workOrder.getStatus() == WorkOrderStatus.CANCELLED) {
            throw new ConflictException(
                    "Cannot modify a work order in status: " + workOrder.getStatus());
        }
    }
}