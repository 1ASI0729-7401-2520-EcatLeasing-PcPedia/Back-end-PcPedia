package com.pcpedia.api.inventory.application.command;

import com.pcpedia.api.shared.application.cqrs.Command;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeleteEquipmentCommand implements Command<Void> {

    private Long equipmentId;
}
