package net.siuda.houseautomata.auth;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import net.siuda.houseautomata.config.Secret;
import net.siuda.houseautomata.model.Token;
import net.siuda.houseautomata.model.auth.ClientId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Service
@Slf4j
public class TokenService {

    @Autowired
    private Secret secret;

    @Autowired
    private TokenTimeProvider tokenTimeProvider;

    @PostConstruct
    private void postInit() {
        log.info("Configured secrets for the following clients: {}", secret.getClients().keySet());
    }

    public Token createToken(ClientId clientId) {
        TokenTimeProvider.TokenTime tstamp = tokenTimeProvider.getCurrentTimeSlot();
        Token token = new Token();
        try {
            token.setToken(makeHash(tstamp, clientId));
        } catch (NoSuchAlgorithmException e) {
            throw new AuthError(AuthError.Action.HASHING_ERROR, e);
        }
        log.debug("Generated token {} for client {}", token.getToken(), clientId.getRemoteIp());
        return token;
    }

    public void verifyToken(Token token, ClientId clientId) {
        String expectedSecret = secret.getClients().get(clientId.getRemoteIp());
        if(expectedSecret == null) {
            log.warn("Client {} not configured", clientId.getRemoteIp());
            throw new AuthError(AuthError.Action.CLIENT_NOT_CONFIGURED);
        }
        TokenTimeProvider.TokenTime tstamp1 = tokenTimeProvider.getCurrentTimeSlot();
        TokenTimeProvider.TokenTime tstamp2 = tokenTimeProvider.getPrevTimeSlot(tstamp1);
        String validToken1 = "";
        String validToken2 = "";
        boolean match = false;
        try {
            validToken1 = makeHash(tstamp1, clientId, expectedSecret);
            validToken2 = makeHash(tstamp2, clientId, expectedSecret);
        } catch (NoSuchAlgorithmException e) {
            throw new AuthError(AuthError.Action.HASHING_ERROR, e);
        }
        log.debug("Client {} has token {} verifying with {} or {}",
                clientId.getRemoteIp(), token.getToken(), validToken1, validToken2);
        match |= validToken1.equals(token.getToken());
        match |= validToken2.equals(token.getToken());
        if(!match) {
            throw new AuthError(AuthError.Action.TOKEN_VERIFICATION_ERROR);
        }
        log.debug("Verified token {} of client {}", token.getToken(), clientId.getRemoteIp());
    }

    private byte [] makeHashArray(TokenTimeProvider.TokenTime timestamp, ClientId clientId) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.writeBytes(clientId.getRemoteIp().getBytes());
        byteArrayOutputStream.writeBytes(secret.getSalt().getBytes());
        byteArrayOutputStream.writeBytes(timestamp.getBytes());
        return digest.digest(byteArrayOutputStream.toByteArray());
    }

    private String makeHash(TokenTimeProvider.TokenTime timestamp, ClientId clientId) throws NoSuchAlgorithmException {
        return new String(Base64.getEncoder().encode(makeHashArray(timestamp, clientId)));
    }

    private String makeHash(TokenTimeProvider.TokenTime timestamp, ClientId clientId, String responseSecret) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.writeBytes(makeHashArray(timestamp, clientId));
        byteArrayOutputStream.writeBytes(responseSecret.getBytes());
        return new String(Base64.getEncoder().encode(digest.digest(byteArrayOutputStream.toByteArray())));
    }

}
