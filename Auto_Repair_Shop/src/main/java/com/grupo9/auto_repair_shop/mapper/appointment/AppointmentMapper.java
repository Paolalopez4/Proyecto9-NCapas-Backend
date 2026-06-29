package com.grupo9.auto_repair_shop.mapper.appointment;

import com.grupo9.auto_repair_shop.dto.response.appointment.AppointmentResponse;
import com.grupo9.auto_repair_shop.entity.appointment.Appointment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {

    @Mapping(source = "client.id", target = "clientId")
    @Mapping(source = "client.user.name", target = "clientName")
    @Mapping(source = "vehicle.id", target = "vehicleId")
    @Mapping(source = "vehicle.plate", target = "vehiclePlate")
    @Mapping(source = "vehicle.brand", target = "vehicleBrand")
    @Mapping(source = "vehicle.model", target = "vehicleModel")
    @Mapping(source = "branch.id", target = "branchId")
    @Mapping(source = "branch.name", target = "branchName")
    @Mapping(source = "mechanic.id", target = "mechanicId")
    @Mapping(source = "mechanic.user.name", target = "mechanicName")
    @Mapping(source = "workOrder.id", target = "workOrderId")
    AppointmentResponse toResponse(Appointment appointment);
}