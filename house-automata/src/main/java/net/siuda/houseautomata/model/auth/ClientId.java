package net.siuda.houseautomata.model.auth;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ClientId {

    private String remoteIp;

    private String token;

    private String key;

}
