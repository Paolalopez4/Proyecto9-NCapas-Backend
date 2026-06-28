package com.grupo9.auto_repair_shop.service.branch;

import com.grupo9.auto_repair_shop.dto.request.branch.BranchRequest;
import com.grupo9.auto_repair_shop.dto.request.branch.UpdateBranchRequest;
import com.grupo9.auto_repair_shop.dto.response.branch.BranchResponse;
import com.grupo9.auto_repair_shop.dto.response.common.PageResponse;
import com.grupo9.auto_repair_shop.entity.branch.Branch;
import com.grupo9.auto_repair_shop.exception.BusinessRuleException;
import com.grupo9.auto_repair_shop.exception.ConflictException;
import com.grupo9.auto_repair_shop.exception.ResourceNotFoundException;
import com.grupo9.auto_repair_shop.exception.ValidationException;
import com.grupo9.auto_repair_shop.mapper.branch.BranchMapper;
import com.grupo9.auto_repair_shop.repository.branch.BranchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class BranchServiceImpl implements BranchService {

    private final BranchRepository branchRepository;

    private final BranchMapper branchMapper;

    @Override
    public BranchResponse create(BranchRequest request) {

        if (branchRepository.existsByName(request.getName())) {
            throw new ConflictException("Branch with same name already exists");
        }

        if (branchRepository.existsByAddress(request.getAddress())) {
            throw new ConflictException("Branch with same address already exists");
        }

        if (branchRepository.existsByPhone(request.getPhone())) {
            throw new ConflictException("Branch with same phone number already exists");
        }

        Branch branch = Branch.builder()
                .name(request.getName())
                .address(request.getAddress())
                .phone(request.getPhone())
                .active(true)
                .build();

        Branch savedBranch = branchRepository.save(branch);

        return branchMapper.toResponse(savedBranch);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<BranchResponse> findAll(Boolean active, int page, int size) {

        if (page < 0) {
            throw new ValidationException("Page cannot be lower than 0");
        }

        if (size < 1 || size > 100) {
            throw new ValidationException("Page size has to be between 1 and 100");
        }

        PageRequest pageRequest = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        Page<Branch> branches;

        if (active != null) {
            branches = branchRepository.findByActive(active, pageRequest);
        } else {
            branches = branchRepository.findAll(pageRequest);
        }

        Page<BranchResponse> responsePage = branches.map(branchMapper::toResponse);

        return PageResponse.from(responsePage);
    }

    @Override
    @Transactional(readOnly = true)
    public BranchResponse findById(UUID id) {
        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cannot find Branch with id: " + id
                ));

        return branchMapper.toResponse(branch);
    }


    @Override
    public BranchResponse update(UUID id, UpdateBranchRequest request) {
        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cannot find Branch with id: " + id
                ));

        if (branchRepository.existsByNameAndIdNot(request.getName(), id)) {
            throw new ConflictException("Branch with same name already exists");
        }

        if (branchRepository.existsByAddressAndIdNot(request.getAddress(), id)) {
            throw new ConflictException("Branch with same address already exists");
        }

        if (branchRepository.existsByPhoneAndIdNot(request.getPhone(), id)) {
            throw new ConflictException("Branch with same phone number already exists");
        }

        branch.setName(request.getName());
        branch.setAddress(request.getAddress());
        branch.setPhone(request.getPhone());

        Branch updatedBranch = branchRepository.save(branch);

        return branchMapper.toResponse(updatedBranch);
    }

    @Override
    public BranchResponse updateActive(UUID id, Boolean active) {
        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cannot find Branch with id: " + id
                ));

        branch.setActive(active);

        Branch updatedBranch = branchRepository.save(branch);

        return branchMapper.toResponse(updatedBranch);
    }

    @Override
    public void delete(UUID id) {
        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cannot find Branch with id: " + id
                ));

        try {
            branchRepository.delete(branch);
            branchRepository.flush();
        } catch (DataIntegrityViolationException ex) {
            throw new BusinessRuleException(
                    "Cannot delete branch because of related data"
            );
        }
    }
}