package com.grupo9.auto_repair_shop.mapper.repairhistory;

import com.grupo9.auto_repair_shop.dto.response.repairhistory.RepairHistoryResponse;
import com.grupo9.auto_repair_shop.entity.repairhistory.RepairHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RepairHistoryMapper {

    @Mapping(source = "vehicle.id", target = "vehicleId")
    @Mapping(source = "vehicle.plate", target = "vehiclePlate")
    @Mapping(source = "vehicle.brand", target = "vehicleBrand")
    @Mapping(source = "vehicle.model", target = "vehicleModel")
    @Mapping(source = "workOrder.id", target = "workOrderId")
    RepairHistoryResponse toResponse(RepairHistory repairHistory);
}