package com.grupo9.auto_repair_shop.service.branch;

import com.grupo9.auto_repair_shop.dto.request.branch.BranchRequest;
import com.grupo9.auto_repair_shop.dto.request.branch.UpdateBranchRequest;
import com.grupo9.auto_repair_shop.dto.response.branch.BranchResponse;
import com.grupo9.auto_repair_shop.dto.response.common.PageResponse;

import java.util.UUID;

public interface BranchService {

    BranchResponse create(BranchRequest request);

    PageResponse<BranchResponse> findAll(Boolean active, int page, int size);

    BranchResponse findById(UUID id);

    BranchResponse update(UUID id, UpdateBranchRequest request);

    BranchResponse updateActive(UUID id, Boolean active);

    void delete(UUID id);
}