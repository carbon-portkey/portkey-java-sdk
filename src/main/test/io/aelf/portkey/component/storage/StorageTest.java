package io.aelf.portkey.component.storage;

import io.aelf.portkey.assertion.AssertChecker;
import io.aelf.portkey.storage.DefaultStorageHandler;
import io.aelf.portkey.storage.StorageProvider;
import io.aelf.portkey.storage.model.IStorageBehaviour;
import io.aelf.utils.AElfException;
import io.fastkv.FastKV;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;

public class StorageTest {

    private final String testEncryptKey = "87bfda5e9290325d1a67895c4bc9de2b";
    private IStorageBehaviour handler;

    private final String expected = "i am mock", key = "mock";


    @Before
    public void init() {
        StorageProvider.init(testEncryptKey);
        handler = StorageProvider.getHandler();
    }

    @Test
    public void putTest() {
        handler.putValue(key, expected);
        Assert.assertEquals(expected, handler.getValue(key));
    }

    @Test
    public void stringEncryptedAndNotTheSameTest() throws NoSuchFieldException, IllegalAccessException {
        DefaultStorageHandler handler1 = new DefaultStorageHandler(testEncryptKey);
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
    public void putAsyncTest(){
        handler.putValueAsync(key, expected,null);
        Assert.assertEquals(expected, handler.getValue(key));
    }

    @Test
    public void manyPutRequestPressureTest(){

    }

}
