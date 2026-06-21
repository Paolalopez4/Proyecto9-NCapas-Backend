package com.grupo9.auto_repair_shop.mapper.auth;

import com.grupo9.auto_repair_shop.dto.response.auth.AuthUserResponse;
import com.grupo9.auto_repair_shop.entity.user.User;
import org.springframework.stereotype.Component;

@Component
public class AuthMapper {

    public AuthUserResponse toAuthUserResponse(User user) {
        return AuthUserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }
}