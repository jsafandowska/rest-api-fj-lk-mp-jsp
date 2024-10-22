package pl.kurs.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI mySwagger(){
        Server prodServer = new Server();
        prodServer.setUrl("http://localhost:8080");
        prodServer.setDescription("Server URL in production environment");

        Info info = new Info()
                .title("Library API")
                .version("1.0");

        return new OpenAPI().info(info).servers(List.of(prodServer));
    }
}
