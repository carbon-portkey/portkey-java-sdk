package io.aelf.portkey.behaviour.entry;

import io.aelf.portkey.assertion.AssertChecker;
import io.aelf.portkey.behaviour.global.EntryCheckConfig;
import io.aelf.portkey.behaviour.login.LoginBehaviourEntity;
import io.aelf.portkey.behaviour.register.RegisterBehaviourEntity;
import io.aelf.portkey.internal.model.guardian.GuardianInfoDTO;
import io.aelf.portkey.internal.model.guardian.GuardianWrapper;
import io.aelf.portkey.internal.model.register.RegisterInfoDTO;
import io.aelf.portkey.internal.tools.DataVerifyTools;
import io.aelf.portkey.network.connecter.INetworkInterface;
import io.aelf.portkey.utils.log.GLogger;
import io.aelf.response.ResultCode;
import io.aelf.utils.AElfException;
import org.apache.http.util.TextUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EntryBehaviourEntity {
    public static CheckedEntry attemptAccountCheck(
            EntryCheckConfig config) {
        if (!checkLoginConfig(config)) {
            throw new AElfException(ResultCode.INTERNAL_ERROR, "login config error.");
        }
        boolean isRegistered = true;
        RegisterInfoDTO registerInfo = null;
        // If the account is not registered, getRegisterInfo() will throw an exception.
        try {
            registerInfo = INetworkInterface.getInstance().getRegisterInfo(config.getAccountIdentifier());
        } catch (Throwable e) {
            GLogger.t(config.getAccountIdentifier() + " is not registered, try to register it.");
            isRegistered = false;
        }
        return new CheckedEntry(isRegistered, config, registerInfo);
    }

    protected static LoginBehaviourEntity createLoginStepEntity(RegisterInfoDTO registerInfoDTO, EntryCheckConfig config) throws AElfException {
        GuardianInfoDTO guardianInfoDTO = INetworkInterface.getInstance().getGuardianInfo(registerInfoDTO.getOriginChainId(), config.getAccountIdentifier());
        List<GuardianWrapper> guardianWrappers = Arrays.stream(guardianInfoDTO.getGuardianList().getGuardians())
                .map(GuardianWrapper::new)
                .collect(Collectors.toList());
        return new LoginBehaviourEntity(guardianWrappers, config);
    }

    protected static RegisterBehaviourEntity createRegisterStepEntity(EntryCheckConfig config) {
        return new RegisterBehaviourEntity(config);
    }

    private static boolean checkLoginConfig(EntryCheckConfig loginConfig) {
        return DataVerifyTools.checkAccountOriginalType(loginConfig.getAccountOriginalType())
                && !TextUtils.isEmpty(loginConfig.getAccountIdentifier());
    }

    @FunctionalInterface
    public interface LogInChain {
        void onLoginStep(LoginCallback callback) throws AElfException;
    }

    @FunctionalInterface
    public interface RegisterChain {
        void onRegisterStep(RegisterCallback callback) throws AElfException;
    }

    public static class CheckedEntry implements LogInChain, RegisterChain {
        private final boolean isRegistered;
        private final EntryCheckConfig config;
        private final RegisterInfoDTO registerInfo;

        public CheckedEntry(boolean isRegistered, @NotNull EntryCheckConfig config, @Nullable RegisterInfoDTO registerInfo) {
            this.isRegistered = isRegistered;
            this.config = config;
            this.registerInfo = registerInfo;
        }

        public boolean isRegistered() {
            return isRegistered;
        }

        public LogInChain asLogInChain() {
            assert registerInfo != null && isRegistered;
            return this;
        }

        public RegisterChain asRegisterChain() {
            assert registerInfo == null && !isRegistered;
            return this;
        }

        @Override
        public void onLoginStep(LoginCallback callback) {
            AssertChecker.assertTrue(isRegistered, "isRegistered is false, better check it using isRegistered() first.");
            assert registerInfo != null;
            callback.onLoginStep(createLoginStepEntity(registerInfo, config));
        }

        @Override
        public void onRegisterStep(RegisterCallback callback) {
            AssertChecker.assertTrue(!isRegistered, "isRegistered is true, better check it using isRegistered() first.");
            assert registerInfo == null;
            callback.onRegisterStep(createRegisterStepEntity(config));
        }

    }

}
