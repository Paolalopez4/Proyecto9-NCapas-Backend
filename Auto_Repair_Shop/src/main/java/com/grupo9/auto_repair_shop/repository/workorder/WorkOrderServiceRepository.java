package com.grupo9.auto_repair_shop.repository.workorder;

import com.grupo9.auto_repair_shop.entity.workorder.WorkOrderService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface WorkOrderServiceRepository extends JpaRepository<WorkOrderService, UUID> {
    Optional<WorkOrderService> findByWorkOrderIdAndServiceId(UUID workOrderId, UUID serviceId);
    boolean existsByWorkOrderIdAndServiceId(UUID workOrderId, UUID serviceId);
}