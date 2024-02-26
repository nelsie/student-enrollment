package com.apromore.challenge.api.courses;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static io.swagger.v3.oas.annotations.enums.SecuritySchemeIn.HEADER;
import static io.swagger.v3.oas.annotations.enums.SecuritySchemeType.APIKEY;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Course API", version = "1.0", description = "CRUD Operations for Course Entity"))
@SecurityScheme(type= APIKEY, name="x-api-key", in = HEADER)
public class CoursesApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(CoursesApiApplication.class, args);
	}
}
