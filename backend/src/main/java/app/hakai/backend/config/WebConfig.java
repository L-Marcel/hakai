package app.hakai.backend.config;

import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import app.hakai.backend.converters.StringToUuidConverter;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addFormatters(
        @SuppressWarnings("null") FormatterRegistry registry
    ) {
        registry.addConverter(new StringToUuidConverter());
    };

    @Override
    public void addCorsMappings(
        @SuppressWarnings("null") CorsRegistry registry
    ) {
        registry
            .addMapping("/**") // Aplica CORS para todos os endpoints
            .allowedOrigins("http://localhost:5173") // Permite apenas o frontend React
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Métodos permitidos
            .allowedHeaders("*") // Permite todos os headers
            .allowCredentials(true); // Permite envio de cookies/sessões
    };
};