package com.healthcare.clinic.security;

import com.healthcare.clinic.identity.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilsTest {

    private JwtUtils jwtUtils;

    @BeforeEach
    void setUp() {
        jwtUtils = new JwtUtils();
        // Set a 256-bit secret string for testing
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", "1234567890123456789012345678901234567890");
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", 900000);
    }

    @Test
    void testGenerateAndValidateJwtToken() {
        User user = User.builder()
                .id(100L)
                .email("testuser@clinic.com")
                .passwordHash("hashedpassword")
                .firstName("Test")
                .lastName("User")
                .enabled(true)
                .roles(Collections.emptySet())
                .build();

        Authentication auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        String token = jwtUtils.generateJwtToken(auth);

        assertNotNull(token);
        assertTrue(jwtUtils.validateJwtToken(token));
        assertEquals("testuser@clinic.com", jwtUtils.getUserNameFromJwtToken(token));
    }

    @Test
    void testInvalidJwtToken() {
        assertFalse(jwtUtils.validateJwtToken("invalid.token.structure"));
    }
}
