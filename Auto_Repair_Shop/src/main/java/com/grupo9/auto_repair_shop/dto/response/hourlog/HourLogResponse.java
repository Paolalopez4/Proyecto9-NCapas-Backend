package com.grupo9.auto_repair_shop.dto.response.hourlog;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HourLogResponse {

    private UUID id;
    private BigDecimal hours;
    private LocalDateTime loggedAt;
    private String notes;
    private UUID workOrderId;
    private UUID mechanicId;
    private String mechanicName;
}
