package com.grupo9.auto_repair_shop.service.hourlog;

import com.grupo9.auto_repair_shop.dto.request.hourlog.HourLogRequest;
import com.grupo9.auto_repair_shop.dto.response.common.PageResponse;
import com.grupo9.auto_repair_shop.dto.response.hourlog.HourLogResponse;
import com.grupo9.auto_repair_shop.entity.hourlog.HourLog;
import com.grupo9.auto_repair_shop.entity.mechanic.Mechanic;
import com.grupo9.auto_repair_shop.entity.workorder.WorkOrder;
import com.grupo9.auto_repair_shop.enums.WorkOrderStatus;
import com.grupo9.auto_repair_shop.exception.BusinessRuleException;
import com.grupo9.auto_repair_shop.exception.ForbiddenException;
import com.grupo9.auto_repair_shop.exception.ResourceNotFoundException;
import com.grupo9.auto_repair_shop.mapper.hourlog.HourLogMapper;
import com.grupo9.auto_repair_shop.repository.hourlog.HourLogRepository;
import com.grupo9.auto_repair_shop.repository.mechanic.MechanicRepository;
import com.grupo9.auto_repair_shop.repository.workorder.WorkOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class HourLogServiceImpl implements HourLogService {

    private final HourLogRepository hourLogRepository;
    private final WorkOrderRepository workOrderRepository;
    private final MechanicRepository mechanicRepository;
    private final HourLogMapper hourLogMapper;

    @Override
    public HourLogResponse create(UUID workOrderId, HourLogRequest request, String mechanicEmail) {

        WorkOrder workOrder = workOrderRepository.findById(workOrderId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Orden de trabajo no encontrada con id: " + workOrderId
                ));

        if (workOrder.getStatus() != WorkOrderStatus.IN_PROGRESS) {
            throw new BusinessRuleException(
                    "Solo se pueden registrar horas en una orden con estado IN_PROGRESS"
            );
        }

        Mechanic mechanic = mechanicRepository.findByUserEmail(mechanicEmail)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Mecánico no encontrado para el usuario: " + mechanicEmail
                ));

        if (!workOrder.getMechanic().getId().equals(mechanic.getId())) {
            throw new ForbiddenException(
                    "El mecánico no está asignado a esta orden de trabajo"
            );
        }

        HourLog hourLog = HourLog.builder()
                .workOrder(workOrder)
                .mechanic(mechanic)
                .hours(request.getHours())
                .notes(request.getNotes())
                .loggedAt(LocalDateTime.now())
                .build();

        HourLog saved = hourLogRepository.save(hourLog);

        return hourLogMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<HourLogResponse> findByWorkOrder(UUID workOrderId, int page, int size) {

        if (!workOrderRepository.existsById(workOrderId)) {
            throw new ResourceNotFoundException(
                    "Orden de trabajo no encontrada con id: " + workOrderId
            );
        }

        PageRequest pageRequest = PageRequest.of(
                page, size, Sort.by(Sort.Direction.DESC, "loggedAt")
        );

        Page<HourLogResponse> result = hourLogRepository
                .findByWorkOrderId(workOrderId, pageRequest)
                .map(hourLogMapper::toResponse);

        return PageResponse.from(result);
    }

    @Override
    @Transactional(readOnly = true)
    public HourLogResponse findById(UUID workOrderId, UUID id) {

        HourLog hourLog = hourLogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Registro de horas no encontrado con id: " + id
                ));

        if (!hourLog.getWorkOrder().getId().equals(workOrderId)) {
            throw new ResourceNotFoundException(
                    "El registro de horas no pertenece a la orden indicada"
            );
        }

        return hourLogMapper.toResponse(hourLog);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<HourLogResponse> findByMechanic(UUID mechanicId, int page, int size) {

        if (!mechanicRepository.existsById(mechanicId)) {
            throw new ResourceNotFoundException(
                    "Mecánico no encontrado con id: " + mechanicId
            );
        }

        PageRequest pageRequest = PageRequest.of(
                page, size, Sort.by(Sort.Direction.DESC, "loggedAt")
        );

        Page<HourLogResponse> result = hourLogRepository
                .findByMechanicId(mechanicId, pageRequest)
                .map(hourLogMapper::toResponse);

        return PageResponse.from(result);
    }

    @Override
    public HourLogResponse update(UUID workOrderId, UUID id, HourLogRequest request) {

        HourLog hourLog = hourLogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Registro de horas no encontrado con id: " + id
                ));

        if (!hourLog.getWorkOrder().getId().equals(workOrderId)) {
            throw new ResourceNotFoundException(
                    "El registro de horas no pertenece a la orden indicada"
            );
        }

        hourLog.setHours(request.getHours());
        hourLog.setNotes(request.getNotes());

        HourLog updated = hourLogRepository.save(hourLog);

        return hourLogMapper.toResponse(updated);
    }

    @Override
    public void delete(UUID workOrderId, UUID id) {

        HourLog hourLog = hourLogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Registro de horas no encontrado con id: " + id
                ));

        if (!hourLog.getWorkOrder().getId().equals(workOrderId)) {
            throw new ResourceNotFoundException(
                    "El registro de horas no pertenece a la orden indicada"
            );
        }

        hourLogRepository.delete(hourLog);
    }
}
