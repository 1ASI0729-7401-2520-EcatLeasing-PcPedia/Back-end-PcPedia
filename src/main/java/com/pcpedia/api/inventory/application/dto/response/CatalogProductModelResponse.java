package com.pcpedia.api.inventory.application.dto.response;

import lombok.Builder;
import lombok.Data;

/**
 * DTO for catalog view - shows ProductModels with available stock
 * Note: NO price information is exposed to clients
 */
@Data
@Builder
public class CatalogProductModelResponse {
    private Long id;
    private String name;
    private String brand;
    private String model;
    private String category;
    private String specifications;
    private String imageUrl;
    private Long availableStock;  // Number of available equipment units
}
