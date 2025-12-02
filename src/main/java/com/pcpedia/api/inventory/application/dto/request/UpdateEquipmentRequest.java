package com.pcpedia.api.inventory.application.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEquipmentRequest {

    @NotBlank(message = "{validation.name.required}")
    private String name;

    private String brand;

    private String model;

    private String serialNumber;

    private String category;

    private String specifications;

    @NotNull(message = "{validation.price.required}")
    @DecimalMin(value = "0.01", message = "{validation.price.min}")
    private BigDecimal basePrice;

    private String imageUrl;

    private LocalDate purchaseDate;
}
