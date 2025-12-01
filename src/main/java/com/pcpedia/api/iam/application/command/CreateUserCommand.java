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
public class CreateUserCommand implements Command<Long> {

    private String email;
    private String password;
    private String name;
    private String companyName;
    private String ruc;
    private String phone;
    private String address;
}
