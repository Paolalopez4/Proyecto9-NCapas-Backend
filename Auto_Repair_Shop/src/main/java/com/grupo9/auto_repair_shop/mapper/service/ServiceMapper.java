package com.grupo9.auto_repair_shop.mapper.service;

import com.grupo9.auto_repair_shop.dto.request.service.ServiceRequest;
import com.grupo9.auto_repair_shop.dto.response.service.ServiceResponse;
import com.grupo9.auto_repair_shop.entity.service.Service;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ServiceMapper {
    Service toEntity(ServiceRequest serviceRequest);
    ServiceResponse toResponse(Service service);
    void updateEntity(ServiceRequest request, @MappingTarget Service service);
}
