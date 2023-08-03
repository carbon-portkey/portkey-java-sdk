package io.aelf.portkey.component.network;

import com.google.gson.JsonElement;
import io.aelf.portkey.assertion.AssertChecker;
import io.aelf.portkey.component.TestParams;
import io.aelf.portkey.network.retrofit.RetrofitProvider;
import org.junit.Before;
import org.junit.Test;
import retrofit2.Call;
import retrofit2.http.GET;

import java.io.IOException;
import java.lang.reflect.Field;

interface TestService {
    @GET("/api/blockChain/chainStatus")
    Call<JsonElement> networkStatus();
}

public class RetrofitTest {

    @Before
    public void init() {
        RetrofitProvider.resetOrInitRetrofit(TestParams.TEST_AELF_NODE_HOST);
    }

    @Test
    public void RetrofitInitTest() {
        AssertChecker.assertNotNull(RetrofitProvider.getAPIService(TestService.class), null);
    }

    @Test(expected = RuntimeException.class)
    public void RetrofitFailInitTest() {
        RetrofitProvider.resetOrInitRetrofit("");
    }

    @Test
    public void RetrofitGetTest() throws IOException {
        AssertChecker.assertNotNull(
                RetrofitProvider.getAPIService(TestService.class)
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
                RetrofitProvider.getAPIService(TestService.class)
                        .networkStatus()
                        .execute()
                        .body(),
                null);
    }
}
