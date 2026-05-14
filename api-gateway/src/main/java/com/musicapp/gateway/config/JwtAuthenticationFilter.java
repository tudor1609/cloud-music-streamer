package com.musicapp.gateway.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends AbstractGatewayFilterFactory<JwtAuthenticationFilter.Config> {

    // Secretul trebuie să fie identic cu cel din Auth-Service pentru a putea decripta token-ul
    private final String SECRET = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";

    // Listă de rute care nu au nevoie de autentificare
    private final List<String> whitelistedPaths = List.of("/login", "/register");

    public JwtAuthenticationFilter() {
        super(Config.class);
    }

    public static class Config {
        // Poți adăuga configurări extra aici dacă e nevoie pe viitor
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String path = request.getURI().getPath();

            // 1. Verificăm dacă ruta curentă este în "whitelist" (ex: /auth/login sau /auth/register)
            // Folosim .contains pentru că ruta poate veni ca /auth/login din frontend
            if (whitelistedPaths.stream().anyMatch(path::contains)) {
                return chain.filter(exchange);
            }

            // 2. Verificăm prezența header-ului Authorization
            if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                return onError(exchange, "Lipseste header-ul Authorization", HttpStatus.UNAUTHORIZED);
            }

            String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return onError(exchange, "Format invalid pentru header-ul Authorization", HttpStatus.UNAUTHORIZED);
            }

            String token = authHeader.substring(7);

            try {
                // 3. Validăm semnătura și extragem informațiile (Claims)
                SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(token)
                        .getBody();

                String username = claims.getSubject();

                // 4. "Mutăm" cererea adăugând header-ul custom X-User-Name
                // Această metodă permite microserviciilor (Music, Auth) să știe cine e user-ul
                // fără să mai aibă nevoie de librării JWT greoaie.
                ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                        .header("X-User-Name", username)
                        .build();

                // Trimitem cererea modificată mai departe în lanț
                return chain.filter(exchange.mutate().request(mutatedRequest).build());

            } catch (Exception e) {
                return onError(exchange, "Token invalid sau expirat", HttpStatus.UNAUTHORIZED);
            }
        };
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus status) {
        exchange.getResponse().setStatusCode(status);
        // Putem adăuga și un mesaj de eroare în body dacă vrei, dar un status code e suficient pentru început
        return exchange.getResponse().setComplete();
    }
}