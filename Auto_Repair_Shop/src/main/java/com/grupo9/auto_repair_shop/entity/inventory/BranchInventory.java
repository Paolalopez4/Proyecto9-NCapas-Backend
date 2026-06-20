package com.grupo9.auto_repair_shop.entity.inventory;

import com.grupo9.auto_repair_shop.entity.branch.Branch;
import com.grupo9.auto_repair_shop.entity.part.Part;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "branch_inventory",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "unique_branch_part",
                        columnNames = {"branch_id", "part_id"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BranchInventory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "part_id", nullable = false)
    private Part part;

    @Column(nullable = false)
    private Integer stock = 0;

    @Column(name = "stock_min", nullable = false)
    private Integer stockMin = 0;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}