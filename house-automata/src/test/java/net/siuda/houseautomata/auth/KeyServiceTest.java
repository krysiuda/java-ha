package net.siuda.houseautomata.auth;

import net.siuda.houseautomata.config.Secret;
import net.siuda.houseautomata.model.auth.ClientId;
import net.siuda.houseautomata.test.NoStorageConfiguration;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest(properties = "secret.keys=key1")
@ExtendWith(SpringExtension.class)
@Import({NoStorageConfiguration.class, Secret.class})
public class KeyServiceTest {

    @Autowired
    private KeyService keyService;

    @Autowired
    private Secret secret;

    @Test
    public void verify() throws Exception {
        var keyClientId = ClientId.builder();
        keyClientId.key("key1");
        keyService.verify(keyClientId.build());
    }

    @Test
    public void failOnUnexisting() throws Exception {
        var keyClientId = ClientId.builder();
        keyClientId.key("key123");
        Assertions.assertThatExceptionOfType(AuthError.class).isThrownBy(() -> {
                keyService.verify(keyClientId.build());
        }).withMessage("KEY_VERIFICATION_ERROR");
    }

}
