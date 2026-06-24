package com.grupo9.auto_repair_shop.service.inventory;

import com.grupo9.auto_repair_shop.dto.request.inventory.BranchInventoryRequest;
import com.grupo9.auto_repair_shop.dto.response.common.PageResponse;
import com.grupo9.auto_repair_shop.dto.response.inventory.BranchInventoryResponse;

import java.util.UUID;

public interface BranchInventoryService {

    BranchInventoryResponse create(UUID branchId, BranchInventoryRequest request);

    BranchInventoryResponse findByBranchAndPart(UUID branchId, UUID partId);

    PageResponse<BranchInventoryResponse> findAllByBranch(UUID branchId, int page, int size);

    PageResponse<BranchInventoryResponse> findLowStockByBranch(UUID branchId, int page, int size);

    BranchInventoryResponse update(UUID branchId, UUID partId, BranchInventoryRequest request);

    BranchInventoryResponse consumeStock(UUID branchId, UUID partId, Integer quantity);
}
