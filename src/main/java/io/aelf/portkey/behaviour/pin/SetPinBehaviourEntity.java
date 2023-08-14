package io.aelf.portkey.behaviour.pin;

import io.aelf.portkey.behaviour.wallet.PortkeyWallet;
import io.aelf.portkey.internal.model.wallet.WalletBuildConfig;
import org.jetbrains.annotations.NotNull;

public class SetPinBehaviourEntity {
    private final WalletBuildConfig config;

    public SetPinBehaviourEntity(WalletBuildConfig config) {
        this.config = config;
    }

    public boolean isValidPin(@NotNull String pinValue) {
        return PinManager.isValidPin(pinValue);
    }

    public PortkeyWallet lockAndGetWallet(@NotNull String pinValue) {
        PinManager.lock(pinValue, config);
        return new PortkeyWallet(config);
    }
}
