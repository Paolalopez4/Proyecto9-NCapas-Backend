package com.grupo9.auto_repair_shop.service.vehicle;

import com.grupo9.auto_repair_shop.dto.request.vehicle.UpdateVehicleRequest;
import com.grupo9.auto_repair_shop.dto.request.vehicle.VehicleRequest;
import com.grupo9.auto_repair_shop.dto.response.common.PageResponse;
import com.grupo9.auto_repair_shop.dto.response.vehicle.VehicleResponse;
import org.springframework.jdbc.object.UpdatableSqlQuery;

import java.util.UUID;

public interface VehicleService {

    VehicleResponse create(VehicleRequest request);

    VehicleResponse findById(UUID id);

    PageResponse<VehicleResponse> findAll(int page, int size);

    PageResponse<VehicleResponse> findByClientId(UUID clientId, int page, int size);

    VehicleResponse update(UUID id, UpdateVehicleRequest request);

    void delete(UUID id);

}
