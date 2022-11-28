package org.home.knowledge.sow;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.extern.slf4j.Slf4j;

/**
 * Spring Web Mvc config
 */
@Configuration
@Slf4j
// WARNING: Do not add @EnableWebMvc as it will break serving html from
// controllers and also swagger
// See: https://github.com/springdoc/springdoc-openapi/issues/236
public class WebConfig implements WebMvcConfigurer {

    // ended up not needing a view controller of any kind unless we want to lose the
    // .html part when directing to the location
    // @Override
    // public void addViewControllers(ViewControllerRegistry registry) {
    // log.info("Adding in route: /server-home, mapped to resource:
    // server-home.html");
    // registry.addViewController("/server-home").setViewName("server-home.html");
    // }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        log.warn(
                "CORS has been enabled for local development with React at port 3000 and 5173. THIS CORS CONFIGURATION SHOULD NOT GO TO QA OR PROD ENVIRONMENT");
        // See: https://www.baeldung.com/spring-cors
        // @formatter:off
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000", "http://localhost:5173", "http://127.0.0.1:5173")
                .allowedMethods("*")
                .allowedHeaders("*")
                .exposedHeaders("Content-Type", "Content-Disposition", "Pragma");
        // @formatter:on
    }

    // TODO: I love that I finally found a solution to json serialization for
    // circular references, see: //
    // https://hellokoding.com/handling-circular-reference-of-jpa-hibernate-bidirectional-entity-relationships-with-jackson-jsonignoreproperties/,
    // but I'd like to write a Jackson custom serializer that would inspect and skip
    // collections that match parent... Maybe:
    // https://stackoverflow.com/questions/26945580/jackson-serialization-how-to-ignore-superclass-properties
}
