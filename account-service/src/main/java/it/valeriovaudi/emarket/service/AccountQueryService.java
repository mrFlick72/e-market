package it.valeriovaudi.emarket.service;

import it.valeriovaudi.emarket.model.Account;
import it.valeriovaudi.emarket.repository.AccountRepository;
import it.valeriovaudi.emarket.validator.AccountDataValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@Transactional
public class AccountQueryService {

    private final ErrorHandlerService errorHandlerService;
    private final AccountDataValidator accountDataValidator;
    private final AccountRepository accountRepository;

    public AccountQueryService(ErrorHandlerService errorHandlerService,
                               AccountDataValidator accountDataValidator,
                               AccountRepository accountRepository) {
        this.errorHandlerService = errorHandlerService;
        this.accountDataValidator = accountDataValidator;
        this.accountRepository = accountRepository;
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


}
