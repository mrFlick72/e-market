package it.valeriovaudi.emarket.security;

import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Created by vvaudi on 06/05/17.
 */

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityOAuth2ResourceServerConfig extends WebSecurityConfigurerAdapter {


    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/account").permitAll()
                .antMatchers("/account/**").authenticated()
                .anyRequest().authenticated()
                .and().oauth2ResourceServer().jwt().jwkSetUri("http://localhost:9090/auth/sign-key/public");

    }
}