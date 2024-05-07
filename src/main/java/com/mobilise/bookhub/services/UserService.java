package com.mobilise.bookhub.services;

import com.mobilise.bookhub.dto.request.LoginRequestDto;
import com.mobilise.bookhub.dto.request.RegistrationRequestDto;
import com.mobilise.bookhub.dto.response.ApiResponse;
import com.mobilise.bookhub.dto.response.LoginResponseDto;
import com.mobilise.bookhub.dto.response.RegistrationResponseDto;

public interface UserService {
    ApiResponse<RegistrationResponseDto> registerUser(RegistrationRequestDto registrationRequest);
    ApiResponse<LoginResponseDto> login(LoginRequestDto loginRequest);
    ApiResponse<String> logout(String authorizationHeader);
}
