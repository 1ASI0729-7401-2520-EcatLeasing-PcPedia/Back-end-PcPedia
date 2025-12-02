package com.pcpedia.api.inventory.application.handler.query;

import com.pcpedia.api.inventory.application.dto.response.EquipmentResponse;
import com.pcpedia.api.inventory.application.mapper.EquipmentMapper;
import com.pcpedia.api.inventory.application.query.GetEquipmentByIdQuery;
import com.pcpedia.api.inventory.domain.model.aggregate.Equipment;
import com.pcpedia.api.inventory.domain.repository.EquipmentRepository;
import com.pcpedia.api.shared.application.cqrs.QueryHandler;
import com.pcpedia.api.shared.infrastructure.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetEquipmentByIdQueryHandler implements QueryHandler<GetEquipmentByIdQuery, EquipmentResponse> {

    private final EquipmentRepository equipmentRepository;
    private final EquipmentMapper equipmentMapper;
    private final MessageSource messageSource;

    @Override
    public EquipmentResponse handle(GetEquipmentByIdQuery query) {
        Equipment equipment = equipmentRepository.findById(query.getEquipmentId())
                .orElseThrow(() -> {
                    String message = messageSource.getMessage(
                            "equipment.not.found",
                            null,
                            "Equipment not found",
                            LocaleContextHolder.getLocale()
                    );
                    return new ResourceNotFoundException(message);
                });

        return equipmentMapper.toResponse(equipment);
    }
}
