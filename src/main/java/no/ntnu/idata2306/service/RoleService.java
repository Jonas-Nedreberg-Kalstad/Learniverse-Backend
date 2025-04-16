package no.ntnu.idata2306.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import no.ntnu.idata2306.model.Role;
import no.ntnu.idata2306.repository.RoleRepository;
import no.ntnu.idata2306.util.repository.RepositoryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    /**
     * Finds a role by role name
     *
     * @param roleName the role name of the role to be found
     * @return the Role object if found
     * @throws EntityNotFoundException if the role with the specified role name is not found
     */
    public Role findRoleByRoleName(String roleName){
        return this.roleRepository.findByRole(roleName).orElseThrow(() -> new EntityNotFoundException("Role " + roleName + " not found"));
    }

    /**
     * Finds a role by their ID.
     *
     * @param id the ID of the role to be found
     * @return the Role object if found
     * @throws EntityNotFoundException if the role with the specified ID is not found
     */
    public Role findRoleById(int id){
        return RepositoryUtils.findEntityById(roleRepository::findById, id, Role.class);
    }
}
