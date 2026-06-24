package com.grupo9.auto_repair_shop.mapper.part;

import com.grupo9.auto_repair_shop.dto.request.part.PartRequest;
import com.grupo9.auto_repair_shop.dto.response.part.PartResponse;
import com.grupo9.auto_repair_shop.entity.part.Part;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PartMapper {

    Part toEntity(PartRequest partRequest);

    PartResponse toResponse(Part part);

    void updateEntity(PartRequest partRequest, @MappingTarget Part part);

}
