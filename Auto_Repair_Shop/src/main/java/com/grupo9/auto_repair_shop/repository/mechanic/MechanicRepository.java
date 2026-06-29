package com.grupo9.auto_repair_shop.repository.mechanic;

import com.grupo9.auto_repair_shop.entity.mechanic.Mechanic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MechanicRepository extends JpaRepository<Mechanic, UUID> {

    Optional<Mechanic> findByUserEmail(String email);

    boolean existsByUserId(UUID userId);

    Optional<Mechanic> findByUserId(UUID userId);

    Page<Mechanic> findByBranchId(UUID branchId, Pageable pageable);

    Page<Mechanic> findBySpecialtyContainingIgnoreCase(String specialty, Pageable pageable);

    Page<Mechanic> findByActive(Boolean active, Pageable pageable);

    List<Mechanic> findByBranchId(UUID branchId);

    List<Mechanic> findByActive(Boolean active);
}
