package it.valeriovaudi.emarket.validator;

import it.valeriovaudi.emarket.model.Account;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
public class CompositeAccountDataValidator implements AccountDataValidator {
    private final AccountIdentityValidationService accountIdentityValidationService;
    private final AccountDataValidationService accountDataValidationService;

    public CompositeAccountDataValidator(AccountIdentityValidationService accountIdentityValidationService,
                                         AccountDataValidationService accountDataValidationService) {
        this.accountIdentityValidationService = accountIdentityValidationService;
        this.accountDataValidationService = accountDataValidationService;
    }

    @Override
    public void validate(Account account) {
        accountIdentityValidationService.validate(account);
        accountDataValidationService.validate(account);
    }

    @Override
    public void validateUserName(String userName) {
        accountIdentityValidationService.validateUserName(userName);
        accountDataValidationService.validateUserName(userName);
    }
}
