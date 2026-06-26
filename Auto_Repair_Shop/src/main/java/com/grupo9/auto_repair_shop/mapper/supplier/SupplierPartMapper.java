package com.grupo9.auto_repair_shop.mapper.supplier;

import com.grupo9.auto_repair_shop.dto.response.supplier.SupplierPartResponse;
import com.grupo9.auto_repair_shop.entity.supplier.SupplierPart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SupplierPartMapper {
    @Mapping(source = "supplier.id", target = "supplierId")
    @Mapping(source = "supplier.name", target = "supplierName")
    @Mapping(source = "part.id", target = "partId")
    @Mapping(source = "part.name", target = "partName")
    @Mapping(source = "part.sku", target = "partSku")
    SupplierPartResponse toResponse(SupplierPart supplierPart);
}
