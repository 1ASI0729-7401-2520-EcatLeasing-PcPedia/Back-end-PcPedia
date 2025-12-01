package com.pcpedia.api.iam.application.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequest {

    @NotBlank(message = "{validation.email.required}")
    @Email(message = "{validation.email.invalid}")
    private String email;

    @NotBlank(message = "{validation.password.required}")
    @Size(min = 8, message = "{validation.password.size}")
    private String password;

    @NotBlank(message = "{validation.name.required}")
    private String name;

    private String companyName;

    @Pattern(regexp = "^[0-9]{11}$", message = "{validation.ruc.invalid}")
    private String ruc;

    private String phone;

    private String address;
}
