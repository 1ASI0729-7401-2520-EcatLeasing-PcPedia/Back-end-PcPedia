package com.pcpedia.api.inventory.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductModelResponse {

    private Long id;
    private String name;
    private String brand;
    private String model;
    private String category;
    private String specifications;
    private BigDecimal basePrice;
    private String imageUrl;
    private Boolean isActive;
    private LocalDateTime createdAt;

    // Stock counts
    private Long totalEquipments;
    private Long availableEquipments;
    private Long leasedEquipments;
    private Long maintenanceEquipments;
}
