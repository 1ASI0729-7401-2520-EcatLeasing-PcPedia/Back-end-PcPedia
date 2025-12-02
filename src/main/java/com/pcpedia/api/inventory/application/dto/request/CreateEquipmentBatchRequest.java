package com.pcpedia.api.inventory.application.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class CreateEquipmentBatchRequest {

    @NotNull(message = "Product model ID is required")
    private Long productModelId;

    @NotEmpty(message = "Serial numbers list is required")
    private List<String> serialNumbers;

    private LocalDate purchaseDate;
}
