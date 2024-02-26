package com.apromore.challenge.student.security;

import com.apromore.challenge.student.controller.StudentCoursesController;
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
 * Test class for the API key authentication. Since the {@link StudentCoursesController} class follows the same URL pattern (/api/student/courses)
 * it is expected that all security config applies to all endpoints of the said class.
 */
@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class ApiKeyAuthTest {
    private static final String API_URL = "/api/student/courses";


    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testApiUnauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(API_URL))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    public void testApiAuthorized() throws Exception {
        // Mocking API key
        String apiKey = "146f5e1cb6a309cfbc05b8bca5ad3ef0";
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-api-key", apiKey);

        mockMvc.perform(MockMvcRequestBuilders.get(API_URL + "/1").headers(headers))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testPublicUrlAuthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/public/swagger-ui/index.html"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}