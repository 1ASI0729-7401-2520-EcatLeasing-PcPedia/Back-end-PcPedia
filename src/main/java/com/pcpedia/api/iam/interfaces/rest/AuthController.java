package com.pcpedia.api.iam.interfaces.rest;

import com.pcpedia.api.iam.application.command.ChangePasswordCommand;
import com.pcpedia.api.iam.application.dto.request.ChangePasswordRequest;
import com.pcpedia.api.iam.application.dto.request.LoginRequest;
import com.pcpedia.api.iam.application.dto.response.AuthResponse;
import com.pcpedia.api.iam.application.dto.response.UserResponse;
import com.pcpedia.api.iam.application.service.AuthService;
import com.pcpedia.api.iam.application.service.UserService;
import com.pcpedia.api.iam.domain.model.aggregate.User;
import com.pcpedia.api.iam.domain.repository.UserRepository;
import com.pcpedia.api.shared.interfaces.rest.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication endpoints")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final MessageSource messageSource;

    @PostMapping("/login")
    @Operation(summary = "Login", description = "Authenticate user and get JWT token")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        String message = messageSource.getMessage(
                "auth.login.success",
                null,
                "Login successful",
                LocaleContextHolder.getLocale()
        );
        return ResponseEntity.ok(ApiResponse.success(message, response));
    }

    @GetMapping("/me")
    @Operation(summary = "Get current user", description = "Get the authenticated user's information")
    public ResponseEntity<ApiResponse<UserResponse>> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        UserResponse user = authService.getCurrentUser(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success(user));
    }

    @PostMapping("/change-password")
    @Operation(summary = "Change password", description = "Change the current user's password")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody ChangePasswordRequest request) {

        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();

        ChangePasswordCommand command = ChangePasswordCommand.builder()
                .userId(user.getId())
                .currentPassword(request.getCurrentPassword())
                .newPassword(request.getNewPassword())
                .build();

        userService.changePassword(command);

        String message = messageSource.getMessage(
                "auth.password.changed",
                null,
                "Password changed successfully",
                LocaleContextHolder.getLocale()
        );
        return ResponseEntity.ok(ApiResponse.success(message));
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout", description = "Logout the current user")
    public ResponseEntity<ApiResponse<Void>> logout() {
        String message = messageSource.getMessage(
                "auth.logout.success",
                null,
                "Logged out successfully",
                LocaleContextHolder.getLocale()
        );
        return ResponseEntity.ok(ApiResponse.success(message));
    }
}
