package it.valeriovaudi.emarket.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by mrflick72 on 04/05/17.
 */

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AccountValidationException extends RuntimeException {
    public AccountValidationException(String msg) {
        super(msg);
    }
}
