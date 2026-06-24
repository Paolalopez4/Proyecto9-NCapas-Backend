package com.grupo9.auto_repair_shop.service.warranty;

import com.grupo9.auto_repair_shop.dto.response.common.PageResponse;
import com.grupo9.auto_repair_shop.dto.response.warranty.WarrantyResponse;
import com.grupo9.auto_repair_shop.entity.warranty.Warranty;
import com.grupo9.auto_repair_shop.exception.BusinessRuleException;
import com.grupo9.auto_repair_shop.exception.ResourceNotFoundException;
import com.grupo9.auto_repair_shop.mapper.warranty.WarrantyMapper;
import com.grupo9.auto_repair_shop.repository.warranty.WarrantyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class WarrantyServiceImpl implements WarrantyService {

    private final WarrantyRepository warrantyRepository;
    private final WarrantyMapper warrantyMapper;

    @Override
    @Transactional(readOnly = true)
    public PageResponse<WarrantyResponse> findAll(int page, int size) {

        PageRequest pageRequest = PageRequest.of(
                page, size, Sort.by(Sort.Direction.DESC, "startDate")
        );

        Page<WarrantyResponse> result = warrantyRepository.findAll(pageRequest)
                .map(warrantyMapper::toResponse);

        return PageResponse.from(result);
    }

    @Override
    @Transactional(readOnly = true)
    public WarrantyResponse findById(UUID id) {

        Warranty warranty = warrantyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Garantía no encontrada con id: " + id
                ));

        return warrantyMapper.toResponse(warranty);
    }

    @Override
    @Transactional(readOnly = true)
    public WarrantyResponse findByWorkOrder(UUID workOrderId) {

        Warranty warranty = warrantyRepository.findByWorkOrderId(workOrderId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Garantía no encontrada para la orden: " + workOrderId
                ));

        return warrantyMapper.toResponse(warranty);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<WarrantyResponse> findByClient(UUID clientId, int page, int size) {

        PageRequest pageRequest = PageRequest.of(
                page, size, Sort.by(Sort.Direction.DESC, "startDate")
        );

        Page<WarrantyResponse> result = warrantyRepository
                .findByWorkOrderVehicleClientId(clientId, pageRequest)
                .map(warrantyMapper::toResponse);

        return PageResponse.from(result);
    }

    @Override
    public WarrantyResponse deactivate(UUID id) {

        Warranty warranty = warrantyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Garantía no encontrada con id: " + id
                ));

        if (!warranty.getActive()) {
            throw new BusinessRuleException(
                    "La garantía ya se encuentra desactivada"
            );
        }

        warranty.setActive(false);

        Warranty updated = warrantyRepository.save(warranty);

        return warrantyMapper.toResponse(updated);
    }
}
