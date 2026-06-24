package com.grupo9.auto_repair_shop.repository.invoice;

import com.grupo9.auto_repair_shop.entity.invoice.Invoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface InvoiceRepository extends JpaRepository<Invoice, UUID> {

    Optional<Invoice> findByWorkOrderId(UUID workOrderId);

    boolean existsByInvoiceNumber(String invoiceNumber);

    Page<Invoice> findByWorkOrderVehicleClientId(UUID clientId, Pageable pageable);
}
