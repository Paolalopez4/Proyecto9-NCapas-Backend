package com.grupo9.auto_repair_shop.mapper.user;

import com.grupo9.auto_repair_shop.dto.response.user.UserResponse;
import com.grupo9.auto_repair_shop.entity.user.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .active(user.getActive())
                .createdAt(user.getCreatedAt())
                .build();
    }
}