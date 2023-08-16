package net.siuda.houseautomata.test;

import net.siuda.houseautomata.storage.StorageLifecycle;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NoStorageConfiguration {

    @MockBean
    private StorageLifecycle storageLifecycle;

}
