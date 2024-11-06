package com.example.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOrigins("https://localhost:4200")
//                .allowedMethods("GET", "POST", "PUT", "DELETE");

//        registry.addMapping("/auth/login")
//                .allowedOrigins("*")
//                .allowedMethods("POST");

        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST") // Allowed HTTP methods
                .allowedHeaders("*"); // Allowed request headers
    }
}
