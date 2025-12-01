package com.pcpedia.api.shared.application.cqrs;

/**
 * Handler interface for CQRS Queries.
 *
 * @param <Q> the query type
 * @param <R> the result type
 */
public interface QueryHandler<Q extends Query<R>, R> {
    R handle(Q query);
}
