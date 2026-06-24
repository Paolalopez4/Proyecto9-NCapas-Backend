package com.grupo9.auto_repair_shop.service.budget;

import com.grupo9.auto_repair_shop.dto.request.budget.BudgetItemRequest;
import com.grupo9.auto_repair_shop.dto.request.budget.BudgetRequest;
import com.grupo9.auto_repair_shop.dto.response.budget.BudgetItemResponse;
import com.grupo9.auto_repair_shop.dto.response.budget.BudgetResponse;

import java.util.UUID;

public interface BudgetService {

    BudgetResponse create(UUID workOrderId, BudgetRequest request);

    BudgetResponse findByWorkOrder(UUID workOrderId);

    BudgetResponse update(UUID workOrderId, BudgetRequest request);

    BudgetResponse send(UUID workOrderId);

    BudgetResponse approve(UUID workOrderId, String clientEmail);

    BudgetResponse reject(UUID workOrderId, String clientEmail);

    BudgetItemResponse addItem(UUID workOrderId, BudgetItemRequest request);

    BudgetItemResponse updateItem(UUID workOrderId, UUID itemId, BudgetItemRequest request);

    void deleteItem(UUID workOrderId, UUID itemId);
}
