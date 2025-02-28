package no.ntnu.idata2306.repository;

import no.ntnu.idata2306.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing User entities.
 * Provides methods for performing CRUD operations and custom queries.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * Finds a user by their email address.
     *
     * @param email the email address of the user to find
     * @return an Optional containing the found user, or empty if no user is found
     */
    Optional<User> findByEmail(String email);

    /**
     * Finds all users that are not marked as deleted.
     *
     * @return a list of users where the deleted field is false
     */
    List<User> findByDeletedFalse();
}
