package com.grupo9.auto_repair_shop.repository.appointment;

import com.grupo9.auto_repair_shop.entity.appointment.Appointment;
import com.grupo9.auto_repair_shop.enums.AppointmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {

    //GET paginado con filtros
    Page<Appointment> findByBranchId(UUID branchId, Pageable pageable);
    Page<Appointment> findByStatus(AppointmentStatus status, Pageable pageable);
    Page<Appointment> findByMechanicId(UUID mechanicId, Pageable pageable);
    Page<Appointment> findByBranchIdAndStatus(UUID branchId, AppointmentStatus status, Pageable pageable);

    @Query("SELECT a FROM Appointment a WHERE DATE(a.scheduledAt) = DATE(:date) AND a.branch.id = :branchId")
    Page<Appointment> findByBranchIdAndDate(@Param("branchId") UUID branchId,
                                            @Param("date") LocalDateTime date,
                                            Pageable pageable);

    //GET citas
    Page<Appointment> findByClientId(UUID clientId, Pageable pageable);

    //validar duplicado
    boolean existsByBranchIdAndScheduledAtAndMechanicId(UUID branchId,
                                                        LocalDateTime scheduledAt,
                                                        UUID mechanicId);
    boolean existsByBranchIdAndScheduledAtAndMechanicIdAndIdNot(UUID branchId,
                                                                LocalDateTime scheduledAt,
                                                                UUID mechanicId,
                                                                UUID id);

    //slots disponibles
    @Query("SELECT a.scheduledAt FROM Appointment a " +
            "WHERE a.branch.id = :branchId " +
            "AND DATE(a.scheduledAt) = DATE(:date) " +
            "AND a.status NOT IN ('CANCELLED')")
    List<LocalDateTime> findOccupiedSlotsByBranchAndDate(@Param("branchId") UUID branchId,
                                                         @Param("date") LocalDateTime date);

    //citas pendientes usuario desactivado
    List<Appointment> findByClientIdAndStatusIn(UUID clientId, List<AppointmentStatus> statuses);
}