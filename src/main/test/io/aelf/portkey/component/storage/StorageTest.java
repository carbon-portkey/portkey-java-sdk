package io.aelf.portkey.component.storage;

import io.aelf.portkey.assertion.AssertChecker;
import io.aelf.portkey.storage.DefaultStorageHandler;
import io.aelf.portkey.storage.model.IStorageBehaviour;
import io.aelf.utils.AElfException;
import io.fastkv.FastKV;
import org.apache.http.util.TextUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

@SuppressWarnings("BusyWait")
public class StorageTest {

    private final String testEncryptKey = "87bfda5e9290325d1a67895c4bc9de2b";
    private final String testBucketName = "portkey-test";
    private IStorageBehaviour handler;

    private final String expected = "i-am-mock", key = "mock";

    @Before
    public void init() {
        handler = new DefaultStorageHandler(testEncryptKey, testBucketName);
    }

    @Test
    public void putTest() {
        handler.putValue(key, expected);
        Assert.assertEquals(expected, handler.getValue(key));
    }

    @Test
    public void stringEncryptedAndNotTheSameTest() throws NoSuchFieldException, IllegalAccessException {
        DefaultStorageHandler handler1 = new DefaultStorageHandler(testEncryptKey, testBucketName);
        Class<DefaultStorageHandler> clazz = DefaultStorageHandler.class;
        Field field = clazz.getDeclaredField("kvProvider");
        field.setAccessible(true);
        FastKV kv = (FastKV) field.get(handler1);
        handler1.putValue(key, expected);
        AssertChecker.assertNotEquals(expected, kv.getString(key));
    }

    @Test(expected = AElfException.class)
    public void useWrongKeyTest() {
        new DefaultStorageHandler("123");
    }

    @Test
    public void headTest() {
        Assert.assertTrue(handler.headValue(key, expected));
    }

    @Test
    public void putAsyncTest() {
        handler.putValueAsync(key, expected, status -> {
            Assert.assertEquals(Boolean.TRUE, status.result);
            Assert.assertEquals(expected, handler.getValue(key));
        });
    }

    @Test
    public void manyPutRequestsPressureTest() throws InterruptedException {
        int limit = 1000, step = 100;
        final long start = System.currentTimeMillis();
        Queue<String> queue = new ConcurrentLinkedDeque<>();
        for (int i = 0; i < step; i++) {
            String key = "test" + i, value = String.valueOf(i);
            handler.putValueAsync(key, value, success -> queue.offer(value));
        }
        while (System.currentTimeMillis() - start < limit) {
            Thread.sleep(1);
            if (queue.size() == step)
                return;
        }
        if (queue.size() != step)
            throw new RuntimeException();
    }

    @Test
    public void removeTest() {
        String anotherKey = "anotherKey", anotherValue = "anotherValue";
        handler.putValue(anotherKey, anotherValue);
        handler.removeValue(anotherKey);
        Assert.assertFalse(handler.contains(anotherKey));
        Assert.assertTrue(TextUtils.isEmpty(handler.getValue(anotherKey)));
    }

    @Test
    public void getUndefinedKeyTest() {
        String undefined = "undefined";
        Assert.assertTrue(TextUtils.isEmpty(handler.getValue(undefined)));
        Assert.assertFalse(handler.contains(undefined));
    }

}
