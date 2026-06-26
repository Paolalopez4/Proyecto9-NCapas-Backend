package com.grupo9.auto_repair_shop.service.invoice;

import com.grupo9.auto_repair_shop.dto.response.common.PageResponse;
import com.grupo9.auto_repair_shop.dto.response.invoice.InvoiceResponse;

import java.util.UUID;

public interface InvoiceService {

    InvoiceResponse generate(UUID workOrderId);

    PageResponse<InvoiceResponse> findAll(int page, int size);

    InvoiceResponse findById(UUID id);

    InvoiceResponse findByWorkOrder(UUID workOrderId);

    PageResponse<InvoiceResponse> findByClient(UUID clientId, int page, int size);

    InvoiceResponse pay(UUID id);

    InvoiceResponse cancel(UUID id);
}
