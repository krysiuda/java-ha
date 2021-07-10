package net.siuda.houseautomata.token;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class TokenError extends RuntimeException {

    public enum Action {
        HASHING_ERROR, VERIFICATION_ERROR, CLIENT_NOT_CONFIGURED
    }

    public TokenError(Action action, Throwable cause) {
        super(action.name(), cause);
    }

    public TokenError(Action action) {
        this(action, null);
    }

}
