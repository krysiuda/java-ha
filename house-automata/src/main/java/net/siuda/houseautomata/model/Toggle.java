package net.siuda.houseautomata.model;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class Toggle extends ToggleValue {

    private Long timestamp = 0L;

    public static Toggle wrap(ToggleValue value) {
        Toggle toggle = new Toggle();
        toggle.setValue(value.getValue());
        return toggle;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public LocalDateTime getLocalDateTime() {
        return LocalDateTime.ofEpochSecond(timestamp / 1000, 0, ZoneOffset.UTC);
    }

}
