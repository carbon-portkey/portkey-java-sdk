package io.aelf.portkey.behaviour;

import io.aelf.portkey.TestParams;
import io.aelf.portkey.assertion.AssertChecker;
import io.aelf.portkey.behaviour.entry.EntryBehaviourEntity;
import io.aelf.portkey.behaviour.global.EntryCheckConfig;
import io.aelf.portkey.behaviour.guardian.state.IGuardianState;
import io.aelf.portkey.internal.model.common.AccountOriginalType;
import io.aelf.portkey.network.retrofit.RetrofitProvider;
import io.aelf.utils.AElfException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class EntryTest {
    protected final String TEST_IDENTIFIER = "carbon.lv@aelf.io";

    @Before
    public void setUp() {
        RetrofitProvider.resetOrInitMainRetrofit(TestParams.TEST_PORTKEY_API_HOST);
    }

    @Test(expected = AElfException.class)
    public void loginWithNoRecaptchaTest() {
        EntryCheckConfig loginConfig = new EntryCheckConfig()
                .setAccountOriginalType(AccountOriginalType.Email)
                .setAccountIdentifier(TEST_IDENTIFIER);
        EntryBehaviourEntity.attemptAccountCheck(loginConfig, (loginEntity) -> {
            AssertChecker.assertNotNull(loginEntity);
            assert loginEntity.getGuardians().size() == 1;
            loginEntity.nextWaitingGuardian().ifPresent(
                    guardian -> {
                        assert !guardian.isVerified();
                        assert guardian.checkForReCaptcha();
                        // This will throw an exception.
                        // Since when google recaptcha config is open, sending such request without recaptcha will fail.
                        guardian.sendVerificationCode();
                        assert guardian.getStage() == IGuardianState.Stage.SENT;
                    }
            );
        }, (registerEntity) -> Assert.fail());
    }


}
