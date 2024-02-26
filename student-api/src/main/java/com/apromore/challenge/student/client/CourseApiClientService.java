package com.apromore.challenge.student.client;

import com.apromore.challenge.student.model.Course;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class CourseApiClientService {
    @Value("${course.api.url}")
    private String apiBaseUrl;

    @Value("${course.api.key}")
    private String apiKey;

    private final Logger logger = LoggerFactory.getLogger(CourseApiClientService.class);

    private WebClient webClient;

    public CourseApiClientService() {
        this.webClient = WebClient.create();
    }

    public void setWebClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Course getCourseFromApiByCode(String code) {
        Mono<Course> response = webClient.get().uri(apiBaseUrl + "/" + code)
                .header("x-api-key", apiKey)
                .retrieve()
                .onStatus(HttpStatusCode::is5xxServerError, resp -> Mono.error(new InternalError("Internal server error")))
                .bodyToMono(Course.class)
                .onErrorResume(WebClientResponseException.NotFound.class, ex -> {
                    logger.error(String.format("Course code %s not found", code));
                    return Mono.just(new Course());
                });
        return response.block();
    }

    public boolean checkUrlReachability() {
        webClient.get().uri(apiBaseUrl).header("x-api-key", apiKey)
                .retrieve().toBodilessEntity().block(); // Send a HEAD request
        return true;
    }

}
