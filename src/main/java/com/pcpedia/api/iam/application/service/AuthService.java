package com.pcpedia.api.iam.application.service;

import com.pcpedia.api.iam.application.dto.request.LoginRequest;
import com.pcpedia.api.iam.application.dto.response.AuthResponse;
import com.pcpedia.api.iam.application.dto.response.UserResponse;
import com.pcpedia.api.iam.application.mapper.UserMapper;
import com.pcpedia.api.iam.domain.model.aggregate.User;
import com.pcpedia.api.iam.domain.repository.UserRepository;
import com.pcpedia.api.shared.infrastructure.exception.ResourceNotFoundException;
import com.pcpedia.api.shared.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final MessageSource messageSource;

    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenProvider.generateToken(authentication);

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    String message = messageSource.getMessage(
                            "user.not.found",
                            null,
                            "User not found",
                            LocaleContextHolder.getLocale()
                    );
                    return new ResourceNotFoundException(message);
                });

        UserResponse userResponse = userMapper.toResponse(user);
        return AuthResponse.of(token, userResponse);
    }

    public UserResponse getCurrentUser(String email) {
        User user = userRepository.findByEmail(email)
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
