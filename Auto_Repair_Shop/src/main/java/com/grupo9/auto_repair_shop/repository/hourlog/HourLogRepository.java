package com.grupo9.auto_repair_shop.repository.hourlog;

import com.grupo9.auto_repair_shop.entity.hourlog.HourLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface HourLogRepository extends JpaRepository<HourLog, UUID> {

    Page<HourLog> findByWorkOrderId(UUID workOrderId, Pageable pageable);

    Page<HourLog> findByMechanicId(UUID mechanicId, Pageable pageable);
}
