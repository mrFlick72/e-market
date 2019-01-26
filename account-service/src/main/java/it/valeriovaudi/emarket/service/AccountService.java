package it.valeriovaudi.emarket.service;

import brave.Tracer;
import brave.Tracing;
import it.valeriovaudi.emarket.event.model.EventType;
import it.valeriovaudi.emarket.event.service.EventDomainPubblishService;
import it.valeriovaudi.emarket.exception.AccountNotFoundException;
import it.valeriovaudi.emarket.exception.ConflictSaveAccountException;
import it.valeriovaudi.emarket.model.Account;
import it.valeriovaudi.emarket.repository.AccountRepository;
import it.valeriovaudi.emarket.validator.AccountDataValidationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

import static java.lang.String.format;

/**
 * Created by mrflick72 on 04/05/17.
 */
@Service
@Transactional
@Slf4j
public class AccountService {

    private final EventDomainPubblishService eventDomainPubblishService;
    private final AccountDataValidationService accountDataValidationService;
    private final AccountRepository accountRepository;
    private final Tracer tracer;
    private final Tracing tracing;

    public AccountService(EventDomainPubblishService eventDomainPubblishService,
                          AccountDataValidationService accountDataValidationService,
                          AccountRepository accountRepository, Tracer tracer, Tracing tracing) {
        this.eventDomainPubblishService = eventDomainPubblishService;
        this.accountDataValidationService = accountDataValidationService;
        this.accountRepository = accountRepository;
        this.tracer = tracer;
        this.tracing = tracing;
    }

    public Account createAccount(Account account) {
        log.info(account.toString());

        accountDataValidationService.validate(account);

        if (accountRepository.existsById(account.getUserName()))
            throw handleConflictError("the user %s is already registered", account.getUserName());

        return accountRepository.save(account);
    }

    @Transactional(readOnly = true)
    public Account findAccount(String userName) {
        return accountRepository.findById(userName)
                .orElseThrow(() -> handleNotFoundError("the user %s is not already registered", userName));
    }

    public Account updateAccount(Account account) {
        log.info(account.toString());

        accountDataValidationService.validate(account);

        if (!accountRepository.existsById(account.getUserName()))
            throw handleNotFoundError("the user %s is not already registered", account.getUserName());

        return accountRepository.save(account);
    }

    public void deleteAccount(String userName) {
        log.info(userName);

        if (!accountRepository.existsById(userName))
            throw handleNotFoundError("the user %s is not already registered", userName);

        accountDataValidationService.validateUserName(userName);
    }

    private ConflictSaveAccountException handleConflictError(String messageTemplate, String userName) {
        String errorMessage = format(messageTemplate, userName);
        eventDomainPubblishService.publishEventAuditData(EventType.CREATION_ACCOUNT_CONFLICT, Map.of("message", errorMessage));
        return new ConflictSaveAccountException(errorMessage);
    }

    private AccountNotFoundException handleNotFoundError(String messageTemplate, String userName) {
        String errorMessage = format(messageTemplate, userName);
        eventDomainPubblishService.publishEventAuditData(EventType.ACCOUNT_NOT_FOUNT, Map.of("message", errorMessage));
        return new AccountNotFoundException(errorMessage);
    }
}
