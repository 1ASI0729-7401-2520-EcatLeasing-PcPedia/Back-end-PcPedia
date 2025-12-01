package com.pcpedia.api.iam.application.command;

import com.pcpedia.api.shared.application.cqrs.Command;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ToggleUserStatusCommand implements Command<Void> {

    private Long userId;
}
