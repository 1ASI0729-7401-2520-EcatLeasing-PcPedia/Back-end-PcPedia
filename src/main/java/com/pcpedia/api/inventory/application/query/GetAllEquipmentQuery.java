package com.pcpedia.api.inventory.application.query;

import com.pcpedia.api.inventory.application.dto.response.EquipmentResponse;
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
public class GetAllEquipmentQuery implements Query<Page<EquipmentResponse>> {

    private Pageable pageable;
    private String search;
    private String category;
    private String status;
}
