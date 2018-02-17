package it.valeriovaudi.emarket.security.integration;

import it.valeriovaudi.emarket.security.AccountUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * Created by mrflick72 on 13/06/17.
 */

@Slf4j
@Component
public class UserDetailsProcessorChannelHandlrer {

    private final AccountUserDetailsService accountUserDetailsService;

    public UserDetailsProcessorChannelHandlrer(AccountUserDetailsService accountUserDetailsService) {
        this.accountUserDetailsService = accountUserDetailsService;
    }

    @RabbitListener(queues = "authServerAccountServiceBridgeInboundQueue")
    public UserDetails getUserDetails(String userName){
        log.info("userName: " + userName);
        UserDetails userDetails = null;

        try {
            userDetails = accountUserDetailsService.loadUserByUsername(userName);
        } catch (Exception e){
            log.error("user didn't found");
        }
        return userDetails;
    }
}
