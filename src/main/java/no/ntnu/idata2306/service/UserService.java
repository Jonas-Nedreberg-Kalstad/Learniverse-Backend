package no.ntnu.idata2306.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import no.ntnu.idata2306.dto.user.UserResponseDto;
import no.ntnu.idata2306.dto.user.UserSignUpDto;
import no.ntnu.idata2306.dto.user.UserUpdateDto;
import no.ntnu.idata2306.mapper.UserMapper;
import no.ntnu.idata2306.model.Provider;
import no.ntnu.idata2306.model.Role;
import no.ntnu.idata2306.model.User;
import no.ntnu.idata2306.repository.UserRepository;
import no.ntnu.idata2306.security.AccessUserDetails;
import no.ntnu.idata2306.security.AuthorityLevel;
import no.ntnu.idata2306.util.repository.RepositoryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final ProviderService providersService;

    /**
     * Constructs a new instance of UserService.
     * The PasswordEncoder is marked as @Lazy to avoid circular dependencies.
     *
     * @param userRepository the repository for managing user data
     * @param passwordEncoder the password encoder for hashing passwords
     * @param roleService the service for executing role logic
     * @param providersService the service for executing  provider logic
     */
    @Autowired
    public UserService(UserRepository userRepository, @Lazy PasswordEncoder passwordEncoder, RoleService roleService, ProviderService providersService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
        this.providersService = providersService;
    }

    /**
     * Retrieves all users from the repository and converts them to UserResponseDto objects.
     *
     * @return a list of UserResponseDto objects representing all users.
     */
    public List<UserResponseDto> getAll() {
        return this.userRepository.findAll().stream()
                .map(UserMapper.INSTANCE::userToUserResponseDto)
                .toList();
    }

    /**
     * Retrieves all users from the repository that are not deleted and converts them to UserResponseDto objects.
     *
     * @return a list of UserResponseDto objects representing all active users.
     */
    public List<UserResponseDto> getAllActiveUsers() {
        return this.userRepository.findByDeletedFalse().stream()
                .map(UserMapper.INSTANCE::userToUserResponseDto)
                .toList();
    }

    /**
     * Creates a new user with the provided sign-up information.
     *
     * @param userSignUpDto the DTO containing the user's sign-up information
     * @return the newly created UserResponseDto object
     */
    public UserResponseDto createUser(UserSignUpDto userSignUpDto) {
        User user = UserMapper.INSTANCE.userSignupDtoToUserWithPassword(userSignUpDto, this.passwordEncoder);
        setRole(user, AuthorityLevel.USER);
        user.setCreated(LocalDateTime.now());
        this.userRepository.save(user);
        return UserMapper.INSTANCE.userToUserResponseDto(user);
    }

    /**
     * Creates a new provider user with the provided sign-up information.
     *
     * @param userSignUpDto the DTO containing the user's sign-up information
     * @return the newly created UserResponseDto object
     */
    public UserResponseDto createProviderUser(UserSignUpDto userSignUpDto) {
        User user = UserMapper.INSTANCE.userSignupDtoToUserWithPassword(userSignUpDto, this.passwordEncoder);
        setRole(user, AuthorityLevel.PROVIDER);
        user.setCreated(LocalDateTime.now());
        this.userRepository.save(user);
        return UserMapper.INSTANCE.userToUserResponseDto(user);
    }

    /**
     * Updates an existing user with the provided information.
     *
     * @param id the ID of the user to be updated
     * @param userUpdateDto the DTO containing the updated user information
     * @return the updated UserResponseDto object
     */
    public UserResponseDto updateUser(int id, UserUpdateDto userUpdateDto) {
        User user = findUserById(id);
        UserMapper.INSTANCE.updateUserFromDtoWithPassword(userUpdateDto, user, this.passwordEncoder);
        userRepository.save(user);
        log.info("User was updated successfully with ID: {}", id);
        return UserMapper.INSTANCE.userToUserResponseDto(user);
    }

    /**
     * Updates an existing provider user with the provided information.
     *
     * @param userId the ID of the user to be updated
     * @param providerId the ID of the provider to be associated with the user
     * @return the updated UserResponseDto object
     */
    public UserResponseDto updateProviderUser(int userId, int providerId) {
        User user = findUserById(userId);
        Provider provider = this.providersService.findProviderById(providerId);
        user.setProvider(provider);
        this.userRepository.save(user);
        log.info("User was updated successfully with ID: {}", userId);
        return UserMapper.INSTANCE.userToUserResponseDto(user);
    }

    /**
     * Marks a user as deleted by setting the deleted field to true.
     *
     * @param id the ID of the user to be marked as deleted
     * @return UserResponseDto containing the updated user information
     */
    public UserResponseDto softDeleteUser(int id) {
        User user = findUserById(id);
        user.setDeleted(true);
        this.userRepository.save(user);
        log.info("User marked as deleted with ID: {}", id);
        return UserMapper.INSTANCE.userToUserResponseDto(user);
    }

    /**
     * Retrieves a user by their ID and converts it to a UserResponseDto.
     *
     * @param id the ID of the user to retrieve
     * @return the UserResponseDto object if found
     * @throws EntityNotFoundException if the user with the specified ID is not found
     */
    public UserResponseDto getUserById(int id) {
        User user = findUserById(id);
        return UserMapper.INSTANCE.userToUserResponseDto(user);
    }

    /**
     * Finds a user by their ID.
     *
     * @param id the ID of the user to be found
     * @return the User object if found
     * @throws EntityNotFoundException if the user with the specified ID is not found
     */
    public User findUserById(int id) {
        return RepositoryUtils.findEntityById(userRepository::findById, id, User.class);
    }

    /**
     * Assigns a role to a user.
     *
     * @param user the user to whom the role will be assigned
     * @param roleName the name of the role to be assigned
     * @throws EntityNotFoundException if the specified role is not found
     */
    public void setRole(User user, String roleName){
        Role role = this.roleService.findRoleByRoleName(roleName);
        user.getRoles().add(role);
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
            return new EntityNotFoundException("Invalid email or password");
        });
        
        return new AccessUserDetails(user);

    }

}
