package com.pcpedia.api.inventory.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CatalogEquipmentResponse {

    private Long id;
    private String name;
    private String brand;
    private String model;
    private String category;
    private String specifications;
    private String imageUrl;
    private String status;
    // Note: basePrice is NOT included - clients should not see prices
}
