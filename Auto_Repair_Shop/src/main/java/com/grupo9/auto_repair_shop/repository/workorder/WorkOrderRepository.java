package com.grupo9.auto_repair_shop.repository.workorder;

import com.grupo9.auto_repair_shop.entity.workorder.WorkOrder;
import com.grupo9.auto_repair_shop.enums.OrderType;
import com.grupo9.auto_repair_shop.enums.WorkOrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Repository
public interface WorkOrderRepository extends JpaRepository<WorkOrder, UUID> {

    //filtros opcionales
    Page<WorkOrder> findByStatus(WorkOrderStatus status, Pageable pageable);
    Page<WorkOrder> findByOrderType(OrderType orderType, Pageable pageable);
    Page<WorkOrder> findByBranchId(UUID branchId, Pageable pageable);
    Page<WorkOrder> findByMechanicId(UUID mechanicId, Pageable pageable);
    Page<WorkOrder> findByStatusAndBranchId(WorkOrderStatus status, UUID branchId, Pageable pageable);

    //GET work orders de un cliente
    @Query("SELECT wo FROM WorkOrder wo WHERE wo.vehicle.client.id = :clientId")
    Page<WorkOrder> findByClientId(@Param("clientId") UUID clientId, Pageable pageable);

    //validar si un vehiculo tiene ordenes activas
    boolean existsByVehicleIdAndStatusIn(UUID vehicleId, List<WorkOrderStatus> statuses);

    //validar ordenes activas de un mecanico
    boolean existsByMechanicIdAndStatus(UUID mechanicId, WorkOrderStatus status);

    //validar si una orden POST_WARRANTY tiene garantia activa referenciada
    @Query("SELECT wo FROM WorkOrder wo WHERE wo.vehicle.id = :vehicleId " +
            "AND wo.warranty IS NOT NULL AND wo.warranty.active = true " +
            "AND wo.warranty.endDate >= CURRENT_DATE")
    List<WorkOrder> findActiveWarrantiesByVehicleId(@Param("vehicleId") UUID vehicleId);

    //mostrar eficiencia en mechanics
    long countByMechanicIdAndStatus(UUID mechanicId, WorkOrderStatus status);

    @Query("""
        SELECT COALESCE(SUM(wos.unitPrice * wos.quantity), 0)
        FROM WorkOrderService wos
        WHERE wos.workOrder.mechanic.id = :mechanicId
        AND wos.workOrder.status = 'DONE'
    """)
    BigDecimal sumServiceRevenueByMechanicId(@Param("mechanicId") UUID mechanicId);

    @Query("""
        SELECT COALESCE(SUM(wop.unitPrice * wop.quantity), 0)
        FROM WorkOrderPart wop
        WHERE wop.workOrder.mechanic.id = :mechanicId
        AND wop.workOrder.status = 'DONE'
    """)
    BigDecimal sumPartRevenueByMechanicId(@Param("mechanicId") UUID mechanicId);
}
