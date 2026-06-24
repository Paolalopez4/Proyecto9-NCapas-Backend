package com.grupo9.auto_repair_shop.dto.response.repairhistory;

import com.grupo9.auto_repair_shop.entity.vehicle.Vehicle;
import com.grupo9.auto_repair_shop.entity.workorder.WorkOrder;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.UUID;

public class RepairHistoryResponse {
    private UUID id;
    private String summary;
    private LocalDate repairDate;
    private Integer mileageAtRepair;
    private UUID vehicleId;
    private String vehiclePlate;
    private String vehicleBrand;
    private String vehicleModel;
    private UUID workOrderId;
}
