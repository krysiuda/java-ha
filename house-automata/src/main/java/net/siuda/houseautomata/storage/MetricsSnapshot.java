package net.siuda.houseautomata.storage;

import net.siuda.houseautomata.model.IntMetric;
import net.siuda.houseautomata.model.Metrics;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class MetricsSnapshot {

    private HashMap<Metrics, MetricList> metrics = new HashMap<>();

    public HashMap<Metrics, MetricList> getMetrics() {
        return metrics;
    }

    public void setMetrics(HashMap<Metrics, MetricList> metrics) {
        this.metrics = metrics;
    }

    public static class MetricList {
        private List<IntMetric> values = new LinkedList<>();

        public MetricList() {
        }

        public MetricList(List<IntMetric> values) {
            this.values = values;
        }

        public List<IntMetric> getValues() {
            return values;
        }

        public void setValues(List<IntMetric> values) {
            this.values = values;
        }
    }
}
