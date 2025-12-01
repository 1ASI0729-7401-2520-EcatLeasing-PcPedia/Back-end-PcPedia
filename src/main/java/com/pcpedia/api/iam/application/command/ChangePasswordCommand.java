package com.pcpedia.api.iam.application.command;

import com.pcpedia.api.shared.application.cqrs.Command;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordCommand implements Command<Void> {

    private Long userId;
    private String currentPassword;
    private String newPassword;
}
