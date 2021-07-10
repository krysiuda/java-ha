package net.siuda.houseautomata.state;

import net.siuda.houseautomata.model.Toggle;

import java.util.concurrent.atomic.AtomicReference;

public class AtomicToggle {

    private AtomicReference<Toggle> value = new AtomicReference<>(new Toggle());

    public Toggle getValue() {
        return value.get();
    }

    public Toggle putValue(Toggle value) {
        return this.value.getAndSet(value);
    }

}
