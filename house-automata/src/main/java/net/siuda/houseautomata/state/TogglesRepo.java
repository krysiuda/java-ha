package net.siuda.houseautomata.state;

import net.siuda.houseautomata.model.Toggles;
import org.springframework.stereotype.Repository;

import java.util.EnumMap;

@Repository
public class TogglesRepo {

    private EnumMap<Toggles, AtomicToggle> state = new EnumMap<>(Toggles.class);

    {
        for(Toggles toggles : Toggles.values()) {
            state.put(toggles, new AtomicToggle());
        }
    }

    public AtomicToggle getState(Toggles toggle) {
        return state.get(toggle);
    }

}
