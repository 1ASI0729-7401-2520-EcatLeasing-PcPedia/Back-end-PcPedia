package com.pcpedia.api.inventory.application.query;

import com.pcpedia.api.inventory.application.dto.response.CatalogEquipmentResponse;
import com.pcpedia.api.shared.application.cqrs.Query;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetAvailableEquipmentQuery implements Query<Page<CatalogEquipmentResponse>> {

    private Pageable pageable;
    private String search;
    private String category;
}
