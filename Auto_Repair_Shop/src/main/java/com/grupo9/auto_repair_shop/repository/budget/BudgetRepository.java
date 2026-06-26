package com.grupo9.auto_repair_shop.repository.budget;

import com.grupo9.auto_repair_shop.entity.budget.Budget;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface BudgetRepository extends JpaRepository<Budget, UUID> {

    Optional<Budget> findByWorkOrderId(UUID workOrderId);

    boolean existsByWorkOrderId(UUID workOrderId);
}
