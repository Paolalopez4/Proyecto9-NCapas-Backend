package com.grupo9.auto_repair_shop.dto.response.branch;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BranchResponse {

    private UUID id;

    private String name;

    private String address;

    private String phone;

    private Boolean active;

    private LocalDateTime createdAt;
}