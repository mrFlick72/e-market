package it.valeriovaudi.emarket.validator;

import it.valeriovaudi.emarket.model.Account;
import it.valeriovaudi.emarket.security.SecurityUtils;
import it.valeriovaudi.emarket.service.ErrorHandlerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static java.util.Optional.ofNullable;

/**
 * Created by mrflick72 on 04/05/17.
 */

@Slf4j
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
        validateUserName(account.getUserName());
    }

    @Override
    public void validateUserName(String userName) {
        ofNullable(securityUtils.getPrincipalUserName())
                .filter(loggedUserName -> !loggedUserName.isBlank())
                .ifPresent(loggedUserName -> {
                    if (!userName.equals(loggedUserName)) {
                        errorHandlerService.handleIdentityViolation();
                    }
                });
    }

}