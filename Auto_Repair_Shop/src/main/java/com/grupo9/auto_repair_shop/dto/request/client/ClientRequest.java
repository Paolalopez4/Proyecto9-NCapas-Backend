package com.grupo9.auto_repair_shop.dto.request.client;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientRequest {

    @NotNull(message = "User ID is required")
    private UUID userId;

    @NotBlank(message = "Phone is required")
    @Size(min = 8, max = 20, message = "Phone must be between 8 and 20 characters")
    @Pattern(regexp = "^[+]?[0-9\\-\\s()]{8,20}$", message = "Phone format is invalid")

    private String phone;

    @NotBlank(message = "Address is required")
    @Size(max = 200, message = "Address must not exceed 200 characters")
    private String address;

}
