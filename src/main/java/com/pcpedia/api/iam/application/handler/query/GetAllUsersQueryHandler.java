package com.pcpedia.api.iam.application.handler.query;

import com.pcpedia.api.iam.application.dto.response.UserResponse;
import com.pcpedia.api.iam.application.mapper.UserMapper;
import com.pcpedia.api.iam.application.query.GetAllUsersQuery;
import com.pcpedia.api.iam.domain.model.aggregate.User;
import com.pcpedia.api.iam.domain.model.enums.Role;
import com.pcpedia.api.iam.domain.repository.UserRepository;
import com.pcpedia.api.shared.application.cqrs.QueryHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetAllUsersQueryHandler implements QueryHandler<GetAllUsersQuery, Page<UserResponse>> {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public Page<UserResponse> handle(GetAllUsersQuery query) {
        Page<User> users;
        boolean hasSearch = StringUtils.hasText(query.getSearch());
        Boolean isActive = query.getIsActive();

        if (isActive != null && hasSearch) {
            users = userRepository.findByRoleAndIsActiveAndSearch(Role.CLIENT, isActive, query.getSearch(), query.getPageable());
        } else if (isActive != null) {
            users = userRepository.findByRoleAndIsActive(Role.CLIENT, isActive, query.getPageable());
        } else if (hasSearch) {
            users = userRepository.findByRoleAndSearch(Role.CLIENT, query.getSearch(), query.getPageable());
        } else {
            users = userRepository.findByRole(Role.CLIENT, query.getPageable());
        }

        return users.map(userMapper::toResponse);
    }
}
