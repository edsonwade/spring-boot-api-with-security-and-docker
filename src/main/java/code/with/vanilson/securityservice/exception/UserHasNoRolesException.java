package code.with.vanilson.securityservice.exception;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserHasNoRolesException extends RuntimeException {
    public UserHasNoRolesException(String message) {
        super(message);
    }
}
