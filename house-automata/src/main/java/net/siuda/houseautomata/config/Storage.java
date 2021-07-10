package net.siuda.houseautomata.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.util.HashMap;

@ConfigurationProperties(prefix = "storage")
@Configuration
public class Storage {

    private File metrics;
    private File metrics2nd;

    public File getMetrics() {
        return metrics;
    }

    public void setMetrics(File metrics) {
        this.metrics = metrics;
    }

    public File getMetrics2nd() {
        return metrics2nd;
    }

    public void setMetrics2nd(File metrics2nd) {
        this.metrics2nd = metrics2nd;
    }
}
