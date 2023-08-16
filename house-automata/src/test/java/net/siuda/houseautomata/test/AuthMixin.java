package net.siuda.houseautomata.test;

import org.springframework.test.web.servlet.MockMvc;

import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;
import java.util.Base64;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public interface AuthMixin {

    String PASSWORD_USED_IN_TEST = "nopass";
    String KEY_USED_IN_TEST = "nopass";

    String TEST_PASSWORD_PROPERTY = "secret.clients.127.0.0.1=" + PASSWORD_USED_IN_TEST;
    String TEST_KEY_PROPERTY = "secret.keys=" + KEY_USED_IN_TEST;

    default String signToken(MockMvc mockMvc) throws Exception {
        var token = queryForToken(mockMvc);
        byte [] tokenBytes = Base64.getDecoder().decode(token);
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.writeBytes(tokenBytes);
        byteArrayOutputStream.writeBytes(PASSWORD_USED_IN_TEST.getBytes());
        byte [] tokenReplyBytes = digest.digest(byteArrayOutputStream.toByteArray());
        return Base64.getEncoder().encodeToString(tokenReplyBytes);
    }

    default String queryForToken(MockMvc mockMvc) throws Exception {
        return mockMvc
                .perform(get("/token/"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

}
