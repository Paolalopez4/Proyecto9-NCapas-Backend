package com.grupo9.auto_repair_shop.mapper.budget;

import com.grupo9.auto_repair_shop.dto.response.budget.BudgetItemResponse;
import com.grupo9.auto_repair_shop.entity.budget.BudgetItem;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class BudgetItemMapper {

    public BudgetItemResponse toResponse(BudgetItem item) {
        BigDecimal subtotal = calculateSubtotal(item);

        return BudgetItemResponse.builder()
                .id(item.getId())
                .itemType(item.getItemType())
                .description(item.getDescription())
                .quantity(item.getQuantity())
                .unitPrice(item.getUnitPrice())
                .discount(item.getDiscount())
                .subtotal(subtotal)
                .budgetId(item.getBudget().getId())
                .build();
    }

    private BigDecimal calculateSubtotal(BudgetItem item) {
        BigDecimal quantity = BigDecimal.valueOf(item.getQuantity());
        BigDecimal unitPrice = item.getUnitPrice();
        BigDecimal discount = item.getDiscount() != null ? item.getDiscount() : BigDecimal.ZERO;

        BigDecimal discountFactor = BigDecimal.ONE
                .subtract(discount.divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP));

        return quantity.multiply(unitPrice).multiply(discountFactor)
                .setScale(2, RoundingMode.HALF_UP);
    }
}
