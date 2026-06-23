package com.grupo9.auto_repair_shop.mapper.branch;

import com.grupo9.auto_repair_shop.dto.response.branch.BranchResponse;
import com.grupo9.auto_repair_shop.entity.branch.Branch;
import org.springframework.stereotype.Component;

@Component
public class BranchMapper {

    public BranchResponse toResponse(Branch branch) {
        return BranchResponse.builder()
                .id(branch.getId())
                .name(branch.getName())
                .address(branch.getAddress())
                .phone(branch.getPhone())
                .active(branch.getActive())
                .createdAt(branch.getCreatedAt())
                .build();
    }
}