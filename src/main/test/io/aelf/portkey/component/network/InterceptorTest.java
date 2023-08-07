package io.aelf.portkey.component.network;

import io.aelf.portkey.component.TestParams;
import io.aelf.portkey.internal.behaviour.GlobalConfig;
import io.aelf.portkey.network.retrofit.RetrofitProvider;
import okhttp3.Response;
import org.junit.Before;
import org.junit.Test;
import retrofit2.Call;

import java.io.IOException;
import java.util.Objects;

public class InterceptorTest {

    private ITestService api;

    @Before
    public void init() {
        RetrofitProvider.resetOrInitRetrofit(TestParams.TEST_PORTKEY_API_HOST);
        GlobalConfig.setTestEnv(true);
        api = RetrofitProvider.getAPIService(ITestService.class);
    }

    @Test
    public void testHeaderReplacement() throws IOException {
        Call<?> call = api.getPhoneCountryCode();
        Response response = call.execute().raw();
        assert Objects.equals(response.request().headers().get("Content-Type"), "application/json");
    }

    @Test
    public void testHeaderReplacement2() throws IOException {
        String formHeader = "application/x-www-form-urlencoded";
        Call<?> call = api.getPhoneCountryCode(formHeader);
        Response response = call.execute().raw();
        assert Objects.equals(response.request().headers().get("Content-Type"), formHeader);
    }

    @Test
    public void testPostQueryBodyRebuildProtect() throws IOException {
        Call<?> call = api.checkGoogleRecaptcha("test");
        Response response = call.execute().raw();
        assert Objects.requireNonNull(response.request().body()).contentLength() > 0;
    }

    @Test
    public void testPostQueryBodyRebuildProtect2() throws IOException {
        Call<?> call = api.getPhoneCountryCode(1);
        Response response = call.execute().raw();
        assert response.request().body() == null || response.request().body().contentLength() == 0;
    }

}
