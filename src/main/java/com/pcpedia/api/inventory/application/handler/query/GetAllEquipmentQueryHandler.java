package com.pcpedia.api.inventory.application.handler.query;

import com.pcpedia.api.inventory.application.dto.response.EquipmentResponse;
import com.pcpedia.api.inventory.application.mapper.EquipmentMapper;
import com.pcpedia.api.inventory.application.query.GetAllEquipmentQuery;
import com.pcpedia.api.inventory.domain.model.aggregate.Equipment;
import com.pcpedia.api.inventory.domain.repository.EquipmentRepository;
import com.pcpedia.api.shared.application.cqrs.QueryHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetAllEquipmentQueryHandler implements QueryHandler<GetAllEquipmentQuery, Page<EquipmentResponse>> {

    private final EquipmentRepository equipmentRepository;
    private final EquipmentMapper equipmentMapper;

    @Override
    public Page<EquipmentResponse> handle(GetAllEquipmentQuery query) {
        Page<Equipment> equipment;

        if (StringUtils.hasText(query.getSearch())) {
            equipment = equipmentRepository.searchEquipment(query.getSearch(), query.getPageable());
        } else if (StringUtils.hasText(query.getCategory())) {
            equipment = equipmentRepository.findByCategory(query.getCategory(), query.getPageable());
        } else {
            equipment = equipmentRepository.findAll(query.getPageable());
        }

        return equipment.map(equipmentMapper::toResponse);
    }
}
