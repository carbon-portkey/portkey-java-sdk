package io.aelf.portkey.storage;


import org.junit.Before;
import org.junit.Test;

public class TestRoutine {
    private IStorageBehaviour storageHandler;

    @Before
    public void setUp() {
        String routineBucketName = "routine";
        storageHandler = new DefaultStorageHandler(routineBucketName, false);
    }

    @Test
    public void storageCheckTest() {
        String something = "something";
        storageHandler.putValue(something, something);
        assert storageHandler.headValue(something, something);
    }

}
