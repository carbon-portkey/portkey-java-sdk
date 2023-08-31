package io.aelf.portkey.behaviour.guardian;

import io.aelf.portkey.behaviour.global.GuardianObserver;
import io.aelf.portkey.behaviour.guardian.state.IGuardianState;
import io.aelf.portkey.behaviour.guardian.state.InitGuardianState;
import io.aelf.portkey.internal.model.common.AccountOriginalType;
import io.aelf.portkey.internal.model.guardian.GuardianDTO;
import io.aelf.portkey.network.connecter.INetworkInterface;
import io.aelf.portkey.utils.log.GLogger;
import io.aelf.response.ResultCode;
import io.aelf.utils.AElfException;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

public class GuardianBehaviourEntity extends GuardianStub implements IGuardianState {
    public GuardianBehaviourEntity(
            @NotNull GuardianDTO guardian,
            int operationType,
            @NotNull GuardianObserver observer,
            AccountOriginalType accountOriginalType,
            boolean isAlreadyVerified
    ) {
        super(observer, operationType, guardian, accountOriginalType, isAlreadyVerified);
    }

    public boolean checkForReCaptcha() {
        return INetworkInterface.getInstance().checkGoogleRecaptcha(this.getOperationType());
    }

    @Override
    public boolean sendVerificationCode() throws AElfException {
        if (checkForReCaptcha()) {
            throw new AElfException(
                    ResultCode.INTERNAL_ERROR,
                    "Need reCaptcha but not set, better check it with checkForReCaptcha() first."
            );
        }
        return state.sendVerificationCode();
    }

    @Override
    public boolean sendVerificationCode(@NonNull String recaptchaToken) throws AElfException {
        if (!checkForReCaptcha()) {
            GLogger.w("No need reCaptcha but set, better check it with checkForReCaptcha() first.");
        }
        return state.sendVerificationCode(recaptchaToken);
    }

    @Override
    public boolean verifyVerificationCode(String code) throws AElfException {
        return state.verifyVerificationCode(code);
    }

    @Override
    public boolean isVerified() {
        return state.isVerified() || isAlreadyVerified;
    }

    @Override
    protected @NotNull IGuardianState getInitialState() {
        return new InitGuardianState(this);
    }

    @Override
    public Stage getStage() {
        return state.getStage();
    }
}
