package org.home.knowledge.sow;

import org.home.knowledge.sow.security.RsaKeyProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

import lombok.extern.slf4j.Slf4j;

/**
 * Spring Security config
 * <p>
 * JWT config, see:
 * https://github.com/danvega/jwt/blob/master/src/main/java/dev/danvega/jwt/config/SecurityConfig.java
 * 
 */
@Configuration
@EnableWebSecurity
@Slf4j
public class SecurityConfig {
    // Other Sources:
    // https://github.com/spring-projects/spring-security-samples/tree/main/servlet/spring-boot/java/authentication/username-password/user-details-service/custom-user/src
    // https://github.com/spring-projects/spring-security-samples/blob/main/servlet/spring-boot/java/jwt/login/src/main/java/example/RestConfig.java

    private final RsaKeyProperties jwtConfigProperties;

    public SecurityConfig(RsaKeyProperties jwtConfigProperties) {
        this.jwtConfigProperties = jwtConfigProperties;
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.warn(
                "H2 database console is unsecured for development purposes. THIS CONFIGURATION SHOULD NOT MAKE IT INTO A QA OR PROD ENVIRONMENT!!!");
        // @formatter:off
        // See: https://www.baeldung.com/spring-cors
        // H2 console required frameOptions be disabled, see: https://springframework.guru/using-the-h2-database-console-in-spring-boot-with-spring-security/

        // disabled BearerTokenAuthenticationEntryPoint due to not always using Rest and it was breaking the browser
        // might have to revisit this: https://www.jessym.com/articles/combining-spring-boot-auth-methods
        http.cors(Customizer.withDefaults())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/").permitAll()
                .requestMatchers("/h2-console/**", "/swagger-ui/**", "/v3/api-docs/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers("/actuator/**", "/admin/**").hasAnyRole("ADMIN")
                .anyRequest().authenticated())
            .formLogin(Customizer.withDefaults())
            .csrf((csrf) -> csrf.ignoringRequestMatchers("/token"))
            .httpBasic(Customizer.withDefaults())
			.oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
			.sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .exceptionHandling((ex) -> ex
                        // .authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint())
                        .accessDeniedHandler(new BearerTokenAccessDeniedHandler()))
            .headers().frameOptions().disable();
        return http.build();
        // @formatter:on
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

    @Bean
    JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(jwtConfigProperties.publicKey()).build();
    }

    @Bean
    JwtEncoder jwtEncoder() {
        // @formatter:off
        JWK jwk = new RSAKey.
                        Builder(jwtConfigProperties.publicKey())
                        .privateKey(jwtConfigProperties.privateKey())
                        .build();
        // @formatter:on
        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }

}
