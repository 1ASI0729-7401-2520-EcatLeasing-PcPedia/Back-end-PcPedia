package com.pcpedia.api.iam.application.handler.command;

import com.pcpedia.api.iam.application.command.UpdateUserCommand;
import com.pcpedia.api.iam.domain.model.aggregate.User;
import com.pcpedia.api.iam.domain.repository.UserRepository;
import com.pcpedia.api.shared.application.cqrs.CommandHandler;
import com.pcpedia.api.shared.infrastructure.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UpdateUserCommandHandler implements CommandHandler<UpdateUserCommand, Void> {

    private final UserRepository userRepository;
    private final MessageSource messageSource;

    @Override
    public Void handle(UpdateUserCommand command) {
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

        user.setName(command.getName());
        user.setCompanyName(command.getCompanyName());
        user.setRuc(command.getRuc());
        user.setPhone(command.getPhone());
        user.setAddress(command.getAddress());

        userRepository.save(user);
        return null;
    }
}
