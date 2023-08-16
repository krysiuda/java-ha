package net.siuda.houseautomata.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.HashSet;

@ConfigurationProperties(prefix = "secret")
@Configuration
@Getter
@Setter
public class Secret {

    private String salt = "salty";

    private HashMap<String, String> clients = new HashMap<>();
    private HashSet<String> keys = new HashSet<>();

}
