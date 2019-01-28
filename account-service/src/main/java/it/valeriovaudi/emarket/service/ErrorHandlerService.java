package it.valeriovaudi.emarket.service;

import it.valeriovaudi.emarket.event.model.EventType;
import it.valeriovaudi.emarket.event.service.EventDomainPubblishService;
import it.valeriovaudi.emarket.exception.AccountNotFoundException;
import it.valeriovaudi.emarket.exception.AccountValidationException;
import it.valeriovaudi.emarket.exception.ConflictSaveAccountException;
import it.valeriovaudi.emarket.exception.IdentityValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

import static java.lang.String.format;

@Slf4j
@Service
public class ErrorHandlerService {
    private static final String ACCOUNT_VALIDATION_EXCEPTION_MESSAGE = "The Account %s hasn't satisfies the constraints: %s";

    private final EventDomainPubblishService eventDomainPubblishService;

    public ErrorHandlerService(EventDomainPubblishService eventDomainPubblishService) {
        this.eventDomainPubblishService = eventDomainPubblishService;
    }

    public void handleAccountDataViolation(String userName, Map<String, String> errors) {
        if (errors.size() > 0) {
            log.error(errors.toString());
            eventDomainPubblishService.publishEventAuditData(EventType.VALIDATION_ERROR, errors);
            throw new AccountValidationException(String.format(ACCOUNT_VALIDATION_EXCEPTION_MESSAGE, userName, errors));
        }
    }

    public void handleIdentityViolation() {
        String errorMessage = "the user that is not the same user logged";
        log.error(errorMessage);
        eventDomainPubblishService.publishEventAuditData(EventType.IDENTITY_VALIDATION_ERROR, Map.of("message", errorMessage));
        throw new IdentityValidationException(errorMessage);
    }

    public void handleConflictError(String userName) {
        String errorMessage = format("the user %s is already registered", userName);
        log.error(errorMessage);
        eventDomainPubblishService.publishEventAuditData(EventType.CREATION_ACCOUNT_CONFLICT, Map.of("message", errorMessage));
        throw new ConflictSaveAccountException(errorMessage);
    }

    public void handleNotFoundError(String userName) {
        String errorMessage = format("the user %s is not already registered", userName);
        log.error(errorMessage);
        eventDomainPubblishService.publishEventAuditData(EventType.ACCOUNT_NOT_FOUNT, Map.of("message", errorMessage));
        throw new AccountNotFoundException(errorMessage);
    }
}
