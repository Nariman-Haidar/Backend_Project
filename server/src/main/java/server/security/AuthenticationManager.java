package server.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import server.model.User;

@Service
public class AuthenticationManager implements  ReactiveAuthenticationManager {
    private final ReactiveUserDetailsService customUserDetailsService;
    private final JwtUtil jwtUtil;
    @Autowired
    public AuthenticationManager(ReactiveUserDetailsService customUserDetailsService, JwtUtil jwtUtil) {
        this.customUserDetailsService = customUserDetailsService;
        this.jwtUtil = jwtUtil;
    }

    public Mono<Authentication> authenticate(Authentication authentication) {
        return Mono.justOrEmpty(authentication)
                .cast(BearerToken.class)
                .flatMap(auth -> {
                    String username = jwtUtil.extractUsername(auth.getCredentials());

                    Mono<UserDetails> foundUser = customUserDetailsService.findByUsername(username).defaultIfEmpty(new User());

                    return foundUser.flatMap(u -> {
                        if (u.getUsername() == null) {
                            return Mono.error(new IllegalAccessException("User Not found"));
                        }

                        try {
                            if (jwtUtil.validateToken(u, auth.getCredentials())) {
                                return Mono.justOrEmpty(new UsernamePasswordAuthenticationToken(u.getUsername(), u.getPassword(), u.getAuthorities()));
                            } else {
                                return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "JWT expired"));
                            }
                        } catch (Exception e) {
                            return Mono.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error"));
                        }
                    });
                });
    }
}




