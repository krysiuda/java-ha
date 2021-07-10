package net.siuda.houseautomata.controller;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@OpenAPIDefinition(
        servers = {
                @Server(url = "/", description = "general cross-origin use")
        },
        info = @Info(title = "House Automata API", description = "Controls house, premises and life")
)
public class RootController {

    @GetMapping(path = "/", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> token() {
        return ResponseEntity.ok("alive");
    }

}
