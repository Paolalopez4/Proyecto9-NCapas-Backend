package com.grupo9.auto_repair_shop.mapper.budget;

import com.grupo9.auto_repair_shop.dto.response.budget.BudgetResponse;
import com.grupo9.auto_repair_shop.entity.budget.Budget;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BudgetMapper {

    private final BudgetItemMapper budgetItemMapper;

    public BudgetResponse toResponse(Budget budget) {
        return BudgetResponse.builder()
                .id(budget.getId())
                .taxRate(budget.getTaxRate())
                .total(budget.getTotal())
                .status(budget.getStatus())
                .sentAt(budget.getSentAt())
                .respondedAt(budget.getRespondedAt())
                .workOrderId(budget.getWorkOrder().getId())
                .items(budget.getItems() != null
                        ? budget.getItems().stream()
                          .map(budgetItemMapper::toResponse)
                          .collect(Collectors.toList())
                        : Collections.emptyList())
                .build();
    }
}
