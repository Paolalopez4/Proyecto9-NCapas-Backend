package com.grupo9.auto_repair_shop.dto.request.supplier;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplierRequest {
    @NotBlank(message = "Supplier name is required")
    @Size(max = 150, message = "Supplier name must not exceed 150 characters")
    private String name;

    @NotBlank(message = "Contact name is required")
    @Size(max = 150, message = "Contact name must not exceed 150 characters")
    private String contactName;

    @NotBlank(message = "Email is required")
    @Size(max = 150, message = "Email must not exceed 150 characters")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Phone number is required")
    @Size(max = 8, message = "Phone number must not exceed 8 characters")
    @Pattern(regexp = "\\d{8}", message = "Phone number must be exactly 8 digits")
    private String phone;
}
