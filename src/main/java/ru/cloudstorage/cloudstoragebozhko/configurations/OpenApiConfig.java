package src.main.java.ru.cloudstorage.cloudstoragebozhko.configarations;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

import io.swagger.v3.oas.models.OpenAPI;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @autor Bozhko Alexander
 * 20.05.2021
 * Конфигурация для OpenApi
 */
@Configuration
@OpenAPIDefinition(
   info = @Info(
           title = "Cloud Storage API",
           version = "1.0.0"
   )
)
public class OpenApiConfig {
//   @Bean
//   public OpenAPI customOpenAPI() {
//      return new OpenAPI()
//              .info(new io.swagger.v3.oas.models.info.Info()
//                      .title("Cloud Storage API")
//                      .version("1.0")
//                      .description("API документация"));
//   }
//
//   @Bean
//   public GroupedOpenApi publicApi() {
//      return GroupedOpenApi.builder()
//              .group("public")
//              .pathsToMatch("/**")
//              .build();
//   }
}
