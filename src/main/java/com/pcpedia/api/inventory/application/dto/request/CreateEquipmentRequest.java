package com.pcpedia.api.inventory.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateEquipmentRequest {

    @NotNull(message = "{validation.productModel.required}")
    private Long productModelId;

    @NotBlank(message = "{validation.serialNumber.required}")
    private String serialNumber;

    private LocalDate purchaseDate;
}
