package com.pcpedia.api.inventory.application.dto.request;

import com.pcpedia.api.inventory.domain.model.enums.EquipmentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeEquipmentStatusRequest {

    @NotNull(message = "{validation.status.required}")
    private EquipmentStatus status;
}
