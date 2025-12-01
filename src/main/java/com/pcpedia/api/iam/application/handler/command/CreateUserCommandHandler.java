package com.pcpedia.api.iam.application.handler.command;

import com.pcpedia.api.iam.application.command.CreateUserCommand;
import com.pcpedia.api.iam.domain.model.aggregate.User;
import com.pcpedia.api.iam.domain.model.enums.Role;
import com.pcpedia.api.iam.domain.repository.UserRepository;
import com.pcpedia.api.shared.application.cqrs.CommandHandler;
import com.pcpedia.api.shared.infrastructure.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CreateUserCommandHandler implements CommandHandler<CreateUserCommand, Long> {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MessageSource messageSource;

    @Override
    public Long handle(CreateUserCommand command) {
        if (userRepository.existsByEmail(command.getEmail())) {
            String message = messageSource.getMessage(
                    "user.email.exists",
                    null,
                    "Email already registered",
                    LocaleContextHolder.getLocale()
            );
            throw new BadRequestException(message);
        }

        User user = User.builder()
                .email(command.getEmail())
                .password(passwordEncoder.encode(command.getPassword()))
                .name(command.getName())
                .companyName(command.getCompanyName())
                .ruc(command.getRuc())
                .phone(command.getPhone())
                .address(command.getAddress())
                .role(Role.CLIENT)
                .isActive(true)
                .build();

        User savedUser = userRepository.save(user);
        return savedUser.getId();
    }
}
