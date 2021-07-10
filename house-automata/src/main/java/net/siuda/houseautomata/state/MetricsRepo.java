package net.siuda.houseautomata.state;

import net.siuda.houseautomata.model.IntMetric;
import net.siuda.houseautomata.model.Metrics;
import org.springframework.stereotype.Repository;

import java.util.EnumMap;
import java.util.List;

@Repository
public class MetricsRepo {

    private EnumMap<Metrics, AtomicMetricList> state = new EnumMap<>(Metrics.class);

    {
        for(Metrics metrics : Metrics.values()) {
            state.put(metrics, new AtomicMetricList());
        }
    }

    public IntMetric getState(Metrics metrics) {
        return state.get(metrics).first();
    }

    public List<IntMetric> getSlice(Metrics metrics, Long from, Long to) {
        return state.get(metrics).slice(from, to);
    }

    public List<IntMetric> getDump(Metrics metrics) {
        return state.get(metrics).dump();
    }

    public List<IntMetric> trim(Metrics metrics, Long from) {
        return state.get(metrics).split(from);
    }

    public void addState(Metrics metrics, IntMetric value) {
        state.get(metrics).add(value);
    }

}
