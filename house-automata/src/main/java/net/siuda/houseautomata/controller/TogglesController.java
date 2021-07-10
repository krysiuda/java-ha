package net.siuda.houseautomata.controller;

import net.siuda.houseautomata.model.*;
import net.siuda.houseautomata.state.AtomicToggle;
import net.siuda.houseautomata.state.TogglesRepo;
import net.siuda.houseautomata.token.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("t")
@CrossOrigin
public class TogglesController {

    @Autowired
    private TogglesRepo togglesState;

    @Autowired
    TokenService tokenService;

    @Autowired
    private ClientId clientId;

    @PutMapping(path = "/{toggle}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Toggle> toggle(@PathVariable Toggles toggle,
                                         @RequestHeader(value = "api-token", required = false) String token,
                                         @RequestBody ToggleValue value) {
        tokenService.verifyToken(new Token(token), clientId);
        AtomicToggle toggleState = togglesState.getState(toggle);
        Toggle oldValue = toggleState.getValue();
        if(!oldValue.getValue().equals(value.getValue())) {
            Toggle newValue = Toggle.wrap(value);
            newValue.setTimestamp(System.currentTimeMillis());
            toggleState.putValue(newValue);
        }
        return ResponseEntity.ok(oldValue);
    }

    @GetMapping(path = "/{toggle}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Toggle> toggle(@PathVariable Toggles toggle) {
        AtomicToggle toggleState = togglesState.getState(toggle);
        return ResponseEntity.ok(toggleState.getValue());
    }

    @GetMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HashMap<Toggles, Toggle>> status() {
        HashMap<Toggles, Toggle> result = new HashMap<>();
        for(Toggles toggle : Toggles.values()) {
            AtomicToggle toggleState = togglesState.getState(toggle);
            result.put(toggle, toggleState.getValue());
        }
        return ResponseEntity.ok(result);
    }

}
