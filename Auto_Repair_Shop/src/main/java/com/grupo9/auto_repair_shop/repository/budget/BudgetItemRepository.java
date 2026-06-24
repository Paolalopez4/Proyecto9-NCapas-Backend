package com.grupo9.auto_repair_shop.repository.budget;

import com.grupo9.auto_repair_shop.entity.budget.BudgetItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BudgetItemRepository extends JpaRepository<BudgetItem, UUID> {

    List<BudgetItem> findByBudgetId(UUID budgetId);
}
