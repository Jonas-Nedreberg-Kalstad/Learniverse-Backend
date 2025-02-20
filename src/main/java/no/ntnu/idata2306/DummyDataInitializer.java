package no.ntnu.idata2306;

import lombok.extern.slf4j.Slf4j;
import no.ntnu.idata2306.model.Role;
import no.ntnu.idata2306.model.User;
import no.ntnu.idata2306.repository.RoleRepository;
import no.ntnu.idata2306.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Responsible for populating database with dummy data for testing.
 */
@Slf4j
@Component
public class DummyDataInitializer implements ApplicationListener<ApplicationEvent> {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DummyDataInitializer(UserRepository userRepository, RoleRepository roleRepository, @Lazy PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        log.info("Importing test data...");

        if (userRepository.count() == 0){
            // Create roles
            Role adminRole = new Role();
            adminRole.setRole("ADMIN");

            Role userRole = new Role();
            userRole.setRole("USER");

            roleRepository.save(adminRole);
            roleRepository.save(userRole);

            // Create users
            User adminUser = new User();
            adminUser.setFirstName("Admin");
            adminUser.setLastName("User");
            adminUser.setEmail("admin@example.com");
            adminUser.setPassword(this.passwordEncoder.encode("adminpass"));
            adminUser.setCreated(LocalDateTime.now());
            adminUser.setDeleted(false);
            adminUser.setRoles(new LinkedHashSet<>(Set.of(adminRole)));

            User regularUser = new User();
            regularUser.setFirstName("Regular");
            regularUser.setLastName("User");
            regularUser.setEmail("user@example.com");
            regularUser.setPassword(this.passwordEncoder.encode("userpass"));
            regularUser.setCreated(LocalDateTime.now());
            regularUser.setDeleted(false);
            regularUser.setRoles(new LinkedHashSet<>(Set.of(userRole)));

            // Save users
            userRepository.save(adminUser);
            userRepository.save(regularUser);
        }

        log.info("Test data imported successfully.");
    }
}