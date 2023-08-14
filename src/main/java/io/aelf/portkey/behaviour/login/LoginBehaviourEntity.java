package io.aelf.portkey.behaviour.login;

import io.aelf.portkey.assertion.AssertChecker;
import io.aelf.portkey.behaviour.global.GuardianObserver;
import io.aelf.portkey.behaviour.guardian.GuardianBehaviourEntity;
import io.aelf.portkey.internal.model.common.AccountOriginalType;
import io.aelf.portkey.internal.model.common.OperationScene;
import io.aelf.portkey.internal.model.guardian.GuardianDTO;
import io.aelf.portkey.internal.model.guardian.GuardianWrapper;
import io.aelf.portkey.utils.log.GLogger;
import io.aelf.response.ResultCode;
import io.aelf.utils.AElfException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class LoginBehaviourEntity implements GuardianObserver {
    private final List<GuardianWrapper> guardians;
    private final int guardianVerifyLimit;
    private final AccountOriginalType accountOriginalType;

    public LoginBehaviourEntity(@NotNull List<GuardianWrapper> guardians) {
        this(guardians, AccountOriginalType.Email);
    }

    public LoginBehaviourEntity(@NotNull List<GuardianWrapper> guardians, AccountOriginalType accountOriginalType) {
        this.guardians = guardians;
        this.guardianVerifyLimit = getGuardianVerifyLimit(guardians);
        this.accountOriginalType = accountOriginalType;
    }

    /**
     * the guardian's verify rule:
     * <p>
     * if length <= 3: verifyLimit = length
     * <p>
     * if length > 3: verifyLimit = Math.floor(guardians.size() * 0.6) + 1
     */
    @Contract(pure = true)
    public static int getGuardianVerifyLimit(@NotNull List<GuardianWrapper> guardians) {
        int length = guardians.size();
        if (length <= 3) return length;
        return (int) (guardians.size() * 0.6 + 1);
    }

    public List<GuardianWrapper> getGuardians() {
        return guardians;
    }

    public GuardianBehaviourEntity getGuardianBehaviourEntity(int position) {
        AssertChecker.assertTrue(position >= 0 && position < guardians.size(), "position out of range");
        return getGuardianBehaviourEntity(guardians.get(position).getOriginalData());
    }

    public synchronized GuardianBehaviourEntity getGuardianBehaviourEntity(@NotNull GuardianDTO guardianDTO) {
        return new GuardianBehaviourEntity(guardianDTO, OperationScene.communityRecovery, this, accountOriginalType);
    }

    public synchronized Optional<GuardianBehaviourEntity> nextWaitingGuardian() {
        if (isFulfilled()) {
            GLogger.t("you have reached the verify limit, there's no need to call nextWaitingGuardian() now.");
            return Optional.empty();
        }
        GuardianWrapper wrapper = guardians.stream()
                .filter(guardian -> !guardian.isVerified())
                .findFirst()
                .orElse(null);
        if (wrapper == null) {
            throw new AElfException(ResultCode.INTERNAL_ERROR, "No waiting guardian found");
        }
        return Optional.ofNullable(getGuardianBehaviourEntity(wrapper.getOriginalData()));
    }

    protected synchronized void setGuardianVerified(GuardianWrapper guardianWrapper) {
        guardians.stream()
                .filter(guardian -> guardian.getOriginalData().equals(guardianWrapper.getOriginalData()))
                .findFirst()
                .ifPresent(guardian -> guardian.setVerifiedData(guardianWrapper.getVerifiedData()));
    }

    public int getGuardianVerifyLimit() {
        return guardianVerifyLimit;
    }

    public synchronized int getFullFilledGuardianCount() {
        return (int) guardians.stream().filter(GuardianWrapper::isVerified).count();
    }

    public boolean isFulfilled() {
        return getFullFilledGuardianCount() >= guardianVerifyLimit;
    }

    @Override
    public void informGuardianReady(GuardianWrapper wrapper) {
        setGuardianVerified(wrapper);
    }
}
