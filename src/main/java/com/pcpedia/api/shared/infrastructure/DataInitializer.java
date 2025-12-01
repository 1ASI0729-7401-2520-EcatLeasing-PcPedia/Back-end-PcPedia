package com.pcpedia.api.shared.infrastructure;

import com.pcpedia.api.iam.domain.model.aggregate.User;
import com.pcpedia.api.iam.domain.model.enums.Role;
import com.pcpedia.api.iam.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.email:}")
    private String adminEmail;

    @Value("${admin.password:}")
    private String adminPassword;

    @Override
    public void run(String... args) {
        createInitialAdminIfNotExists();
    }

    private void createInitialAdminIfNotExists() {
        long adminCount = userRepository.countByRole(Role.ADMIN);

        if (adminCount == 0) {
            if (isValidAdminConfig()) {
                User admin = User.builder()
                        .email(adminEmail)
                        .password(passwordEncoder.encode(adminPassword))
                        .name("Administrador PcPedia")
                        .role(Role.ADMIN)
                        .isActive(true)
                        .build();

                userRepository.save(admin);
                log.info("Admin inicial creado exitosamente: {}", adminEmail);
            } else {
                log.warn("No se encontraron las variables ADMIN_EMAIL y ADMIN_PASSWORD");
                log.warn("Configure estas variables para crear el administrador inicial");
            }
        } else {
            log.info("Ya existe(n) {} administrador(es) en el sistema", adminCount);
        }
    }

    private boolean isValidAdminConfig() {
        return adminEmail != null && !adminEmail.trim().isEmpty()
                && adminPassword != null && !adminPassword.trim().isEmpty();
    }
}
