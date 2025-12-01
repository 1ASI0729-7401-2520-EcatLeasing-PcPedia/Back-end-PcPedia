package com.pcpedia.api.iam.interfaces.rest;

import com.pcpedia.api.iam.application.command.CreateUserCommand;
import com.pcpedia.api.iam.application.command.UpdateUserCommand;
import com.pcpedia.api.iam.application.dto.request.CreateUserRequest;
import com.pcpedia.api.iam.application.dto.request.UpdateUserRequest;
import com.pcpedia.api.iam.application.dto.response.UserResponse;
import com.pcpedia.api.iam.application.mapper.UserMapper;
import com.pcpedia.api.iam.application.service.UserService;
import com.pcpedia.api.shared.interfaces.rest.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "User management - ADMIN only")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final MessageSource messageSource;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create client", description = "Create a new client user")
    public ResponseEntity<ApiResponse<Long>> createUser(@Valid @RequestBody CreateUserRequest request) {
        CreateUserCommand command = userMapper.toCommand(request);
        Long userId = userService.createUser(command);
        String message = messageSource.getMessage(
                "user.created",
                null,
                "User created successfully",
                LocaleContextHolder.getLocale()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(message, userId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get client by ID", description = "Get client details by ID")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable Long id) {
        UserResponse user = userService.getUserById(id);
        return ResponseEntity.ok(ApiResponse.success(user));
    }

    @GetMapping
    @Operation(summary = "List clients", description = "Get paginated list of clients with optional search and status filter")
    public ResponseEntity<ApiResponse<Page<UserResponse>>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Boolean isActive) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<UserResponse> users = userService.getAllUsers(pageable, search, isActive);
        return ResponseEntity.ok(ApiResponse.success(users));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update client", description = "Update client information")
    public ResponseEntity<ApiResponse<Void>> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request) {

        UpdateUserCommand command = userMapper.toCommand(id, request);
        userService.updateUser(command);
        String message = messageSource.getMessage(
                "user.updated",
                null,
                "User updated successfully",
                LocaleContextHolder.getLocale()
        );
        return ResponseEntity.ok(ApiResponse.success(message));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Toggle client status", description = "Activate/Deactivate client")
    public ResponseEntity<ApiResponse<Void>> toggleUserStatus(@PathVariable Long id) {
        userService.toggleUserStatus(id);
        String message = messageSource.getMessage(
                "user.status.updated",
                null,
                "User status updated successfully",
                LocaleContextHolder.getLocale()
        );
        return ResponseEntity.ok(ApiResponse.success(message));
    }
}
