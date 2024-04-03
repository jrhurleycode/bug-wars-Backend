package com.bugwarsBackend.bugwars.controller;

import static org.junit.jupiter.api.Assertions.*;
import com.bugwarsBackend.bugwars.dto.request.LoginRequest;
import com.bugwarsBackend.bugwars.dto.request.SignupRequest;
import com.bugwarsBackend.bugwars.dto.request.TokenRefreshRequest;
import com.bugwarsBackend.bugwars.dto.response.JwtResponse;
import com.bugwarsBackend.bugwars.dto.response.TokenRefreshResponse;
import com.bugwarsBackend.bugwars.model.User;
import com.bugwarsBackend.bugwars.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.security.Principal;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;
    @Mock
    private JwtResponse jwtResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testRegisterUser() {
        SignupRequest signUpRequest = new SignupRequest();
        User user = new User();

        when(authService.registerUser(signUpRequest)).thenReturn(user);

        User result = authController.registerUser(signUpRequest);

        assertEquals(user, result);
    }

    @Test
    void testAuthenticateUser() {
        LoginRequest loginRequest = new LoginRequest();

        JwtResponse jwtResponse = new JwtResponse();
        jwtResponse.setAccessToken("token");

        when(authService.authenticateUser(loginRequest)).thenReturn(jwtResponse);

        JwtResponse result = authController.authenticateUser(loginRequest);

        assertEquals(jwtResponse, result);
    }

    @Test
    void testRefreshToken() {
        TokenRefreshRequest tokenRefreshRequest = new TokenRefreshRequest();
        TokenRefreshResponse tokenRefreshResponse = new TokenRefreshResponse();
        tokenRefreshResponse.setRefreshToken("refreshedToken");

        when(authService.refreshToken(tokenRefreshRequest)).thenReturn(tokenRefreshResponse);

        TokenRefreshResponse result = authController.refreshToken(tokenRefreshRequest);

        assertEquals(tokenRefreshResponse, result);
    }

    @Test
    void testLogout() {
        Principal principal = () -> "username";

        authController.logout(principal);

        verify(authService, times(1)).logout(principal);
    }
}
