package net.siuda.houseautomata.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import lombok.extern.slf4j.Slf4j;
import net.siuda.houseautomata.auth.AuthService;
import net.siuda.houseautomata.model.IntMetric;
import net.siuda.houseautomata.model.IntMetricValue;
import net.siuda.houseautomata.model.Metrics;
import net.siuda.houseautomata.state.MetricsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.EnumMap;
import java.util.List;

@RestController
@RequestMapping("m")
@CrossOrigin
@Slf4j
public class MetricsController {

    @Autowired
    MetricsRepo metricsRepo;

    @Autowired
    AuthService authService;

    @PostMapping(path = "/{metric}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirements({
            @SecurityRequirement(name = DefinedSecuritySchemes.KEY),
            @SecurityRequirement(name = DefinedSecuritySchemes.TOKEN)
    })
    public ResponseEntity<IntMetric> save(@PathVariable Metrics metric,
                                          @RequestBody IntMetricValue value) {
        authService.assertAuth();
        IntMetric intMetric = new IntMetric();
        intMetric.setTimestamp(System.currentTimeMillis());
        intMetric.setValue(value.getValue());
        metricsRepo.addState(metric, intMetric);
        return ResponseEntity.ok(intMetric);
    }

    @GetMapping(path = "/{metric}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IntMetric> status(@PathVariable Metrics metric) {
        IntMetric intMetric = metricsRepo.getState(metric);
        return ResponseEntity.ok(intMetric);
    }

    @GetMapping(path = "/{metric}/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<IntMetric>> dump(@PathVariable Metrics metric) {
        List<IntMetric> intMetric = metricsRepo.getDump(metric);
        return ResponseEntity.ok(intMetric);
    }

    @GetMapping(path = "/{metric}/{from}/{to}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<IntMetric>> dump(@PathVariable Metrics metric,
                                                @Parameter(schema = @Schema(type="string" ,format = "date", example = "2020-05-10"))
                                                @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                                                @Parameter(schema = @Schema(type="string" ,format = "date", example = "2020-05-11"))
                                                @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        Long timestampFrom = from.atStartOfDay().toEpochSecond(ZoneOffset.UTC) * 1000;
        Long timestampTo = to.atStartOfDay().toEpochSecond(ZoneOffset.UTC) * 1000;
        log.debug("slicing from {} to {}", timestampFrom, timestampTo);
        List<IntMetric> intMetric = metricsRepo.getSlice(metric, timestampFrom, timestampTo);
        return ResponseEntity.ok(intMetric);
    }

    @DeleteMapping(path = "/{metric}/{from}", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirements({
            @SecurityRequirement(name = DefinedSecuritySchemes.KEY),
            @SecurityRequirement(name = DefinedSecuritySchemes.TOKEN)
    })
    public ResponseEntity<List<IntMetric>> trim(@PathVariable Metrics metric,
                                                @Parameter(schema = @Schema(type="string" ,format = "date", example = "2020-05-10"))
                                                @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from) {
        authService.assertAuth();
        Long timestampFrom = from.atStartOfDay().toEpochSecond(ZoneOffset.UTC) * 1000;
        log.debug("slicing from {}", timestampFrom);
        List<IntMetric> intMetric = metricsRepo.trim(metric, timestampFrom);
        return ResponseEntity.ok(intMetric);
    }

    @GetMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EnumMap<Metrics,IntMetric>> status() {
        EnumMap<Metrics,IntMetric> result = new EnumMap<>(Metrics.class);
        for(Metrics metric : Metrics.values()) {
            result.put(metric, metricsRepo.getState(metric));
        }
        return ResponseEntity.ok(result);
    }

}
