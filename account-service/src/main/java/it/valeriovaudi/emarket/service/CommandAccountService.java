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

@Slf4j
@Service
@Transactional
public class CommandAccountService {

    private final ErrorHandlerService errorHandlerService;
    private final AccountDataValidator accountDataValidator;
    private final AccountRepository accountRepository;
    private final EventDomainPubblishService eventDomainPubblishService;
    private final QueryAccountService queryAccountService;

    public CommandAccountService(ErrorHandlerService errorHandlerService
            , AccountDataValidator accountDataValidator,
                                 AccountRepository accountRepository,
                                 EventDomainPubblishService eventDomainPubblishService,
                                 QueryAccountService queryAccountService) {
        this.errorHandlerService = errorHandlerService;
        this.accountDataValidator = accountDataValidator;
        this.accountRepository = accountRepository;
        this.eventDomainPubblishService = eventDomainPubblishService;
        this.queryAccountService = queryAccountService;
    }


    public Account createAccount(Account account) {
        accountDataValidator.validate(account);

        if (accountRepository.existsById(account.getUserName()))
            errorHandlerService.handleConflictError(account.getUserName());

        Account save = accountRepository.save(account);
        eventDomainPubblishService.publishEventAuditData(EventType.SAVE_NEW_ACCOUNT_DATA, Map.of());

        return save;
    }


    public Account updateAccount(Account account) {
        accountDataValidator.validate(account);

        Account found = queryAccountService.findAccount(account.getUserName());
        Account save = accountRepository.save(account);

        if (!found.getPassword().equals(save.getPassword())) {
            eventDomainPubblishService.publishEventAuditData(EventType.CHANGE_PASSWORD, Map.of());
        }
        eventDomainPubblishService.publishEventAuditData(EventType.SAVE_ACCOUNT_DATA, Map.of());

        return save;
    }

    public void deleteAccount(String userName) {
        queryAccountService.findAccount(userName);

        accountRepository.deleteById(userName);
        eventDomainPubblishService.publishEventAuditData(EventType.DELETE_AN_ACCOUNT, Map.of());
    }
}
