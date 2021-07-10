package net.siuda.houseautomata.model;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class IntMetric extends IntMetricValue {

    private Long timestamp = 0L;

    public static IntMetric wrap(IntMetricValue value) {
        IntMetric metric = new IntMetric();
        metric.setValue(value.getValue());
        return metric;
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
