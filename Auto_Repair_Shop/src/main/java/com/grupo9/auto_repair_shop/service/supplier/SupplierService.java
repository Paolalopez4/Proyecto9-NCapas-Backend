package com.grupo9.auto_repair_shop.service.supplier;

import com.grupo9.auto_repair_shop.dto.request.supplier.SupplierPartRequest;
import com.grupo9.auto_repair_shop.dto.request.supplier.SupplierRequest;
import com.grupo9.auto_repair_shop.dto.response.supplier.SupplierPartResponse;
import com.grupo9.auto_repair_shop.dto.response.supplier.SupplierResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface SupplierService {
    //supplier
    Page<SupplierResponse> findAll(Pageable pageable);
    SupplierResponse findById(UUID id);
    SupplierResponse create(SupplierRequest request);
    SupplierResponse update(UUID id, SupplierRequest request);
    void delete(UUID id);

    //supplierpart
    List<SupplierPartResponse> findPartsBySupplier(UUID supplierId);
    SupplierPartResponse getBestSupplierForPart(UUID partId);
    SupplierPartResponse linkPart(UUID supplierId, SupplierPartRequest request);
    SupplierPartResponse updateSupplierPart(UUID supplierId, UUID partId, SupplierPartRequest request);
    void removeSupplierPart(UUID supplierId, UUID partId);
}
