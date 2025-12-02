package com.pcpedia.api.inventory.application.handler.command;

import com.pcpedia.api.inventory.application.command.UpdateEquipmentCommand;
import com.pcpedia.api.inventory.domain.model.aggregate.Equipment;
import com.pcpedia.api.inventory.domain.repository.EquipmentRepository;
import com.pcpedia.api.shared.application.cqrs.CommandHandler;
import com.pcpedia.api.shared.infrastructure.exception.BadRequestException;
import com.pcpedia.api.shared.infrastructure.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Transactional
public class UpdateEquipmentCommandHandler implements CommandHandler<UpdateEquipmentCommand, Void> {

    private final EquipmentRepository equipmentRepository;
    private final MessageSource messageSource;

    @Override
    public Void handle(UpdateEquipmentCommand command) {
        Equipment equipment = equipmentRepository.findById(command.getEquipmentId())
                .orElseThrow(() -> {
                    String message = messageSource.getMessage(
                            "equipment.not.found",
                            null,
                            "Equipment not found",
                            LocaleContextHolder.getLocale()
                    );
                    return new ResourceNotFoundException(message);
                });

        // Check if serial number is being changed and if it already exists
        if (StringUtils.hasText(command.getSerialNumber()) &&
                !command.getSerialNumber().equals(equipment.getSerialNumber()) &&
                equipmentRepository.existsBySerialNumber(command.getSerialNumber())) {
            String message = messageSource.getMessage(
                    "equipment.serial.exists",
                    null,
                    "Serial number already exists",
                    LocaleContextHolder.getLocale()
            );
            throw new BadRequestException(message);
        }

        equipment.setName(command.getName());
        equipment.setBrand(command.getBrand());
        equipment.setModel(command.getModel());
        equipment.setSerialNumber(command.getSerialNumber());
        equipment.setCategory(command.getCategory());
        equipment.setSpecifications(command.getSpecifications());
        equipment.setBasePrice(command.getBasePrice());
        equipment.setImageUrl(command.getImageUrl());
        equipment.setPurchaseDate(command.getPurchaseDate());

        equipmentRepository.save(equipment);
        return null;
    }
}
