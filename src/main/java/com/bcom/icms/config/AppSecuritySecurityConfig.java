package com.bcom.icms.config;

import static org.springframework.security.config.Customizer.withDefaults;

import com.bcom.icms.util.UserRoles;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class AppSecuritySecurityConfig {

    /**
     * Read claims from attribute realm_access.roles as SimpleGrantedAuthority.
     */
    private List<GrantedAuthority> mapAuthorities(final Map<String, Object> attributes) {
        @SuppressWarnings("unchecked") final Map<String, Object> realmAccess =
                ((Map<String, Object>)attributes.getOrDefault("realm_access", Collections.emptyMap()));
        @SuppressWarnings("unchecked") final Collection<String> roles =
                ((Collection<String>)realmAccess.getOrDefault("roles", List.of()));
        return roles.stream()
                .map(role -> ((GrantedAuthority)new SimpleGrantedAuthority(role)))
                .toList();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        final JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(source -> mapAuthorities(source.getClaims()));
        return converter;
    }

    @Bean
    public SecurityFilterChain appSecurityFilterChain(final HttpSecurity http) throws Exception {
        return http.cors(withDefaults())
                .csrf(csrf -> csrf.disable())
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()))
                .authorizeHttpRequests(authorize -> authorize
                    .requestMatchers(EndpointRequest.toAnyEndpoint()).hasAuthority(UserRoles.SUPERADMIN)
                    .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").hasAuthority(UserRoles.SUPERADMIN)
                    .anyRequest().permitAll())
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(withDefaults()))
                .build();
    }

}
