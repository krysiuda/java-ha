package net.siuda.houseautomata.token;

import net.siuda.houseautomata.config.Secret;
import net.siuda.houseautomata.model.ClientId;
import net.siuda.houseautomata.model.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Service
public class TokenService {

    private static final Logger LOG = LoggerFactory.getLogger(TokenService.class);

    @Autowired
    private Secret secret;

    @Autowired
    private TokenTimeProvider tokenTimeProvider;

    @PostConstruct
    private void postInit() {
        LOG.info("Configured secrets for the following clients: {}", secret.getClients().keySet());
    }

    public Token createToken(ClientId clientId) {
        TokenTimeProvider.TokenTime tstamp = tokenTimeProvider.getCurrentTimeSlot();
        Token token = new Token();
        try {
            token.setToken(makeHash(tstamp, clientId));
        } catch (NoSuchAlgorithmException e) {
            throw new TokenError(TokenError.Action.HASHING_ERROR, e);
        }
        LOG.debug("Generated token {} for client {}", token.getToken(), clientId.getValue());
        return token;
    }

    public void verifyToken(Token token, ClientId clientId) {
        String expectedSecret = secret.getClients().get(clientId.getValue());
        if(expectedSecret == null) {
            LOG.warn("Client {} not configured", clientId.getValue());
            throw new TokenError(TokenError.Action.CLIENT_NOT_CONFIGURED);
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
            throw new TokenError(TokenError.Action.HASHING_ERROR, e);
        }
        LOG.debug("Client {} has token {} verifying with {} or {}", clientId.getValue(), token.getToken(), validToken1, validToken2);
        match |= validToken1.equals(token.getToken());
        match |= validToken2.equals(token.getToken());
        if(!match) {
            throw new TokenError(TokenError.Action.VERIFICATION_ERROR);
        }
        LOG.debug("Verified token {} of client {}", token.getToken(), clientId.getValue());
    }

    private byte [] makeHashArray(TokenTimeProvider.TokenTime timestamp, ClientId clientId) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.writeBytes(clientId.getValue().getBytes());
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
