package net.siuda.houseautomata.auth;

import net.siuda.houseautomata.model.Token;
import net.siuda.houseautomata.model.auth.ClientId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static net.siuda.houseautomata.auth.AuthError.Action.NO_VERIFICATION_METHOD;

@Service
public class AuthService {

    @Autowired
    TokenService tokenService;

    @Autowired
    KeyService keyService;

    @Autowired
    ClientId clientId;

    public void assertAuth() {
        var key = clientId.getKey();
        if (key != null) {
            keyService.verify(clientId);
        } else {
            var token = clientId.getToken();
            if (token != null) {
                tokenService.verifyToken(new Token(token), clientId);
            } else {
                throw new AuthError(NO_VERIFICATION_METHOD);
            }
        }
    }
}
