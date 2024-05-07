package com.mobilise.bookhub.security.implementation;

import com.mobilise.bookhub.exception.UserNotFoundException;
import com.mobilise.bookhub.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

/**
 * UserDetailsServiceImpl class is responsible for loading user details from the database.
 * It implements the UserDetailsService interface provided by Spring Security.
 *
 * @author codecharlan
 * @version 1.0.0
 */
@RequiredArgsConstructor
@Component
@ToString
public class UserDetailsServiceImpl implements UserDetailsService {

    /**
     * The UserRepository dependency is used to interact with the database and fetch user details.
     */
    private final UserRepository userRepository;

    /**
     * The loadUserByUsername method is called by Spring Security to load user details.
     * It takes a username as input and returns a UserDetails object.
     * If the user is not found, it throws a UserNotFoundException.
     *
     * @param username The username of the user to be loaded.
     * @return A UserDetails object representing the user details.
     * @throws UserNotFoundException If the user is not found in the database.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UserNotFoundException {
        return userRepository.findByEmail(username)
                .map(UserDetailsImpl::new)
                .orElseThrow(() -> new UserNotFoundException("No active user found with email: " + username));
    }
}
