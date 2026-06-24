package com.grupo9.auto_repair_shop.service.part;

import com.grupo9.auto_repair_shop.dto.request.part.PartRequest;
import com.grupo9.auto_repair_shop.dto.response.common.PageResponse;
import com.grupo9.auto_repair_shop.dto.response.part.PartResponse;

import java.util.UUID;

public interface PartService {

    PartResponse create(PartRequest partRequest);

    PartResponse findById(UUID id);

    PageResponse<PartResponse> findAll(int page, int size);

    PartResponse update(UUID id, PartRequest partRequest);

    void updateActive(UUID id, boolean active);

    void delete(UUID id);

}
