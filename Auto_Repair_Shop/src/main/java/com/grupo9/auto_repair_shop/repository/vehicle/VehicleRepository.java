package com.grupo9.auto_repair_shop.repository.vehicle;

import com.grupo9.auto_repair_shop.entity.vehicle.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VehicleRepository extends JpaRepository<Vehicle, UUID> {

    boolean existsByPlateIgnoreCase(String plate);

    boolean existsByVinIgnoreCase(String vin);

    boolean existsByPlateIgnoreCaseAndIdNot(String plate, UUID id);

    boolean existsByVinIgnoreCaseAndIdNot(String vin, UUID id);

    Page<Vehicle> findByClientId(UUID clientId, Pageable pageable);

}
