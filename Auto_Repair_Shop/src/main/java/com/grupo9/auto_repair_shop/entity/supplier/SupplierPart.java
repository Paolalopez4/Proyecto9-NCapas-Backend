package com.grupo9.auto_repair_shop.entity.supplier;

import com.grupo9.auto_repair_shop.entity.part.Part;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "supplier_parts",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "unique_supplier_part",
                        columnNames = {"supplier_id", "part_id"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplierPart {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "part_id", nullable = false)
    private Part part;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "lead_time_days")
    private Integer leadTimeDays;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;
}