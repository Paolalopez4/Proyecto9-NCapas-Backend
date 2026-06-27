package com.grupo9.auto_repair_shop.repository.appointment;

import com.grupo9.auto_repair_shop.entity.appointment.AppointmentService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AppointmentServiceRepository extends JpaRepository<AppointmentService, UUID> {
    Optional<AppointmentService> findByAppointmentIdAndServiceId(UUID appointmentId, UUID serviceId);
    boolean existsByAppointmentIdAndServiceId(UUID appointmentId, UUID serviceId);
}