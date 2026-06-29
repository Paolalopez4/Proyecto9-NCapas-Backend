package com.grupo9.auto_repair_shop.dto.response.mechanic;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MechanicResponse {

    private UUID id;
    private String specialty;
    private BigDecimal hourlyRate;
    private Boolean active;
    private LocalDateTime createdAt;

    private UUID userId;
    private String userName;
    private String userEmail;

    private UUID branchId;
    private String branchName;

}
