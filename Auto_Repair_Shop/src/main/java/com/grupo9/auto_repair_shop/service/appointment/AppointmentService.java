package com.grupo9.auto_repair_shop.service.appointment;

import com.grupo9.auto_repair_shop.dto.request.appointment.AppointmentRequest;
import com.grupo9.auto_repair_shop.dto.request.appointment.AppointmentServiceRequest;
import com.grupo9.auto_repair_shop.dto.response.appointment.AppointmentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface AppointmentService {
    //CRUD
    Page<AppointmentResponse> findAll(UUID branchId, LocalDateTime date,
                                      String status, UUID mechanicId, Pageable pageable);
    AppointmentResponse findById(UUID id);
    Page<AppointmentResponse> findByClientId(UUID clientId, Pageable pageable);
    List<LocalDateTime> getAvailableSlots(UUID branchId, LocalDateTime date);
    AppointmentResponse create(AppointmentRequest request);
    AppointmentResponse update(UUID id, AppointmentRequest request);

    //estado
    AppointmentResponse confirm(UUID id);
    AppointmentResponse cancel(UUID id);

    //appointment service
    AppointmentResponse addService(UUID appointmentId, AppointmentServiceRequest request);
    AppointmentResponse removeService(UUID appointmentId, UUID serviceId);
}