package com.mobilise.bookhub.services.serviceImpl;

import com.mobilise.bookhub.dto.request.LoginRequestDto;
import com.mobilise.bookhub.dto.request.RegistrationRequestDto;
import com.mobilise.bookhub.dto.response.ApiResponse;
import com.mobilise.bookhub.dto.response.LoginResponseDto;
import com.mobilise.bookhub.dto.response.RegistrationResponseDto;
import com.mobilise.bookhub.entity.User;
import com.mobilise.bookhub.enums.Role;
import com.mobilise.bookhub.exception.InvalidCredentialException;
import com.mobilise.bookhub.exception.UserAlreadyExistException;
import com.mobilise.bookhub.repository.UserRepository;
import com.mobilise.bookhub.security.JwtService;
import com.mobilise.bookhub.security.implementation.UserDetailsImpl;
import com.mobilise.bookhub.services.UserService;
import com.mobilise.bookhub.utils.DtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import static com.mobilise.bookhub.constants.Constants.NETWORK_AUTHENTICATION_REQUIRED;
import static java.net.HttpURLConnection.*;

/**
 * UserServiceImpl class implements the UserService interface.
 * It provides methods for user registration, login, and logout.
 *
 * @author codecharlan
 * @version 1. 0. 0
 */
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final DtoMapper dtoMapper;

    /**
     * Registers a new user.
     *
     * @param registrationRequest the request containing the user's registration details
     * @return an ApiResponse object containing the registration response and HTTP status code
     * @throws UserAlreadyExistException if the user already exists
     */
    @Override
    public ApiResponse<RegistrationResponseDto> registerUser(RegistrationRequestDto registrationRequest) {
        Optional<User> optionalUser = userRepository.findByEmail(registrationRequest.email());
        if (optionalUser.isPresent()) {
            throw new UserAlreadyExistException("User Already Exists");
        }
        User newUser = dtoMapper.createNewUser(registrationRequest);
        User savedUser = userRepository.save(newUser);
        RegistrationResponseDto registrationResponse = dtoMapper.createUserResponse(savedUser);
        return new ApiResponse<>("User created successfully", registrationResponse, HTTP_CREATED);
    }

    /**
     * Logs in a user.
     *
     * @param loginRequest the request containing the user's login credentials
     * @return an ApiResponse object containing the login response and HTTP status code
     * @throws InvalidCredentialException if the provided credentials are invalid
     * @throws DisabledException          if the user is disabled
     */
    @Override
    public ApiResponse<LoginResponseDto> login(LoginRequestDto loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.email(),
                            loginRequest.password())
            );

            if (authentication.isAuthenticated()) {
                SecurityContextHolder.getContext().setAuthentication(authentication);
                UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

                Role userRole = extractUserRole(userDetails);

                String token = jwtService.generateToken(authentication, userRole);
                LoginResponseDto loginResponse = dtoMapper.createLoginResponse(userDetails, token);

                return new ApiResponse<>("Successfully logged in", loginResponse, HTTP_OK);
            } else
                return new ApiResponse<>("Logged in failed", null, NETWORK_AUTHENTICATION_REQUIRED);

        } catch (DisabledException e) {
            throw new RuntimeException("User is disabled");
        } catch (BadCredentialsException e) {
            throw new InvalidCredentialException("Invalid credentials: Enter email and password again");
        }
    }

    /**
     * Extracts the user's role from the provided UserDetails object.
     *
     * @param userDetails the UserDetails object containing the user's details
     * @return the user's role as a Role enum
     */
    private Role extractUserRole(UserDetails userDetails) {
        if (userDetails == null) {
            throw new IllegalArgumentException("Invalid entry: retry again");
        }

        if (userDetails instanceof UserDetailsImpl userDetailsImpl) {
            return Role.valueOf(
                    userDetailsImpl.getAuthorities().stream().findFirst().map(GrantedAuthority::getAuthority).orElse(null)
            );
        } else {
            Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
            if (authorities.isEmpty()) {
                throw new IllegalStateException("User has no authorities");
            }
            GrantedAuthority authority = authorities.iterator().next();
            String roleString = authority.getAuthority();

            try {
                return Role.valueOf(roleString);
            } catch (IllegalArgumentException e) {
                throw new IllegalStateException("Invalid user role: " + roleString);
            }
        }
    }
    /**
     * Logs out the user.
     *
     * @param authorizationHeader the authorization header containing the user's token
     * @return an ApiResponse object containing the logout response and HTTP status code
     */
    @Override
    public ApiResponse<String> logout(String authorizationHeader) {
        try {
            String token = authorizationHeader.substring(7);
            Map<String, String> userDetails = jwtService.parseTokenClaims(token);
            if (userDetails != null) {
                SecurityContextHolder.clearContext();
                return new ApiResponse<>("Successfully logged out", "You have been logged out", HTTP_OK);
            }
            return new ApiResponse<>("Already out of session", "User not in session", HTTP_NOT_FOUND);
        } catch (Exception e) {
            return new ApiResponse<>("Logout failure", "An error occurred while logging out", HTTP_INTERNAL_ERROR);
        }
    }
}
