package com.grupo9.auto_repair_shop.mapper.hourlog;

import com.grupo9.auto_repair_shop.dto.response.hourlog.HourLogResponse;
import com.grupo9.auto_repair_shop.entity.hourlog.HourLog;
import org.springframework.stereotype.Component;

@Component
public class HourLogMapper {

    public HourLogResponse toResponse(HourLog hourLog) {
        return HourLogResponse.builder()
                .id(hourLog.getId())
                .hours(hourLog.getHours())
                .loggedAt(hourLog.getLoggedAt())
                .notes(hourLog.getNotes())
                .workOrderId(hourLog.getWorkOrder().getId())
                .mechanicId(hourLog.getMechanic().getId())
                .mechanicName(hourLog.getMechanic().getUser().getName())
                .build();
    }
}
