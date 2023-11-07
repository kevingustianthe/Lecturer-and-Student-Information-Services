package com.fikupnvj.restfulapi.configuration;

import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfiguration {

    @Bean
    public OpenApiCustomizer customOpenAPI() {
        return openApi ->  openApi.info(new Info()
                .title("Lecturer and Student Information Services")
                .version("v1.0")
                .description("Documentation API")
                .contact(new Contact()
                        .name("Kevin Gustian The - Fakultas Ilmu Komputer UPNVJ")
                        .email("2010511065@mahasiswa.upnvj.ac.id")
                        .url("https://new-fik.upnvj.ac.id/")
                )
        );
    }
}
