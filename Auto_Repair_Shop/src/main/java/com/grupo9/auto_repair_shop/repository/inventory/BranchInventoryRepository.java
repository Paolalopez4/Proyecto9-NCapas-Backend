package com.grupo9.auto_repair_shop.repository.inventory;

import com.grupo9.auto_repair_shop.entity.inventory.BranchInventory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface BranchInventoryRepository extends JpaRepository<BranchInventory, UUID> {

    boolean existsByBranchIdAndPartId(UUID branchId, UUID partId);

    Optional<BranchInventory> findByBranchIdAndPartId(UUID branchId, UUID partId);

    Page<BranchInventory> findByBranchId(UUID branchId, Pageable pageable);

    @Query("SELECT bi FROM BranchInventory bi WHERE bi.branch.id = :branchId AND bi.stock < bi.stockMin")
    Page<BranchInventory> findLowStockByBranchId(@Param("branchId") UUID branchId, Pageable pageable);

}
