package com.grupo9.auto_repair_shop.dto.response.user;

import com.grupo9.auto_repair_shop.enums.UserRole;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

    private UUID id;

    private String name;

    private String email;

    private UserRole role;

    private Boolean active;

    private LocalDateTime createdAt;
}