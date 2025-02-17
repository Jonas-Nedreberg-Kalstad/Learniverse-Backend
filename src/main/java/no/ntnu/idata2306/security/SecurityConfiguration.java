package no.ntnu.idata2306.security;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

    /**
     * Configures the authentication manager to use the user details service for loading user data from the database.
     *
     * @param auth the authentication manager builder
     * @throws Exception if an error occurs during configuration
     */
    @Autowired
    protected void configure(AuthenticationManagerBuilder auth, UserDetailsService userDetailsService) throws Exception {
        auth.userDetailsService(userDetailsService);
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
    public SecurityFilterChain configureAuthorizationFilterChain(HttpSecurity http, JwtRequestFilter jwtFilter) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/admin/**").hasAuthority("ADMIN")
                        .requestMatchers("/api/user/**").hasAnyAuthority("USER")
                        .requestMatchers("/v3/api-docs/**").hasAuthority("ADMIN")
                        .requestMatchers("/swagger-ui/**").hasAuthority("ADMIN")
                        .requestMatchers("/swagger-ui.html").hasAuthority("ADMIN")
                        .anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
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