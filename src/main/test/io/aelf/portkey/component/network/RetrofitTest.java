package io.aelf.portkey.component.network;

import io.aelf.portkey.assertion.AssertChecker;
import io.aelf.portkey.component.TestParams;
import io.aelf.portkey.network.retrofit.RetrofitProvider;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Field;

public class RetrofitTest {

    @Before
    public void init() {
        RetrofitProvider.resetOrInitRetrofit(TestParams.TEST_AELF_NODE_HOST);
    }

    @Test
    public void RetrofitInitTest() {
        AssertChecker.assertNotNull(RetrofitProvider.getAPIService(ITestService.class), null);
    }

    @Test(expected = RuntimeException.class)
    public void RetrofitFailInitTest() {
        RetrofitProvider.resetOrInitRetrofit("");
    }

    @Test
    public void RetrofitGetTest() throws IOException {
        AssertChecker.assertNotNull(
                RetrofitProvider.getAPIService(ITestService.class)
                        .networkStatus()
                        .execute()
                        .body(),
                null);
    }

    @Test(expected = RuntimeException.class)
    public void RetrofitFailGetTest() throws NoSuchFieldException, IllegalAccessException, IOException {
        Class<RetrofitProvider> clazz = RetrofitProvider.class;
        Field retrofit = clazz.getDeclaredField("retrofit");
        retrofit.setAccessible(true);
        retrofit.set(null, null);
        AssertChecker.assertNotNull(
                RetrofitProvider.getAPIService(ITestService.class)
                        .networkStatus()
                        .execute()
                        .body(),
                null);
    }
}
