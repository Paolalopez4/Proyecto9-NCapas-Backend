package com.grupo9.auto_repair_shop.dto.response.supplier;

import jakarta.persistence.Column;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplierResponse {
    private UUID id;
    private String name;
    private String contactName;
    private String email;
    private String phone;
    private LocalDateTime createdAt;
}
