package com.pcpedia.api.iam.application.mapper;

import com.pcpedia.api.iam.application.command.CreateUserCommand;
import com.pcpedia.api.iam.application.command.UpdateUserCommand;
import com.pcpedia.api.iam.application.dto.request.CreateUserRequest;
import com.pcpedia.api.iam.application.dto.request.UpdateUserRequest;
import com.pcpedia.api.iam.application.dto.response.UserResponse;
import com.pcpedia.api.iam.domain.model.aggregate.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    CreateUserCommand toCommand(CreateUserRequest request);

    @Mapping(target = "userId", source = "id")
    UpdateUserCommand toCommand(Long id, UpdateUserRequest request);

    @Mapping(target = "role", expression = "java(user.getRole().name())")
    UserResponse toResponse(User user);
}
