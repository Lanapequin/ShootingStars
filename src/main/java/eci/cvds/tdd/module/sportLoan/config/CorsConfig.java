package eci.cvds.tdd.module.sportLoan.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Permite todas las rutas
                .allowedOrigins("*") // URL de tu frontend
                .allowedMethods("GET", "POST", "PUT", "DELETE") // Métodos permitidos o end points permitidos
                .allowedHeaders("*"); // Permitir todos los encabezados
    }
}
