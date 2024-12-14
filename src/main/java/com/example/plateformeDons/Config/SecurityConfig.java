package com.example.plateformeDons.Config;

import com.example.plateformeDons.Security.JWTAuthenticationFilter;
import com.example.plateformeDons.Security.JWTHelper;
import com.example.plateformeDons.Security.JWTAthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)  // Active l'utilisation des annotations @PreAuthorize
public class SecurityConfig {

    private final JWTAthenticationEntryPoint point;
    private final JWTAuthenticationFilter filter;
    private final JWTHelper jwtHelper;

    // Injecter les dépendances nécessaires
    public SecurityConfig(JWTAthenticationEntryPoint point, JWTHelper jwtHelper) {
        this.point = point;
        this.jwtHelper = jwtHelper;
        this.filter = new JWTAuthenticationFilter(jwtHelper); // Créer le filtre avec le JWTHelper
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .cors(cors -> cors.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/register", "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/annonces/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/annonces").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/users/**").permitAll()
                        .requestMatchers("/api/users/**").permitAll()

                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/notifications/**").permitAll()
                        .requestMatchers("/api/annonces/**").permitAll()
                        .requestMatchers("/api/recherches/**").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers(
                                "/api/v1/auth/**",
                                "/v3/api-docs/**",
                                "/v3/api-docs.yaml",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()
                        .anyRequest().permitAll())
                .exceptionHandling(ex -> ex.authenticationEntryPoint(point))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
