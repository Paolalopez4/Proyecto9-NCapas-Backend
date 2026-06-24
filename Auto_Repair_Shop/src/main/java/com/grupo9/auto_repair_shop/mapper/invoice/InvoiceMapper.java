package com.grupo9.auto_repair_shop.mapper.invoice;

import com.grupo9.auto_repair_shop.dto.response.invoice.InvoiceResponse;
import com.grupo9.auto_repair_shop.entity.invoice.Invoice;
import org.springframework.stereotype.Component;

@Component
public class InvoiceMapper {

    public InvoiceResponse toResponse(Invoice invoice) {
        return InvoiceResponse.builder()
                .id(invoice.getId())
                .invoiceNumber(invoice.getInvoiceNumber())
                .subtotal(invoice.getSubtotal())
                .tax(invoice.getTax())
                .total(invoice.getTotal())
                .status(invoice.getStatus())
                .issuedAt(invoice.getIssuedAt())
                .paidAt(invoice.getPaidAt())
                .workOrderId(invoice.getWorkOrder().getId())
                .clientId(invoice.getWorkOrder().getVehicle().getClient().getId())
                .clientName(invoice.getWorkOrder().getVehicle().getClient().getUser().getName())
                .vehicleId(invoice.getWorkOrder().getVehicle().getId())
                .vehiclePlate(invoice.getWorkOrder().getVehicle().getPlate())
                .build();
    }
}
