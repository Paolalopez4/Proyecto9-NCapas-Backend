package com.grupo9.auto_repair_shop.service.reminder;

import com.grupo9.auto_repair_shop.dto.request.reminder.MaintenanceReminderRequest;
import com.grupo9.auto_repair_shop.dto.request.reminder.MaintenanceReminderUpdateRequest;
import com.grupo9.auto_repair_shop.dto.response.common.PageResponse;
import com.grupo9.auto_repair_shop.dto.response.reminder.MaintenanceReminderResponse;

import java.util.UUID;

public interface MaintenanceReminderService {

    MaintenanceReminderResponse create(MaintenanceReminderRequest request);

    MaintenanceReminderResponse findById(UUID id);

    PageResponse<MaintenanceReminderResponse> findAll(UUID vehicleId, UUID clientId, Boolean sent, Boolean acknowledged, int page, int size);

    MaintenanceReminderResponse update(UUID id, MaintenanceReminderUpdateRequest request);

    MaintenanceReminderResponse markAsSent(UUID id);

    MaintenanceReminderResponse acknowledge(UUID id);

    void delete(UUID id);
}