package com.apromore.challenge.api.courses.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ApiKeyAuthFilterTest {

    @Autowired
    private ApiKeyAuthFilter apiKeyAuthFilter;

    @Value("${application.security.api-key}")
    private String apiKey;

    @Test
    void testDoFilterInternal_WithValidApiKey() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("x-api-key", apiKey); // Using "x-api-key" as per the provided extractor implementation
        MockHttpServletResponse response = new MockHttpServletResponse();

        FilterChain filterChain = (req, res) -> {
            // No operation, since we are testing filter behavior
        };
        apiKeyAuthFilter.doFilterInternal(request, response, filterChain);
        // Verify authentication is set in SecurityContextHolder
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assert authentication != null; // Assert that authentication is not null
        assertEquals(apiKey, authentication.getPrincipal()); // Assert that the API key matches the principal
    }

    @Test
    void testDoFilterInternal_WithInvalidApiKey() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("x-api-key", "invalid_api_key"); // Using an invalid API key
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = (req, res) -> {
            // No operation, since we are testing filter behavior
        };
        apiKeyAuthFilter.doFilterInternal(request, response, filterChain);
        // Verify authentication is not set in SecurityContextHolder
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assert authentication == null; // Assert that authentication is null
    }
}
