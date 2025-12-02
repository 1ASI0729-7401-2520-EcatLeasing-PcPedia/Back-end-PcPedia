package com.pcpedia.api.inventory.application.handler.command;

import com.pcpedia.api.inventory.application.command.CreateEquipmentCommand;
import com.pcpedia.api.inventory.domain.model.aggregate.Equipment;
import com.pcpedia.api.inventory.domain.model.aggregate.ProductModel;
import com.pcpedia.api.inventory.domain.model.enums.EquipmentStatus;
import com.pcpedia.api.inventory.domain.repository.EquipmentRepository;
import com.pcpedia.api.inventory.domain.repository.ProductModelRepository;
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
public class CreateEquipmentCommandHandler implements CommandHandler<CreateEquipmentCommand, Long> {

    private final EquipmentRepository equipmentRepository;
    private final ProductModelRepository productModelRepository;
    private final MessageSource messageSource;

    @Override
    public Long handle(CreateEquipmentCommand command) {
        // Verify ProductModel exists
        ProductModel productModel = productModelRepository.findById(command.getProductModelId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        getMessage("productModel.not.found", "Product model not found")));

        // Verify serial number is unique
        if (StringUtils.hasText(command.getSerialNumber()) &&
                equipmentRepository.existsBySerialNumber(command.getSerialNumber())) {
            throw new BadRequestException(
                    getMessage("equipment.serial.exists", "Serial number already exists"));
        }

        // Create equipment from ProductModel data
        Equipment equipment = Equipment.builder()
                .productModel(productModel)
                .name(productModel.getName())
                .brand(productModel.getBrand())
                .model(productModel.getModel())
                .category(productModel.getCategory())
                .specifications(productModel.getSpecifications())
                .basePrice(productModel.getBasePrice())
                .imageUrl(productModel.getImageUrl())
                .serialNumber(command.getSerialNumber())
                .purchaseDate(command.getPurchaseDate())
                .status(EquipmentStatus.AVAILABLE)
                .build();

        Equipment savedEquipment = equipmentRepository.save(equipment);
        return savedEquipment.getId();
    }

    private String getMessage(String key, String defaultMessage) {
        return messageSource.getMessage(key, null, defaultMessage, LocaleContextHolder.getLocale());
    }
}
