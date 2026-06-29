package com.grupo9.auto_repair_shop.repository.hourlog;

import com.grupo9.auto_repair_shop.entity.hourlog.HourLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.UUID;

public interface HourLogRepository extends JpaRepository<HourLog, UUID> {

    Page<HourLog> findByWorkOrderId(UUID workOrderId, Pageable pageable);

    Page<HourLog> findByMechanicId(UUID mechanicId, Pageable pageable);

    @Query("SELECT COALESCE(SUM(hl.hours), 0) FROM HourLog hl WHERE hl.mechanic.id = :mechanicId")
    BigDecimal sumHoursByMechanicId(@Param("mechanicId") UUID mechanicId);
}
