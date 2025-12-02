package com.pcpedia.api.inventory.application.command;

import com.pcpedia.api.shared.application.cqrs.Command;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateEquipmentCommand implements Command<Long> {

    private Long productModelId;
    private String serialNumber;
    private LocalDate purchaseDate;
}
