package com.grupo9.auto_repair_shop.dto.response.budget;

import com.grupo9.auto_repair_shop.enums.BudgetStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BudgetResponse {

    private UUID id;
    private BigDecimal taxRate;
    private BigDecimal total;
    private BudgetStatus status;
    private LocalDateTime sentAt;
    private LocalDateTime respondedAt;
    private UUID workOrderId;
    private List<BudgetItemResponse> items;
}
