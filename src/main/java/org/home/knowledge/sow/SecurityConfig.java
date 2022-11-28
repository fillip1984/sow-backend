package org.home.knowledge.sow;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import lombok.extern.slf4j.Slf4j;

/**
 * Spring Security config
 * 
 */
@Configuration
@EnableWebSecurity
@Slf4j
public class SecurityConfig {

    // TODO: someday, go back to Spring Boot 3 and try and implement JWT. here were
    // some useful resources:
    // https://github.com/danvega/jwt/blob/master/src/main/java/dev/danvega/jwt/config/SecurityConfig.java
    // https://github.com/spring-projects/spring-security-samples/tree/main/servlet/spring-boot/java/authentication/username-password/user-details-service/custom-user/src
    // https://github.com/spring-projects/spring-security-samples/blob/main/servlet/spring-boot/java/jwt/login/src/main/java/example/RestConfig.java

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // http://phillips-macbook-pro.local:7878/sow/h2-console/login.do?jsessionid=169664501c38e7fe9e26a442a19f3398
        http
                .authorizeHttpRequests(auth -> auth
                        .antMatchers("/", "/server-home*/**", "/v3/api-docs*/**", "/actuator/health*/**").permitAll()
                        .antMatchers("/h2-console*/**", "/actuator*/**", "/swagger-ui*/**").hasAnyRole("ADMIN")
                        .anyRequest().authenticated())
                .formLogin(withDefaults())
                .httpBasic(withDefaults())
                .logout(withDefaults())
                // See: https://www.baeldung.com/spring-cors#cors-with-spring-security
                .cors(withDefaults())
                .csrf(csrf -> csrf.disable())
                // See:
                // https://springframework.guru/using-the-h2-database-console-in-spring-boot-with-spring-security/
                .headers(headers -> headers.frameOptions().disable())
                .exceptionHandling(ex -> ex.accessDeniedPage("/error/accessDenied.html"));

        return http.build();
    }

    @Bean
    UserDetailsService users() {
        log.warn(
                "NoOp password encoder is being used with in memory authentication. THIS CONFIGURATION SHOULD NOT MAKE IT INTO A QA OR PROD ENVIRONMENT!!!");
        // @formatter:off
            var user = User.builder()
                            .username("user")
                            .password("{noop}user")
                            .roles("USER")
                            .build();
            var admin = User.builder()
                            .username("admin")
                            .password("{noop}admin")
                            .roles("USER", "ADMIN")
                            .build();
        // @formatter:on
        return new InMemoryUserDetailsManager(user, admin);
    }

}
