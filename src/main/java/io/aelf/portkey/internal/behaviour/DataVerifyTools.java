package io.aelf.portkey.internal.behaviour;

import io.aelf.portkey.internal.model.common.AccountOriginalType;
import io.aelf.portkey.internal.model.common.OperationScene;
import io.aelf.response.ResultCode;
import io.aelf.utils.AElfException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static io.aelf.portkey.internal.behaviour.GlobalConfig.ChainIds.*;

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
    public static void verifyChainIdParams(@NotNull String chainId) throws AElfException {
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
        if (type < AccountOriginalType.email || type > AccountOriginalType.apple) {
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
}
