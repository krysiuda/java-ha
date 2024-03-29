package net.siuda.houseautomata.controller;

import net.siuda.houseautomata.config.Secret;
import net.siuda.houseautomata.test.AuthMixin;
import net.siuda.houseautomata.test.NoStorageConfiguration;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static net.siuda.houseautomata.test.AuthMixin.TEST_PASSWORD_PROPERTY;

@AutoConfigureMockMvc
@SpringBootTest(properties = TEST_PASSWORD_PROPERTY)
@ExtendWith(SpringExtension.class)
@Import({NoStorageConfiguration.class, Secret.class})
public class TokenControllerTest implements AuthMixin {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void generateToken() throws Exception {
        Assertions.assertThat(queryForToken(mockMvc)).isNotEmpty();
    }

    @Test
    public void verifyToken() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.post("/token/")
                        .contentType(MediaType.TEXT_PLAIN_VALUE)
                        .content(signToken(mockMvc)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

}
