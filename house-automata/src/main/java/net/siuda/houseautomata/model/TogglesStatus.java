package net.siuda.houseautomata.model;

import java.util.EnumMap;

public class TogglesStatus {

    private EnumMap<Toggles, Toggle> status = new EnumMap<Toggles, Toggle>(Toggles.class);

    public EnumMap<Toggles, Toggle> getStatus() {
        return status;
    }

    public void setStatus(EnumMap<Toggles, Toggle> status) {
        this.status = status;
    }

}
