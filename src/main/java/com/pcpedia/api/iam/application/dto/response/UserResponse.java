package com.pcpedia.api.iam.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private Long id;
    private String email;
    private String name;
    private String companyName;
    private String ruc;
    private String phone;
    private String address;
    private String role;
    private Boolean isActive;
    private LocalDateTime createdAt;
}
