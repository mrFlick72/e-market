package it.valeriovaudi.emarket.service;

import it.valeriovaudi.emarket.event.model.EventType;
import it.valeriovaudi.emarket.event.service.EventDomainPubblishService;
import it.valeriovaudi.emarket.model.Account;
import it.valeriovaudi.emarket.repository.AccountRepository;
import it.valeriovaudi.emarket.validator.AccountDataValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

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
    private final EventDomainPubblishService eventDomainPubblishService;

    public AccountService(ErrorHandlerService errorHandlerService,
                          AccountDataValidator accountDataValidator,
                          AccountRepository accountRepository,
                          EventDomainPubblishService eventDomainPubblishService) {
        this.errorHandlerService = errorHandlerService;

        this.accountDataValidator = accountDataValidator;
        this.accountRepository = accountRepository;
        this.eventDomainPubblishService = eventDomainPubblishService;
    }

    public Account createAccount(Account account) {
        accountDataValidator.validate(account);

        if (accountRepository.existsById(account.getUserName()))
            errorHandlerService.handleConflictError(account.getUserName());

        Account save = accountRepository.save(account);
        eventDomainPubblishService.publishEventAuditData(EventType.SAVE_NEW_ACCOUNT_DATA, Map.of());

        return save;
    }

    @Transactional(readOnly = true)
    public Account findAccount(String userName) {
        accountDataValidator.validateUserName(userName);
        Optional<Account> optionalAccount = accountRepository.findById(userName);

        if (optionalAccount.isEmpty()) {
            errorHandlerService.handleNotFoundError(userName);
        }
        return optionalAccount.get();
    }

    public Account updateAccount(Account account) {
        accountDataValidator.validate(account);

        Account found = findAccount(account.getUserName());
        Account save = accountRepository.save(account);

        if (!found.getPassword().equals(save.getPassword())) {
            eventDomainPubblishService.publishEventAuditData(EventType.CHANGE_PASSWORD, Map.of());
        }
        eventDomainPubblishService.publishEventAuditData(EventType.SAVE_ACCOUNT_DATA, Map.of());

        return save;
    }

    public void deleteAccount(String userName) {
        findAccount(userName);

        accountRepository.deleteById(userName);
        eventDomainPubblishService.publishEventAuditData(EventType.DELETE_AN_ACCOUNT, Map.of());
    }

}
