package com.grupo9.auto_repair_shop.repository.supplier;

import com.grupo9.auto_repair_shop.entity.supplier.SupplierPart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SupplierPartRepository extends JpaRepository<SupplierPart, UUID> {
    //validar duplicado de proveedor y respuesto
    boolean existsBySupplierIdAndPartId(UUID supplierId, UUID partId);
    boolean existsBySupplierIdAndPartIdAndIdNot(UUID supplierId, UUID partId, UUID id);

    //listar repuestos de un proveedor
    List<SupplierPart> findBySupplierId(UUID supplierId);

    //obtener el mejor proveedor para un repuesto
    @Query("SELECT sp FROM SupplierPart sp WHERE sp.part.id = :partId ORDER BY sp.price ASC")
    List<SupplierPart> findByPartIdOrderByPriceAsc(@Param("partId") UUID partId);

    //buscar una relacion especifica
    Optional<SupplierPart> findBySupplierIdAndPartId(UUID supplierId, UUID partId);
}