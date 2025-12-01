package com.pcpedia.api.shared.application.cqrs;

/**
 * Handler interface for CQRS Commands.
 *
 * @param <C> the command type
 * @param <R> the result type
 */
public interface CommandHandler<C extends Command<R>, R> {
    R handle(C command);
}
