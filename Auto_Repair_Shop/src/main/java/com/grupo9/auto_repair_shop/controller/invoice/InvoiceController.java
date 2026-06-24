package com.grupo9.auto_repair_shop.controller.invoice;

import com.grupo9.auto_repair_shop.dto.response.common.ApiResponse;
import com.grupo9.auto_repair_shop.dto.response.common.PageResponse;
import com.grupo9.auto_repair_shop.dto.response.invoice.InvoiceResponse;
import com.grupo9.auto_repair_shop.service.invoice.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;

    @PostMapping("/api/work-orders/{workOrderId}/invoice")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<InvoiceResponse>> generate(
            @PathVariable UUID workOrderId
    ) {
        InvoiceResponse invoice = invoiceService.generate(workOrderId);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<InvoiceResponse>builder()
                        .success(true)
                        .message("Factura generada correctamente")
                        .data(invoice)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @GetMapping("/api/invoices")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PageResponse<InvoiceResponse>>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PageResponse<InvoiceResponse> invoices = invoiceService.findAll(page, size);

        return ResponseEntity.ok(
                ApiResponse.<PageResponse<InvoiceResponse>>builder()
                        .success(true)
                        .message("Facturas obtenidas correctamente")
                        .data(invoices)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @GetMapping("/api/invoices/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT')")
    public ResponseEntity<ApiResponse<InvoiceResponse>> findById(
            @PathVariable UUID id
    ) {
        InvoiceResponse invoice = invoiceService.findById(id);

        return ResponseEntity.ok(
                ApiResponse.<InvoiceResponse>builder()
                        .success(true)
                        .message("Factura obtenida correctamente")
                        .data(invoice)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @GetMapping("/api/work-orders/{workOrderId}/invoice")
    @PreAuthorize("hasAnyRole('ADMIN', 'MECHANIC', 'CLIENT')")
    public ResponseEntity<ApiResponse<InvoiceResponse>> findByWorkOrder(
            @PathVariable UUID workOrderId
    ) {
        InvoiceResponse invoice = invoiceService.findByWorkOrder(workOrderId);

        return ResponseEntity.ok(
                ApiResponse.<InvoiceResponse>builder()
                        .success(true)
                        .message("Factura obtenida correctamente")
                        .data(invoice)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @GetMapping("/api/clients/{clientId}/invoices")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT')")
    public ResponseEntity<ApiResponse<PageResponse<InvoiceResponse>>> findByClient(
            @PathVariable UUID clientId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PageResponse<InvoiceResponse> invoices = invoiceService.findByClient(clientId, page, size);

        return ResponseEntity.ok(
                ApiResponse.<PageResponse<InvoiceResponse>>builder()
                        .success(true)
                        .message("Facturas del cliente obtenidas correctamente")
                        .data(invoices)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @PatchMapping("/api/invoices/{id}/pay")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<InvoiceResponse>> pay(
            @PathVariable UUID id
    ) {
        InvoiceResponse invoice = invoiceService.pay(id);

        return ResponseEntity.ok(
                ApiResponse.<InvoiceResponse>builder()
                        .success(true)
                        .message("Factura marcada como pagada correctamente")
                        .data(invoice)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @PatchMapping("/api/invoices/{id}/cancel")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<InvoiceResponse>> cancel(
            @PathVariable UUID id
    ) {
        InvoiceResponse invoice = invoiceService.cancel(id);

        return ResponseEntity.ok(
                ApiResponse.<InvoiceResponse>builder()
                        .success(true)
                        .message("Factura cancelada correctamente")
                        .data(invoice)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }
}
