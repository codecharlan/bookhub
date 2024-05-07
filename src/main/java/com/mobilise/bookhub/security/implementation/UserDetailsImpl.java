package com.mobilise.bookhub.security.implementation;

import com.mobilise.bookhub.entity.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
/**
 * UserDetailsImpl class implements UserDetails interface and represents a user in the system.
 * It contains user's id, email, full name, password, and authorities.
 *
 * @author  codecharlan
 * @version 1.0.0
 */
@Getter
public class UserDetailsImpl implements UserDetails {
    private final Long id;
    private final String email;
    private final String fullName;
    private final String password;
    private final List<GrantedAuthority> authorities;
    /**
     * Constructor for UserDetailsImpl.
     *
     * @param user the User entity
     */
    public UserDetailsImpl(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.fullName = user.getFullName();
        this.password = user.getPassword();
        this.authorities = Stream.of(new SimpleGrantedAuthority(user.getRole().name()))
                .collect(Collectors.toList());
    }

    /**
     * Get the list of authorities granted to the user.
     *
     * @return the list of authorities
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    /**
     * Get the user's password.
     *
     * @return the user's password
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * Get the user's email.
     *
     * @return the user's email
     */
    @Override
    public String getUsername() {
        return email;
    }

    /**
     * Check if the user's account is non-expired.
     *
     * @return true if the account is non-expired, false otherwise
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Check if the user's account is non-locked.
     *
     * @return true if the account is non-locked, false otherwise
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Check if the user's credentials are non-expired.
     *
     * @return true if the credentials are non-expired, false otherwise
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Check if the user's account is enabled.
     *
     * @return true if the account is enabled, false otherwise
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
