package io.aelf.portkey.component.network;

import com.google.gson.JsonElement;
import io.aelf.portkey.assertion.AssertChecker;
import io.aelf.portkey.network.RetrofitProvider;
import org.junit.Before;
import org.junit.Test;
import retrofit2.http.GET;
import retrofit2.http.Query;

import java.lang.reflect.Field;

interface TestService {
    @GET("anything")
    JsonElement getGreet(@Query("name") String name);
}

public class RetrofitTest {
    @Before
    public void init() {
        RetrofitProvider.resetOrInitRetrofit("http://www.example.com");
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
    public void RetrofitGetTest() {
        AssertChecker.assertNotNull(RetrofitProvider.getAPIService(TestService.class).getGreet("test"), null);
    }

    @Test(expected = RuntimeException.class)
    public void RetrofitFailGetTest() throws NoSuchFieldException, IllegalAccessException {
        Class<RetrofitProvider> clazz = RetrofitProvider.class;
        Field retrofit = clazz.getDeclaredField("retrofit");
        retrofit.setAccessible(true);
        retrofit.set(null, null);
        AssertChecker.assertNotNull(RetrofitProvider.getAPIService(TestService.class).getGreet("test"), null);
    }
}
