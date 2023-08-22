package io.aelf.portkey.behaviour.pin;

import io.aelf.portkey.behaviour.wallet.PortkeyWallet;
import io.aelf.portkey.internal.model.wallet.WalletBuildConfig;
import io.aelf.portkey.internal.tools.GlobalConfig;
import io.aelf.utils.AElfException;
import org.jetbrains.annotations.NotNull;

public class WalletUnlockEntity {
    public boolean isValidPinValue(@NotNull String pinValue) {
        return pinValue.length() >= GlobalConfig.PinConfig.MIN_LENGTH
                && pinValue.length() <= GlobalConfig.PinConfig.MAX_LENGTH
                && pinValue.matches(GlobalConfig.PinConfig.REGEX);
    }

    public boolean checkPin(@NotNull String pinValue) {
        return PinManager.checkIfSessionExists() && PinManager.headPin(pinValue);
    }

    public PortkeyWallet unlockAndBuildWallet(@NotNull String pinValue) throws AElfException {
        if (!isValidPinValue(pinValue) || !checkPin(pinValue)) {
            throw new IllegalArgumentException("Invalid pin value.");
        }
        WalletBuildConfig config = PinManager.unlock(pinValue);
        return new PortkeyWallet(config);
    }
}
