package com.pcpedia.api.inventory.application.handler.command;

import com.pcpedia.api.inventory.application.command.ChangeEquipmentStatusCommand;
import com.pcpedia.api.inventory.domain.model.aggregate.Equipment;
import com.pcpedia.api.inventory.domain.repository.EquipmentRepository;
import com.pcpedia.api.shared.application.cqrs.CommandHandler;
import com.pcpedia.api.shared.infrastructure.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ChangeEquipmentStatusCommandHandler implements CommandHandler<ChangeEquipmentStatusCommand, Void> {

    private final EquipmentRepository equipmentRepository;
    private final MessageSource messageSource;

    @Override
    public Void handle(ChangeEquipmentStatusCommand command) {
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

        switch (command.getStatus()) {
            case AVAILABLE -> equipment.markAsAvailable();
            case LEASED -> equipment.markAsLeased();
            case MAINTENANCE -> equipment.markAsMaintenance();
            case RETIRED -> equipment.retire();
        }

        equipmentRepository.save(equipment);
        return null;
    }
}
