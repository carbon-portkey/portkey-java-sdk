package io.aelf.portkey.behaviour.pin;

import io.aelf.internal.sdkv2.AElfClientV2;
import io.aelf.portkey.TestParams;
import io.aelf.portkey.assertion.AssertChecker;
import io.aelf.portkey.internal.model.wallet.WalletBuildConfig;
import io.aelf.portkey.internal.tools.GlobalConfig;
import io.aelf.portkey.network.retrofit.RetrofitProvider;
import io.aelf.portkey.storage.StorageProvider;
import io.aelf.schemas.KeyPairInfo;
import org.junit.Before;
import org.junit.Test;

public class PinBehaviourTest {
    private final String mockPin = "123456";

    private KeyPairInfo keyPairInfo;

    @Before
    public void setUp() {
        StorageProvider.init();
        RetrofitProvider.resetOrInitMainRetrofit(TestParams.TEST_PORTKEY_API_HOST);
        GlobalConfig.setTestEnv(true);
        keyPairInfo = new AElfClientV2(TestParams.TEST_AELF_NODE_HOST).generateKeyPairInfo();
        PinManager.lock(
                mockPin,
                new WalletBuildConfig()
                        .setSessionId("mock")
                        .setPrivKey(keyPairInfo.getPrivateKey())
        );
    }

    @Test
    public void unLockTest() {
        AssertChecker.assertTrue(PinManager.checkIfSessionExists(), "session not exists");
        WalletBuildConfig config = PinManager.unlock(mockPin);
        assert config.getPrivKey().equals(keyPairInfo.getPrivateKey());
        assert config.getSessionId().equals("mock");
    }
}
