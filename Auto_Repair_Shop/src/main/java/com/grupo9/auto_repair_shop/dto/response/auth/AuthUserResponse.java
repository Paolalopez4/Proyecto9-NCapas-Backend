package com.grupo9.auto_repair_shop.dto.response.auth;

import com.grupo9.auto_repair_shop.enums.UserRole;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthUserResponse {

    private UUID id;

    private String name;

    private String email;

    private UserRole role;
}