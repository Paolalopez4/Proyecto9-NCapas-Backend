package com.grupo9.auto_repair_shop.service.service;

import com.grupo9.auto_repair_shop.dto.request.service.ServiceRequest;
import com.grupo9.auto_repair_shop.dto.response.service.ServiceResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ServiceCatalogService {
    Page<ServiceResponse> findAll(String name, Boolean active, Pageable pageable);
    ServiceResponse findById(UUID id);
    ServiceResponse create(ServiceRequest request);
    ServiceResponse update(UUID id, ServiceRequest request);
    ServiceResponse toggleActive(UUID id, boolean active);
    void delete(UUID id);
}
