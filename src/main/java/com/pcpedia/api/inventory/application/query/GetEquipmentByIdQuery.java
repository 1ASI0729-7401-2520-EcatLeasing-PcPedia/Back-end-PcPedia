package com.pcpedia.api.inventory.application.query;

import com.pcpedia.api.inventory.application.dto.response.EquipmentResponse;
import com.pcpedia.api.shared.application.cqrs.Query;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetEquipmentByIdQuery implements Query<EquipmentResponse> {

    private Long equipmentId;
}
