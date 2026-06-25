package com.grupo9.auto_repair_shop.service.vehicle;

import com.grupo9.auto_repair_shop.dto.request.vehicle.UpdateVehicleRequest;
import com.grupo9.auto_repair_shop.dto.request.vehicle.VehicleRequest;
import com.grupo9.auto_repair_shop.dto.response.common.PageResponse;
import com.grupo9.auto_repair_shop.dto.response.vehicle.VehicleResponse;
import com.grupo9.auto_repair_shop.entity.client.Client;
import com.grupo9.auto_repair_shop.entity.vehicle.Vehicle;
import com.grupo9.auto_repair_shop.exception.BusinessRuleException;
import com.grupo9.auto_repair_shop.exception.ConflictException;
import com.grupo9.auto_repair_shop.exception.ForbiddenException;
import com.grupo9.auto_repair_shop.exception.ResourceNotFoundException;
import com.grupo9.auto_repair_shop.mapper.vehicle.VehicleMapper;
import com.grupo9.auto_repair_shop.repository.client.ClientRepository;
import com.grupo9.auto_repair_shop.repository.vehicle.VehicleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;
    private final ClientRepository clientRepository;
    private final VehicleMapper vehicleMapper;

    @Override
    @Transactional
    public VehicleResponse create(VehicleRequest request) {

        Client client = clientRepository.findById(request.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Client not found with id: " + request.getClientId()));

        if (!Boolean.TRUE.equals(client.getUser().getActive())) {
            throw new BusinessRuleException(
                    "Client's user account must be active");
        }

        if (vehicleRepository.existsByPlateIgnoreCase(request.getPlate())) {
            throw new ConflictException(
                    "A vehicle with plate '" + request.getPlate() + "' already exists");
        }

        if (vehicleRepository.existsByVinIgnoreCase(request.getVin())) {
            throw new ConflictException(
                    "A vehicle with VIN '" + request.getVin() + "' already exists");
        }

        Vehicle vehicle = Vehicle.builder()
                .client(client)
                .plate(request.getPlate())
                .brand(request.getBrand())
                .model(request.getModel())
                .year(request.getYear())
                .color(request.getColor())
                .vin(request.getVin())
                .mileage(request.getMileage())
                .build();

        Vehicle saved = vehicleRepository.save(vehicle);
        return vehicleMapper.toResponse(saved);
    }

    @Override
    public VehicleResponse findById(UUID id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Vehicle not found with id: " + id));

        checkOwnershipIfClient(vehicle);

        return vehicleMapper.toResponse(vehicle);
    }

    @Override
    public PageResponse<VehicleResponse> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Vehicle> vehiclePage = vehicleRepository.findAll(pageable);
        Page<VehicleResponse> responsePage = vehiclePage.map(vehicleMapper::toResponse);
        return PageResponse.from(responsePage);
    }

    @Override
    public PageResponse<VehicleResponse> findByClientId(UUID clientId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Vehicle> vehiclePage = vehicleRepository.findByClientId(clientId, pageable);
        Page<VehicleResponse> responsePage = vehiclePage.map(vehicleMapper::toResponse);
        return PageResponse.from(responsePage);
    }

    @Override
    @Transactional
    public VehicleResponse update(UUID id, UpdateVehicleRequest request) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Vehicle not found with id: " + id));

        checkOwnershipIfClient(vehicle);

        if (vehicleRepository.existsByPlateIgnoreCaseAndIdNot(request.getPlate(), id)) {
            throw new ConflictException(
                    "A vehicle with plate '" + request.getPlate() + "' already exists");
        }

        if (vehicleRepository.existsByVinIgnoreCaseAndIdNot(request.getVin(), id)) {
            throw new ConflictException(
                    "A vehicle with VIN '" + request.getVin() + "' already exists");
        }

        vehicleMapper.updateEntity(request, vehicle);

        Vehicle updated = vehicleRepository.save(vehicle);
        return vehicleMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Vehicle not found with id: " + id));

        try {
            vehicleRepository.delete(vehicle);
            vehicleRepository.flush();
        } catch (DataIntegrityViolationException ex) {
            throw new BusinessRuleException(
                    "Cannot delete vehicle because it has active work orders");
        }
    }

    private void checkOwnershipIfClient(Vehicle vehicle) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        boolean isClientRole = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_CLIENT"));

        if (isClientRole) {
            String currentUserEmail = authentication.getName();
            String ownerEmail = vehicle.getClient().getUser().getEmail();

            if (!ownerEmail.equalsIgnoreCase(currentUserEmail)) {
                throw new ForbiddenException(
                        "You do not have permission to access this vehicle");
            }
        }
    }

}
