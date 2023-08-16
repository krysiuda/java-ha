package net.siuda.houseautomata.controller;

import com.jayway.jsonpath.JsonPath;
import net.siuda.houseautomata.config.Secret;
import net.siuda.houseautomata.model.Toggles;
import net.siuda.houseautomata.state.TogglesRepo;
import net.siuda.houseautomata.test.AuthMixin;
import net.siuda.houseautomata.test.NoStorageConfiguration;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static net.siuda.houseautomata.test.AuthMixin.TEST_KEY_PROPERTY;
import static net.siuda.houseautomata.test.AuthMixin.TEST_PASSWORD_PROPERTY;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest(properties = { TEST_PASSWORD_PROPERTY, TEST_KEY_PROPERTY })
@ExtendWith(SpringExtension.class)
@Import({NoStorageConfiguration.class, Secret.class})
public class TogglesControllerTest implements AuthMixin {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TogglesRepo togglesRepo;

    @BeforeEach
    public void reset() {
        Arrays.stream(Toggles.values())
                .forEach(toggles -> {
                    var toggle = togglesRepo.getState(toggles);
                    toggle.getValue().setTimestamp(0L);
                    toggle.getValue().setValue(Boolean.FALSE);
                });
    }

    @Test
    public void listToggles() throws Exception {
        mockMvc
                .perform(get("/t/"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                            "SW1":{
                                "value":false,
                                "timestamp":0,
                                "localDateTime":"1970-01-01T00:00:00"
                            },
                            "SW4":{
                                "value":false,
                                "timestamp":0,
                                "localDateTime":"1970-01-01T00:00:00"
                            },
                            "GATE":{
                                "value":false,
                                "timestamp":0,
                                "localDateTime":"1970-01-01T00:00:00"
                            },
                            "SW2":{
                                "value":false,
                                "timestamp":0,
                                "localDateTime":"1970-01-01T00:00:00"
                            },
                            "SW3":{
                                "value":false,
                                "timestamp":0,
                                "localDateTime":"1970-01-01T00:00:00"
                            }
                        }
                        """));
    }

    @Test
    public void getToggle() throws Exception {
        mockMvc
                .perform(get("/t/SW1"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                            "value":false,
                            "timestamp":0,
                            "localDateTime":"1970-01-01T00:00:00"
                        }
                        """));
    }

    @Test
    public void forbidden() throws Exception {
        mockMvc
                .perform(put("/t/SW1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("""
                        { "value":false }
                        """))
                .andExpect(status().isForbidden());
    }

    @Test
    public void toggleWithKey() throws Exception {
        mockMvc
                .perform(put("/t/SW1")
                        .header(ClientIdProvider.KEY_HEADER, KEY_USED_IN_TEST)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("""
                        { "value":true }
                        """))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                            "value":false,
                            "timestamp":0,
                            "localDateTime":"1970-01-01T00:00:00"
                        }
                        """));
    }

    @Test
    public void toggle() throws Exception {
        mockMvc
                .perform(put("/t/SW1")
                        .header("api-token", signToken(mockMvc))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("""
                        { "value":true }
                        """))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                            "value":false,
                            "timestamp":0,
                            "localDateTime":"1970-01-01T00:00:00"
                        }
                        """));
        var getResult = mockMvc
                .perform(get("/t/SW1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("value").value("true"))
                .andReturn();
        var putResult = mockMvc
                .perform(put("/t/SW1")
                        .header("api-token", signToken(mockMvc))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("""
                        { "value":false }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("value").value("true"))
                .andReturn();
        var getTimestamp = JsonPath.read(getResult.getResponse().getContentAsString(), "timestamp");
        var putTimestamp = JsonPath.read(putResult.getResponse().getContentAsString(), "timestamp");
        Assertions.assertThat(putTimestamp).isEqualTo(getTimestamp);
        var getDate = JsonPath.read(getResult.getResponse().getContentAsString(), "localDateTime");
        var putDate = JsonPath.read(putResult.getResponse().getContentAsString(), "localDateTime");
        Assertions.assertThat(putDate).isEqualTo(getDate);
    }

}
