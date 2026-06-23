package com.grupo9.auto_repair_shop.mapper.supplier;

import com.grupo9.auto_repair_shop.dto.request.supplier.SupplierRequest;
import com.grupo9.auto_repair_shop.dto.response.supplier.SupplierResponse;
import com.grupo9.auto_repair_shop.entity.supplier.Supplier;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface SupplierMapper {
    Supplier toEntity(SupplierRequest supplierRequest);
    SupplierResponse toResponse(Supplier supplier);
    void updateEntity(SupplierRequest supplierRequest, @MappingTarget Supplier supplier);
}
