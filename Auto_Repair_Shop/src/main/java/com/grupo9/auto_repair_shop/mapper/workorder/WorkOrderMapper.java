package com.grupo9.auto_repair_shop.mapper.workorder;

import com.grupo9.auto_repair_shop.dto.response.workorder.WorkOrderResponse;
import com.grupo9.auto_repair_shop.entity.workorder.WorkOrder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WorkOrderMapper {
    @Mapping(source = "vehicle.id", target = "vehicleId")
    @Mapping(source = "vehicle.plate", target = "vehiclePlate")
    @Mapping(source = "vehicle.brand", target = "vehicleBrand")
    @Mapping(source = "vehicle.model", target = "vehicleModel")
    @Mapping(source = "mechanic.id", target = "mechanicId")
    @Mapping(source = "mechanic.user.name", target = "mechanicName")
    @Mapping(source = "branch.id", target = "branchId")
    @Mapping(source = "branch.name", target = "branchName")
    @Mapping(source = "appointment.id", target = "appointmentId")
    @Mapping(source = "budget.id", target = "budgetId")
    @Mapping(source = "invoice.id", target = "invoiceId")
    @Mapping(source = "warranty.id", target = "warrantyId")
    WorkOrderResponse toResponse(WorkOrder workOrder);
}
