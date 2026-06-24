package com.grupo9.auto_repair_shop.dto.response.budget;

import com.grupo9.auto_repair_shop.enums.ItemType;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BudgetItemResponse {

    private UUID id;
    private ItemType itemType;
    private String description;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal discount;
    private BigDecimal subtotal;
    private UUID budgetId;
}
