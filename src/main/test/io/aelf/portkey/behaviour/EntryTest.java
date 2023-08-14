package io.aelf.portkey.behaviour;

import io.aelf.portkey.TestParams;
import io.aelf.portkey.behaviour.entry.EntryBehaviourEntity;
import io.aelf.portkey.behaviour.global.EntryCheckConfig;
import io.aelf.portkey.internal.model.common.AccountOriginalType;
import io.aelf.portkey.network.retrofit.RetrofitProvider;
import io.aelf.utils.AElfException;
import org.junit.Before;
import org.junit.Test;

public class EntryTest {
    protected final String TEST_IDENTIFIER = "carbon.lv@aelf.io";

    @Before
    public void setUp() {
        RetrofitProvider.resetOrInitMainRetrofit(TestParams.TEST_PORTKEY_API_HOST);
    }

    @Test
    public void loginWithNoRecaptchaTest() {
        EntryCheckConfig loginConfig = new EntryCheckConfig()
                .setAccountOriginalType(AccountOriginalType.Email)
                .setAccountIdentifier(TEST_IDENTIFIER);
        EntryBehaviourEntity.CheckedEntry checkedEntry = EntryBehaviourEntity.attemptAccountCheck(loginConfig);
        if (checkedEntry.isRegistered()) {
            checkedEntry.asLogInChain().onLoginStep(
                    loginEntry -> {
                        assert !loginEntry.getGuardians().isEmpty();
                        assert loginEntry.nextWaitingGuardian().isPresent();
                        assert !loginEntry.nextWaitingGuardian().get().isVerified();
                    }
            );
        } else {
            checkedEntry.asRegisterChain().onRegisterStep(
                    registerEntry -> {
                        throw new RuntimeException();
                    }
            );
        }
    }

    @Test(expected = AssertionError.class)
    public void registerWhenRegisteredTest() {
        EntryCheckConfig loginConfig = new EntryCheckConfig()
                .setAccountOriginalType(AccountOriginalType.Email)
                .setAccountIdentifier(TEST_IDENTIFIER);
        EntryBehaviourEntity.CheckedEntry checkedEntry = EntryBehaviourEntity.attemptAccountCheck(loginConfig);
        assert checkedEntry.isRegistered();
        checkedEntry.asRegisterChain().onRegisterStep(
                registerEntry -> {
                    throw new RuntimeException();
                }
        );
    }

    @Test(expected = AssertionError.class)
    public void LoginWhenNotRegisteredTest() {
        EntryCheckConfig loginConfig = new EntryCheckConfig()
                .setAccountOriginalType(AccountOriginalType.Email)
                .setAccountIdentifier(TEST_IDENTIFIER.concat("a"));
        EntryBehaviourEntity.CheckedEntry checkedEntry = EntryBehaviourEntity.attemptAccountCheck(loginConfig);
        assert checkedEntry.isRegistered();
        checkedEntry.asLogInChain().onLoginStep(
                loginBehaviourEntity -> {
                    throw new RuntimeException();
                }
        );
    }




}
