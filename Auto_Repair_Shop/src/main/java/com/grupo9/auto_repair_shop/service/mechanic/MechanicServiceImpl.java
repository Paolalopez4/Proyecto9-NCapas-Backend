package com.grupo9.auto_repair_shop.service.mechanic;

import com.grupo9.auto_repair_shop.dto.request.mechanic.MechanicRequest;
import com.grupo9.auto_repair_shop.dto.request.mechanic.UpdateMechanicRequest;
import com.grupo9.auto_repair_shop.dto.response.common.PageResponse;
import com.grupo9.auto_repair_shop.dto.response.mechanic.MechanicEfficiencyResponse;
import com.grupo9.auto_repair_shop.dto.response.mechanic.MechanicResponse;
import com.grupo9.auto_repair_shop.entity.branch.Branch;
import com.grupo9.auto_repair_shop.entity.mechanic.Mechanic;
import com.grupo9.auto_repair_shop.entity.user.User;
import com.grupo9.auto_repair_shop.enums.UserRole;
import com.grupo9.auto_repair_shop.enums.WorkOrderStatus;
import com.grupo9.auto_repair_shop.exception.BusinessRuleException;
import com.grupo9.auto_repair_shop.exception.ConflictException;
import com.grupo9.auto_repair_shop.exception.ResourceNotFoundException;
import com.grupo9.auto_repair_shop.mapper.mechanic.MechanicMapper;
import com.grupo9.auto_repair_shop.repository.branch.BranchRepository;
import com.grupo9.auto_repair_shop.repository.hourlog.HourLogRepository;
import com.grupo9.auto_repair_shop.repository.mechanic.MechanicRepository;
import com.grupo9.auto_repair_shop.repository.user.UserRepository;
import com.grupo9.auto_repair_shop.repository.workorder.WorkOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MechanicServiceImpl implements MechanicService {

    private final MechanicRepository mechanicRepository;
    private final UserRepository userRepository;
    private final BranchRepository branchRepository;
    private final MechanicMapper mechanicMapper;
    private final HourLogRepository hourLogRepository;
    private final WorkOrderRepository workOrderRepository;

    @Override
    @Transactional
    public MechanicResponse create(MechanicRequest request) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + request.getUserId()));

        if (user.getRole() != UserRole.MECHANIC) {
            throw new BusinessRuleException(
                    "User must have role MECHANIC to create a mechanic profile");
        }

        if (mechanicRepository.existsByUserId(request.getUserId())) {
            throw new ConflictException(
                    "A mechanic profile already exists for this user");
        }

        if (request.getHourlyRate().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessRuleException("Hourly rate must be greater than zero");
        }

        Branch branch = branchRepository.findById(request.getBranchId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Branch not found with id: " + request.getBranchId()));

        Mechanic mechanic = Mechanic.builder()
                .user(user)
                .branch(branch)
                .specialty(request.getSpecialty())
                .hourlyRate(request.getHourlyRate())
                .active(true)
                .build();

        Mechanic saved = mechanicRepository.save(mechanic);
        return mechanicMapper.toResponse(saved);
    }

    @Override
    public MechanicResponse findById(UUID id) {
        Mechanic mechanic = mechanicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Mechanic not found with id: " + id));

        return mechanicMapper.toResponse(mechanic);
    }

    @Override
    public PageResponse<MechanicResponse> findAll(UUID branchId, String specialty, Boolean active, int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Mechanic> mechanicPage;

        if (branchId != null) {
            mechanicPage = mechanicRepository.findByBranchId(branchId, pageable);
        } else if (specialty != null && !specialty.isBlank()) {
            mechanicPage = mechanicRepository.findBySpecialtyContainingIgnoreCase(specialty, pageable);
        } else if (active != null) {
            mechanicPage = mechanicRepository.findByActive(active, pageable);
        } else {
            mechanicPage = mechanicRepository.findAll(pageable);
        }

        Page<MechanicResponse> responsePage = mechanicPage.map(mechanicMapper::toResponse);
        return PageResponse.from(responsePage);
    }

    @Override
    @Transactional
    public MechanicResponse update(UUID id, UpdateMechanicRequest request) {

        Mechanic mechanic = mechanicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Mechanic not found with id: " + id));

        if (request.getHourlyRate().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessRuleException(
                    "Hourly rate must be greater than zero");
        }

        if (!mechanic.getBranch().getId().equals(request.getBranchId())) {
            Branch newBranch = branchRepository.findById(request.getBranchId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Branch not found with id: " + request.getBranchId()));
            mechanic.setBranch(newBranch);
        }

        mechanicMapper.updateFromRequest(request, mechanic);

        Mechanic updated = mechanicRepository.save(mechanic);
        return mechanicMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public MechanicResponse updateActive(UUID id, Boolean active) {

        Mechanic mechanic = mechanicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Mechanic not found with id: " + id));

        if (Boolean.FALSE.equals(active)) {
            boolean hasInProgressOrders = mechanic.getWorkOrders() != null &&
                    mechanic.getWorkOrders().stream()
                            .anyMatch(wo -> wo.getStatus() == WorkOrderStatus.IN_PROGRESS);

            if (hasInProgressOrders) {
                throw new BusinessRuleException(
                        "Cannot deactivate mechanic with orders in progress");
            }
        }

        mechanic.setActive(active);
        Mechanic updated = mechanicRepository.save(mechanic);
        return mechanicMapper.toResponse(updated);
    }

    @Override
    public List<MechanicEfficiencyResponse> getEfficiencyReport(UUID branchId, Boolean active) {

        List<Mechanic> mechanics;

        if (branchId != null) {
            mechanics = mechanicRepository.findByBranchId(branchId);
        } else if (active != null) {
            mechanics = mechanicRepository.findByActive(active);
        } else {
            mechanics = mechanicRepository.findAll();
        }

        return mechanics.stream()
                .map(this::buildEfficiencyResponse)
                .toList();
    }

    private MechanicEfficiencyResponse buildEfficiencyResponse(Mechanic mechanic) {

        BigDecimal totalHours = hourLogRepository.sumHoursByMechanicId(mechanic.getId());
        long completedOrders = workOrderRepository.countByMechanicIdAndStatus(mechanic.getId(), WorkOrderStatus.DONE);

        BigDecimal serviceRevenue = workOrderRepository.sumServiceRevenueByMechanicId(mechanic.getId());
        BigDecimal partRevenue = workOrderRepository.sumPartRevenueByMechanicId(mechanic.getId());
        BigDecimal totalRevenue = serviceRevenue.add(partRevenue);

        BigDecimal laborCost = mechanic.getHourlyRate().multiply(totalHours);

        BigDecimal revenuePerHour = totalHours.compareTo(BigDecimal.ZERO) > 0
                ? totalRevenue.divide(totalHours, 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        return MechanicEfficiencyResponse.builder()
                .mechanicId(mechanic.getId())
                .mechanicName(mechanic.getUser().getName())
                .specialty(mechanic.getSpecialty())
                .hourlyRate(mechanic.getHourlyRate())
                .totalHoursWorked(totalHours)
                .completedOrders(completedOrders)
                .totalRevenue(totalRevenue)
                .laborCost(laborCost)
                .revenuePerHour(revenuePerHour)
                .build();
    }
}