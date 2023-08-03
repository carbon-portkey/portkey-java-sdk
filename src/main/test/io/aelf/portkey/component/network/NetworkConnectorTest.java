package io.aelf.portkey.component.network;

import io.aelf.portkey.component.TestParams;
import io.aelf.portkey.internal.model.common.OperationScene;
import io.aelf.portkey.network.connecter.NetworkService;
import io.aelf.portkey.network.retrofit.RetrofitProvider;
import org.junit.Before;
import org.junit.Test;

public class NetworkConnectorTest {
    private NetworkService networkService;

    @Before
    public void init() {
        RetrofitProvider.resetOrInitRetrofit(TestParams.TEST_PORTKEY_API_HOST);
        networkService = new NetworkService();
    }

    @Test
    public void captchaTest() {
        networkService.checkGoogleRecaptcha(OperationScene.register);
    }
}
