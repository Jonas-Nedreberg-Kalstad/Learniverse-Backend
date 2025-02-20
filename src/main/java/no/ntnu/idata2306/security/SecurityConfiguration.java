package no.ntnu.idata2306.security;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
@Tag(name = "Security Configuration", description = "Security settings for the application")
public class SecurityConfiguration {


    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtRequestFilter jwtFilter;

    /**
     * Constructs a new SecurityConfiguration with the specified UserDetailsService, PasswordEncoder, and JwtRequestFilter.
     * The PasswordEncoder is marked as @Lazy to avoid circular dependencies.
     *
     * @param userDetailsService the user details service for loading user data
     * @param passwordEncoder the password encoder for encoding passwords
     * @param jwtFilter the JWT request filter for handling JWT authentication
     */
    @Autowired
    public SecurityConfiguration(UserDetailsService userDetailsService, @Lazy PasswordEncoder passwordEncoder, JwtRequestFilter jwtFilter) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.jwtFilter = jwtFilter;
    }

    /**
     * Configures the authentication manager to use the user details service for loading user data from the database
     * and the password encoder for encoding passwords.
     *
     * @param auth the authentication manager builder
     * @throws Exception if an error occurs during configuration
     */
    @Autowired
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(this.userDetailsService).passwordEncoder(this.passwordEncoder);
    }

    /**
     * Sets up the authorization filter chain for the application, defining security rules and filters.
     *
     * @param http the HttpSecurity configuration builder
     * @return the configured SecurityFilterChain
     * @throws Exception if an error occurs during configuration
     */
    @Bean
    @Operation(
            summary = "Configure Authorization Filter Chain",
            description = "Sets up the authorization filter chain for the application, defining security rules and filters."
    )
    public SecurityFilterChain configureAuthorizationFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/admin/**").hasAuthority(AuthorityLevel.ADMIN)
                        .requestMatchers("/api/user/**").hasAnyAuthority(AuthorityLevel.ADMIN, AuthorityLevel.USER, AuthorityLevel.PROVIDER)
                        .requestMatchers("/api/authenticate").permitAll()
                        .requestMatchers("/api/anonymous").permitAll()
                        .requestMatchers("/v3/api-docs/**").hasAuthority(AuthorityLevel.ADMIN)
                        .requestMatchers("/swagger-ui/**").hasAuthority(AuthorityLevel.ADMIN)
                        .requestMatchers("/swagger-ui.html").hasAuthority(AuthorityLevel.ADMIN)
                        .anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(this.jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    /**
     * Creates an authentication manager bean.
     *
     * @param config the authentication configuration
     * @return the authentication manager
     * @throws Exception if an error occurs during configuration
     */
    @Bean
    @Operation(summary = "Authentication manager", description = "Creates an authentication manager bean.")
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Defines the password encoder bean using BCrypt for password encryption.
     *
     * @return the password encoder
     */
    @Operation(summary = "Password encoder", description = "Defines the password encoder bean using BCrypt.")
    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}