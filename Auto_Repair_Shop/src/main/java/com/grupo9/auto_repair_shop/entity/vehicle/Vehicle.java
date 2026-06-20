package com.grupo9.auto_repair_shop.entity.vehicle;

import com.grupo9.auto_repair_shop.entity.BaseEntity;
import com.grupo9.auto_repair_shop.entity.client.Client;
import com.grupo9.auto_repair_shop.entity.repairhistory.RepairHistory;
import com.grupo9.auto_repair_shop.entity.workorder.WorkOrder;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "vehicles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vehicle extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Column(nullable = false, unique = true, length = 50)
    private String plate;

    @Column(length = 100)
    private String brand;

    @Column(length = 100)
    private String model;

    @Column(name = "year")
    private Integer year;

    @Column(length = 50)
    private String color;

    @Column(unique = true, length = 100)
    private String vin;

    @Column(name = "mileage")
    private Integer mileage = 0;

    @OneToMany(mappedBy = "vehicle")
    private List<WorkOrder> workOrders;

    @OneToMany(mappedBy = "vehicle")
    private List<RepairHistory> repairHistory;
}