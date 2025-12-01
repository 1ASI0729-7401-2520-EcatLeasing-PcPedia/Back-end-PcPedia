package com.pcpedia.api.iam.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordRequest {

    @NotBlank(message = "{validation.password.current.required}")
    private String currentPassword;

    @NotBlank(message = "{validation.password.new.required}")
    @Size(min = 8, message = "{validation.password.size}")
    private String newPassword;
}
