package de.openended.cloudurlwatcher;

import org.junit.After;
import org.junit.Before;

import com.google.apphosting.api.ApiProxy;

public class BaseTestFixture {

    private final boolean cleanEnvironment = true;

    @Before
    public void setUp() {
        // if (cleanEnvironment) {
        // ApiProxyLocalImpl proxy = new ApiProxyLocalImpl(new File(".")) {};
        // proxy.setProperty(LocalDatastoreService.NO_STORAGE_PROPERTY,
        // Boolean.TRUE.toString());
        // ApiProxy.setDelegate(proxy);
        // ApiProxy.setEnvironmentForCurrentThread(new
        // GoogleAppEngineTestEnvironment());
        // }
    }

    @After
    public void tearDown() {
        if (cleanEnvironment) {
            ApiProxy.clearEnvironmentForCurrentThread();
        }
    }
}
