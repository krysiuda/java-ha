package net.siuda.houseautomata.auth;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import net.siuda.houseautomata.config.Secret;
import net.siuda.houseautomata.model.auth.ClientId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static net.siuda.houseautomata.auth.AuthError.Action.KEY_VERIFICATION_ERROR;

@Service
@Slf4j
public class KeyService {

    @Autowired
    private Secret secret;

    @PostConstruct
    private void postInit() {
        log.info("Configured {} keys", secret.getKeys().size());
    }

    public void verify(ClientId clientId) {
        var keys = secret.getKeys();
        var key = clientId.getKey();
        if (!keys.contains(key)) {
            throw new AuthError(KEY_VERIFICATION_ERROR);
        }
    }

}
