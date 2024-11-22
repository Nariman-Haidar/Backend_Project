package server.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.security.authorization.AuthorizationDecision;

import java.util.Arrays;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    private final String[] publicRoutes = {"/api/users/**",
            "/api/users", "/api/users/register",//"/api/queue-items/***",
            "/api/users/login", "/api/users/users", "/api/course/courses","/api/access/courses/**",
            "/api/queue-items/delete/***",  "/api/queue-items/join",
            "/api/queue-items/update","/favicon.ico"};

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http,
                                                         JwtAuthenticationConverter jwtAuthConverter,
                                                         AuthenticationManager authManager,
                                                         CourseAccessManager courseAccessManager) {
        AuthenticationWebFilter authenticationWebFilter = new AuthenticationWebFilter(authManager);
        authenticationWebFilter.setServerAuthenticationConverter(jwtAuthConverter);

        return http
                .httpBasic(Customizer.withDefaults())
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .cors(Customizer.withDefaults())
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/api/users/login","/api/users/users","/api/course/courses","/api/users/register").permitAll()
                         .pathMatchers(publicRoutes).hasAnyRole("USER","ADMIN") 
                        .pathMatchers("/api/queue-items/{courseId}**").access(
                                (authentication, authorizationContext) ->
                                        courseAccessManager.check(authentication, authorizationContext) 
                        )
                        .anyExchange().authenticated()
                )
                .addFilterAt(authenticationWebFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }





   
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.addAllowedHeader("Authorization");
        configuration.addAllowedHeader("Content-Type");

        configuration.setExposedHeaders(Arrays.asList(HttpHeaders.AUTHORIZATION)); 
        configuration.setAllowCredentials(true); 
        configuration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}