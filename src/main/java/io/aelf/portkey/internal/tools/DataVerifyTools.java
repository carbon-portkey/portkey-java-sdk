package io.aelf.portkey.internal.tools;

import io.aelf.portkey.internal.model.common.AccountOriginalType;
import io.aelf.portkey.internal.model.common.OperationScene;
import io.aelf.portkey.utils.log.GLogger;
import io.aelf.response.ResultCode;
import io.aelf.schemas.KeyPairInfo;
import io.aelf.utils.AElfException;
import io.aelf.utils.Base58Ext;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.Sha256Hash;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.List;

import static io.aelf.portkey.internal.tools.GlobalConfig.ChainIds.*;

public class DataVerifyTools {
    private static final List<String> chainIdList =
            List.of(MAINNET_CHAIN_ID, TESTNET_CHAIN_ID, TESTNET_CHAIN_ID_ALTERNATIVE);

    /**
     * Verify the params of the register request.
     *
     * @param chainId must be one of: "AELF"、"tDVV"、"tDVW"
     * @throws AElfException if not match
     * @see GlobalConfig
     */
    @Contract(pure = true, value = "_ -> _")
    public static void verifyChainId(@NotNull String chainId) throws AElfException {
        if ((!chainIdList.contains(chainId))) {
            throw new AElfException(ResultCode.PARAM_ERROR, "invalid chainId");
        }
    }

    /**
     * Verify the type parameter, which should be between email(0) and apple(3).
     *
     * @param type the type of the account
     * @throws AElfException if not match
     * @see AccountOriginalType
     */
    @Contract(pure = true, value = "_ -> _")
    public static void verifyAccountOriginType(int type) throws AElfException {
        if (type < AccountOriginalType.Email.getValue() || type > AccountOriginalType.Apple.getValue()) {
            throw new AElfException(ResultCode.PARAM_ERROR, "invalid account origin type");
        }
    }

    /**
     * Verify the operation type, which should be between unknown(0) and setLoginAccount(7).
     *
     * @param type the type of the operation
     * @throws AElfException if not match
     * @see io.aelf.portkey.internal.model.common.OperationScene
     */
    public static void verifyOperationType(int type) throws AElfException {
        if (type < OperationScene.unknown || type > OperationScene.setLoginAccount) {
            throw new AElfException(ResultCode.PARAM_ERROR, "invalid operation type");
        }
    }

    public static boolean checkAccountOriginalType(AccountOriginalType accountOriginalType) {
        boolean isAccessible = accountOriginalType != AccountOriginalType.Phone
                && accountOriginalType != AccountOriginalType.Apple;
        if (!isAccessible) {
            GLogger.e("sorry but accountOriginalType : " + accountOriginalType.name() + " is not accessible by now, check for version update later.");
            throw new AElfException();
        }
        return true;
    }

    public static KeyPairInfo generateKeyPairInfo() {
        ECKey keyPair = new ECKey();
        String privateKey = keyPair.getPrivateKeyAsHex();
        String publicKey = keyPair.getPublicKeyAsHex();
        String address = getAddressFromPrivateKey(privateKey);
        KeyPairInfo keyPairInfo = new KeyPairInfo();
        keyPairInfo.setPrivateKey(privateKey);
        keyPairInfo.setPublicKey(publicKey);
        keyPairInfo.setAddress(address);
        return keyPairInfo;
    }

    public static String getAddressFromPrivateKey(String privateKey) {
        org.bitcoinj.core.ECKey aelfKey = org.bitcoinj.core.ECKey
                .fromPrivate(new BigInteger(privateKey, 16)).decompress();
        byte[] publicKey = aelfKey.getPubKey();
        byte[] hashTwice = Sha256Hash.hashTwice(publicKey);
        return Base58Ext.encodeChecked(hashTwice);
    }
}
