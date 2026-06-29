package com.grupo9.auto_repair_shop.repository.workorder;

import com.grupo9.auto_repair_shop.entity.workorder.WorkOrderPart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface WorkOrderPartRepository extends JpaRepository<WorkOrderPart, UUID> {
    Optional<WorkOrderPart> findByWorkOrderIdAndPartId(UUID workOrderId, UUID partId);
    boolean existsByWorkOrderIdAndPartId(UUID workOrderId, UUID partId);
}