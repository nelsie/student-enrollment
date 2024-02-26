package com.apromore.challenge.student.health;

import com.apromore.challenge.student.client.CourseApiClientService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component("CourseApi")
@RequiredArgsConstructor
public class CourseApiHealthIndicator implements HealthIndicator {
    private final CourseApiClientService courseApiClientService;
    private final Logger logger = LoggerFactory.getLogger(CourseApiHealthIndicator.class);

    @Override
    public Health health() {
        try {
            boolean isReachable = courseApiClientService.checkUrlReachability();
            if (isReachable) {
                return Health.up().withDetail("message", "CourseApi is reachable").build();
            } else {
                logger.error("Course API is unreachable.");
                return Health.down().withDetail("message", "CourseApi is unreachable").build();
            }
        } catch (Exception e) {
            logger.error("Course API is unreachable. Exception occurred {}", e.getMessage());
            return Health.down().withDetail("message", "CourseApi is unreachable").build();
        }
    }
}
