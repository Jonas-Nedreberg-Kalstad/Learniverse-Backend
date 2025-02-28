package no.ntnu.idata2306.repository;

import no.ntnu.idata2306.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    /**
     * Finds a Role by their role name.
     *
     * @param role the role name of the role to find
     * @return an Optional containing the found Role, or empty if no Role is found
     */
    Optional<Role> findByRole(String role);
}