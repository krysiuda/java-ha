package net.siuda.houseautomata.controller;

import net.siuda.houseautomata.model.ClientId;
import net.siuda.houseautomata.model.Token;
import net.siuda.houseautomata.token.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("token")
@CrossOrigin
public class TokenController {

    private static final Logger LOG = LoggerFactory.getLogger(TokenController.class);

    @Autowired
    private TokenService tokenService;

    @Autowired
    private ClientId clientId;

    @GetMapping(path = "/", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> token() {
        Token token = tokenService.createToken(clientId);
        return ResponseEntity.ok(token.getToken());
    }

    @PostMapping(path = "/", consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.ALL_VALUE)
    public ResponseEntity<Void> verify(@RequestBody String token) {
        tokenService.verifyToken(new Token(token), clientId);
        return ResponseEntity.ok().build();
    }

}
