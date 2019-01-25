package it.valeriovaudi.emarket.service;

import it.valeriovaudi.emarket.model.Account;
import it.valeriovaudi.emarket.repository.AccountRepository;
import it.valeriovaudi.emarket.validator.AccountDataValidationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Created by mrflick72 on 04/05/17.
 */
@Service
@Transactional
public class AccountService {

    private final AccountDataValidationService accountDataValidationService;
    private final AccountRepository accountRepository;

    public AccountService(AccountDataValidationService accountDataValidationService,
                          AccountRepository accountRepository) {
        this.accountDataValidationService = accountDataValidationService;
        this.accountRepository = accountRepository;
    }

    public Account createAccount(Account account) {
        // data validation
        accountDataValidationService.validate(account);
        // save account
        Account save = accountRepository.save(account);
        // fire save account event
        return save;
    }

    @Transactional(readOnly = true)
    public Optional<Account> findAccount(String userName) {
        return accountRepository.findById(userName);
    }

    public Account updateAccount(Account account) {
        // data validation
        accountDataValidationService.validate(account);

        // save account
        Account save = accountRepository.save(account);

        return save;
    }

    public void deleteAccount(String userName) {
        accountDataValidationService.validateUserName(userName);
    }

}
