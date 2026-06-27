package com.grupo9.auto_repair_shop.service.appointment;

import com.grupo9.auto_repair_shop.dto.request.appointment.AppointmentRequest;
import com.grupo9.auto_repair_shop.dto.request.appointment.AppointmentServiceRequest;
import com.grupo9.auto_repair_shop.dto.response.appointment.AppointmentResponse;
import com.grupo9.auto_repair_shop.entity.appointment.Appointment;
import com.grupo9.auto_repair_shop.entity.branch.Branch;
import com.grupo9.auto_repair_shop.entity.client.Client;
import com.grupo9.auto_repair_shop.entity.mechanic.Mechanic;
import com.grupo9.auto_repair_shop.entity.service.Service;
import com.grupo9.auto_repair_shop.entity.vehicle.Vehicle;
import com.grupo9.auto_repair_shop.enums.AppointmentStatus;
import com.grupo9.auto_repair_shop.exception.BusinessRuleException;
import com.grupo9.auto_repair_shop.exception.ConflictException;
import com.grupo9.auto_repair_shop.exception.ResourceNotFoundException;
import com.grupo9.auto_repair_shop.mapper.appointment.AppointmentMapper;
import com.grupo9.auto_repair_shop.repository.appointment.AppointmentRepository;
import com.grupo9.auto_repair_shop.repository.appointment.AppointmentServiceRepository;
import com.grupo9.auto_repair_shop.repository.branch.BranchRepository;
import com.grupo9.auto_repair_shop.repository.client.ClientRepository;
import com.grupo9.auto_repair_shop.repository.mechanic.MechanicRepository;
import com.grupo9.auto_repair_shop.repository.service.ServiceRepository;
import com.grupo9.auto_repair_shop.repository.vehicle.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final AppointmentServiceRepository appointmentServiceRepository;
    private final ClientRepository clientRepository;
    private final VehicleRepository vehicleRepository;
    private final BranchRepository branchRepository;
    private final MechanicRepository mechanicRepository;
    private final ServiceRepository serviceRepository;
    private final AppointmentMapper appointmentMapper;

    //GET paginado
    @Override
    public Page<AppointmentResponse> findAll(UUID branchId, LocalDateTime date,
                                             String status, UUID mechanicId, Pageable pageable) {
        if (branchId != null && status != null) {
            return appointmentRepository
                    .findByBranchIdAndStatus(branchId, AppointmentStatus.valueOf(status), pageable)
                    .map(appointmentMapper::toResponse);
        } else if (branchId != null && date != null) {
            return appointmentRepository
                    .findByBranchIdAndDate(branchId, date, pageable)
                    .map(appointmentMapper::toResponse);
        } else if (branchId != null) {
            return appointmentRepository.findByBranchId(branchId, pageable)
                    .map(appointmentMapper::toResponse);
        } else if (status != null) {
            return appointmentRepository
                    .findByStatus(AppointmentStatus.valueOf(status), pageable)
                    .map(appointmentMapper::toResponse);
        } else if (mechanicId != null) {
            return appointmentRepository.findByMechanicId(mechanicId, pageable)
                    .map(appointmentMapper::toResponse);
        }
        return appointmentRepository.findAll(pageable).map(appointmentMapper::toResponse);
    }

    //GET por id
    @Override
    public AppointmentResponse findById(UUID id) {
        return appointmentMapper.toResponse(findOrThrow(id));
    }

    //GET citas de un cliente
    @Override
    public Page<AppointmentResponse> findByClientId(UUID clientId, Pageable pageable) {
        return appointmentRepository.findByClientId(clientId, pageable)
                .map(appointmentMapper::toResponse);
    }

    //GET slots disponibles
    @Override
    public List<LocalDateTime> getAvailableSlots(UUID branchId, LocalDateTime date) {
        branchRepository.findById(branchId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Branch not found with id: " + branchId));

        List<LocalDateTime> occupied = appointmentRepository
                .findOccupiedSlotsByBranchAndDate(branchId, date);

        List<LocalDateTime> allSlots = new java.util.ArrayList<>();
        LocalDateTime slot = date.toLocalDate().atTime(8, 0);
        LocalDateTime end = date.toLocalDate().atTime(17, 0);

        while (!slot.isAfter(end)) {
            allSlots.add(slot);
            slot = slot.plusHours(1);
        }

        allSlots.removeAll(occupied);
        return allSlots;
    }

    //POST crear cita
    @Override
    public AppointmentResponse create(AppointmentRequest request) {
        Client client = clientRepository.findById(request.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Client not found with id: " + request.getClientId()));

        Vehicle vehicle = vehicleRepository.findById(request.getVehicleId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Vehicle not found with id: " + request.getVehicleId()));

        Branch branch = branchRepository.findById(request.getBranchId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Branch not found with id: " + request.getBranchId()));

        if (request.getScheduledAt().getMinute() != 0 || request.getScheduledAt().getSecond() != 0) {
            throw new BusinessRuleException(
                    "Appointments must be scheduled on the hour (minutes and seconds must be 0).");
        }

        if (!vehicle.getClient().getId().equals(client.getId())) {
            throw new BusinessRuleException(
                    "The vehicle does not belong to the specified client.");
        }

        Mechanic mechanic = null;
        if (request.getMechanicId() != null) {
            mechanic = mechanicRepository.findById(request.getMechanicId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Mechanic not found with id: " + request.getMechanicId()));

            if (appointmentRepository.existsByBranchIdAndScheduledAtAndMechanicId(
                    branch.getId(), request.getScheduledAt(), mechanic.getId())) {
                throw new ConflictException(
                        "The slot " + request.getScheduledAt() + " at this branch is already taken for this mechanic.");
            }
        }

        Appointment appointment = Appointment.builder()
                .scheduledAt(request.getScheduledAt())
                .status(AppointmentStatus.PENDING)
                .notes(request.getNotes())
                .client(client)
                .vehicle(vehicle)
                .branch(branch)
                .mechanic(mechanic)
                .build();

        return appointmentMapper.toResponse(appointmentRepository.save(appointment));
    }

    //PUT actualizar cita
    @Override
    public AppointmentResponse update(UUID id, AppointmentRequest request) {
        Appointment appointment = findOrThrow(id);
        validateNotTerminal(appointment);

        if (request.getScheduledAt().getMinute() != 0 || request.getScheduledAt().getSecond() != 0) {
            throw new BusinessRuleException(
                    "Appointments must be scheduled on the hour (minutes and seconds must be 0).");
        }

        Vehicle vehicle = vehicleRepository.findById(request.getVehicleId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Vehicle not found with id: " + request.getVehicleId()));

        Client client = clientRepository.findById(request.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Client not found with id: " + request.getClientId()));

        if (!vehicle.getClient().getId().equals(client.getId())) {
            throw new BusinessRuleException(
                    "The vehicle does not belong to the specified client.");
        }

        Branch branch = branchRepository.findById(request.getBranchId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Branch not found with id: " + request.getBranchId()));

        if (request.getMechanicId() != null) {
            Mechanic mechanic = mechanicRepository.findById(request.getMechanicId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Mechanic not found with id: " + request.getMechanicId()));

            if (appointmentRepository.existsByBranchIdAndScheduledAtAndMechanicIdAndIdNot(
                    branch.getId(), request.getScheduledAt(), mechanic.getId(), id)) {
                throw new ConflictException(
                        "The slot " + request.getScheduledAt() + " at this branch is already taken for this mechanic.");
            }
            appointment.setMechanic(mechanic);
        }

        appointment.setScheduledAt(request.getScheduledAt());
        appointment.setNotes(request.getNotes());
        appointment.setClient(client);
        appointment.setVehicle(vehicle);
        appointment.setBranch(branch);

        return appointmentMapper.toResponse(appointmentRepository.save(appointment));
    }

    //PATCH /confirm
    @Override
    public AppointmentResponse confirm(UUID id) {
        Appointment appointment = findOrThrow(id);

        if (appointment.getStatus() != AppointmentStatus.PENDING) {
            throw new ConflictException(
                    "Only PENDING appointments can be confirmed. Current status: "
                            + appointment.getStatus());
        }

        appointment.setStatus(AppointmentStatus.CONFIRMED);
        return appointmentMapper.toResponse(appointmentRepository.save(appointment));
    }

    //PATCH /cancel
    @Override
    public AppointmentResponse cancel(UUID id) {
        Appointment appointment = findOrThrow(id);

        if (appointment.getStatus() != AppointmentStatus.PENDING
                && appointment.getStatus() != AppointmentStatus.CONFIRMED) {
            throw new ConflictException(
                    "Only PENDING or CONFIRMED appointments can be cancelled. Current status: "
                            + appointment.getStatus());
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);
        return appointmentMapper.toResponse(appointmentRepository.save(appointment));
    }

    //POST /{id}/services
    @Override
    public AppointmentResponse addService(UUID appointmentId, AppointmentServiceRequest request) {
        Appointment appointment = findOrThrow(appointmentId);
        validateNotTerminal(appointment);

        Service service = serviceRepository.findById(request.getServiceId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Service not found with id: " + request.getServiceId()));

        if (appointmentServiceRepository.existsByAppointmentIdAndServiceId(
                appointmentId, service.getId())) {
            throw new ConflictException(
                    "This service is already added to the appointment.");
        }

        var pivotService = com.grupo9.auto_repair_shop.entity.appointment.AppointmentService.builder()
                .appointment(appointment)
                .service(service)
                .build();

        appointmentServiceRepository.save(pivotService);
        return appointmentMapper.toResponse(findOrThrow(appointmentId));
    }

    //DELETE /{id}/services/{serviceId}
    @Override
    public AppointmentResponse removeService(UUID appointmentId, UUID serviceId) {
        Appointment appointment = findOrThrow(appointmentId);
        validateNotTerminal(appointment);

        var pivotService = appointmentServiceRepository
                .findByAppointmentIdAndServiceId(appointmentId, serviceId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Service not found in this appointment."));

        appointmentServiceRepository.delete(pivotService);
        return appointmentMapper.toResponse(findOrThrow(appointmentId));
    }

    //helpers privados
    private Appointment findOrThrow(UUID id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Appointment not found with id: " + id));
    }

    private void validateNotTerminal(Appointment appointment) {
        if (appointment.getStatus() == AppointmentStatus.DONE
                || appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new ConflictException(
                    "Cannot modify an appointment in status: " + appointment.getStatus());
        }
    }
}