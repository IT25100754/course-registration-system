package com.example.courseregistrationsystem.controller;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

/**
 * WebConfig — Spring MVC configuration.
 * - Enables CORS for all origins (dev-friendly)
 * - Serves the frontend (index.html, style.css, app.js) from /static
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Serve frontend files from src/main/resources/static/
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
    }
}
