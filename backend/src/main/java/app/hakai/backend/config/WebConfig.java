package app.hakai.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import app.hakai.backend.converters.StringToUuidConverter;

@Configuration
@PropertySource("classpath:secret.properties")
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
            .addMapping("/**")
            .allowedOrigins("http://localhost:5173", "*..ngrok-free.app")
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
            .allowedHeaders("*")
            .allowCredentials(true);
    };
};