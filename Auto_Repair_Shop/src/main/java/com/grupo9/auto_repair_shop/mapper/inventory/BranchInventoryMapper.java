package com.grupo9.auto_repair_shop.mapper.inventory;

import com.grupo9.auto_repair_shop.dto.request.inventory.BranchInventoryRequest;
import com.grupo9.auto_repair_shop.dto.response.inventory.BranchInventoryResponse;
import com.grupo9.auto_repair_shop.entity.inventory.BranchInventory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface BranchInventoryMapper {

    @Mapping(target = "partId", source = "part.id")
    @Mapping(target = "partName", source = "part.name")
    @Mapping(target = "partSku", source = "part.sku")
    @Mapping(target = "partCategory", source = "part.category")
    @Mapping(target = "branchId", source = "branch.id")
    @Mapping(target = "branchName", source = "branch.name")
    @Mapping(target = "lowStock", source = ".", qualifiedByName = "calculateLowStock")
    BranchInventoryResponse toResponse(BranchInventory inventory);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "branch", ignore = true)
    @Mapping(target = "part", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(BranchInventoryRequest request, @MappingTarget BranchInventory branchInventory);

    @Named("calculateLowStock")
    default Boolean calculateLowStock(BranchInventory inventory) {
        if (inventory.getStock() == null || inventory.getStockMin() == null) {
            return false;
        }
        return inventory.getStock() < inventory.getStockMin();
    }
}
