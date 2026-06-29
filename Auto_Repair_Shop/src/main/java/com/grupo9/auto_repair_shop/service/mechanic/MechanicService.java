package com.grupo9.auto_repair_shop.service.mechanic;

import com.grupo9.auto_repair_shop.dto.request.mechanic.MechanicRequest;
import com.grupo9.auto_repair_shop.dto.request.mechanic.UpdateMechanicRequest;
import com.grupo9.auto_repair_shop.dto.response.common.PageResponse;
import com.grupo9.auto_repair_shop.dto.response.mechanic.MechanicEfficiencyResponse;
import com.grupo9.auto_repair_shop.dto.response.mechanic.MechanicResponse;

import java.util.List;
import java.util.UUID;

public interface MechanicService {

    MechanicResponse create(MechanicRequest request);

    MechanicResponse findById(UUID id);

    PageResponse<MechanicResponse> findAll(UUID branchId, String specialty, Boolean active, int page, int size);

    MechanicResponse update(UUID id, UpdateMechanicRequest request);

    MechanicResponse updateActive(UUID id, Boolean active);

    List<MechanicEfficiencyResponse> getEfficiencyReport(UUID branchId, Boolean active);
}