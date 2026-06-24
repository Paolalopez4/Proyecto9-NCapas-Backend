package com.grupo9.auto_repair_shop.service.hourlog;

import com.grupo9.auto_repair_shop.dto.request.hourlog.HourLogRequest;
import com.grupo9.auto_repair_shop.dto.response.common.PageResponse;
import com.grupo9.auto_repair_shop.dto.response.hourlog.HourLogResponse;

import java.util.UUID;

public interface HourLogService {

    HourLogResponse create(UUID workOrderId, HourLogRequest request, String mechanicEmail);

    PageResponse<HourLogResponse> findByWorkOrder(UUID workOrderId, int page, int size);

    HourLogResponse findById(UUID workOrderId, UUID id);

    PageResponse<HourLogResponse> findByMechanic(UUID mechanicId, int page, int size);

    HourLogResponse update(UUID workOrderId, UUID id, HourLogRequest request);

    void delete(UUID workOrderId, UUID id);
}
