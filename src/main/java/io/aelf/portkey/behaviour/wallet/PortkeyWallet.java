package io.aelf.portkey.behaviour.wallet;

import io.aelf.internal.sdkv2.AElfClientV2;
import io.aelf.portkey.internal.model.wallet.WalletBuildConfig;
import io.aelf.schemas.KeyPairInfo;
import io.aelf.utils.Base58Ext;
import io.aelf.utils.ByteArrayHelper;
import org.bitcoinj.core.Sha256Hash;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.math.BigInteger;

public class PortkeyWallet {
    private final AElfClientV2 chainInstance;

    private final KeyPairInfo keyPairInfo;
    private final String sessionId;

    public PortkeyWallet(@NotNull WalletBuildConfig config) {
        this.chainInstance = new AElfClientV2(config.getAElfEndpoint());
        this.sessionId = config.getSessionId();
        this.keyPairInfo = getKeyPairInfo(config.getPrivKey());
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

    public AElfClientV2 getChainInstance() {
        return chainInstance;
    }


    public KeyPairInfo getKeyPairInfo() {
        return keyPairInfo;
    }

}
