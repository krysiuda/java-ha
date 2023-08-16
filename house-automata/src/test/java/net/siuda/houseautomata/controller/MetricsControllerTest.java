package net.siuda.houseautomata.controller;

import net.siuda.houseautomata.config.Secret;
import net.siuda.houseautomata.model.Metrics;
import net.siuda.houseautomata.state.MetricsRepo;
import net.siuda.houseautomata.test.AuthMixin;
import net.siuda.houseautomata.test.NoStorageConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static net.siuda.houseautomata.test.AuthMixin.TEST_KEY_PROPERTY;
import static net.siuda.houseautomata.test.AuthMixin.TEST_PASSWORD_PROPERTY;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(properties = { TEST_PASSWORD_PROPERTY, TEST_KEY_PROPERTY })
@ExtendWith(SpringExtension.class)
@Import({NoStorageConfiguration.class, Secret.class})
public class MetricsControllerTest implements AuthMixin {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MetricsRepo metricsRepo;

    @BeforeEach
    public void reset() {
        Arrays.stream(Metrics.values())
                .forEach(metric -> {
                    metricsRepo.trim(metric, 0L);
                });
    }

    @Test
    public void listToggles() throws Exception {
        mockMvc
                .perform(get("/m/"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                            "TEMP":null,
                            "HUM":null,
                            "ATM":null,
                            "DUST":null
                        }
                        """));
    }

}
