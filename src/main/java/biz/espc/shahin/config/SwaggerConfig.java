package biz.espc.shahin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;
import java.util.List;

/**
 * provided by ESPC software team
 * created on 1/28/2026 at 10:49 AM
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket webChannelApi() {
        return new Docket(DocumentationType.SWAGGER_2).groupName("WebChannel")
                .select()
                .apis(RequestHandlerSelectors.basePackage("biz.espc.shahin.controller"))
                .paths(PathSelectors.regex("/api/.*"))
                .build()
                .forCodeGeneration(true)
                .apiInfo(getWebChannelApiInfo())
                .securityContexts(Collections.singletonList(securityContext()))
                .securitySchemes(Collections.singletonList(apiKey()));
    }

    private ApiInfo getWebChannelApiInfo() {
        return new ApiInfo(
                "Espc Services Platform",
                "Espc Api Documentation",
                "1.0",
                "",
                new Contact("Espc Software Team", "https://www.ham-sun.com", "api@ham-sun.com"),
                "Apache 2.0",
                "http://www.apache.org/licenses/LICENSE-2.0.html"
                , Collections.emptyList()
        );
    }

    private ApiKey apiKey() {
        return new ApiKey("basic", "Authorization", "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.regex("/api/.*"))
                .build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope
                = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Collections.singletonList(new SecurityReference("basic", authorizationScopes));
    }
}