package net.siuda.houseautomata.controller;

import net.siuda.houseautomata.model.ClientId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import javax.servlet.http.HttpServletRequest;

@Service
public class ClientIdProvider {

    @Bean @RequestScope
    public ClientId map(@Autowired HttpServletRequest request) {
        ClientId clientId = new ClientId();
        clientId.setValue(request.getRemoteAddr());
        return clientId;
    }

}
