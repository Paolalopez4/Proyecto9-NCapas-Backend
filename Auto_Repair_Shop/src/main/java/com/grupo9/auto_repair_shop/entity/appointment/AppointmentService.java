package com.grupo9.auto_repair_shop.entity.appointment;

import com.grupo9.auto_repair_shop.entity.service.Service;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(
        name = "appointment_services",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "unique_appointment_service",
                        columnNames = {"appointment_id", "service_id"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentService {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id", nullable = false)
    private Appointment appointment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", nullable = false)
    private Service service;
}