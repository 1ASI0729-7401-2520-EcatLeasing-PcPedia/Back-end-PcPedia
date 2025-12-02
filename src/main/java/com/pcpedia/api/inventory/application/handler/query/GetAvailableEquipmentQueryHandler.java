package com.pcpedia.api.inventory.application.handler.query;

import com.pcpedia.api.inventory.application.dto.response.CatalogEquipmentResponse;
import com.pcpedia.api.inventory.application.mapper.EquipmentMapper;
import com.pcpedia.api.inventory.application.query.GetAvailableEquipmentQuery;
import com.pcpedia.api.inventory.domain.model.aggregate.Equipment;
import com.pcpedia.api.inventory.domain.model.enums.EquipmentStatus;
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
public class GetAvailableEquipmentQueryHandler implements QueryHandler<GetAvailableEquipmentQuery, Page<CatalogEquipmentResponse>> {

    private final EquipmentRepository equipmentRepository;
    private final EquipmentMapper equipmentMapper;

    @Override
    public Page<CatalogEquipmentResponse> handle(GetAvailableEquipmentQuery query) {
        Page<Equipment> equipment;

        if (StringUtils.hasText(query.getSearch())) {
            equipment = equipmentRepository.searchByStatusAndKeyword(
                    EquipmentStatus.AVAILABLE,
                    query.getSearch(),
                    query.getPageable()
            );
        } else if (StringUtils.hasText(query.getCategory())) {
            equipment = equipmentRepository.findByStatusAndCategory(
                    EquipmentStatus.AVAILABLE,
                    query.getCategory(),
                    query.getPageable()
            );
        } else {
            equipment = equipmentRepository.findByStatus(EquipmentStatus.AVAILABLE, query.getPageable());
        }

        return equipment.map(equipmentMapper::toCatalogResponse);
    }
}
