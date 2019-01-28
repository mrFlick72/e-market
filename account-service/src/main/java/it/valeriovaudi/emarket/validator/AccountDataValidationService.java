package it.valeriovaudi.emarket.validator;

import it.valeriovaudi.emarket.event.service.EventDomainPubblishService;
import it.valeriovaudi.emarket.model.Account;
import it.valeriovaudi.emarket.service.ErrorHandlerService;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mrflick72 on 04/05/17.
 */

@Component
public class AccountDataValidationService implements AccountDataValidator {

    private static final int TAX_CODE_LENGTH = 16;
    private static final String ACCOUNT_VALIDATION_EXCEPTION_MESSAGE = "The Account %s hasn't satisfies the constraints";
    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    private final ErrorHandlerService errorHandlerService;
    private final MessageSource messageSource;

    public AccountDataValidationService(ErrorHandlerService errorHandlerService, MessageSource messageSource) {
        this.errorHandlerService = errorHandlerService;
        this.messageSource = messageSource;
    }

    @Override
    public void validate(Account account) {
        Map<String, String> errors = new HashMap<>();

        String userName = account.getUserName();
        String password = account.getPassword();
        String taxCode = account.getTaxCode();
        String firstName = account.getFirstName();
        String lastName = account.getLastName();
        String mail = account.getMail();

        validateNotNullAndNotEmpty("userName", userName, "AccountDataValidationService.Account.userName", errors);
        validateNotNullAndNotEmpty("password", password, "AccountDataValidationService.Account.password", errors);
        validateNotNullAndNotEmpty("firstName", firstName, "AccountDataValidationService.Account.firstName", errors);
        validateNotNullAndNotEmpty("lastName", lastName, "AccountDataValidationService.Account.lastName", errors);
        validateTaxCode("taxCode", taxCode, "AccountDataValidationService.Account.taxCode", errors);
        validateMail("mail", mail, "AccountDataValidationService.Account.mail", errors);

        manageError(userName, errors);
    }

    public void validateUserName(String userName) {
        Map<String, String> errors = new HashMap<>();

        validateNotNullAndNotEmpty("userName", userName, "AccountDataValidationService.Account.userName", errors);
        manageError(userName, errors);
    }

    private void manageError(String userName, Map<String, String> errors) {
         errorHandlerService.handleAccountDataViolation(userName, errors);
    }

    private void validateMail(String key, String value, String validationMessageKey, Map<String, String> errors) {
        boolean inError = value == null;

        if (!inError) {
            Pattern pattern = Pattern.compile(EMAIL_PATTERN);
            Matcher matcher = pattern.matcher(value);
            inError = !matcher.matches();
        }
        if (inError) {
            errors.put(key, messageSource.getMessage(validationMessageKey, new Object[]{}, Locale.ENGLISH));
        }
    }

    private void validateTaxCode(String key, String value, String validationMessageKey, Map<String, String> errors) {
        boolean inError = value == null || value.length() != TAX_CODE_LENGTH;

        if (inError) {
            errors.put(key, messageSource.getMessage(validationMessageKey, new Object[]{}, Locale.ENGLISH));
        }
    }

    private void validateNotNullAndNotEmpty(String key, String value, String validationMessageKey, Map<String, String> errors) {
        if (value == null || "".equals(value)) {
            errors.put(key, messageSource.getMessage(validationMessageKey, new Object[]{}, Locale.ENGLISH));
        }
    }
}