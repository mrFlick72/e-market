package it.valeriovaudi.emarket.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by vvaudi on 06/05/17.
 */

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityOAuth2ResourceServerConfig extends WebSecurityConfigurerAdapter {

    @Value("${security.oauth2.resourceserver.jwt.jwk-set-uri}")
    private String jwkSetUri;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/account").permitAll()
                .antMatchers("/account/**").hasRole("USER")
                .anyRequest().authenticated()
                .and().oauth2ResourceServer()
                .jwt()
                .jwtAuthenticationConverter(new GrantedAuthoritiesExtractor())
                .jwkSetUri(jwkSetUri);

    }

    static class GrantedAuthoritiesExtractor extends JwtAuthenticationConverter {

        protected Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {
            Stream<SimpleGrantedAuthority> tokenAuthorities =
                    ((List<String>) jwt.getClaims().getOrDefault("authorities", Collections.<String>emptyList()))
                            .stream()
                            .map(role -> role.startsWith("ROLE_") ? role : "ROLE_" + role)
                            .map(SimpleGrantedAuthority::new);


            return Stream.concat(super.extractAuthorities(jwt).stream(), tokenAuthorities).collect(Collectors.toList());
        }
    }
}