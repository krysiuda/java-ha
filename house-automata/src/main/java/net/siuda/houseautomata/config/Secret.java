package net.siuda.houseautomata.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

@ConfigurationProperties(prefix = "secret")
@Configuration
public class Secret {

    private String salt = "salty";

    private HashMap<String, String> clients;// = new HashMap<>();

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public HashMap<String, String> getClients() {
        return clients;
    }

    public void setClients(HashMap<String, String> clients) {
        this.clients = clients;
    }

}
