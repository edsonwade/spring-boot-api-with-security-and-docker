package code.with.vanilson.securityservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UsernameAlreadyExistsException extends RuntimeException {
    public UsernameAlreadyExistsException(String s) {
        super(s);
    }
}
