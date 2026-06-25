package com.grupo9.auto_repair_shop.mapper.vehicle;

import com.grupo9.auto_repair_shop.dto.request.vehicle.UpdateVehicleRequest;
import com.grupo9.auto_repair_shop.dto.request.vehicle.VehicleRequest;
import com.grupo9.auto_repair_shop.dto.response.vehicle.VehicleResponse;
import com.grupo9.auto_repair_shop.entity.vehicle.Vehicle;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface VehicleMapper {

        @Mapping(target = "clientId", source = "client.id")
        @Mapping(target = "clientName", source = "client.user.name")
        VehicleResponse toResponse(Vehicle vehicle);

        @Mapping(target = "plate", source = "request.plate")
        @Mapping(target = "brand", source = "request.brand")
        @Mapping(target = "model", source = "request.model")
        @Mapping(target = "year", source = "request.year")
        @Mapping(target = "color", source = "request.color")
        @Mapping(target = "mileage", source = "request.mileage")
        void updateEntity(UpdateVehicleRequest request, @MappingTarget Vehicle vehicle);

}