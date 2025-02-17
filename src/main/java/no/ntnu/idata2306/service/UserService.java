package no.ntnu.idata2306.service;

import lombok.extern.slf4j.Slf4j;
import no.ntnu.idata2306.model.User;
import no.ntnu.idata2306.repository.UserRepository;
import no.ntnu.idata2306.security.AccessUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;


    /**
     * creates a new instance of userService.
     *
     * @param userRepository userRepository
     */
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            return new AccessUserDetails(user.get());
        } else {
            log.error("Failed to load user details: User with email {} not found.", email);
            throw new IllegalArgumentException("No user found with the provided email address: " + email);
        }
    }

    /**
     * Generates a secure hash of the given plaintext password using the BCrypt hashing algorithm.
     * A random salt is used to ensure the hash is unique even for identical passwords.
     *
     * @param password the plaintext password to be hashed
     * @return the BCrypt hash of the password, including the salt
     */
    private String createHash(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
}
