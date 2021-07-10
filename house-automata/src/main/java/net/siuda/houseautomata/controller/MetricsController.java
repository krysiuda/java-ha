package net.siuda.houseautomata.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import net.siuda.houseautomata.model.*;
import net.siuda.houseautomata.state.MetricsRepo;
import net.siuda.houseautomata.token.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class MetricsController {

    private static final Logger LOG = LoggerFactory.getLogger(MetricsController.class);

    @Autowired
    MetricsRepo metricsRepo;

    @Autowired
    TokenService tokenService;

    @Autowired
    private ClientId clientId;

    @PostMapping(path = "/{metric}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IntMetric> save(@PathVariable Metrics metric,
                                          @RequestHeader(value = "api-token", required = false) String token,
                                          @RequestBody IntMetricValue value) {
        tokenService.verifyToken(new Token(token), clientId);
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
        LOG.debug("slicing from {} to {}", timestampFrom, timestampTo);
        List<IntMetric> intMetric = metricsRepo.getSlice(metric, timestampFrom, timestampTo);
        return ResponseEntity.ok(intMetric);
    }

    @DeleteMapping(path = "/{metric}/{from}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<IntMetric>> trim(@PathVariable Metrics metric,
                                                @RequestHeader(value = "api-token", required = false) String token,
                                                @Parameter(schema = @Schema(type="string" ,format = "date", example = "2020-05-10"))
                                                @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from) {
        tokenService.verifyToken(new Token(token), clientId);
        Long timestampFrom = from.atStartOfDay().toEpochSecond(ZoneOffset.UTC) * 1000;
        LOG.debug("slicing from {}", timestampFrom);
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
