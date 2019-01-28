package it.valeriovaudi.emarket.validator;

import it.valeriovaudi.emarket.model.Account;

public interface AccountDataValidator {
    void validate(Account account);

    void validateUserName(String userName);
}
