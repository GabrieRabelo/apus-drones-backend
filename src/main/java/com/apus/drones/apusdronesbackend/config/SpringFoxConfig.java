package com.apus.drones.apusdronesbackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.AuthorizationScopeBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.HttpAuthenticationScheme;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;
import java.util.List;

import static springfox.documentation.service.ApiInfo.DEFAULT_CONTACT;

@Configuration
@EnableOpenApi
public class SpringFoxConfig {

    public static final ApiInfo DEFAULT_API_INFO = new ApiInfo(
        "Apus Drones",
        "Aplicativo de delivery por drones!", "1.0-SNAPSHOT",
        "urn:tos", DEFAULT_CONTACT,
        "Apache 2.0", "http://www.apache.org/licenses/LICENSE-2.0", Collections.emptyList()
    );

    @Bean
    public Docket api() {
        AuthorizationScope[] authScopes = new AuthorizationScope[] {
            new AuthorizationScopeBuilder().scope("global").description("full access").build()
        };

        SecurityReference securityReference = SecurityReference
            .builder()
            .reference("JWT")
            .scopes(authScopes)
            .build();

        List<SecurityContext> securityContexts = List.of(
            SecurityContext.builder().securityReferences(
                    List.of(securityReference)
                )
                .operationSelector(
                    o -> o.requestMappingPattern().matches("^(?:(?!/api/v\\d+/authenticate/.*).)*$"))
                .build()
        );

        HttpAuthenticationScheme authenticationScheme = HttpAuthenticationScheme
            .JWT_BEARER_BUILDER
            .name("JWT")
            .build();

        return new Docket(DocumentationType.OAS_30)
            .select()
            .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
            .paths(PathSelectors.any())
            .build()
            .securitySchemes(Collections.singletonList(authenticationScheme))
            .securityContexts(securityContexts);
    }
}