package io.aelf.portkey.behaviour.wallet;

import io.aelf.portkey.internal.model.wallet.CAInfo;
import io.aelf.portkey.internal.model.wallet.WalletBuildConfig;
import io.aelf.schemas.KeyPairInfo;
import io.aelf.utils.Base58Ext;
import io.aelf.utils.ByteArrayHelper;
import org.apache.http.util.TextUtils;
import org.bitcoinj.core.Sha256Hash;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.math.BigInteger;

import static io.aelf.portkey.behaviour.wallet.WalletAbilitiesAddOnKt.initWallet;

public class PortkeyWallet {
    private final KeyPairInfo keyPairInfo;
    private final String sessionId;
    private final boolean fromRegister;
    private String originalChainId;
    private CAInfo caInfo;
    private volatile @NotNull WalletStage walletStage = WalletStage.INIT;
    private final @NotNull WalletInitObserver walletInitObserver;

    public PortkeyWallet(@NotNull WalletBuildConfig config, @NotNull WalletInitObserver walletInitObserver) {
        this.sessionId = config.getSessionId();
        this.keyPairInfo = getKeyPairInfo(config.getPrivKey());
        this.fromRegister = config.isFromRegister();
        this.originalChainId = config.getOriginalChainId();
        this.walletInitObserver = walletInitObserver;
        AElfHolder.INSTANCE.triggerUpdate();
        initWallet(this);
    }

    public boolean isAvailable() {
        return caInfo != null
                && !TextUtils.isEmpty(this.caInfo.getCaAddress())
                && !TextUtils.isEmpty(this.caInfo.getCaHash())
                && this.walletStage == WalletStage.READY;
    }

    public void disableWallet() {
        this.walletStage = WalletStage.FAILED;
    }

    protected static String getPublicKeyFromPrivKey(String privateKey) {
        org.bitcoinj.core.ECKey aelfKey = org.bitcoinj.core.ECKey
                .fromPrivate(new BigInteger(privateKey, 16)).decompress();
        byte[] publicKey = aelfKey.getPubKey();
        return org.bitcoinj.core.Utils.HEX.encode(publicKey);
    }

    protected static String getAddressFromPubKey(@Nonnull String pubKey) {
        byte[] publicKey = ByteArrayHelper.hexToByteArray(pubKey);
        byte[] hashTwice = Sha256Hash.hashTwice(publicKey);
        return Base58Ext.encodeChecked(hashTwice);
    }

    protected KeyPairInfo getKeyPairInfo(String privateKey) {
        KeyPairInfo keyPairInfo = new KeyPairInfo();
        keyPairInfo.setPrivateKey(privateKey);
        keyPairInfo.setPublicKey(getPublicKeyFromPrivKey(privateKey));
        keyPairInfo.setAddress(getAddressFromPubKey(keyPairInfo.getPublicKey()));
        return keyPairInfo;
    }

    public KeyPairInfo getKeyPairInfo() {
        return keyPairInfo;
    }

    public String getSessionId() {
        return sessionId;
    }

    public boolean isFromRegister() {
        return fromRegister;
    }

    public CAInfo getCaInfo() {
        return caInfo;
    }

    protected PortkeyWallet setCaInfo(CAInfo caInfo) {
        this.caInfo = caInfo;
        return this;
    }

    protected void setWalletStage(@NotNull WalletStage walletStage) {
        this.walletStage = walletStage;
    }

    public @NotNull WalletStage getWalletStage() {
        return walletStage;
    }

    protected @NotNull WalletInitObserver getWalletInitObserver() {
        return walletInitObserver;
    }

    public String getOriginalChainId() {
        return originalChainId;
    }

    public PortkeyWallet setOriginalChainId(String originalChainId) {
        this.originalChainId = originalChainId;
        return this;
    }
}
