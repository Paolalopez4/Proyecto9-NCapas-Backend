package com.grupo9.auto_repair_shop.repository.reminder;

import com.grupo9.auto_repair_shop.entity.reminder.MaintenanceReminder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MaintenanceReminderRepository extends JpaRepository<MaintenanceReminder, UUID> {

    Page<MaintenanceReminder> findByVehicleId(UUID vehicleId, Pageable pageable);

    Page<MaintenanceReminder> findByClientId(UUID clientId, Pageable pageable);

    Page<MaintenanceReminder> findBySent(Boolean sent, Pageable pageable);

    Page<MaintenanceReminder> findByAcknowledged(Boolean acknowledged, Pageable pageable);

}