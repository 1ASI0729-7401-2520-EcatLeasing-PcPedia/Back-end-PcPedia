package com.pcpedia.api.inventory.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentResponse {

    private Long id;
    private String name;
    private String brand;
    private String model;
    private String serialNumber;
    private String category;
    private String specifications;
    private BigDecimal basePrice;
    private String imageUrl;
    private String status;
    private LocalDate purchaseDate;
    private LocalDateTime createdAt;
}
