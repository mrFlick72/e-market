package it.valeriovaudi.emarket.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by mrflick72 on 04/05/17.
 */

@ResponseStatus(HttpStatus.CONFLICT)
public class ConflictSaveAccountException extends RuntimeException {
    public ConflictSaveAccountException(String msg) {
        super(msg);
    }

}
