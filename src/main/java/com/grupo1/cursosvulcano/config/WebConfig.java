package com.grupo1.cursosvulcano.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

/**
 * Configuración para permitir que Spring sirva archivos desde una carpeta local externa a los resources.
 * Esto permite que las imágenes subidas a la carpeta 'uploads/' sean visibles desde el navegador.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Obtenemos la ruta absoluta de la carpeta 'uploads' en la raíz del proyecto
        String uploadPath = Paths.get("uploads").toAbsolutePath().toUri().toString();

        // Mapeamos la URL '/uploads/**' a la carpeta física 'uploads/'
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(uploadPath);
    }
}
