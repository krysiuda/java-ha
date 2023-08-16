package net.siuda.houseautomata.controller;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@OpenAPIDefinition(
        info = @Info(title = "House Automata API", description = "Controls house, premises and life")
)
@SecuritySchemes({
        @SecurityScheme(name = DefinedSecuritySchemes.KEY, in = SecuritySchemeIn.HEADER, type = SecuritySchemeType.APIKEY,
                paramName = ClientIdProvider.KEY_HEADER),
        @SecurityScheme(name = DefinedSecuritySchemes.TOKEN, in = SecuritySchemeIn.HEADER, type = SecuritySchemeType.APIKEY,
                paramName = ClientIdProvider.TOKEN_HEADER),
})
public class RootController {

    @GetMapping(path = "/", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("alive");
    }

}
