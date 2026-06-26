package com.grupo9.auto_repair_shop.service.supplier;

import com.grupo9.auto_repair_shop.dto.request.supplier.SupplierPartRequest;
import com.grupo9.auto_repair_shop.dto.request.supplier.SupplierRequest;
import com.grupo9.auto_repair_shop.dto.response.supplier.SupplierPartResponse;
import com.grupo9.auto_repair_shop.dto.response.supplier.SupplierResponse;
import com.grupo9.auto_repair_shop.entity.part.Part;
import com.grupo9.auto_repair_shop.entity.supplier.Supplier;
import com.grupo9.auto_repair_shop.entity.supplier.SupplierPart;
import com.grupo9.auto_repair_shop.exception.ConflictException;
import com.grupo9.auto_repair_shop.exception.ResourceNotFoundException;
import com.grupo9.auto_repair_shop.mapper.supplier.SupplierMapper;
import com.grupo9.auto_repair_shop.mapper.supplier.SupplierPartMapper;
import com.grupo9.auto_repair_shop.repository.part.PartRepository;
import com.grupo9.auto_repair_shop.repository.supplier.SupplierPartRepository;
import com.grupo9.auto_repair_shop.repository.supplier.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;
    private final SupplierPartRepository supplierPartRepository;
    private final PartRepository partRepository;
    private final SupplierMapper supplierMapper;
    private final SupplierPartMapper supplierPartMapper;

    //supplier GET paginado
    @Override
    public Page<SupplierResponse> findAll(Pageable pageable) {
        return supplierRepository.findAll(pageable)
                .map(supplierMapper::toResponse);
    }

    //supplier GET por id
    @Override
    public SupplierResponse findById(UUID id) {
        return supplierMapper.toResponse(findSupplierOrThrow(id));
    }

    // supplier POST crear
    @Override
    public SupplierResponse create(SupplierRequest request) {
        validateSupplierBusinessRules(request, null);
        Supplier supplier = supplierMapper.toEntity(request);
        return supplierMapper.toResponse(supplierRepository.save(supplier));
    }

    //supplier PUT actualizar
    @Override
    public SupplierResponse update(UUID id, SupplierRequest request) {
        Supplier supplier = findSupplierOrThrow(id);
        validateSupplierBusinessRules(request, id);
        supplierMapper.updateEntity(request, supplier);
        return supplierMapper.toResponse(supplierRepository.save(supplier));
    }

    //supplier DELETE
    @Override
    public void delete(UUID id) {
        Supplier supplier = findSupplierOrThrow(id);
        supplierRepository.delete(supplier);
    }

    //supplierpart GET todos los repuestos de un proveedor
    @Override
    public List<SupplierPartResponse> findPartsBySupplier(UUID supplierId) {
        findSupplierOrThrow(supplierId);
        return supplierPartRepository.findBySupplierId(supplierId)
                .stream()
                .map(supplierPartMapper::toResponse)
                .toList();
    }

    //supplierpart GET mejor proveedor para un repuesto (devuelve supplierpart con menor precio de esa part)
    @Override
    public SupplierPartResponse getBestSupplierForPart(UUID partId) {
        findPartOrThrow(partId);
        return supplierPartRepository.findByPartIdOrderByPriceAsc(partId)
                .stream()
                .findFirst()
                .map(supplierPartMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No suppliers found for part with id: " + partId));
    }

    //supplierpart POST vincular repuesto a proveedor
    @Override
    public SupplierPartResponse linkPart(UUID supplierId, SupplierPartRequest request) {
        Supplier supplier = findSupplierOrThrow(supplierId);
        Part part = findPartOrThrow(request.getPartId());

        if (supplierPartRepository.existsBySupplierIdAndPartId(supplierId, request.getPartId())) {
            throw new ConflictException(
                    "A relation between this supplier and part already exists.");
        }

        SupplierPart supplierPart = SupplierPart.builder()
                .supplier(supplier)
                .part(part)
                .price(request.getPrice())
                .leadTimeDays(request.getLeadTimeDays())
                .lastUpdated(LocalDateTime.now())
                .build();

        return supplierPartMapper.toResponse(supplierPartRepository.save(supplierPart));
    }

    //supplierpart PUT actualizar relacion proveedor y repuesto
    @Override
    public SupplierPartResponse updateSupplierPart(UUID supplierId, UUID partId, SupplierPartRequest request) {
        findSupplierOrThrow(supplierId);

        SupplierPart supplierPart = supplierPartRepository
                .findBySupplierIdAndPartId(supplierId, partId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "SupplierPart relation not found for supplier: "
                                + supplierId + " and part: " + partId));

        if (!request.getPartId().equals(partId)) {
            Part newPart = findPartOrThrow(request.getPartId());
            if (supplierPartRepository.existsBySupplierIdAndPartIdAndIdNot(
                    supplierId, request.getPartId(), supplierPart.getId())) {
                throw new ConflictException(
                        "A relation between this supplier and the new part already exists.");
            }
            supplierPart.setPart(newPart);
        }

        supplierPart.setPrice(request.getPrice());
        supplierPart.setLeadTimeDays(request.getLeadTimeDays());
        supplierPart.setLastUpdated(LocalDateTime.now());

        return supplierPartMapper.toResponse(supplierPartRepository.save(supplierPart));
    }

    //supplierpart DELETE relacion
    @Override
    public void removeSupplierPart(UUID supplierId, UUID partId) {
        findSupplierOrThrow(supplierId);
        SupplierPart supplierPart = supplierPartRepository
                .findBySupplierIdAndPartId(supplierId, partId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "SupplierPart relation not found for supplier: "
                                + supplierId + " and part: " + partId));
        supplierPartRepository.delete(supplierPart);
    }

    //helpers privados
    private Supplier findSupplierOrThrow(UUID id) {
        return supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Supplier not found with id: " + id));
    }

    private Part findPartOrThrow(UUID id) {
        return partRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Part not found with id: " + id));
    }

    private void validateSupplierBusinessRules(SupplierRequest request, UUID id) {
        boolean nameTaken = (id == null)
                ? supplierRepository.existsByNameIgnoreCase(request.getName())
                : supplierRepository.existsByNameIgnoreCaseAndIdNot(request.getName(), id);

        if (nameTaken) {
            throw new ConflictException(
                    "A supplier with the name '" + request.getName() + "' already exists.");
        }

        boolean emailTaken = (id == null)
                ? supplierRepository.existsByEmailIgnoreCase(request.getEmail())
                : supplierRepository.existsByEmailIgnoreCaseAndIdNot(request.getEmail(), id);

        if (emailTaken) {
            throw new ConflictException(
                    "A supplier with the email '" + request.getEmail() + "' already exists.");
        }
    }
}