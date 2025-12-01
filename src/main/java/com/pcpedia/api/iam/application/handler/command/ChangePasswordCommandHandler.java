package com.pcpedia.api.iam.application.handler.command;

import com.pcpedia.api.iam.application.command.ChangePasswordCommand;
import com.pcpedia.api.iam.domain.model.aggregate.User;
import com.pcpedia.api.iam.domain.repository.UserRepository;
import com.pcpedia.api.shared.application.cqrs.CommandHandler;
import com.pcpedia.api.shared.infrastructure.exception.BadRequestException;
import com.pcpedia.api.shared.infrastructure.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ChangePasswordCommandHandler implements CommandHandler<ChangePasswordCommand, Void> {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MessageSource messageSource;

    @Override
    public Void handle(ChangePasswordCommand command) {
        User user = userRepository.findById(command.getUserId())
                .orElseThrow(() -> {
                    String message = messageSource.getMessage(
                            "user.not.found",
                            null,
                            "User not found",
                            LocaleContextHolder.getLocale()
                    );
                    return new ResourceNotFoundException(message);
                });

        if (!passwordEncoder.matches(command.getCurrentPassword(), user.getPassword())) {
            String message = messageSource.getMessage(
                    "user.password.invalid",
                    null,
                    "Current password is incorrect",
                    LocaleContextHolder.getLocale()
            );
            throw new BadRequestException(message);
        }

        user.setPassword(passwordEncoder.encode(command.getNewPassword()));
        userRepository.save(user);
        return null;
    }
}
