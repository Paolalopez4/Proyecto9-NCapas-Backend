package com.grupo9.auto_repair_shop.entity.repairhistory;

import com.grupo9.auto_repair_shop.entity.vehicle.Vehicle;
import com.grupo9.auto_repair_shop.entity.workorder.WorkOrder;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "repair_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RepairHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "work_order_id", nullable = false)
    private WorkOrder workOrder;

    @Column(columnDefinition = "TEXT")
    private String summary;

    @Column(name = "repair_date")
    private LocalDate repairDate;

    @Column(name = "mileage_at_repair")
    private Integer mileageAtRepair;
}