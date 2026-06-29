package com.grupo9.auto_repair_shop.service.reminder;

import com.grupo9.auto_repair_shop.dto.request.reminder.MaintenanceReminderRequest;
import com.grupo9.auto_repair_shop.dto.request.reminder.MaintenanceReminderUpdateRequest;
import com.grupo9.auto_repair_shop.dto.response.common.PageResponse;
import com.grupo9.auto_repair_shop.dto.response.reminder.MaintenanceReminderResponse;
import com.grupo9.auto_repair_shop.entity.client.Client;
import com.grupo9.auto_repair_shop.entity.reminder.MaintenanceReminder;
import com.grupo9.auto_repair_shop.entity.vehicle.Vehicle;
import com.grupo9.auto_repair_shop.exception.BusinessRuleException;
import com.grupo9.auto_repair_shop.exception.ResourceNotFoundException;
import com.grupo9.auto_repair_shop.mapper.reminder.MaintenanceReminderMapper;
import com.grupo9.auto_repair_shop.repository.client.ClientRepository;
import com.grupo9.auto_repair_shop.repository.reminder.MaintenanceReminderRepository;
import com.grupo9.auto_repair_shop.repository.vehicle.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MaintenanceReminderServiceImpl implements MaintenanceReminderService {

    private final MaintenanceReminderRepository reminderRepository;
    private final VehicleRepository vehicleRepository;
    private final ClientRepository clientRepository;
    private final MaintenanceReminderMapper reminderMapper;

    @Override
    @Transactional
    public MaintenanceReminderResponse create(MaintenanceReminderRequest request) {

        if (request.getDueDate() == null && request.getDueMileage() == null) {
            throw new BusinessRuleException(
                    "Reminder must have at least a due date or a due mileage");
        }

        if (request.getDueDate() != null && !request.getDueDate().isAfter(LocalDate.now())) {
            throw new BusinessRuleException("Due date must be a future date");
        }

        Vehicle vehicle = vehicleRepository.findById(request.getVehicleId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Vehicle not found with id: " + request.getVehicleId()));

        Client client = clientRepository.findById(request.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Client not found with id: " + request.getClientId()));

        MaintenanceReminder reminder = MaintenanceReminder.builder()
                .reminderType(request.getReminderType())
                .dueDate(request.getDueDate())
                .dueMileage(request.getDueMileage())
                .notes(request.getNotes())
                .sent(false)
                .acknowledged(false)
                .vehicle(vehicle)
                .client(client)
                .build();

        MaintenanceReminder saved = reminderRepository.save(reminder);
        return reminderMapper.toResponse(saved);
    }

    @Override
    public MaintenanceReminderResponse findById(UUID id) {
        MaintenanceReminder reminder = reminderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Maintenance reminder not found with id: " + id));
        return reminderMapper.toResponse(reminder);
    }

    @Override
    public PageResponse<MaintenanceReminderResponse> findAll(
            UUID vehicleId, UUID clientId, Boolean sent, Boolean acknowledged, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<MaintenanceReminder> result;

        if (vehicleId != null) {
            result = reminderRepository.findByVehicleId(vehicleId, pageable);
        } else if (clientId != null) {
            result = reminderRepository.findByClientId(clientId, pageable);
        } else if (sent != null) {
            result = reminderRepository.findBySent(sent, pageable);
        } else if (acknowledged != null) {
            result = reminderRepository.findByAcknowledged(acknowledged, pageable);
        } else {
            result = reminderRepository.findAll(pageable);
        }

        Page<MaintenanceReminderResponse> mapped = result.map(reminderMapper::toResponse);
        return PageResponse.from(mapped);
    }

    @Override
    @Transactional
    public MaintenanceReminderResponse update(UUID id, MaintenanceReminderUpdateRequest request) {

        MaintenanceReminder reminder = reminderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Maintenance reminder not found with id: " + id));

        if (request.getDueDate() == null && request.getDueMileage() == null) {
            throw new BusinessRuleException(
                    "Reminder must have at least a due date or a due mileage");
        }

        if (request.getDueDate() != null && !request.getDueDate().isAfter(LocalDate.now())) {
            throw new BusinessRuleException("Due date must be a future date");
        }

        reminderMapper.updateEntity(request, reminder);

        MaintenanceReminder updated = reminderRepository.save(reminder);
        return reminderMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public MaintenanceReminderResponse markAsSent(UUID id) {
        MaintenanceReminder reminder = reminderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Maintenance reminder not found with id: " + id));

        reminder.setSent(true);

        MaintenanceReminder updated = reminderRepository.save(reminder);
        return reminderMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public MaintenanceReminderResponse acknowledge(UUID id) {
        MaintenanceReminder reminder = reminderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Maintenance reminder not found with id: " + id));

        if (!Boolean.TRUE.equals(reminder.getSent())) {
            throw new BusinessRuleException(
                    "Cannot acknowledge a reminder that has not been sent yet");
        }

        reminder.setAcknowledged(true);

        MaintenanceReminder updated = reminderRepository.save(reminder);
        return reminderMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        MaintenanceReminder reminder = reminderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Maintenance reminder not found with id: " + id));

        reminderRepository.delete(reminder);
    }
}