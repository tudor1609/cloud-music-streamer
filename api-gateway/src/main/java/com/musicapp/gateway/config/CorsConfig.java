package com.musicapp.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class CorsConfig {

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();

        // 1. Permitem doar originea frontend-ului tău (localhost:3000)
        corsConfig.setAllowedOrigins(Arrays.asList(
                "http://localhost:3000",                   // Pentru când testezi tu acasă
                "https://dracify-frontend.vercel.app/"
        ));

        // 2. Ce metode lăsăm să treacă (punem tot ce e important)
        corsConfig.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // 3. Ce headere acceptăm (Authorization e critic pentru token-ul tău)
        corsConfig.setAllowedHeaders(Arrays.asList("Content-Type", "Authorization"));

        // 4. Permitem trimiterea de cookies sau Auth headers (important pentru JWT)
        corsConfig.setAllowCredentials(true);

        // 5. Cât timp să țină browserul minte setările astea (1 oră)
        corsConfig.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);

        return new CorsWebFilter(source);
    }
}
