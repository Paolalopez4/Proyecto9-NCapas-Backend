package com.grupo9.auto_repair_shop.dto.response.invoice;

import com.grupo9.auto_repair_shop.enums.InvoiceStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceResponse {

    private UUID id;
    private String invoiceNumber;
    private BigDecimal subtotal;
    private BigDecimal tax;
    private BigDecimal total;
    private InvoiceStatus status;
    private LocalDateTime issuedAt;
    private LocalDateTime paidAt;
    private UUID workOrderId;
    private UUID clientId;
    private String clientName;
    private UUID vehicleId;
    private String vehiclePlate;
}