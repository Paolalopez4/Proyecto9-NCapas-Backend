package com.grupo9.auto_repair_shop.dto.response.common;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ApiResponse<T>(

        boolean success,
        String message,
        T data,
        LocalDateTime timestamp

) {
}