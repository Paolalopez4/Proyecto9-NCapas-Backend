package com.grupo9.auto_repair_shop.entity.supplier;

import com.grupo9.auto_repair_shop.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "suppliers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Supplier extends BaseEntity {

    @Column(nullable = false, length = 150)
    private String name;

    @Column(name = "contact_name", length = 150)
    private String contactName;

    @Column(length = 150)
    private String email;

    @Column(length = 30)
    private String phone;
}