package com.grupo9.auto_repair_shop.entity.client;

import com.grupo9.auto_repair_shop.entity.BaseEntity;
import com.grupo9.auto_repair_shop.entity.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "clients")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Client extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(length = 30)
    private String phone;

    @Column(length = 255)
    private String address;
}