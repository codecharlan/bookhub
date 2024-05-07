package com.mobilise.bookhub.controller;


import com.mobilise.bookhub.dto.request.LoginRequestDto;
import com.mobilise.bookhub.dto.request.RegistrationRequestDto;
import com.mobilise.bookhub.dto.response.ApiResponse;
import com.mobilise.bookhub.dto.response.LoginResponseDto;
import com.mobilise.bookhub.dto.response.RegistrationResponseDto;
import com.mobilise.bookhub.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.mobilise.bookhub.constants.Constants.*;
/**
 * UserController class handles user-related API endpoints.
 *
 * @author charlancodes
 */
@RequiredArgsConstructor
@RestController
@RequestMapping(BASE_USER_API_URL)
public class UserController {
    /**
     * UserService dependency for handling user-related operations.
     */
    private final UserService userService;
    /**
     * Creates a new user.
     *
     * @param newUser the user details to be registered
     * @return a response containing the registered user's details
     */
    @PostMapping(REGISTER_URL)
    public ResponseEntity<ApiResponse<RegistrationResponseDto>> createUser(@Valid @RequestBody RegistrationRequestDto newUser) {
        ApiResponse<RegistrationResponseDto> response = userService.registerUser(newUser);
        HttpStatus httpStatus = HttpStatus.valueOf(response.status());
        return new ResponseEntity<>(response, httpStatus);
    }
    /**
     * Logs in a user.
     *
     * @param loginRequest the user's login credentials
     * @return a response containing the logged-in user's details
     */
    @PostMapping(LOGIN_URL)
    public ResponseEntity<ApiResponse<LoginResponseDto>> login(@RequestBody LoginRequestDto loginRequest) {
        ApiResponse<LoginResponseDto> response = userService.login(loginRequest);
        HttpStatus httpStatus = HttpStatus.valueOf(response.status());
        return new ResponseEntity<>(response, httpStatus);
    }
    /**
     * Logs out a user.
     *
     * @param authorizationHeader the user's authorization header
     * @return a response containing a success message upon successful logout
     */
    @PostMapping(LOGOUT_URL)
    public ResponseEntity<ApiResponse<String>> logout(@RequestHeader(name = AUTHORIZATION_HEADER) String authorizationHeader) {
        ApiResponse<String> response = userService.logout(authorizationHeader);
        HttpStatus httpStatus = HttpStatus.valueOf(response.status());
        return new ResponseEntity<>(response, httpStatus);
    }
}
