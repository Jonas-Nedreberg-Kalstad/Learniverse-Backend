package no.ntnu.idata2306.util.repository;

import java.util.Optional;

/**
 * Functional interface for repository findById method.
 *
 * @param <T> the type of the entity
 */
@FunctionalInterface
public interface FindByIdFunction<T> {
    /**
     * Finds an entity by its ID.
     *
     * @param id the ID of the entity to find
     * @return an Optional containing the found entity, or an empty Optional if not found
     */
    Optional<T> findById(int id);
}
