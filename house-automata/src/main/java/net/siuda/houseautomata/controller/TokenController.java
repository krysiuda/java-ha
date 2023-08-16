package net.siuda.houseautomata.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import net.siuda.houseautomata.auth.TokenService;
import net.siuda.houseautomata.model.Token;
import net.siuda.houseautomata.model.auth.ClientId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("token")
@CrossOrigin
public class TokenController {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private ClientId clientId;

    @GetMapping(path = "/", produces = MediaType.TEXT_PLAIN_VALUE)
    @SecurityRequirements()
    public ResponseEntity<String> token() {
        Token token = tokenService.createToken(clientId);
        return ResponseEntity.ok(token.getToken());
    }

    @PostMapping(path = "/", consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.ALL_VALUE)
    @SecurityRequirements(@SecurityRequirement(name = DefinedSecuritySchemes.TOKEN))
    public ResponseEntity<Void> verify(@RequestBody String token) {
        tokenService.verifyToken(new Token(token), clientId);
        return ResponseEntity.ok().build();
    }

}
