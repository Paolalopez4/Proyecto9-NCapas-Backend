package com.grupo9.auto_repair_shop.service.inventory;

import com.grupo9.auto_repair_shop.dto.request.inventory.BranchInventoryRequest;
import com.grupo9.auto_repair_shop.dto.response.common.PageResponse;
import com.grupo9.auto_repair_shop.dto.response.inventory.BranchInventoryResponse;
import com.grupo9.auto_repair_shop.entity.branch.Branch;
import com.grupo9.auto_repair_shop.entity.inventory.BranchInventory;
import com.grupo9.auto_repair_shop.entity.part.Part;
import com.grupo9.auto_repair_shop.exception.BusinessRuleException;
import com.grupo9.auto_repair_shop.exception.ConflictException;
import com.grupo9.auto_repair_shop.exception.ResourceNotFoundException;
import com.grupo9.auto_repair_shop.mapper.inventory.BranchInventoryMapper;
import com.grupo9.auto_repair_shop.repository.branch.BranchRepository;
import com.grupo9.auto_repair_shop.repository.inventory.BranchInventoryRepository;
import com.grupo9.auto_repair_shop.repository.part.PartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BranchInventoryServiceImpl implements BranchInventoryService {

    private final BranchInventoryRepository branchInventoryRepository;
    private final BranchRepository branchRepository;
    private final PartRepository partRepository;
    private final BranchInventoryMapper branchInventoryMapper;

    @Override
    @Transactional
    public BranchInventoryResponse create(UUID branchId, BranchInventoryRequest request) {

        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Branch not found with id: " + branchId));

        Part part = partRepository.findById(request.getPartId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Part not found with id: " + request.getPartId()));

        if (branchInventoryRepository.existsByBranchIdAndPartId(branchId, request.getPartId())) {
            throw new ConflictException(
                    "Inventory entry already exists for this part at this branch");
        }

        if (request.getStockMin() > request.getStock()) {
            throw new BusinessRuleException(
                    "Minimum stock cannot exceed current stock");
        }

        BranchInventory inventory = BranchInventory.builder()
                .branch(branch)
                .part(part)
                .stock(request.getStock())
                .stockMin(request.getStockMin())
                .updatedAt(LocalDateTime.now())
                .build();

        BranchInventory saved = branchInventoryRepository.save(inventory);
        return branchInventoryMapper.toResponse(saved);
    }

    @Override
    public BranchInventoryResponse findByBranchAndPart(UUID branchId, UUID partId) {
        BranchInventory inventory = branchInventoryRepository
                .findByBranchIdAndPartId(branchId, partId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Inventory entry not found for this part at this branch"));
        return branchInventoryMapper.toResponse(inventory);
    }

    @Override
    public PageResponse<BranchInventoryResponse> findAllByBranch(UUID branchId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<BranchInventory> inventoryPage = branchInventoryRepository.findByBranchId(branchId, pageable);
        Page<BranchInventoryResponse> responsePage = inventoryPage.map(branchInventoryMapper::toResponse);
        return PageResponse.from(responsePage);
    }

    @Override
    public PageResponse<BranchInventoryResponse> findLowStockByBranch(UUID branchId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<BranchInventory> inventoryPage = branchInventoryRepository.findLowStockByBranchId(branchId, pageable);
        Page<BranchInventoryResponse> responsePage = inventoryPage.map(branchInventoryMapper::toResponse);
        return PageResponse.from(responsePage);
    }

    @Override
    @Transactional
    public BranchInventoryResponse update(UUID branchId, UUID partId, BranchInventoryRequest request) {

        if (!partId.equals(request.getPartId())) {
            throw new BusinessRuleException(
                    "Part ID in the request body does not match the Part ID in the URL");
        }

        BranchInventory inventory = branchInventoryRepository
                .findByBranchIdAndPartId(branchId, partId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Inventory entry not found for this part at this branch"));

        if (request.getStockMin() > request.getStock()) {
            throw new BusinessRuleException(
                    "Minimum stock cannot exceed current stock");
        }

        branchInventoryMapper.updateEntity(request, inventory);
        inventory.setUpdatedAt(LocalDateTime.now());

        BranchInventory updated = branchInventoryRepository.save(inventory);
        return branchInventoryMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public BranchInventoryResponse consumeStock(UUID branchId, UUID partId, Integer quantity) {
        BranchInventory inventory = branchInventoryRepository
                .findByBranchIdAndPartId(branchId, partId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Inventory entry not found for this part at this branch"));

        if (inventory.getStock() < quantity) {
            throw new BusinessRuleException(
                    "Insufficient stock. Current stock: " + inventory.getStock() +
                            ", Request quantity: " + quantity);
        }

        inventory.setStock(inventory.getStock() - quantity);
        inventory.setUpdatedAt(LocalDateTime.now());

        BranchInventory saved = branchInventoryRepository.save(inventory);
        return branchInventoryMapper.toResponse(saved);
    }
}