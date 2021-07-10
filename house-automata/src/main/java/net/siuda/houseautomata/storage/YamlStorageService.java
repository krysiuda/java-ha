package net.siuda.houseautomata.storage;

import net.siuda.houseautomata.config.Storage;
import net.siuda.houseautomata.model.IntMetric;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.composer.Composer;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Service
public class YamlStorageService {

    private static final Logger LOG = LoggerFactory.getLogger(YamlStorageService.class);

    @Autowired
    Storage storage;

    @Autowired
    SnapshotService snapshotService;

    private Yaml makeYaml() {
        Constructor constructor = new Constructor(MetricsSnapshot.class);
        Yaml yaml = new Yaml(constructor);
        yaml.addTypeDescription(new TypeDescription(MetricsSnapshot.class, Tag.MAP));
        return yaml;
    }

    public void load() {
        Yaml yaml = makeYaml();
        Path path = storage.getMetrics().toPath();
        Path path2nd = storage.getMetrics2nd().toPath();
        if(Files.exists(path2nd)) {
            LOG.warn("Backup state found, recovering");
            try {
                Reader reader = Files.newBufferedReader(path2nd);
                MetricsSnapshot metricsSnapshot = yaml.load(reader);
                snapshotService.loadMetricsSnapshot(metricsSnapshot);
            } catch (IOException e) {
                LOG.error("Could not load state backup", e);
                throw new StorageError(StorageError.Action.LOAD_ERROR, e);
            }
            LOG.info("Cleaning up backup");
            try {
                Files.delete(path2nd);
            } catch (IOException e) {
                LOG.error("Could not remove state backup", e);
                throw new StorageError(StorageError.Action.LOAD_ERROR, e);
            }
            LOG.info("Backup state recovery completed");
        } else if(Files.exists(path)) {
            LOG.debug("Clean startup state load");
            try {
                Reader reader = Files.newBufferedReader(path);
                MetricsSnapshot metricsSnapshot = yaml.load(reader);
                snapshotService.loadMetricsSnapshot(metricsSnapshot);
            } catch (IOException e) {
                LOG.error("Could not load state", e);
                throw new StorageError(StorageError.Action.LOAD_ERROR, e);
            }
            LOG.info("Clean startup state load completed");
        } else {
            LOG.warn("No existing state");
        }
    }

    public void save() {
        Path path = storage.getMetrics().toPath();
        Path path2nd = storage.getMetrics2nd().toPath();
        try {
            LOG.debug("Storing backup state");
            Writer writer = Files.newBufferedWriter(path2nd);
            MetricsSnapshot metricsSnapshot = snapshotService.createMetricsSnapshot();
            Yaml yaml = makeYaml();
            yaml.dump(metricsSnapshot, writer);
            writer.close();
            LOG.debug("Committing state save");
            Files.move(path2nd, path, StandardCopyOption.ATOMIC_MOVE);
            LOG.info("State saved");
        } catch (IOException e) {
            LOG.error("Could not save state", e);
            throw new StorageError(StorageError.Action.SAVE_ERROR, e);
        }
    }

}
