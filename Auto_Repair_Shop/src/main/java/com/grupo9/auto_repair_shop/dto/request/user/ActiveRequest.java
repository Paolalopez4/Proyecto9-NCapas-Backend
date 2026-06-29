package com.grupo9.auto_repair_shop.dto.request.user;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActiveRequest {

    @NotNull(message = "El estado activo es obligatorio")
    private Boolean active;
}