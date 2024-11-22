package server.security;

import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;



@Component
public class JwtAuthenticationConverter implements ServerAuthenticationConverter {


    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {

        String authorizationHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        Mono<String> tokenMono = Mono.justOrEmpty(authorizationHeader)
                .filter(header -> header.startsWith("Bearer "))
                .map(header -> header.substring(7));

        String tokenFromQueryParam = exchange.getRequest().getQueryParams().getFirst("token");
        Mono<String> finalTokenMono = Mono.justOrEmpty(tokenFromQueryParam)
                .switchIfEmpty(tokenMono);
        return finalTokenMono.map(BearerToken::new);
    }
}