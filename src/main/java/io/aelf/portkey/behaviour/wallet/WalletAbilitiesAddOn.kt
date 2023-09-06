package io.aelf.portkey.behaviour.wallet

import com.google.gson.JsonObject
import io.aelf.portkey.internal.model.wallet.CAInfo
import io.aelf.portkey.internal.tools.GlobalConfig.Status
import io.aelf.portkey.internal.tools.GsonProvider
import io.aelf.portkey.network.connecter.INetworkInterface
import io.aelf.portkey.utils.log.GLogger
import io.aelf.response.ResultCode
import io.aelf.utils.AElfException
import kotlinx.coroutines.*
import org.apache.http.util.TextUtils

private const val CHECK_DELAY = 1000L
private const val CHECK_TIMES = 10

internal fun PortkeyWallet.initWallet() {
    CoroutineScope(Dispatchers.IO).launch {
        updateWalletStatus()
    }
}

internal suspend fun PortkeyWallet.updateWalletStatus() {
    var checkTimes = 0
    CoroutineScope(Dispatchers.IO).launch {
        while (this@updateWalletStatus.walletStage == WalletStage.INIT) {
            val job = CoroutineScope(Dispatchers.IO).launch {
                dealWithSessionProcess()
            }
            joinAll(job)
            checkTimes++
            if (checkTimes > CHECK_TIMES) {
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
        if (result != null) updateCAInfo(result.caAddress!!, result.caHash!!)
        if (!TextUtils.isEmpty(result?.registerStatus)) {
            result?.registerStatus!!
        } else {
            Status.PENDING
        }
    } else {
        val result = try {
            INetworkInterface.getInstance().getRecoveryStatus(this.sessionId)
        } catch (ignore: Exception) {
            null
        }
        if (result != null) updateCAInfo(result.caAddress!!, result.caHash!!)
        if (!TextUtils.isEmpty(result?.recoveryStatus)) {
            result?.recoveryStatus!!
        } else {
            Status.PENDING
        }
    }
    this.walletStage = when (statusMsg) {
        Status.PENDING -> WalletStage.INIT
        Status.PASS -> WalletStage.READY
        Status.FAIL -> WalletStage.FAILED
        else -> WalletStage.INIT
    }
}

internal fun PortkeyWallet.updateCAInfo(caAddress: String, caHash: String) {
    this.caInfo = CAInfo().apply {
        this.caAddress = caAddress
        this.caHash = caHash
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
fun PortkeyWallet.callCAContractMethod(
        methodName: String,
        isViewMethod: Boolean,
        params: JsonObject = JsonObject()
): String {
    val isReady = isAvailable
    if (!isReady) {
        throw AElfException(ResultCode.INTERNAL_ERROR, "Wallet is not available by now.")
    }
    val aelfClient = AElfHolder.getAElfClient(this.originalChainId)
    return aelfClient.callContractMethodWithAddress(
            this.caInfo.caAddress,
            methodName,
            this.keyPairInfo.privateKey,
            isViewMethod,
            GsonProvider.getGson().toJson(params),
    )
}

enum class WalletStage {
    INIT, // We can't be sure that the wallet is ready to use, need to check the sessionId.
    READY, // The wallet is ready to use.
    FAILED // Either the wallet is disabled or the sessionId shows the register/social recovery process is failed.
}