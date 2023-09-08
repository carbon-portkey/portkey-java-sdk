package io.aelf.portkey.behaviour.wallet

import com.google.protobuf.MessageLite
import io.aelf.portkey.internal.model.wallet.CAInfo
import io.aelf.portkey.internal.tools.GlobalConfig.Status
import io.aelf.portkey.network.connecter.INetworkInterface
import io.aelf.portkey.utils.log.GLogger
import io.aelf.response.ResultCode
import io.aelf.utils.AElfException
import kotlinx.coroutines.*
import org.apache.http.util.TextUtils

private const val CHECK_DELAY = 1000L
private const val CHECK_TIMEOUT = 30 * 1000

internal fun PortkeyWallet.initWallet() {
    CoroutineScope(Dispatchers.IO).launch {
        updateWalletStatus()
    }
}

internal suspend fun PortkeyWallet.updateWalletStatus() {
    val startTime = System.currentTimeMillis()
    CoroutineScope(Dispatchers.IO).launch {
        while (this@updateWalletStatus.walletStage == WalletStage.INIT) {
            val job = CoroutineScope(Dispatchers.IO).launch {
                dealWithSessionProcess()
            }
            joinAll(job)
            if (System.currentTimeMillis() - startTime > CHECK_TIMEOUT) {
                this@updateWalletStatus.walletStage = WalletStage.FAILED
                break
            }
            delay(CHECK_DELAY)
        }
        handleWalletState()
    }
}

internal fun PortkeyWallet.dealWithSessionProcess() {
    val statusMsg: String = if (this.isFromRegister) {
        val result = try {
            INetworkInterface.getInstance().getRegisterStatus(this.sessionId)
        } catch (ignore: Exception) {
            null
        }
        val statusItem = result?.items?.firstOrNull()
        if (statusItem != null) {
            updateCAInfo(statusItem.caAddress, statusItem.caHash)
        }
        statusItem?.registerStatus ?: Status.PENDING
    } else {
        val result = try {
            INetworkInterface.getInstance().getRecoveryStatus(this.sessionId)
        } catch (ignore: Exception) {
            null
        }
        val statusItem = result?.items?.firstOrNull()
        if (statusItem != null) {
            updateCAInfo(statusItem.caAddress, statusItem.caHash)
        }
        statusItem?.recoveryStatus ?: Status.PENDING
    }
    this.walletStage = when (statusMsg) {
        Status.PENDING -> WalletStage.INIT
        Status.PASS -> WalletStage.READY
        Status.FAIL -> WalletStage.FAILED
        else -> WalletStage.INIT
    }
}

internal fun PortkeyWallet.updateCAInfo(caAddress: String?, caHash: String?) {
    this.caInfo = CAInfo().apply {
        if (!TextUtils.isEmpty(caAddress)) this.caAddress = caAddress
        if (!TextUtils.isEmpty(caHash)) this.caHash = caHash
    }
}

internal fun PortkeyWallet.handleWalletState() {
    when (this.walletStage) {
        WalletStage.READY -> {
            GLogger.i("Wallet init success.")
        }

        WalletStage.FAILED,
        WalletStage.INIT
        -> {
            GLogger.e("Wallet init failed.")
        }
    }
    walletInitObserver.onWalletInitResult(this.walletStage)
}

@Throws(AElfException::class)
@JvmName("callCAContractMethod")
fun <R> PortkeyWallet.callCAContractMethod(
        methodName: String,
        isViewMethod: Boolean,
        params: ByteArray = ByteArray(0),
        parser: (String) -> R
): R? {
    val res = callCAContractMethod(methodName, isViewMethod, params)
    return res?.let { parseResult(it, parser) }
}

@Throws(AElfException::class)
@JvmName("callCAContractMethod")
fun <T : MessageLite, R> PortkeyWallet.callCAContractMethod(
        methodName: String,
        isViewMethod: Boolean,
        params: T,
        parser: (String) -> R
): R? {
    return callCAContractMethod(methodName, isViewMethod, params.toByteArray() ?: ByteArray(0), parser)
}

@Throws(AElfException::class)
@JvmName("callCAContractMethod")
fun <T : MessageLite> PortkeyWallet.callCAContractMethod(
        methodName: String,
        isViewMethod: Boolean,
        params: T,
): String? {
    return callCAContractMethod(methodName, isViewMethod, params.toByteArray() ?: ByteArray(0))
}

@Throws(AElfException::class)
@JvmName("callCAContractMethod")
fun PortkeyWallet.callCAContractMethod(
        methodName: String,
        isViewMethod: Boolean,
): String? {
    return callCAContractMethod(methodName, isViewMethod, ByteArray(0))
}

@Throws(AElfException::class)
@JvmName("callCAContractMethod")
private fun PortkeyWallet.callCAContractMethod(
        methodName: String,
        isViewMethod: Boolean,
        params: ByteArray = ByteArray(0),
): String? {
    val isReady = isAvailable
    val chainId = this.originalChainId
    if (!isReady) {
        throw AElfException(ResultCode.INTERNAL_ERROR, "Wallet is not available by now.")
    }
    val aelfClient = AElfHolder.getAElfClient(chainId)
    val res = try {
        aelfClient.callContractMethodWithAddress(
                AElfHolder.getCaAddress(chainId), // caAddress IS NOT caContractAddress !
                methodName,
                this.keyPairInfo.privateKey,
                isViewMethod,
                params,
        )
    } catch (e: Exception) {
        GLogger.e("callCAContractMethod error.", AElfException(e))
        null
    }
    return res
}

@Throws(AElfException::class)
private fun <R> parseResult(result: String, parser: (String) -> R): R = parser.invoke(result)

enum class WalletStage {
    INIT, // We can't be sure that the wallet is ready to use, need to check the sessionId.
    READY, // The wallet is ready to use.
    FAILED // Either the wallet is disabled or the sessionId shows the register/social recovery process is failed.
}