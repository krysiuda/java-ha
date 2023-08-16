package net.siuda.houseautomata.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.File;

@ConfigurationProperties(prefix = "storage")
@Configuration
@Getter
@Setter
public class Storage {

    private File metrics;
    private File metrics2nd;

}
