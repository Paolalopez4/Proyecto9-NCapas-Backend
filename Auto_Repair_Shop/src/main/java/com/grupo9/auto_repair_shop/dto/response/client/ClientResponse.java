package com.grupo9.auto_repair_shop.dto.response.client;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class ClientResponse {

    private UUID id;
    private String phone;
    private String address;
    private LocalDateTime createdAt;

    private UUID userId;
    private String userName;
    private String userEmail;
}
