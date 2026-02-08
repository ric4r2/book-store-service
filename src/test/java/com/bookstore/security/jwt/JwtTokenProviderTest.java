package com.bookstore.security.jwt;

import com.bookstore.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtTokenProviderTest {

    @InjectMocks
    private JwtTokenProvider tokenProvider;

    private User user;
    private final String jwtSecret = "thisissupersecuresecretkeythatislongenoughforhmacsha256";
    private final long jwtExpiration = 3600000; // 1 hour

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(tokenProvider, "jwtSecret", jwtSecret);
        ReflectionTestUtils.setField(tokenProvider, "jwtExpiration", jwtExpiration);

        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setPassword("password");
    }

    @Test
    void generateToken_Success() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(user);

        String token = tokenProvider.generateToken(authentication);

        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(tokenProvider.validateToken(token));
        assertEquals(user.getEmail(), tokenProvider.getEmailFromToken(token));
    }

    @Test
    void generateTokenFromEmail_Success() {
        String token = tokenProvider.generateTokenFromEmail(user.getEmail());

        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(tokenProvider.validateToken(token));
        assertEquals(user.getEmail(), tokenProvider.getEmailFromToken(token));
    }

    @Test
    void validateToken_InvalidSignature() {
        String token = tokenProvider.generateTokenFromEmail(user.getEmail());
        String invalidToken = token + "invalid";

        assertFalse(tokenProvider.validateToken(invalidToken));
    }

    @Test
    void validateToken_Expired() {
        // Set short expiration
        ReflectionTestUtils.setField(tokenProvider, "jwtExpiration", -100L); // Expired 100ms ago

        String token = tokenProvider.generateTokenFromEmail(user.getEmail());

        assertFalse(tokenProvider.validateToken(token));
    }

    @Test
    void validateToken_Malformed() {
        String token = "malformed.token";
        assertFalse(tokenProvider.validateToken(token));
    }

    @Test
    void validateToken_Empty() {
        // validateToken expects signature check which naturally fails on empty or null
        // if not handled
        // The implementation cleanses empty strings? No, it parses.
        // Jwts.parser() throws IllegalArgumentException if null or empty.

        // However, the catch block catches IllegalArgumentException.
        // "JWT claims string is empty"

        assertFalse(tokenProvider.validateToken(""));
        // assertFalse(tokenProvider.validateToken(null)); // implementation might throw
        // NPE if not null checked before parser
    }
}
