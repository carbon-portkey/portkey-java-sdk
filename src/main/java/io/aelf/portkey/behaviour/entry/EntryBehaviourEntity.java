package io.aelf.portkey.behaviour.entry;


import io.aelf.portkey.behaviour.global.EntryCheckConfig;
import io.aelf.portkey.behaviour.login.LoginBehaviourEntity;
import io.aelf.portkey.behaviour.register.RegisterBehaviourEntity;
import io.aelf.portkey.internal.model.common.AccountOriginalType;
import io.aelf.portkey.internal.model.guardian.GuardianInfoDTO;
import io.aelf.portkey.internal.model.guardian.GuardianWrapper;
import io.aelf.portkey.internal.model.register.RegisterInfoDTO;
import io.aelf.portkey.network.connecter.INetworkInterface;
import io.aelf.portkey.utils.log.GLogger;
import io.aelf.response.ResultCode;
import io.aelf.utils.AElfException;
import org.apache.http.util.TextUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EntryBehaviourEntity {
    public static void attemptAccountCheck(
            EntryCheckConfig loginConfig,
            @NotNull LoginCallback loginCallback,
            @NotNull RegisterCallback registerCallback) {
        if (!checkLoginConfig(loginConfig)) {
            throw new AElfException(ResultCode.INTERNAL_ERROR, "login config error");
        }
        boolean isRegistered = true;
        RegisterInfoDTO registerInfo = null;
        // If the account is not registered, this method will throw an exception.
        try {
            registerInfo = INetworkInterface.getInstance().getRegisterInfo(loginConfig.getAccountIdentifier());
        } catch (Throwable e) {
            GLogger.e(loginConfig.getAccountIdentifier() + " is not registered, try to register it.");
            isRegistered = false;
        }
        if (isRegistered) {
            loginCallback.onLoginStep(createLoginStepEntity(registerInfo, loginConfig));
        } else {
            registerCallback.onRegisterStep(createRegisterStepEntity(registerInfo));
        }
    }

    protected static LoginBehaviourEntity createLoginStepEntity(RegisterInfoDTO registerInfoDTO, EntryCheckConfig config) throws AElfException {
        GuardianInfoDTO guardianInfoDTO = INetworkInterface.getInstance().getGuardianInfo(registerInfoDTO.getOriginChainId(), config.getAccountIdentifier());
        List<GuardianWrapper> guardianWrappers = Arrays.stream(guardianInfoDTO.getGuardianList().getGuardians())
                .map(GuardianWrapper::new)
                .collect(Collectors.toList());
        return new LoginBehaviourEntity(guardianWrappers, config.getAccountOriginalType());
    }

    protected static RegisterBehaviourEntity createRegisterStepEntity(RegisterInfoDTO registerInfoDTO) {
        return new RegisterBehaviourEntity();
    }

    private static boolean checkLoginConfig(EntryCheckConfig loginConfig) {
        return loginConfig.getAccountOriginalType() != AccountOriginalType.Phone
                && loginConfig.getAccountOriginalType() != AccountOriginalType.Apple
                && !TextUtils.isEmpty(loginConfig.getAccountIdentifier());
    }

}
