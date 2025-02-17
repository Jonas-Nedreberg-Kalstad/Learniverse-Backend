package no.ntnu.idata2306.security;

import no.ntnu.idata2306.model.Role;
import no.ntnu.idata2306.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

public class AccessUserDetails implements UserDetails {

    private final String email;
    private final String password;
    private final boolean deleted;
    private final List<GrantedAuthority> authorities = new ArrayList<>();

    /**
     * Creates a new instance of AccessUserDetails.
     *
     * @param user user
     */
    public AccessUserDetails(User user) {
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.deleted = user.isDeleted();
        this.convertRoles(user.getRoles());
    }

    private void convertRoles(Set<Role> roles) {
        authorities.clear();
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getRole()));
        }
    }

    /**
     * Returns authorities of user.
     *
     * @return authorities
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    /**
     * Returns password.
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * Returns the username used for login in, which is user email.
     *
     * @return email
     */
    @Override
    public String getUsername() {
        return email;
    }

    /**
     * Checks if account is expired.
     *
     * @return true if the account is not expired
     */
    @Override
    public boolean isAccountNonExpired() {
        return !deleted;
    }

    /**
     * Checks if account is locked.
     *
     * @return true if the account is not locked
     */
    @Override
    public boolean isAccountNonLocked() {
        return !deleted;
    }

    /**
     * Checks if credentials are expired.
     *
     * @return true if the credentials are not expired
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return !deleted;
    }

    /**
     * Checks if account is enabled.
     *
     * @return true if the account is enabled
     */
    @Override
    public boolean isEnabled() {
        return !deleted;
    }
}