package com.pcpedia.api.iam.application.query;

import com.pcpedia.api.iam.application.dto.response.UserResponse;
import com.pcpedia.api.shared.application.cqrs.Query;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetUserByIdQuery implements Query<UserResponse> {

    private Long userId;
}
