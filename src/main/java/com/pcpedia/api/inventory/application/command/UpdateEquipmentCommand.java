package com.pcpedia.api.inventory.application.command;

import com.pcpedia.api.shared.application.cqrs.Command;
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
public class UpdateEquipmentCommand implements Command<Void> {

    private Long equipmentId;
    private String name;
    private String brand;
    private String model;
    private String serialNumber;
    private String category;
    private String specifications;
    private BigDecimal basePrice;
    private String imageUrl;
    private LocalDate purchaseDate;
}
