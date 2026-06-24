package com.grupo9.auto_repair_shop.service.repairhistory;

import com.grupo9.auto_repair_shop.dto.response.repairhistory.RepairHistoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface RepairHistoryService {
    Page<RepairHistoryResponse> findAll(LocalDate from, LocalDate to, Pageable pageable);
    RepairHistoryResponse findById(UUID id);
    List<RepairHistoryResponse> findByVehicleId(UUID vehicleId);
}