package com.pcpedia.api.iam.application.query;

import com.pcpedia.api.iam.application.dto.response.UserResponse;
import com.pcpedia.api.shared.application.cqrs.Query;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetAllUsersQuery implements Query<Page<UserResponse>> {

    private Pageable pageable;
    private String search;
    private Boolean isActive;
}
