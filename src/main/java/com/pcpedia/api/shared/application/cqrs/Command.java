package com.pcpedia.api.shared.application.cqrs;

/**
 * Marker interface for CQRS Commands.
 * Commands represent write operations that modify state.
 *
 * @param <R> the type of result returned by the command
 */
public interface Command<R> {
}
