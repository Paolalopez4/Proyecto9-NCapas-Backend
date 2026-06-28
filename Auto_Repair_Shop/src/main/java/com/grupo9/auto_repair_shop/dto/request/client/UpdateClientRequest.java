package com.grupo9.auto_repair_shop.dto.request.client;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateClientRequest {

    @NotBlank(message = "El teléfono es obligatorio")
    @Size(min = 8, max = 20, message = "El teléfono debe tener entre 8 y 20 caracteres")
    @Pattern(regexp = "^[+]?[0-9\\-\\s()]{8,20}$", message = "Formato de teléfono inválido")
    private String phone;

    @NotBlank(message = "La dirección es obligatoria")
    @Size(max = 200, message = "La dirección no puede superar 200 caracteres")
    private String address;
}
