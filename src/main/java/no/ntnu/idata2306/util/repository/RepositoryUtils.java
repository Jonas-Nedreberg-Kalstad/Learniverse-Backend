package no.ntnu.idata2306.util.repository;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

/**
 * Utility class for repository operations.
 * Provides generic methods for common repository operations.
 */
@Slf4j
public class RepositoryUtils {

    /**
     * Finds an entity by its ID using the provided repository.
     * If the entity is not found, logs an error and throws an EntityNotFoundException.
     *
     * @param repository the repository to use for finding the entity
     * @param id the ID of the entity to find
     * @param entityClass the class of the entity
     * @param <T> the type of the entity
     * @return the found entity
     * @throws EntityNotFoundException if the entity is not found
     */
    public static <T> T findEntityById(FindByIdFunction<T> repository, int id, Class<T> entityClass) {
        return repository.findById(id)
                .orElseThrow(() -> {
                    String entityName = entityClass.getSimpleName();
                    log.error("{} not found with ID: {}", entityName, id);
                    return new EntityNotFoundException(entityName + " not found with ID: " + id);
                });
    }

    private RepositoryUtils() {
    }
}
