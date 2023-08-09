package io.aelf.portkey.storage;

import io.aelf.portkey.assertion.AssertChecker;
import io.aelf.utils.AElfException;
import io.fastkv.FastKV;
import org.apache.http.util.TextUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedDeque;

@SuppressWarnings({"BusyWait", "FieldCanBeLocal"})
public class StorageTest {

    private final String testEncryptKey = "87bfda5e9290325d1a67895c4bc9de2b";
    private final String testEncryptBucketName = "portkey-test";
    private final String testNonEncryptBucketName = "portkey-test2";
    private final String expected = "i-am-mock", key = "mock";
    private IStorageBehaviour useEncryptHandler, nonEncryptHandler;

    @Before
    public void init() {
        useEncryptHandler = new DefaultStorageHandler(testEncryptKey, testEncryptBucketName);
        nonEncryptHandler = new DefaultStorageHandler(testNonEncryptBucketName, false);
    }

    @Test
    public void putTest() {
        useEncryptHandler.putValue(key, expected);
        Assert.assertEquals(expected, useEncryptHandler.getValue(key));
    }

    @Test
    public void stringEncryptedAreNotTheSameTest() throws NoSuchFieldException, IllegalAccessException {
        Class<DefaultStorageHandler> clazz = DefaultStorageHandler.class;
        Field field = clazz.getDeclaredField("kvProvider");
        field.setAccessible(true);
        FastKV kv = (FastKV) field.get(useEncryptHandler);
        useEncryptHandler.putValue(key, expected);
        AssertChecker.assertNotEquals(expected, kv.getString(key));
    }

    @Test
    public void stringNonEncryptedAreSameTest() throws NoSuchFieldException, IllegalAccessException {
        Class<DefaultStorageHandler> clazz = DefaultStorageHandler.class;
        Field field = clazz.getDeclaredField("kvProvider");
        field.setAccessible(true);
        FastKV kv = (FastKV) field.get(nonEncryptHandler);
        nonEncryptHandler.putValue(key, expected);
        Assert.assertEquals(expected, kv.getString(key));
    }

    @Test(expected = AElfException.class)
    public void useWrongKeyTest() {
        new DefaultStorageHandler("123");
    }

    @Test
    public void headTest() {
        this.putTest();
        Assert.assertTrue(useEncryptHandler.headValue(key, expected));
    }

    @Test
    public void putAsyncTest() {
        useEncryptHandler.putValueAsync(key, expected, status -> {
            Assert.assertEquals(Boolean.TRUE, status.result);
            Assert.assertEquals(expected, useEncryptHandler.getValue(key));
        });
    }

    @Test
    public void manyPutRequestsPressureTest() throws InterruptedException {
        int limit = 1000, step = 100;
        final long start = System.currentTimeMillis();
        final Queue<String> queue = new ConcurrentLinkedDeque<>();
        for (int i = 0; i < step; i++) {
            String key = "test" + i, value = String.valueOf(i);
            useEncryptHandler.putValueAsync(key, value, success -> queue.offer(value));
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
        useEncryptHandler.putValue(anotherKey, anotherValue);
        useEncryptHandler.removeValue(anotherKey);
        Assert.assertFalse(useEncryptHandler.contains(anotherKey));
        Assert.assertTrue(TextUtils.isEmpty(useEncryptHandler.getValue(anotherKey)));
    }

    @Test
    public void getUndefinedKeyTest() {
        String undefined = "undefined";
        Assert.assertTrue(TextUtils.isEmpty(useEncryptHandler.getValue(undefined)));
        Assert.assertFalse(useEncryptHandler.contains(undefined));
    }

    @Test
    public void exportKeyNotTheSameTest() {
        Set<String> set = new HashSet<>();
        for (int i = 0; i < 100; i++) {
            String key = StorageProvider.exportNewEncryptKey();
            Assert.assertFalse(set.contains(key));
            set.add(key);
        }
    }

    @Test(expected = AElfException.class)
    public void provideNotTheSameKeyTest() {
        putTest();
        String anotherKey = StorageProvider.exportNewEncryptKey();
        new DefaultStorageHandler(anotherKey, testEncryptBucketName);
    }

    @Test(expected = AElfException.class)
    public void useEncryptAndThenNotUseEncryptTest() {
        putTest();
        new DefaultStorageHandler(testEncryptBucketName, false);
    }

    @Test(expected = AElfException.class)
    public void useNonEncryptAndThenUseEncryptTest() {
        putTest();
        new DefaultStorageHandler(testNonEncryptBucketName, true);
    }

    @Test
    public void clearTest() {
        putTest();
        useEncryptHandler.clear();
        nonEncryptHandler.clear();
        Assert.assertFalse(useEncryptHandler.contains(key));
    }

}
