package it.valeriovaudi.emarket.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

/**
 * Created by mrflick72 on 08/06/17.
 */
@Slf4j
@Component
public class SecurityUtils {

    public String getPrincipalUserName(){
        String userName = "";
        try{
            Jwt principal = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            userName = principal.getClaims().get("user_name").toString();
        } catch (Throwable t){
            // ignore it
            log.error("session without an authenticated user");
        }
        return userName;
    }

}
