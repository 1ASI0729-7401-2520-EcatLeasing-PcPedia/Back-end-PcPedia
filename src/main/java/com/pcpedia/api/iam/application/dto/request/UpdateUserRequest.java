package com.pcpedia.api.iam.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequest {

    @NotBlank(message = "{validation.name.required}")
    private String name;

    private String companyName;

    @Pattern(regexp = "^[0-9]{11}$", message = "{validation.ruc.invalid}")
    private String ruc;

    private String phone;

    private String address;
}
