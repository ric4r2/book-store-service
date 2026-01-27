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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for authentication operations.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;

    /**
     * Authenticate user and generate JWT tokens.
     */
    @Transactional
    public JwtResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);

        User user = (User) authentication.getPrincipal();
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        log.info("User logged in successfully: {}", user.getEmail());

        return new JwtResponse(
                jwt,
                refreshToken.getToken(),
                user.getEmail(),
                user.getUserType().name());
    }

    /**
     * Register a new client.
     */
    @Transactional
    public JwtResponse register(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new ResourceAlreadyExistsException("User", "email", registerRequest.getEmail());
        }

        Client client = new Client();
        client.setName(registerRequest.getName());
        client.setEmail(registerRequest.getEmail());
        client.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        client.setPhone(registerRequest.getPhone());
        client.setAddress(registerRequest.getAddress());
        client.setUserType(UserRole.CLIENT);
        client.setIsBlocked(false);

        client = clientRepository.save(client);
        log.info("New client registered: {}", client.getEmail());

        // Auto-login after registration
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        registerRequest.getEmail(),
                        registerRequest.getPassword()));

        String jwt = tokenProvider.generateToken(authentication);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(client);

        return new JwtResponse(
                jwt,
                refreshToken.getToken(),
                client.getEmail(),
                client.getUserType().name());
    }

    /**
     * Refresh access token using refresh token.
     */
    @Transactional
    public JwtResponse refreshToken(String refreshTokenStr) {
        return refreshTokenService.findByToken(refreshTokenStr)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = tokenProvider.generateTokenFromEmail(user.getEmail());
                    return new JwtResponse(
                            token,
                            refreshTokenStr,
                            user.getEmail(),
                            user.getUserType().name());
                })
                .orElseThrow(() -> new RuntimeException("Refresh token not found"));
    }

    /**
     * Logout user by deleting refresh token.
     */
    @Transactional
    public void logout(String refreshToken) {
        refreshTokenService.deleteByToken(refreshToken);
        log.info("User logged out successfully");
    }
}
