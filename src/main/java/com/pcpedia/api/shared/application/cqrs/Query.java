package com.pcpedia.api.shared.application.cqrs;

/**
 * Marker interface for CQRS Queries.
 * Queries represent read operations that don't modify state.
 *
 * @param <R> the type of result returned by the query
 */
public interface Query<R> {
}
