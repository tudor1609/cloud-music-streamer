package com.musicapp.gateway.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.http.HttpHeaders; // <--- Ăsta trebuie să fie!
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Component
public class JwtAuthenticationFilter extends AbstractGatewayFilterFactory<JwtAuthenticationFilter.Config> {

    // FOLOSEȘTE EXACT ACELAȘI SECRET DIN AUTH-SERVICE!
    private final String SECRET = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";

    public JwtAuthenticationFilter() {
        super(Config.class);
    }

    public static class Config {}

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            // 1. Verificăm dacă cererea are header-ul Authorization
            String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return onError(exchange, "Lipseste sau este invalid header-ul Authorization", HttpStatus.UNAUTHORIZED);
            }

            String token = authHeader.substring(7);

            try {
                // 2. Verificăm semnătura JWT
                SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
                Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            } catch (Exception e) {
                return onError(exchange, "Token invalid sau expirat", HttpStatus.UNAUTHORIZED);
            }

            // Dacă totul e ok, mergi mai departe la Music Service
            return chain.filter(exchange);
        };
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus status) {
        exchange.getResponse().setStatusCode(status);
        return exchange.getResponse().setComplete();
    }
}