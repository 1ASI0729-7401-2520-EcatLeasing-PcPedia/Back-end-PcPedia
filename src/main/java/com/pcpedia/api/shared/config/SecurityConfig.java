package com.pcpedia.api.shared.config;

import com.pcpedia.api.shared.security.JwtAuthenticationEntryPoint;
import com.pcpedia.api.shared.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final JwtAuthenticationEntryPoint jwtEntryPoint;
    private final UserDetailsService userDetailsService;
    private final CorsConfigurationSource corsConfigurationSource;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .exceptionHandling(ex -> ex.authenticationEntryPoint(jwtEntryPoint))
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints
                        .requestMatchers("/api/auth/login").permitAll()
                        .requestMatchers("/swagger-ui/**", "/api-docs/**", "/swagger-ui.html", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/actuator/health").permitAll()

                        // ADMIN only
                        .requestMatchers("/api/users/**").hasRole("ADMIN")
                        .requestMatchers("/api/inventory/**").hasRole("ADMIN")
                        .requestMatchers("/api/payments/**").hasRole("ADMIN")
                        .requestMatchers("/api/dashboard/admin").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/quotes").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/quotes/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/quotes/*/send").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/requests/*/reject").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/contracts").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/contracts/*/cancel").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/contracts/*/renew").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/invoices").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/tickets/*/status").hasRole("ADMIN")

                        // CLIENT only
                        .requestMatchers("/api/catalog/**").hasRole("CLIENT")
                        .requestMatchers("/api/my-equipment/**").hasRole("CLIENT")
                        .requestMatchers("/api/dashboard/client").hasRole("CLIENT")
                        .requestMatchers(HttpMethod.POST, "/api/requests").hasRole("CLIENT")
                        .requestMatchers(HttpMethod.POST, "/api/tickets").hasRole("CLIENT")
                        .requestMatchers(HttpMethod.PATCH, "/api/quotes/*/accept").hasRole("CLIENT")
                        .requestMatchers(HttpMethod.PATCH, "/api/quotes/*/reject").hasRole("CLIENT")

                        // All authenticated users (BOTH)
                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
