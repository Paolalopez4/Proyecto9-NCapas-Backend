package com.grupo9.auto_repair_shop.entity.branch;

import com.grupo9.auto_repair_shop.entity.BaseEntity;
import com.grupo9.auto_repair_shop.entity.inventory.BranchInventory;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "branches")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Branch extends BaseEntity {

    @Column(nullable = false, length = 150)
    private String name;

    @Column(length = 255)
    private String address;

    @Column(length = 30)
    private String phone;

    @Column(nullable = false)
    private Boolean active = true;

    @OneToMany(mappedBy = "branch")
    private List<BranchInventory> inventory;
}