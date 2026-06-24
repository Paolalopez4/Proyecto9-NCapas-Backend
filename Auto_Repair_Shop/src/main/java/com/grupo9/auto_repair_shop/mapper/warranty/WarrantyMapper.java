package com.grupo9.auto_repair_shop.mapper.warranty;

import com.grupo9.auto_repair_shop.dto.response.warranty.WarrantyResponse;
import com.grupo9.auto_repair_shop.entity.warranty.Warranty;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class WarrantyMapper {

    public WarrantyResponse toResponse(Warranty warranty) {
        boolean expired = warranty.getEndDate() != null
                && warranty.getEndDate().isBefore(LocalDate.now());

        return WarrantyResponse.builder()
                .id(warranty.getId())
                .startDate(warranty.getStartDate())
                .endDate(warranty.getEndDate())
                .coverage(warranty.getCoverage())
                .active(warranty.getActive())
                .workOrderId(warranty.getWorkOrder().getId())
                .vehicleId(warranty.getWorkOrder().getVehicle().getId())
                .vehiclePlate(warranty.getWorkOrder().getVehicle().getPlate())
                .expired(expired)
                .build();
    }
}
