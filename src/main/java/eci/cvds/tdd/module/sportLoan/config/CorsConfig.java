package eci.cvds.tdd.module.sportLoan.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuración de CORS para la aplicación.
 * Permite el acceso desde diferentes orígenes y define los métodos HTTP habilitados.
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    /**
     * Configura las reglas de CORS permitiendo el acceso desde cualquier origen.
     * Se habilitan los métodos HTTP más comunes y se aceptan todos los encabezados.
     *
     * @param registry Registro donde se establecen las reglas de CORS.
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Permite todas las rutas
                .allowedOrigins("https://frontend-modulo4.vercel.app",
                        "https://frontend-modulo4-rfw3tcgjl-santiagoamaya21s-projects.vercel.app") // URL de tu frontend.
                .allowedMethods("GET", "POST", "PUT", "DELETE") // Métodos permitidos o end points permitidos
                .allowedHeaders("*"); // Permitir todos los encabezados
    }
}
