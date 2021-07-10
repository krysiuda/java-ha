package net.siuda.houseautomata.token;

import net.siuda.houseautomata.config.Secret;
import net.siuda.houseautomata.model.ClientId;
import net.siuda.houseautomata.model.Token;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockReset;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;
import java.util.Base64;

@SpringBootTest(properties = "secret.clients.127.0.0.1=nopass")
@ExtendWith(SpringExtension.class)
@Import(Secret.class)
public class TokenServiceTest {

    @MockBean(answer = Answers.RETURNS_DEFAULTS, reset = MockReset.BEFORE)
    private TokenTimeProvider mockTokenTimeProvider;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private Secret secret;

//    @Test
//    public void generateAndVerify() throws Exception {
//        Mockito.when(mockTokenTimeProvider.getCurrentTimeSlot()).thenReturn(fixedTokenTime(1L), fixedTokenTime(1L));
//        Mockito.when(mockTokenTimeProvider.getNextTimeSlot(Mockito.any())).thenReturn(fixedTokenTime(2L));
//        ClientId clientId = getClientId();
//        Token token = tokenService.createToken(clientId);
//        token.setToken(signTokenWithSecret(token.getToken(0), getSecret()), signTokenWithSecret(token.getToken(1), getSecret()));
//        tokenService.verifyToken(token, clientId);
//    }
//
//    @Test
//    public void generateAndVerifyDelayed() throws Exception {
//        Mockito.when(mockTokenTimeProvider.getCurrentTimeSlot()).thenReturn(fixedTokenTime(1L), fixedTokenTime(2L));
//        Mockito.when(mockTokenTimeProvider.getNextTimeSlot(Mockito.any())).thenReturn(fixedTokenTime(2L));
//        ClientId clientId = getClientId();
//        Token token = tokenService.createToken(clientId);
//        token.setToken(signTokenWithSecret(token.getToken(0), getSecret()), signTokenWithSecret(token.getToken(1), getSecret()));
//        tokenService.verifyToken(token, clientId);
//    }
//
//    @Test
//    public void failMalformedToken() {
//        Mockito.when(mockTokenTimeProvider.getCurrentTimeSlot()).thenReturn(fixedTokenTime(1L), fixedTokenTime(1L));
//        Mockito.when(mockTokenTimeProvider.getNextTimeSlot(Mockito.any())).thenReturn(fixedTokenTime(2L));
//        ClientId clientId = getClientId();
//        Token token = new Token();
//        token.setToken("");
//        Assertions.assertThrows(TokenError.class, () -> tokenService.verifyToken(token, clientId));
//    }
//
//    @Test
//    public void failOldToken() throws Exception {
//        Mockito.when(mockTokenTimeProvider.getCurrentTimeSlot()).thenReturn(fixedTokenTime(1L), fixedTokenTime(3L));
//        Mockito.when(mockTokenTimeProvider.getNextTimeSlot(Mockito.any())).thenReturn(fixedTokenTime(2L));
//        ClientId clientId = getClientId();
//        Token token = tokenService.createToken(clientId);
//        token.setToken(signTokenWithSecret(token.getToken(0), getSecret()));
//        Assertions.assertThrows(TokenError.class, () -> tokenService.verifyToken(token, clientId));
//    }
//
//    @Test
//    public void failUnsignedToken() throws Exception {
//        Mockito.when(mockTokenTimeProvider.getCurrentTimeSlot()).thenReturn(fixedTokenTime(1L), fixedTokenTime(1L));
//        Mockito.when(mockTokenTimeProvider.getNextTimeSlot(Mockito.any())).thenReturn(fixedTokenTime(2L));
//        ClientId clientId = getClientId();
//        Token token = tokenService.createToken(clientId);
//        token.setToken(signTokenWithSecret(token.getToken(0), "WrongSecret"));
//        Assertions.assertThrows(TokenError.class, () -> tokenService.verifyToken(token, clientId));
//    }

    private String signTokenWithSecret(String token, String secret) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(Base64.getDecoder().decode(token));
        byteArrayOutputStream.write(secret.getBytes());
        return new String(Base64.getEncoder().encode(digest.digest(byteArrayOutputStream.toByteArray())));
    }

    private TokenTimeProvider.TokenTime fixedTokenTime(long value) {
        TokenTimeProvider.TokenTime result = new TokenTimeProvider.TokenTime();
        result.setTime(value);
        return result;
    }

    private ClientId getClientId() {
        ClientId clientId = new ClientId();
        clientId.setValue(secret.getClients().keySet().iterator().next());
        return clientId;
    }

    private String getSecret() {
        return secret.getClients().values().iterator().next();
    }

}
