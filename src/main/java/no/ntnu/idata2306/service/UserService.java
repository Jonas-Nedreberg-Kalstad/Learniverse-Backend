package no.ntnu.idata2306.service;

import lombok.extern.slf4j.Slf4j;
import no.ntnu.idata2306.dto.UserSignUpDto;
import no.ntnu.idata2306.dto.UserUpdateDto;
import no.ntnu.idata2306.model.Role;
import no.ntnu.idata2306.model.User;
import no.ntnu.idata2306.repository.RoleRepository;
import no.ntnu.idata2306.repository.UserRepository;
import no.ntnu.idata2306.security.AccessUserDetails;
import no.ntnu.idata2306.security.AuthorityLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
     * Retrieves a user by their unique ID.
     *
     * @param id the unique identifier of the user
     * @return an Optional containing the user if found, or an empty Optional if no user is found with the given ID
     */
    public Optional<User> getUserById(int id) {
        return this.userRepository.findById(id);
    }

    /**
     * Creates a new user with the provided sign-up information.
     *
     * @param userSignUpDto the DTO containing the user's sign-up information
     * @return the newly created User object
     */
    public User createUser(UserSignUpDto userSignUpDto){
        User user = new User(userSignUpDto);
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        setRole(user, AuthorityLevel.USER);
        this.userRepository.save(user);
        return user;
    }

    /**
     * Updates an existing user with the provided information.
     *
     * @param id the ID of the user to be updated
     * @param userUpdateDto the DTO containing the updated user update information
     * @return the updated User object
     * @throws RuntimeException if the user with the specified ID is not found
     */
    public User updateUser(int id, UserUpdateDto userUpdateDto) {
        User user = findUserById(id);

        user.setFirstName(userUpdateDto.getFirstName());
        user.setLastName(userUpdateDto.getLastName());
        user.setEmail(userUpdateDto.getEmail());
        user.setPassword(this.passwordEncoder.encode(userUpdateDto.getPassword()));
        user.setPhoneNumber(userUpdateDto.getPhoneNumber());

        userRepository.save(user);
        log.info("User was updated successfully with ID: {}", id);
        return user;
    }

    /**
     * Marks a user as deleted by setting the deleted field to true.
     *
     * @param id the ID of the user to be marked as deleted
     * @throws RuntimeException if the user with the specified ID is not found
     */
    public void softDeleteUser(int id) {
        User user = findUserById(id);
        user.setDeleted(true);
        this.userRepository.save(user);
        log.info("User marked as deleted with ID: {}", id);
    }

    /**
     * Finds a user by their ID.
     *
     * @param id the ID of the user to be found
     * @return the User object if found
     * @throws RuntimeException if the user with the specified ID is not found
     */
    public User findUserById(int id) {
        return this.userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("User not found with ID: {}", id);
                    return new RuntimeException("User not found with ID: " + id);
                });
    }

    public void setRole(User user, String roleName){
        Role userRole = this.roleRepository.findByRole(roleName).orElseThrow(() -> new IllegalArgumentException("Role " + roleName + " not found"));
        user.getRoles().add(userRole);
    }

    /**
     * Retrieves the user details associated with the specified email address.
     * This method is utilized by Spring Security during the authentication process.
     *
     * @param email the email address of the user to be retrieved
     * @return UserDetails containing the user's information and authorities
     * @throws UsernameNotFoundException if no user is found with the given email address
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = this.userRepository.findByEmail(email);
        if (user.isPresent()) {
            return new AccessUserDetails(user.get());
        } else {
            log.error("Failed to load user details: User with email {} not found.", email);
            throw new IllegalArgumentException("No user found with the provided email address: " + email);
        }
    }

}
