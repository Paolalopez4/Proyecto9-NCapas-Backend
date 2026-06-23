package com.grupo9.auto_repair_shop.dto.request.branch;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BranchActiveRequest {

    @NotNull(message = "El estado activo es obligatorio")
    private Boolean active;
}