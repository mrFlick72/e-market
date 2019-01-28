package it.valeriovaudi.emarket.validator;

import it.valeriovaudi.emarket.event.model.EventType;
import it.valeriovaudi.emarket.event.service.EventDomainPubblishService;
import it.valeriovaudi.emarket.exception.IdentityValidationException;
import it.valeriovaudi.emarket.model.Account;
import it.valeriovaudi.emarket.security.SecurityUtils;
import it.valeriovaudi.emarket.service.ErrorHandlerService;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by mrflick72 on 04/05/17.
 */

@Component
public class AccountIdentityValidationService implements AccountDataValidator {

    private final SecurityUtils securityUtils;
    private final ErrorHandlerService errorHandlerService;

    public AccountIdentityValidationService(SecurityUtils securityUtils,
                                            ErrorHandlerService errorHandlerService) {
        this.securityUtils = securityUtils;
        this.errorHandlerService = errorHandlerService;
    }

    @Override
    public void validate(Account account) {
        System.out.println(account);
        validateUserName(account.getUserName());
    }

    @Override
    public void validateUserName(String userName) {
        if (!userName.equals(securityUtils.getPrincipalUserName())) {
            errorHandlerService.handleIdentityViolation();
        }
    }

}