package com.apromore.challenge.student.security;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.Authentication;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ApiKeyAuthExtractorTest {

    private final ApiKeyAuthExtractor apiKeyAuthExtractor = new ApiKeyAuthExtractor();

    @Test
    void testExtract_ValidApiKey() {
        String validApiKey = "validApiKey";
        apiKeyAuthExtractor.setApiKey(validApiKey);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("x-api-key", validApiKey);

        Optional<Authentication> authenticationOptional = apiKeyAuthExtractor.extract(request);

        assertTrue(authenticationOptional.isPresent());
        Authentication authentication = authenticationOptional.get();
        assertEquals(validApiKey, authentication.getPrincipal());
        assertTrue(authentication.getAuthorities().isEmpty());
    }

    @Test
    void testExtract_InvalidApiKey() {
        String invalidApiKey = "invalidApiKey";
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("x-api-key", invalidApiKey);
        Optional<Authentication> authenticationOptional = apiKeyAuthExtractor.extract(request);
        assertTrue(authenticationOptional.isEmpty());
    }
}