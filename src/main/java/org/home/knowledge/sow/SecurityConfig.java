package org.home.knowledge.sow;

import static org.springframework.security.config.Customizer.withDefaults;

import org.home.knowledge.sow.security.JpaUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security config
 * 
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // TODO: someday, go back to Spring Boot 3 and try and implement JWT. here were
    // some useful resources:
    // https://github.com/danvega/jwt/blob/master/src/main/java/dev/danvega/jwt/config/SecurityConfig.java
    // https://github.com/spring-projects/spring-security-samples/tree/main/servlet/spring-boot/java/authentication/username-password/user-details-service/custom-user/src
    // https://github.com/spring-projects/spring-security-samples/blob/main/servlet/spring-boot/java/jwt/login/src/main/java/example/RestConfig.java

    private final JpaUserDetailsService jpaUserAccountService;

    public SecurityConfig(JpaUserDetailsService jpaUserAccountService) {
        this.jpaUserAccountService = jpaUserAccountService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .antMatchers("/", "/server-home*/**", "/v3/api-docs*/**", "/actuator/health*/**").permitAll()
                        .antMatchers("/h2-console*/**", "/actuator*/**", "/swagger-ui*/**").hasAnyRole("ADMIN")
                        .anyRequest().authenticated())
                .userDetailsService(
                        jpaUserAccountService)
                .formLogin(withDefaults())
                .httpBasic(withDefaults())
                .logout(withDefaults())
                // See: https://www.baeldung.com/spring-cors#cors-with-spring-security
                .cors(withDefaults())
                // CSRF and headers.sameOrigin enable h2 console to work. They can be removed if
                // we're not using h2 console
                // .csrf(csrf -> csrf.ignoringAntMatchers("/h2-console/**"))
                .csrf(csrf -> csrf.disable())
                .headers(headers -> headers.frameOptions().sameOrigin())
                .exceptionHandling(ex -> ex.accessDeniedPage("/error/accessDenied.html"));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
