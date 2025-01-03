package com.example.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(HttpMethod.POST, "/auth/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/users/register").permitAll()
                        .requestMatchers(HttpMethod.GET, "/users/get-permission").authenticated()
                        .requestMatchers(HttpMethod.POST, "/comments/sport").authenticated()
                        .requestMatchers(HttpMethod.POST, "/comments/music").authenticated()
                        .requestMatchers(HttpMethod.POST, "/comments/culture").authenticated()
                        .requestMatchers(HttpMethod.POST, "/comments/science").authenticated()
                        .requestMatchers(HttpMethod.POST, "/comments/insert").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/comments/delete/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/comments/update/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/users/get-unregistered-users").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/users/update/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/comments/unapproved-messages").hasRole("MODERATOR")
                        .requestMatchers(HttpMethod.GET, "/comments/approve/{id}").hasRole("MODERATOR")
                        .requestMatchers(HttpMethod.GET, "/comments/unapproved-messages").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/users/get-current-user").authenticated()
                        .anyRequest().authenticated()
                )
                .sessionManagement(manager -> manager.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
