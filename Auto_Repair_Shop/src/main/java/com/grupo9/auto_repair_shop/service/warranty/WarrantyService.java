package com.grupo9.auto_repair_shop.service.warranty;

import com.grupo9.auto_repair_shop.dto.response.common.PageResponse;
import com.grupo9.auto_repair_shop.dto.response.warranty.WarrantyResponse;

import java.util.UUID;

public interface WarrantyService {

    PageResponse<WarrantyResponse> findAll(int page, int size);

    WarrantyResponse findById(UUID id);

    WarrantyResponse findByWorkOrder(UUID workOrderId);

    PageResponse<WarrantyResponse> findByClient(UUID clientId, int page, int size);

    WarrantyResponse deactivate(UUID id);
}
