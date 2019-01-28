package it.valeriovaudi.emarket.service;

import it.valeriovaudi.emarket.event.service.EventDomainPubblishService;
import it.valeriovaudi.emarket.model.Account;
import it.valeriovaudi.emarket.repository.AccountRepository;
import it.valeriovaudi.emarket.validator.AccountDataValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.lang.String.format;

/**
 * Created by mrflick72 on 04/05/17.
 */
@Slf4j
@Service
@Transactional
public class AccountService {

    private final ErrorHandlerService errorHandlerService;
    private final AccountDataValidator accountDataValidator;
    private final AccountRepository accountRepository;

    public AccountService(ErrorHandlerService errorHandlerService,
                          AccountDataValidator accountDataValidator,
                          AccountRepository accountRepository) {
        this.errorHandlerService = errorHandlerService;

        this.accountDataValidator = accountDataValidator;
        this.accountRepository = accountRepository;
    }

    public Account createAccount(Account account) {
        log.info(account.toString());

        accountDataValidator.validate(account);

        if (accountRepository.existsById(account.getUserName()))
            errorHandlerService.handleConflictError(account.getUserName());

        return accountRepository.save(account);
    }

    @Transactional(readOnly = true)
    public Account findAccount(String userName) {
        accountDataValidator.validateUserName(userName);

        return accountRepository.findById(userName)
                .orElseThrow(() -> errorHandlerService.handleNotFoundError(userName));
    }

    public Account updateAccount(Account account) {
        log.info(account.toString());

        accountDataValidator.validate(account);

        if (!accountRepository.existsById(account.getUserName()))
            throw errorHandlerService.handleNotFoundError(account.getUserName());

        return accountRepository.save(account);
    }

    public void deleteAccount(String userName) {
        log.info(userName);

        if (!accountRepository.existsById(userName))
            throw errorHandlerService.handleNotFoundError(userName);

        accountDataValidator.validateUserName(userName);
    }

}
