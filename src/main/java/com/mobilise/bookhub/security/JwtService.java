package com.mobilise.bookhub.security;

import com.mobilise.bookhub.enums.Role;
import com.mobilise.bookhub.exception.JwtParsingException;
import org.springframework.security.core.Authentication;

import java.util.Map;

/**
 * JwtService interface provides methods for generating and parsing JWT tokens.
 *
 * @author codecharlan
 */
public interface JwtService {

    /**
     * Generates a JWT token for the given Authentication and Role.
     *
     * @param authentication the Authentication object containing user credentials
     * @param role             the Role of the user
     * @return a string representing the generated JWT token
     */
    String generateToken(Authentication authentication, Role role);

    /**
     * Parses the claims from the given JWT token.
     *
     * @param token the JWT token to parse
     * @return a map containing the parsed claims
     * @throws JwtParsingException if the token cannot be parsed
     */
    Map<String, String> parseTokenClaims(String token) throws JwtParsingException;
}
