package com.grupo9.auto_repair_shop.service.workorder;

import com.grupo9.auto_repair_shop.dto.request.workorder.WorkOrderPartRequest;
import com.grupo9.auto_repair_shop.dto.request.workorder.WorkOrderRequest;
import com.grupo9.auto_repair_shop.dto.request.workorder.WorkOrderServiceRequest;
import com.grupo9.auto_repair_shop.dto.response.workorder.WorkOrderResponse;
import com.grupo9.auto_repair_shop.enums.OrderType;
import com.grupo9.auto_repair_shop.enums.WorkOrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface WorkOrderService {
    //CRUD
    Page<WorkOrderResponse> findAll(WorkOrderStatus status, OrderType orderType,
                                    UUID branchId, UUID mechanicId, Pageable pageable);
    WorkOrderResponse findById(UUID id);
    Page<WorkOrderResponse> findByClientId(UUID clientId, Pageable pageable);
    WorkOrderResponse create(WorkOrderRequest request);
    WorkOrderResponse update(UUID id, WorkOrderRequest request);

    //transiciones de estado
    WorkOrderResponse start(UUID id);
    WorkOrderResponse requestApproval(UUID id);
    WorkOrderResponse close(UUID id);
    WorkOrderResponse cancel(UUID id);

    //workorderservice
    WorkOrderResponse addService(UUID workOrderId, WorkOrderServiceRequest request);
    WorkOrderResponse updateService(UUID workOrderId, UUID serviceId, WorkOrderServiceRequest request);
    WorkOrderResponse removeService(UUID workOrderId, UUID serviceId);

    //workorderpart
    WorkOrderResponse addPart(UUID workOrderId, WorkOrderPartRequest request);
    WorkOrderResponse updatePart(UUID workOrderId, UUID partId, WorkOrderPartRequest request);
    WorkOrderResponse removePart(UUID workOrderId, UUID partId);
}