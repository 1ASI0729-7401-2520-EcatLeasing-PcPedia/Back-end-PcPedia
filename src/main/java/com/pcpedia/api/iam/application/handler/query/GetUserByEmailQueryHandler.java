package com.pcpedia.api.iam.application.handler.query;

import com.pcpedia.api.iam.application.dto.response.UserResponse;
import com.pcpedia.api.iam.application.mapper.UserMapper;
import com.pcpedia.api.iam.application.query.GetUserByEmailQuery;
import com.pcpedia.api.iam.domain.model.aggregate.User;
import com.pcpedia.api.iam.domain.repository.UserRepository;
import com.pcpedia.api.shared.application.cqrs.QueryHandler;
import com.pcpedia.api.shared.infrastructure.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetUserByEmailQueryHandler implements QueryHandler<GetUserByEmailQuery, UserResponse> {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final MessageSource messageSource;

    @Override
    public UserResponse handle(GetUserByEmailQuery query) {
        User user = userRepository.findByEmail(query.getEmail())
                .orElseThrow(() -> {
                    String message = messageSource.getMessage(
                            "user.not.found",
                            null,
                            "User not found",
                            LocaleContextHolder.getLocale()
                    );
                    return new ResourceNotFoundException(message);
                });

        return userMapper.toResponse(user);
    }
}
