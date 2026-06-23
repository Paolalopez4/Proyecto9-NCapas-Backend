package com.grupo9.auto_repair_shop.service.service;

import com.grupo9.auto_repair_shop.dto.request.service.ServiceRequest;
import com.grupo9.auto_repair_shop.dto.response.service.ServiceResponse;
import com.grupo9.auto_repair_shop.entity.service.Service;
import com.grupo9.auto_repair_shop.exception.BusinessRuleException;
import com.grupo9.auto_repair_shop.exception.ConflictException;
import com.grupo9.auto_repair_shop.exception.ResourceNotFoundException;
import com.grupo9.auto_repair_shop.mapper.service.ServiceMapper;
import com.grupo9.auto_repair_shop.repository.service.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.UUID;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class ServiceCatalogServiceImpl implements ServiceCatalogService {

    private final ServiceRepository serviceRepository;
    private final ServiceMapper serviceMapper;

    //GET paginado con filtros opcionales
    @Override
    public Page<ServiceResponse> findAll(String name, Boolean active, Pageable pageable) {
        Page<Service> page;

        if (name != null && active != null) {
            page = serviceRepository.findByNameContainingIgnoreCaseAndActive(name, active, pageable);
        } else if (name != null) {
            page = serviceRepository.findByNameContainingIgnoreCase(name, pageable);
        } else if (active != null) {
            page = serviceRepository.findByActive(active, pageable);
        } else {
            page = serviceRepository.findAll(pageable);
        }

        return page.map(serviceMapper::toResponse);
    }

    //GET por ID
    @Override
    public ServiceResponse findById(UUID id) {
        Service service = findOrThrow(id);
        return serviceMapper.toResponse(service);
    }

    //POST crear servicio
    @Override
    public ServiceResponse create(ServiceRequest request) {
        validateBusinessRules(request, null);

        Service service = serviceMapper.toEntity(request);
        service.setActive(true);
        return serviceMapper.toResponse(serviceRepository.save(service));
    }

    //PUT actualizar servicio
    @Override
    public ServiceResponse update(UUID id, ServiceRequest request) {
        Service service = findOrThrow(id);
        validateBusinessRules(request, id);

        serviceMapper.updateEntity(request, service);
        return serviceMapper.toResponse(serviceRepository.save(service));
    }

    //PATCH habilitar o deshabilitar
    @Override
    public ServiceResponse toggleActive(UUID id, boolean active) {
        Service service = findOrThrow(id);
        service.setActive(active);
        return serviceMapper.toResponse(serviceRepository.save(service));
    }

    //DELETE eliminar del catálogo
    @Override
    public void delete(UUID id) {
        Service service = findOrThrow(id);
        serviceRepository.delete(service);
    }

    //helpers privados
    private Service findOrThrow(UUID id) {
        return serviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Service not found with id: " + id));
    }

    //valida reglas de negocio para crear y actualizar.
    private void validateBusinessRules(ServiceRequest request, UUID id) {
        if (request.getBasePrice() == null || request.getBasePrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessRuleException("Base price must be greater than zero.");
        }

        if (request.getEstimatedMinutes() != null && request.getEstimatedMinutes() <= 0) {
            throw new BusinessRuleException("Estimated minutes must be greater than zero.");
        }

        boolean nameTaken = (id == null)
                ? serviceRepository.existsByNameIgnoreCase(request.getName())
                : serviceRepository.existsByNameIgnoreCaseAndIdNot(request.getName(), id);

        if (nameTaken) {
            throw new ConflictException(
                    "A service with the name '" + request.getName() + "' already exists.");
        }
    }
}