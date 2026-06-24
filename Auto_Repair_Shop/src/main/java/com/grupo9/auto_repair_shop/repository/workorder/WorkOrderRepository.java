package com.grupo9.auto_repair_shop.repository.workorder;

import com.grupo9.auto_repair_shop.entity.workorder.WorkOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WorkOrderRepository extends JpaRepository<WorkOrder, UUID> {
}
