package io.aelf.portkey.behaviour.pin;

import io.aelf.portkey.behaviour.login.LoginBehaviourEntity;
import io.aelf.portkey.behaviour.register.RegisterBehaviourEntity;
import io.aelf.portkey.behaviour.wallet.PortkeyWallet;
import io.aelf.portkey.behaviour.wallet.WalletInitObserver;
import io.aelf.portkey.internal.model.wallet.WalletBuildConfig;
import io.aelf.response.ResultCode;
import io.aelf.utils.AElfException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SetPinBehaviourEntity {

    private final LoginBehaviourEntity login;
    private final RegisterBehaviourEntity register;

    public SetPinBehaviourEntity(
            @NotNull LoginBehaviourEntity login
    ) {
        this(login, null);
    }

    public SetPinBehaviourEntity(
            @NotNull RegisterBehaviourEntity register
    ) {
        this(null, register);
    }

    protected SetPinBehaviourEntity(
            @Nullable LoginBehaviourEntity login,
            @Nullable RegisterBehaviourEntity register
    ) {
        if (login != null && register != null) {
            throw new AElfException(
                    ResultCode.PARAM_ERROR,
                    "login and register can not be both not null" +
                            " --- you can not login and register at the same time, check it."
            );
        }
        this.login = login;
        this.register = register;
    }

    public boolean isValidPin(@NotNull String pinValue) {
        return PinManager.isValidPin(pinValue);
    }

    public @Nullable PortkeyWallet lockAndGetWallet(@NotNull String pinValue, @NotNull WalletInitObserver observer) throws AElfException {
        if (!PinManager.isValidPin(pinValue)) {
            return null;
        }
        WalletBuildConfig config = getConfig();
        if (config == null) {
            return null;
        }
        PinManager.lock(pinValue, config);
        return new PortkeyWallet(config, observer);
    }

    protected WalletBuildConfig getConfig() throws AElfException {
        if (login != null) {
            return login.buildConfig();
        } else if (register != null) {
            return register.buildConfig();
        } else {
            throw new AElfException(ResultCode.INTERNAL_ERROR, "login and register can not be both null");
        }
    }
}
