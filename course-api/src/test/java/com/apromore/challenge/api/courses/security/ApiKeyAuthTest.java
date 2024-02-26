package com.apromore.challenge.api.courses.security;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 * Test class for the API key authentication. Since the CourseController class follows the same URL pattern "/api/course"
 * it is expected that all security config applies to all endpoints of the said class.
 */
@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ApiKeyAuthTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testApiCourseUnauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/course"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    public void testApiCourseAuthorized() throws Exception {
        // Mocking API key
        String apiKey = "268d7f300e113cf987e1a1bdb151ae8e";
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-api-key", apiKey);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/course").headers(headers))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testPublicUrlAuthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/public/swagger-ui/index.html"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
