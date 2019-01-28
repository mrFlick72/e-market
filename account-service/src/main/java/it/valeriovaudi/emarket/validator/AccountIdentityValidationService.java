package it.valeriovaudi.emarket.validator;

import it.valeriovaudi.emarket.event.model.EventType;
import it.valeriovaudi.emarket.event.service.EventDomainPubblishService;
import it.valeriovaudi.emarket.exception.IdentityValidationException;
import it.valeriovaudi.emarket.model.Account;
import it.valeriovaudi.emarket.security.SecurityUtils;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by mrflick72 on 04/05/17.
 */

@Component
public class AccountIdentityValidationService implements AccountDataValidator {

    private final SecurityUtils securityUtils;
    private final EventDomainPubblishService eventDomainPubblishService;

    public AccountIdentityValidationService(SecurityUtils securityUtils,
                                            EventDomainPubblishService eventDomainPubblishService) {
        this.securityUtils = securityUtils;
        this.eventDomainPubblishService = eventDomainPubblishService;
    }

    @Override
    public void validate(Account account) {
        System.out.println(account);
        validateUserName(account.getUserName());
    }

    @Override
    public void validateUserName(String userName) {
        System.out.println(userName);
        System.out.println(securityUtils.getPrincipalUserName());
        if (!userName.equals(securityUtils.getPrincipalUserName())) {
            String errorMessage = "the user that is not the same user logged";
            eventDomainPubblishService.publishEventAuditData(EventType.IDENTITY_VALIDATION_ERROR, Map.of("message", errorMessage));
            throw new IdentityValidationException(errorMessage);
        }
    }

}