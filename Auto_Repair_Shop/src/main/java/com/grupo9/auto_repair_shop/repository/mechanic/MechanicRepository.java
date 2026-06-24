package com.grupo9.auto_repair_shop.repository.mechanic;

import com.grupo9.auto_repair_shop.entity.mechanic.Mechanic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MechanicRepository extends JpaRepository<Mechanic, UUID> {

    Optional<Mechanic> findByUserEmail(String email);
}
