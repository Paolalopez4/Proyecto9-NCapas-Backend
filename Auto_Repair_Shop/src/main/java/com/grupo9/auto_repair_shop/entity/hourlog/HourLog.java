package com.grupo9.auto_repair_shop.entity.hourlog;

import com.grupo9.auto_repair_shop.entity.mechanic.Mechanic;
import com.grupo9.auto_repair_shop.entity.workorder.WorkOrder;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "hour_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HourLog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "work_order_id", nullable = false)
    private WorkOrder workOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mechanic_id", nullable = false)
    private Mechanic mechanic;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal hours;

    @Column(name = "logged_at")
    private LocalDateTime loggedAt;

    @Column(columnDefinition = "TEXT")
    private String notes;
}