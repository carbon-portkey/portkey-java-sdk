package io.aelf.portkey.behaviour.pin;

import io.aelf.portkey.internal.model.wallet.WalletBuildConfig;

public interface IAfterVerifiedBehaviour {
    SetPinBehaviourEntity afterVerified();
    WalletBuildConfig buildConfig();
}
