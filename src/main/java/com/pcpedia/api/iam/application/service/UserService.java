package com.pcpedia.api.iam.application.service;

import com.pcpedia.api.iam.application.command.ChangePasswordCommand;
import com.pcpedia.api.iam.application.command.CreateUserCommand;
import com.pcpedia.api.iam.application.command.ToggleUserStatusCommand;
import com.pcpedia.api.iam.application.command.UpdateUserCommand;
import com.pcpedia.api.iam.application.dto.response.UserResponse;
import com.pcpedia.api.iam.application.handler.command.ChangePasswordCommandHandler;
import com.pcpedia.api.iam.application.handler.command.CreateUserCommandHandler;
import com.pcpedia.api.iam.application.handler.command.ToggleUserStatusCommandHandler;
import com.pcpedia.api.iam.application.handler.command.UpdateUserCommandHandler;
import com.pcpedia.api.iam.application.handler.query.GetAllUsersQueryHandler;
import com.pcpedia.api.iam.application.handler.query.GetUserByEmailQueryHandler;
import com.pcpedia.api.iam.application.handler.query.GetUserByIdQueryHandler;
import com.pcpedia.api.iam.application.query.GetAllUsersQuery;
import com.pcpedia.api.iam.application.query.GetUserByEmailQuery;
import com.pcpedia.api.iam.application.query.GetUserByIdQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    // Command Handlers
    private final CreateUserCommandHandler createUserHandler;
    private final UpdateUserCommandHandler updateUserHandler;
    private final ChangePasswordCommandHandler changePasswordHandler;
    private final ToggleUserStatusCommandHandler toggleStatusHandler;

    // Query Handlers
    private final GetUserByIdQueryHandler getUserByIdHandler;
    private final GetUserByEmailQueryHandler getUserByEmailHandler;
    private final GetAllUsersQueryHandler getAllUsersHandler;

    // Commands
    public Long createUser(CreateUserCommand command) {
        return createUserHandler.handle(command);
    }

    public void updateUser(UpdateUserCommand command) {
        updateUserHandler.handle(command);
    }

    public void changePassword(ChangePasswordCommand command) {
        changePasswordHandler.handle(command);
    }

    public void toggleUserStatus(Long userId) {
        toggleStatusHandler.handle(new ToggleUserStatusCommand(userId));
    }

    // Queries
    public UserResponse getUserById(Long userId) {
        return getUserByIdHandler.handle(new GetUserByIdQuery(userId));
    }

    public UserResponse getUserByEmail(String email) {
        return getUserByEmailHandler.handle(new GetUserByEmailQuery(email));
    }

    public Page<UserResponse> getAllUsers(Pageable pageable, String search, Boolean isActive) {
        return getAllUsersHandler.handle(new GetAllUsersQuery(pageable, search, isActive));
    }
}
