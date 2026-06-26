package com.grupo9.auto_repair_shop.repository.repairhistory;

import com.grupo9.auto_repair_shop.entity.repairhistory.RepairHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RepairHistoryRepository extends JpaRepository<RepairHistory, UUID> {

    //GET paginado filtrado por fecha
    Page<RepairHistory> findByRepairDateBetween(LocalDate from, LocalDate to, Pageable pageable);
    Page<RepairHistory> findByRepairDateAfter(LocalDate from, Pageable pageable);
    Page<RepairHistory> findByRepairDateBefore(LocalDate to, Pageable pageable);

    //GET por vehiculo
    List<RepairHistory> findByVehicleId(UUID vehicleId);

    //validacion
    Optional<RepairHistory> findByWorkOrderId(UUID workOrderId);
}