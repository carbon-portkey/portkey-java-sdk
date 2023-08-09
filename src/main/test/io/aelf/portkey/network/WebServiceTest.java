package io.aelf.portkey.network;

import io.aelf.portkey.TestParams;
import io.aelf.portkey.assertion.AssertChecker;
import io.aelf.portkey.internal.model.common.CountryCodeInfoDTO;
import io.aelf.portkey.internal.model.common.OperationScene;
import io.aelf.portkey.internal.model.verify.SendVerificationCodeParams;
import io.aelf.portkey.internal.tools.GlobalConfig;
import io.aelf.portkey.network.connecter.INetworkInterface;
import io.aelf.portkey.network.retrofit.RetrofitProvider;
import io.aelf.portkey.network.slice.account.AccountOperationAPISlice;
import io.aelf.utils.AElfException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class WebServiceTest {
    private AccountOperationAPISlice networkService;

    @Before
    public void init() {
        RetrofitProvider.resetOrInitMainRetrofit(TestParams.TEST_PORTKEY_API_HOST);
        networkService = INetworkInterface.getInstance();
        GlobalConfig.setTestEnv(true);
    }

    @Test
    public void captchaTest() throws AElfException {
        AssertChecker.assertNotThrow(() -> networkService.checkGoogleRecaptcha(OperationScene.register));
    }

    @Ignore
    @Test
    public void getVerificationCodeTest() {
        SendVerificationCodeParams params = new SendVerificationCodeParams()
                .setOperationType(OperationScene.register)
                .setChainId(TestParams.TEST_CHAIN_ID);
    }

    @Test
    public void getPhoneCountryCodeTest() throws AElfException {
        CountryCodeInfoDTO countryCodeItem = networkService.getPhoneCountryCode();
        AssertChecker.assertNotNull(countryCodeItem);
        AssertChecker.assertNotEmptyList(countryCodeItem.getData());
        AssertChecker.assertNotNull(countryCodeItem.getLocateData());
    }


}
