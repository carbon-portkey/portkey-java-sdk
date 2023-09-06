package io.aelf.portkey.behaviour.wallet

import io.aelf.internal.sdkv2.AElfClientV2
import io.aelf.portkey.internal.model.common.ChainInfoDTO
import io.aelf.portkey.internal.tools.GlobalConfig
import io.aelf.portkey.network.connecter.INetworkInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

internal object AElfHolder {
    private val aelfMap = mutableMapOf<AElfChainType, AElfClientV2>()

    @Synchronized
    @JvmName("triggerUpdate")
    internal fun triggerUpdate() {
        CoroutineScope(Dispatchers.IO).launch {
            val info = try {
                INetworkInterface.getInstance().globalChainInfo
            } catch (ignore: Exception) {
                null
            }
            info?.let { setChainsInfo(it) }
        }
    }

    @Synchronized
    private fun setChainsInfo(info: ChainInfoDTO) {
        CoroutineScope(Dispatchers.IO).launch {
            aelfMap.clear()
            info.items.forEach {
                val chainType = stringToAElfChainType(it.chainId)
                val aelfClient = AElfClientV2(it.endPoint)
                aelfMap[chainType] = aelfClient
            }
        }
    }

    internal fun getAElfClient(chainType: String): AElfClientV2 =
            getAElfClient(stringToAElfChainType(chainType))

    private fun getAElfClient(chainType: AElfChainType): AElfClientV2 {
        return aelfMap[chainType]
                ?: throw IllegalArgumentException("no chain client $chainType found. Did you forget to call setChainsInfo() ?")
    }
}

internal enum class AElfChainType {
    AELF, // the main chain
    tDVV,
    tDVW
}

private fun stringToAElfChainType(chainType: String): AElfChainType {
    return when (chainType) {
        GlobalConfig.ChainIds.MAINNET_CHAIN_ID -> AElfChainType.AELF
        GlobalConfig.ChainIds.TESTNET_CHAIN_ID -> AElfChainType.tDVV
        GlobalConfig.ChainIds.TESTNET_CHAIN_ID_ALTERNATIVE -> AElfChainType.tDVW
        else -> throw IllegalArgumentException("Unknown chain type: $chainType, please check it again.")
    }
}