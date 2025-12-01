package com.pcpedia.api.shared.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

/**
 * Centralized service for i18n message resolution.
 * Provides a clean API for retrieving localized messages throughout the application.
 */
@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageSource messageSource;

    /**
     * Get a localized message by key.
     *
     * @param key the message key
     * @return the localized message
     */
    public String get(String key) {
        return messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
    }

    /**
     * Get a localized message by key with arguments.
     *
     * @param key  the message key
     * @param args the arguments to interpolate
     * @return the localized message with interpolated arguments
     */
    public String get(String key, Object... args) {
        return messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
    }

    /**
     * Get a localized message by key with a default value.
     *
     * @param key          the message key
     * @param defaultValue the default value if key is not found
     * @return the localized message or default value
     */
    public String getOrDefault(String key, String defaultValue) {
        return messageSource.getMessage(key, null, defaultValue, LocaleContextHolder.getLocale());
    }
}
