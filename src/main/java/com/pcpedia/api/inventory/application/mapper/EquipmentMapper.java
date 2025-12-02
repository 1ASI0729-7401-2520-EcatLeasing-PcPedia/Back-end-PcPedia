package com.pcpedia.api.inventory.application.mapper;

import com.pcpedia.api.inventory.application.command.CreateEquipmentCommand;
import com.pcpedia.api.inventory.application.command.UpdateEquipmentCommand;
import com.pcpedia.api.inventory.application.dto.request.CreateEquipmentRequest;
import com.pcpedia.api.inventory.application.dto.request.UpdateEquipmentRequest;
import com.pcpedia.api.inventory.application.dto.response.CatalogEquipmentResponse;
import com.pcpedia.api.inventory.application.dto.response.EquipmentResponse;
import com.pcpedia.api.inventory.domain.model.aggregate.Equipment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface EquipmentMapper {

    CreateEquipmentCommand toCommand(CreateEquipmentRequest request);

    @Mapping(target = "equipmentId", source = "id")
    UpdateEquipmentCommand toCommand(Long id, UpdateEquipmentRequest request);

    @Mapping(target = "status", expression = "java(equipment.getStatus().name())")
    EquipmentResponse toResponse(Equipment equipment);

    @Mapping(target = "status", expression = "java(equipment.getStatus().name())")
    CatalogEquipmentResponse toCatalogResponse(Equipment equipment);
}
