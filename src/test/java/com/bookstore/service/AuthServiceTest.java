package com.bookstore.service;

import com.bookstore.dto.request.LoginRequest;
import com.bookstore.dto.request.RegisterRequest;
import com.bookstore.dto.response.JwtResponse;
import com.bookstore.exception.ResourceAlreadyExistsException;
import com.bookstore.model.Client;
import com.bookstore.model.RefreshToken;
import com.bookstore.model.User;
import com.bookstore.model.enums.UserRole;
import com.bookstore.repository.ClientRepository;
import com.bookstore.repository.UserRepository;
import com.bookstore.security.jwt.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider tokenProvider;

    @Mock
    private RefreshTokenService refreshTokenService;

    @InjectMocks
    private AuthService authService;

    private User user;
    private Client client;
    private RefreshToken refreshToken;
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setUserType(UserRole.CLIENT);

        client = new Client();
        client.setId(1L);
        client.setEmail("test@example.com");
        client.setPassword("password");
        client.setUserType(UserRole.CLIENT);

        refreshToken = new RefreshToken();
        refreshToken.setToken("refresh-token");
        refreshToken.setUser(user);

        authentication = mock(Authentication.class);
    }

    @Test
    void login_Success() {
        LoginRequest loginRequest = new LoginRequest("test@example.com", "password");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        when(tokenProvider.generateToken(authentication)).thenReturn("jwt-token");
        when(refreshTokenService.createRefreshToken(user)).thenReturn(refreshToken);

        JwtResponse response = authService.login(loginRequest);

        assertNotNull(response);
        assertEquals("jwt-token", response.getAccessToken());
        assertEquals("refresh-token", response.getRefreshToken());
        assertEquals("test@example.com", response.getEmail());

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(tokenProvider).generateToken(authentication);
        verify(refreshTokenService).createRefreshToken(user);
    }

    @Test
    void register_Success() {
        RegisterRequest request = new RegisterRequest();
        request.setName("Test User");
        request.setEmail("new@example.com");
        request.setPassword("password");
        request.setPhone("1234567890");
        request.setAddress("123 Main St");

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encoded-password");
        when(clientRepository.save(any(Client.class))).thenReturn(client);

        // Mock authentication inside register
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(tokenProvider.generateToken(authentication)).thenReturn("jwt-token");
        when(refreshTokenService.createRefreshToken(any(Client.class))).thenReturn(refreshToken);

        JwtResponse response = authService.register(request);

        assertNotNull(response);
        assertEquals("jwt-token", response.getAccessToken());
        assertEquals("refresh-token", response.getRefreshToken());

        verify(userRepository).existsByEmail(request.getEmail());
        verify(clientRepository).save(any(Client.class));
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void register_DuplicateEmail() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("test@example.com");

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> authService.register(request));

        verify(userRepository).existsByEmail(request.getEmail());
        verify(clientRepository, never()).save(any(Client.class));
    }

    @Test
    void refreshToken_Success() {
        String tokenStr = "refresh-token";

        // Fix for findByToken matching Optional<RefreshToken>
        when(refreshTokenService.findByToken(tokenStr)).thenReturn(Optional.of(refreshToken));

        // verifyExpiration should return the RefreshToken if valid
        when(refreshTokenService.verifyExpiration(refreshToken)).thenReturn(refreshToken);

        when(tokenProvider.generateTokenFromEmail(user.getEmail())).thenReturn("new-jwt-token");

        JwtResponse response = authService.refreshToken(tokenStr);

        assertNotNull(response);
        assertEquals("new-jwt-token", response.getAccessToken());
        assertEquals(tokenStr, response.getRefreshToken());

        verify(refreshTokenService).findByToken(tokenStr);
        verify(refreshTokenService).verifyExpiration(refreshToken);
        verify(tokenProvider).generateTokenFromEmail(user.getEmail());
    }

    @Test
    void refreshToken_NotFound() {
        String tokenStr = "invalid-token";
        when(refreshTokenService.findByToken(tokenStr)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> authService.refreshToken(tokenStr));

        verify(refreshTokenService).findByToken(tokenStr);
    }

    @Test
    void logout_Success() {
        String tokenStr = "refresh-token";

        doNothing().when(refreshTokenService).deleteByToken(tokenStr);

        authService.logout(tokenStr);

        verify(refreshTokenService).deleteByToken(tokenStr);
    }
}
