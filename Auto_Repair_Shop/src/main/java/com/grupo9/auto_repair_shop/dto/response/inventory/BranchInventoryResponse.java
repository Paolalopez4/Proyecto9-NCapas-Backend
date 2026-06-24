package com.grupo9.auto_repair_shop.dto.response.inventory;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BranchInventoryResponse {

    private UUID id;
    private Integer stock;
    private Integer stockMin;
    private LocalDateTime updatedAt;

    private UUID partId;
    private String partName;
    private String partSku;
    private String partCategory;

    private UUID branchId;
    private String branchName;

    private Boolean lowStock;
}
