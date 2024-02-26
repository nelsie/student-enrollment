package com.apromore.challenge.student.client;

import com.apromore.challenge.student.model.Course;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

class CourseApiClientServiceTest {

    private CourseApiClientService courseApiClientService;
    private MockWebServer mockWebServer;

    @BeforeEach
    void setUp() throws Exception {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        WebClient webClient = WebClient.builder()
                .baseUrl(mockWebServer.url("/api/course/").toString())
                .build();
        courseApiClientService = new CourseApiClientService();
        courseApiClientService.setWebClient(webClient);
    }

    @AfterEach
    void tearDown() throws Exception {
        mockWebServer.shutdown();
    }

    @Test
    void getCourseFromApiByCode_ReturnsCourse() {
        // Set up mock response
        String responseBody = "{\"code\": \"CS01\", \"name\": \"Course 1\"}";
        mockWebServer.enqueue(new MockResponse().setBody(responseBody)
                .addHeader("Content-type", APPLICATION_JSON_VALUE));
        // Test
        Course course = courseApiClientService.getCourseFromApiByCode("CS01");
        assertEquals("CS01", course.getCode());
        assertEquals("Course 1", course.getName());
    }

    @Test
    void getCourseFromApiByCode_ReturnsDefaultCourse_OnNotFound() {
        // Set up mock 404 response
        mockWebServer.enqueue(new MockResponse().setResponseCode(404));
        // Test
        Course course = courseApiClientService.getCourseFromApiByCode("nonexistent_code");
        assertNull(course.getCode());
    }
}
