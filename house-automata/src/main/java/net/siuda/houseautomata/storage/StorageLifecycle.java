package net.siuda.houseautomata.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class StorageLifecycle {

    private static final Logger LOG = LoggerFactory.getLogger(StorageLifecycle.class);

    @Autowired
    YamlStorageService storageService;

    private AtomicBoolean failed = new AtomicBoolean(true);

    @EventListener(ContextClosedEvent.class)
    public void shutdown() {
        if(!failed.getAndSet(true)) {
            LOG.info("Saving state before shutdown");
            storageService.save();
        } else {
            LOG.info("State not saved due to startup failure");
        }
    }

    @EventListener(ApplicationStartedEvent.class)
    public void startup() {
        LOG.info("Restoring state after startup");
        storageService.load();
        failed.set(false);
    }

    @Scheduled(fixedDelayString = "${storage.interval}", initialDelayString = "${storage.delay}")
    public void timer() {
        if(!failed.get()) {
            LOG.info("Saving state");
            storageService.save();
        }
    }
}
