package net.siuda.houseautomata.storage;

import net.siuda.houseautomata.model.IntMetric;
import net.siuda.houseautomata.model.Metrics;
import net.siuda.houseautomata.state.MetricsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class SnapshotService {

    @Autowired
    MetricsRepo metricsRepo;

    public MetricsSnapshot createMetricsSnapshot() {
        MetricsSnapshot metricsSnapshot = new MetricsSnapshot();
        for(Metrics metric : Metrics.values()) {
            List<IntMetric> reversed = metricsRepo.getDump(metric);
            Collections.reverse(reversed);
            metricsSnapshot.getMetrics().put(metric, new MetricsSnapshot.MetricList(reversed));
        }
        return metricsSnapshot;
    }

    public void loadMetricsSnapshot(MetricsSnapshot metricsSnapshot) {
        if(metricsSnapshot != null) {
            for (Metrics metric : metricsSnapshot.getMetrics().keySet()) {
                for (IntMetric value : metricsSnapshot.getMetrics().get(metric).getValues()) {
                    metricsRepo.addState(metric, value);
                }
            }
        }
    }

}
