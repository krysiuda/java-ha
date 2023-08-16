package net.siuda.houseautomata.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicBoolean;

@Service
@Slf4j
public class StorageLifecycle {

    @Autowired
    YamlStorageService storageService;

    private AtomicBoolean failed = new AtomicBoolean(true);

    @EventListener(ContextClosedEvent.class)
    public void shutdown() {
        if(!failed.getAndSet(true)) {
            log.info("Saving state before shutdown");
            storageService.save();
        } else {
            log.info("State not saved due to startup failure");
        }
    }

    @EventListener(ApplicationStartedEvent.class)
    public void startup() {
        log.info("Restoring state after startup");
        storageService.load();
        failed.set(false);
    }

    @Scheduled(fixedDelayString = "${storage.interval}", initialDelayString = "${storage.delay}")
    public void timer() {
        if(!failed.get()) {
            log.info("Saving state");
            storageService.save();
        }
    }
}
