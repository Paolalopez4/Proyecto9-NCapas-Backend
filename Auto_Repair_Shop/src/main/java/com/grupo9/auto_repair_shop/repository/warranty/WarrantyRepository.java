package com.grupo9.auto_repair_shop.repository.warranty;

import com.grupo9.auto_repair_shop.entity.warranty.Warranty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface WarrantyRepository extends JpaRepository<Warranty, UUID> {

    Optional<Warranty> findByWorkOrderId(UUID workOrderId);
}
