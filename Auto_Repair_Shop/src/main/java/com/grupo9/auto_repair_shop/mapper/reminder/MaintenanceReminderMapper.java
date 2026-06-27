package com.grupo9.auto_repair_shop.mapper.reminder;

import com.grupo9.auto_repair_shop.dto.request.reminder.MaintenanceReminderUpdateRequest;
import com.grupo9.auto_repair_shop.dto.response.reminder.MaintenanceReminderResponse;
import com.grupo9.auto_repair_shop.entity.reminder.MaintenanceReminder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MaintenanceReminderMapper {

        @Mapping(target = "vehicleId",   source = "vehicle.id")
        @Mapping(target = "vehiclePlate", source = "vehicle.plate")
        @Mapping(target = "vehicleBrand", source = "vehicle.brand")
        @Mapping(target = "clientId",    source = "client.id")
        @Mapping(target = "clientName",  source = "client.user.name")
        MaintenanceReminderResponse toResponse(MaintenanceReminder reminder);

        @Mapping(target = "reminderType", source = "request.reminderType")
        @Mapping(target = "dueDate",      source = "request.dueDate")
        @Mapping(target = "dueMileage",   source = "request.dueMileage")
        @Mapping(target = "notes",        source = "request.notes")
        void updateEntity(MaintenanceReminderUpdateRequest request, @MappingTarget MaintenanceReminder reminder);

}
