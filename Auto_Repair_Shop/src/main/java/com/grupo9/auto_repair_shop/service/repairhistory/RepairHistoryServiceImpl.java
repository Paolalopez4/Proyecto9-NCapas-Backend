package com.grupo9.auto_repair_shop.service.repairhistory;

import com.grupo9.auto_repair_shop.dto.response.repairhistory.RepairHistoryResponse;
import com.grupo9.auto_repair_shop.entity.repairhistory.RepairHistory;
import com.grupo9.auto_repair_shop.exception.ResourceNotFoundException;
import com.grupo9.auto_repair_shop.mapper.repairhistory.RepairHistoryMapper;
import com.grupo9.auto_repair_shop.repository.repairhistory.RepairHistoryRepository;
import com.grupo9.auto_repair_shop.repository.vehicle.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RepairHistoryServiceImpl implements RepairHistoryService {
    private final RepairHistoryRepository repairHistoryRepository;
    private final VehicleRepository vehicleRepository;
    private final RepairHistoryMapper repairHistoryMapper;

    //GET paginado con filtro opcional por fecha
    @Override
    public Page<RepairHistoryResponse> findAll(LocalDate from, LocalDate to, Pageable pageable) {
        Page<RepairHistory> page;

        if (from != null && to != null) {
            page = repairHistoryRepository.findByRepairDateBetween(from, to, pageable);
        } else if (from != null) {
            page = repairHistoryRepository.findByRepairDateAfter(from, pageable);
        } else if (to != null) {
            page = repairHistoryRepository.findByRepairDateBefore(to, pageable);
        } else {
            page = repairHistoryRepository.findAll(pageable);
        }

        return page.map(repairHistoryMapper::toResponse);
    }

    //GET por ID
    @Override
    public RepairHistoryResponse findById(UUID id) {
        return repairHistoryMapper.toResponse(findOrThrow(id));
    }

    //GET por vehiculo
    @Override
    public List<RepairHistoryResponse> findByVehicleId(UUID vehicleId) {
        vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Vehicle not found with id: " + vehicleId));

        return repairHistoryRepository.findByVehicleId(vehicleId)
                .stream()
                .map(repairHistoryMapper::toResponse)
                .toList();
    }

    //helper privado
    private RepairHistory findOrThrow(UUID id) {
        return repairHistoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Repair history not found with id: " + id));
    }
}