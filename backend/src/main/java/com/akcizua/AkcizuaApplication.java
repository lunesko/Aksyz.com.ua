package com.akcizua;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@EnableScheduling
@OpenAPIDefinition(
    info = @Info(
        title = "АкцизUA API",
        version = "1.0",
        description = "API для агрегатора скидок на алкоголь и табачную продукцию в Украине"
    )
)
public class AkcizuaApplication {

    public static void main(String[] args) {
        SpringApplication.run(AkcizuaApplication.class, args);
    }
}
