package net.siuda.houseautomata.auth;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class AuthError extends RuntimeException {

    public enum Action {
        HASHING_ERROR, TOKEN_VERIFICATION_ERROR, CLIENT_NOT_CONFIGURED, KEY_VERIFICATION_ERROR, NO_VERIFICATION_METHOD
    }

    public AuthError(Action action, Throwable cause) {
        super(action.name(), cause);
    }

    public AuthError(Action action) {
        this(action, null);
    }

}
