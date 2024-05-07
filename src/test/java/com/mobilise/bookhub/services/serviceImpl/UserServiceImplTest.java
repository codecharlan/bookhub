package com.mobilise.bookhub.services.serviceImpl;

import com.mobilise.bookhub.dto.request.LoginRequestDto;
import com.mobilise.bookhub.dto.request.RegistrationRequestDto;
import com.mobilise.bookhub.dto.response.ApiResponse;
import com.mobilise.bookhub.dto.response.LoginResponseDto;
import com.mobilise.bookhub.dto.response.RegistrationResponseDto;
import com.mobilise.bookhub.entity.User;
import com.mobilise.bookhub.enums.Gender;
import com.mobilise.bookhub.enums.Role;
import com.mobilise.bookhub.exception.InvalidCredentialException;
import com.mobilise.bookhub.exception.UserAlreadyExistException;
import com.mobilise.bookhub.repository.UserRepository;
import com.mobilise.bookhub.security.JwtService;
import com.mobilise.bookhub.security.implementation.UserDetailsImpl;
import com.mobilise.bookhub.utils.DtoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtService jwtService;
    @Mock
    private DtoMapper dtoMapper;
    @InjectMocks
    private UserServiceImpl userService;
    private RegistrationRequestDto registrationRequestDto;
    private RegistrationResponseDto registrationResponseDto;
    private LoginRequestDto loginRequestDto;
    private LoginResponseDto loginResponseDto;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = User.builder().id(1L).fullName("John Doe").email("john.doe@example.com").password("securePassword").role(Role.USER).gender(Gender.MALE).balance(BigDecimal.ZERO).build();
        registrationRequestDto = RegistrationRequestDto.builder().fullName("John Doe").email("john.doe@example.com").password("securePassword").gender(Gender.MALE).role(Role.USER).build();
        registrationResponseDto = RegistrationResponseDto.builder().fullName("John Doe").email("john.doe@example.com").gender(Gender.MALE).role(Role.USER).build();
        loginRequestDto = LoginRequestDto.builder().email("john.doe@example.com").password("password").build();
        loginResponseDto = LoginResponseDto.builder().id(1L).fullName("John Doe").email("john.doe@example.com").jwtToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9").build();

    }

    @Test
    void testRegisterUser_Success() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(dtoMapper.createNewUser(any(RegistrationRequestDto.class))).thenReturn(new User());
        when(userRepository.save(any(User.class))).thenReturn(new User());
        when(dtoMapper.createUserResponse(any(User.class))).thenReturn(registrationResponseDto);

        ApiResponse<RegistrationResponseDto> response = userService.registerUser(registrationRequestDto);

        // Assertions
        assertNotNull(response);
        assertEquals("User created successfully", response.message());
        assertEquals(201, response.status());
    }

    @Test
    void testLogin_Success() {
        UserDetails userDetails = new UserDetailsImpl(user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(jwtService.generateToken(any(Authentication.class), any(Role.class))).thenReturn("token");
        when(dtoMapper.createLoginResponse(any(UserDetailsImpl.class), anyString())).thenReturn(loginResponseDto);

        ApiResponse<LoginResponseDto> response = userService.login(loginRequestDto);

        // Assertions
        assertNotNull(response);
    }

    @Test
    void testRegisterUser_UserAlreadyExists() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(new User()));

        assertThrows(UserAlreadyExistException.class, () -> userService.registerUser(registrationRequestDto));

        verify(userRepository, times(1)).findByEmail(anyString());
        verifyNoMoreInteractions(dtoMapper, userRepository);
    }

    @Test
    void testLogin_AuthenticationFailure() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenThrow(BadCredentialsException.class);

        assertThrows(InvalidCredentialException.class, () -> userService.login(loginRequestDto));

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verifyNoMoreInteractions(jwtService, dtoMapper);
    }

    @Test
    void testLogout_Success() {
        String authorizationHeader = "Bearer token";
        when(jwtService.parseTokenClaims(anyString())).thenReturn(Map.of("key", "value"));

        ApiResponse<String> response = userService.logout(authorizationHeader);

        // Assertions
        assertNotNull(response);
        assertEquals("Successfully logged out", response.message());
        assertEquals(200, response.status());
    }

    @Test
    void testLogout_AlreadyLoggedOut() {
        String authorizationHeader = "Bearer token";
        when(jwtService.parseTokenClaims(anyString())).thenReturn(null);

        ApiResponse<String> response = userService.logout(authorizationHeader);

        // Assertions
        assertNotNull(response);
        assertEquals("Already out of session", response.message());
        assertEquals(404, response.status());
    }
}
