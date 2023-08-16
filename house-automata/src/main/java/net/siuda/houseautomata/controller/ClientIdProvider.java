package net.siuda.houseautomata.controller;

import jakarta.servlet.http.HttpServletRequest;
import net.siuda.houseautomata.model.auth.ClientId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

@Service
public class ClientIdProvider {

    public static final String KEY_HEADER = "API-KEY";
    public static final String TOKEN_HEADER = "API-TOKEN";

    @Bean
    @RequestScope
    public ClientId readClientId(@Autowired HttpServletRequest request) {
        String key = request.getHeader(KEY_HEADER);
        String token = request.getHeader(TOKEN_HEADER);
        var clientId = ClientId.builder();
        clientId.remoteIp(request.getRemoteAddr());
        clientId.key(key);
        clientId.token(token);
        return clientId.build();
    }

}
