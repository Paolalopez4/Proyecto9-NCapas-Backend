package com.grupo9.auto_repair_shop.mapper.mechanic;

import com.grupo9.auto_repair_shop.dto.request.mechanic.MechanicRequest;
import com.grupo9.auto_repair_shop.dto.request.mechanic.UpdateMechanicRequest;
import com.grupo9.auto_repair_shop.dto.response.mechanic.MechanicResponse;
import com.grupo9.auto_repair_shop.entity.mechanic.Mechanic;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MechanicMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userName", source = "user.name")
    @Mapping(target = "userEmail", source = "user.email")
    @Mapping(target = "branchId", source = "branch.id")
    @Mapping(target = "branchName", source = "branch.name")
    MechanicResponse toResponse(Mechanic mechanic);

    void updateFromRequest(UpdateMechanicRequest request, @MappingTarget Mechanic mechanic);

}
