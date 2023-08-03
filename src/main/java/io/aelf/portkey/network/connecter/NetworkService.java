package io.aelf.portkey.network.connecter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import io.aelf.portkey.assertion.AssertChecker;
import io.aelf.portkey.internal.model.common.CheckCaptchaParams;
import io.aelf.portkey.internal.model.common.OperationScene;
import io.aelf.portkey.internal.model.guardian.GuardianInfoDTO;
import io.aelf.portkey.internal.model.register.RegisterHeader;
import io.aelf.portkey.internal.model.register.RegisterInfoDTO;
import io.aelf.portkey.internal.model.register.SendVerificationCodeParams;
import io.aelf.portkey.internal.model.register.VerifyCodeResultDTO;
import io.aelf.portkey.network.api.PresetAPIs;
import io.aelf.portkey.network.retrofit.RetrofitProvider;
import io.aelf.portkey.utils.log.GLogger;
import io.aelf.response.ResultCode;
import io.aelf.utils.AElfException;
import okhttp3.ResponseBody;
import org.apache.http.util.TextUtils;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Response;


public class NetworkService implements IConnector {
    protected static volatile PresetAPIs api;
    protected static volatile Gson gson;

    public NetworkService() {
        if (api == null) {
            synchronized (NetworkService.class) {
                if (api == null) {
                    api = RetrofitProvider.getAPIService(PresetAPIs.class);
                }
            }
        }
        if (gson == null) {
            synchronized (NetworkService.class) {
                if (gson == null) {
                    gson = new Gson();
                }
            }
        }
    }

    @NotNull
    protected static <T> T realExecute(@NotNull Call<T> call) throws AElfException {
        try {
            GLogger.i("Network connection start, path:"
                    .concat(call.request().url().toString()));
            Response<T> response = call.execute();
            if (!response.isSuccessful()) {
                try (ResponseBody errorBody = response.errorBody()) {
                    String msg = null;
                    if (errorBody != null) {
                        msg = errorBody.string();
                    }
                    if (!TextUtils.isEmpty(msg)) {
                        Gson prettyGson = new GsonBuilder().setPrettyPrinting().create();
                        msg = prettyGson.toJson(JsonParser.parseString(msg));
                    }
                    throw new AElfException(
                            ResultCode.PEER_REJECTED,
                            String.format("it seems that the peer has rejected your request," +
                                            " code:%d, msg:\n%s",
                                    response.code(),
                                    TextUtils.isEmpty(msg) ? "unknown" : msg
                            )
                    );
                }
            }

            AssertChecker.assertNotNull(response);
            T result = response.body();
            assert result != null;
            return result;
        } catch (Throwable e) {
            AElfException exception = new AElfException(e);
            GLogger.e("Network failure! path: " + call.request().url(), exception);
            throw exception;
        }
    }


    @Override
    public boolean checkGoogleRecaptcha(int scene) throws AElfException {
        CheckCaptchaParams params = new CheckCaptchaParams();
        params.setOperationType(scene);
        return realExecute(api.checkGoogleRecaptcha(params));
    }

    @Override
    public GuardianInfoDTO getGuardianInfo(String chainId, String caHash, String guardianIdentifier) {
        return realExecute(api.getGuardianInfo(chainId, caHash, guardianIdentifier));
    }

    @Override
    public RegisterInfoDTO getRegisterInfo(String loginGuardianIdentifier, String caHash) {
        return realExecute(api.getRegisterInfo(loginGuardianIdentifier, caHash));
    }

    @Override
    public VerifyCodeResultDTO getVerificationCode(@NotNull SendVerificationCodeParams params, @NotNull RegisterHeader headers) {
        return realExecute(api.getVerificationCode(params, headers.getReCaptchaToken()));
    }
}
