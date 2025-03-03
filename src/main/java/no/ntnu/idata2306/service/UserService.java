package no.ntnu.idata2306.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import no.ntnu.idata2306.dto.user.UserResponseDto;
import no.ntnu.idata2306.dto.user.UserSignUpDto;
import no.ntnu.idata2306.dto.user.UserUpdateDto;
import no.ntnu.idata2306.model.Role;
import no.ntnu.idata2306.model.User;
import no.ntnu.idata2306.repository.RoleRepository;
import no.ntnu.idata2306.repository.UserRepository;
import no.ntnu.idata2306.security.AccessUserDetails;
import no.ntnu.idata2306.security.AuthorityLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    /**
     * Constructs a new instance of UserService.
     * The PasswordEncoder is marked as @Lazy to avoid circular dependencies.
     *
     * @param userRepository the repository for managing user data
     * @param passwordEncoder the password encoder for hashing passwords
     * @param roleRepository the repository for managing role data
     */
    @Autowired
    public UserService(UserRepository userRepository, @Lazy PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    /**
     * Retrieves all users from the repository and converts them to UserResponseDto objects to not include password.
     *
     * @return a list of UserResponseDto objects representing all users.
     */
    public List<UserResponseDto> getAll() {
        return this.userRepository.findAll().stream()
                .map(UserResponseDto::new)
                .toList();
    }

    /**
     * Retrieves all users from the repository that is not deleted and converts them to UserResponseDto objects.
     *
     * @return a list of UserResponseDto objects representing all users.
     */
    public List<UserResponseDto> getAllActiveUsers(){
        return this.userRepository.findByDeletedFalse().stream()
                .map(UserResponseDto:: new)
                .toList();
    }

    /**
     * Creates a new user with the provided sign-up information.
     *
     * @param userSignUpDto the DTO containing the user's sign-up information
     * @return the newly created UserResponseDto object
     */
    public UserResponseDto createUser(UserSignUpDto userSignUpDto){
        User user = new User(userSignUpDto);
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        setRole(user, AuthorityLevel.USER);
        this.userRepository.save(user);
        return new UserResponseDto(user);
    }

    /**
     * Updates an existing user with the provided information.
     *
     * @param id the ID of the user to be updated
     * @param userUpdateDto the DTO containing the updated user update information
     * @return the updated UserResponseDto object
     */
    public UserResponseDto updateUser(int id, UserUpdateDto userUpdateDto) {
        User user = getUserById(id);

        user.setFirstName(userUpdateDto.getFirstName());
        user.setLastName(userUpdateDto.getLastName());
        user.setEmail(userUpdateDto.getEmail());
        user.setPassword(this.passwordEncoder.encode(userUpdateDto.getPassword()));
        user.setPhoneNumber(userUpdateDto.getPhoneNumber());

        userRepository.save(user);
        log.info("User was updated successfully with ID: {}", id);
        return new UserResponseDto(user);
    }

    /**
     * Marks a user as deleted by setting the deleted field to true.
     *
     * @param id the ID of the user to be marked as deleted
     * @return UserResponseDto containing the updated user information
     */
    public UserResponseDto softDeleteUser(int id) {
        User user = getUserById(id);
        user.setDeleted(true);
        this.userRepository.save(user);
        log.info("User marked as deleted with ID: {}", id);

        return new UserResponseDto(user);
    }

    /**
     * Finds a user by their ID.
     *
     * @param id the ID of the user to be found
     * @return the User object if found
     * @throws EntityNotFoundException if the user with the specified ID is not found
     */
    public User getUserById(int id) {
        return this.userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("User not found with ID: {}", id);
                    return new EntityNotFoundException("User not found with ID: " + id);
                });
    }

    /**
     * Assigns a role to a user.
     *
     * @param user the user to whom the role will be assigned
     * @param roleName the name of the role to be assigned
     * @throws EntityNotFoundException if the specified role is not found
     */
    public void setRole(User user, String roleName){
        Role userRole = this.roleRepository.findByRole(roleName).orElseThrow(() -> new EntityNotFoundException("Role " + roleName + " not found"));
        user.getRoles().add(userRole);
    }

    /**
     * Returns the user of the curren session.
     *
     * @return user of current session.
     * @throws EntityNotFoundException if no user is found with the given email address
     */
    public User getSessionUser() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        String email = authentication.getName();
        return this.userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("User with email: " + email + " was not found"));
    }

    /**
     * Retrieves the user details associated with the specified email address.
     * This method is utilized by Spring Security during the authentication process.
     *
     * @param email the email address of the user to be retrieved
     * @return UserDetails containing the user's information and authorities
     * @throws EntityNotFoundException if no user is found with the given email address
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws EntityNotFoundException {
        User user = this.userRepository.findByEmail(email).orElseThrow(() -> {
            log.error("Failed to load user details: User with email {} not found.", email);
            return new EntityNotFoundException("No user found with the provided email address: " + email);
        });
        
        return new AccessUserDetails(user);

    }

}
