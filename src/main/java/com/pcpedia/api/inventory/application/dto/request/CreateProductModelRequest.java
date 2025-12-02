package com.pcpedia.api.inventory.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateProductModelRequest {

    @NotBlank(message = "Name is required")
    @Size(max = 150)
    private String name;

    @Size(max = 100)
    private String brand;

    @Size(max = 100)
    private String model;

    @Size(max = 50)
    private String category;

    private String specifications;

    private BigDecimal basePrice;

    @Size(max = 500)
    private String imageUrl;
}
