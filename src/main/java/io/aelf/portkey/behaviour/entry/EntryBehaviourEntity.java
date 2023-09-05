package io.aelf.portkey.behaviour.entry;

import io.aelf.portkey.assertion.AssertChecker;
import io.aelf.portkey.behaviour.global.EntryCheckConfig;
import io.aelf.portkey.behaviour.login.LoginBehaviourEntity;
import io.aelf.portkey.behaviour.pin.PinManager;
import io.aelf.portkey.behaviour.pin.WalletUnlockEntity;
import io.aelf.portkey.behaviour.register.RegisterBehaviourEntity;
import io.aelf.portkey.internal.model.common.AccountOriginalType;
import io.aelf.portkey.internal.model.google.GoogleAccount;
import io.aelf.portkey.internal.model.guardian.GuardianInfoDTO;
import io.aelf.portkey.internal.model.guardian.GuardianWrapper;
import io.aelf.portkey.internal.model.register.RegisterInfoDTO;
import io.aelf.portkey.internal.tools.DataVerifyTools;
import io.aelf.portkey.network.connecter.INetworkInterface;
import io.aelf.response.ResultCode;
import io.aelf.utils.AElfException;
import org.apache.http.util.TextUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class EntryBehaviourEntity {
    public static boolean ifLockedWalletExists() {
        return PinManager.checkIfSessionExists();
    }

    public static Optional<WalletUnlockEntity> attemptToGetLockedWallet() {
        return Optional.ofNullable(ifLockedWalletExists() ? new WalletUnlockEntity() : null);
    }

    public static synchronized CheckedEntry attemptAccountCheck(
            EntryCheckConfig config
    ) {
        return attemptAccountCheck(config, null);
    }

    public static synchronized CheckedEntry attemptAccountCheck(
            EntryCheckConfig config,
            @Nullable GoogleAccount givenGoogleAccount
    ) throws AElfException {
        if (!checkLoginConfig(config)) {
            throw new AElfException(ResultCode.INTERNAL_ERROR, "login config error.");
        }
        boolean isRegistered = true;
        RegisterInfoDTO registerInfo = INetworkInterface
                .getInstance()
                .getRegisterInfo(config.getAccountIdentifier());
        if (registerInfo == null) {
            throw new AElfException(ResultCode.INTERNAL_ERROR, "register info with null means internal error.");
        }
        if (registerInfo.isErrCodeMatchNotRegistered() && TextUtils.isEmpty(registerInfo.getOriginChainId())) {
            isRegistered = false;
            registerInfo = null;
        } else {
            // if there's no guardian for this account, it is not registered either.
            try {
                GuardianInfoDTO guardianInfoDTO = INetworkInterface
                        .getInstance()
                        .getGuardianInfo(
                                registerInfo.getOriginChainId(),
                                config.getAccountIdentifier()
                        );
                if (guardianInfoDTO == null)
                    throw new AElfException(ResultCode.INTERNAL_ERROR, "guardian info with null means internal error.");
            } catch (Exception e) {
                isRegistered = false;
                registerInfo = null;
            }
        }
        return new CheckedEntry(isRegistered, config, registerInfo, givenGoogleAccount);
    }

    public static CheckedEntry googleAccountCheck(GoogleAccount googleAccount) {
        return attemptAccountCheck(
                new EntryCheckConfig()
                        .setAccountOriginalType(AccountOriginalType.Google)
                        .setAccountIdentifier(googleAccount.getId()
                        ),
                googleAccount
        );
    }

    protected static LoginBehaviourEntity createLoginStepEntity(
            RegisterInfoDTO registerInfoDTO,
            EntryCheckConfig config,
            @Nullable GoogleAccount googleAccount
    ) throws AElfException {
        GuardianInfoDTO guardianInfoDTO = INetworkInterface.getInstance().getGuardianInfo(registerInfoDTO.getOriginChainId(), config.getAccountIdentifier());
        List<GuardianWrapper> guardianWrappers = Arrays.stream(guardianInfoDTO.getGuardianList().getGuardians())
                .map(it -> new GuardianWrapper(it, googleAccount))
                .collect(Collectors.toList());
        return new LoginBehaviourEntity(guardianWrappers, config, googleAccount);
    }

    protected static RegisterBehaviourEntity createRegisterStepEntity(
            EntryCheckConfig config,
            @Nullable GoogleAccount googleAccount
    ) {
        return new RegisterBehaviourEntity(config, googleAccount);
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
        private final GoogleAccount googleAccount;

        public CheckedEntry(
                boolean isRegistered,
                @NotNull EntryCheckConfig config,
                @Nullable RegisterInfoDTO registerInfo,
                @Nullable GoogleAccount googleAccount
        ) {
            this.isRegistered = isRegistered;
            this.config = config;
            this.registerInfo = registerInfo;
            this.googleAccount = googleAccount;
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
            callback.onLoginStep(createLoginStepEntity(registerInfo, config, googleAccount));
        }

        @Override
        public void onRegisterStep(RegisterCallback callback) {
            AssertChecker.assertTrue(!isRegistered, "isRegistered is true, better check it using isRegistered() first.");
            assert registerInfo == null;
            callback.onRegisterStep(createRegisterStepEntity(config, googleAccount));
        }

    }

}
