package com.grupo9.auto_repair_shop.service.part;

import com.grupo9.auto_repair_shop.dto.request.part.PartRequest;
import com.grupo9.auto_repair_shop.dto.response.common.PageResponse;
import com.grupo9.auto_repair_shop.dto.response.part.PartResponse;
import com.grupo9.auto_repair_shop.entity.part.Part;
import com.grupo9.auto_repair_shop.exception.ConflictException;
import com.grupo9.auto_repair_shop.exception.ResourceNotFoundException;
import com.grupo9.auto_repair_shop.mapper.part.PartMapper;
import com.grupo9.auto_repair_shop.repository.part.PartRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PartServiceImpl implements PartService {

    private final PartRepository partRepository;
    private final PartMapper partMapper;

    @Override
    @Transactional
    public PartResponse create(PartRequest partRequest) {
        if (partRepository.existsBySku(partRequest.getSku())) {
            throw new ConflictException("A part with SKU" + partRequest.getSku() + "already exists");
        }
        Part part = partMapper.toEntity(partRequest);
        Part saved = partRepository.save(part);
        return partMapper.toResponse(saved);
    }

    @Override
    public PartResponse findById(UUID id) {
        Part part = partRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Part not found with id: " + id));
        return partMapper.toResponse(part);
    }

    @Override
    public PageResponse<PartResponse> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Part> partPage = partRepository.findAll(pageable);
        Page<PartResponse> responsePage = partPage.map(partMapper::toResponse);
        return PageResponse.from(responsePage);
    }

    @Override
    @Transactional
    public PartResponse update(UUID id, PartRequest partRequest) {
        Part part = partRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Part not found with id: " + id));

        partMapper.updateEntity(partRequest, part);

        Part updated = partRepository.save(part);
        return partMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public void updateActive(UUID id, boolean active) {
        Part part = partRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Part not found with id: " + id));

        part.setActive(active);
        partRepository.save(part);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        Part part = partRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Part not found with id: " + id));

        partRepository.delete(part);
    }
}
