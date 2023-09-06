package io.aelf.portkey.network.connecter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.aelf.portkey.assertion.AssertChecker;
import io.aelf.portkey.internal.model.apple.AppleExtraInfoParams;
import io.aelf.portkey.internal.model.apple.AppleExtraInfoResultDTO;
import io.aelf.portkey.internal.model.apple.AppleVerifyTokenParams;
import io.aelf.portkey.internal.model.common.ChainInfoDTO;
import io.aelf.portkey.internal.model.common.CheckCaptchaParams;
import io.aelf.portkey.internal.model.common.CountryCodeInfoDTO;
import io.aelf.portkey.internal.model.common.RegisterOrRecoveryResultDTO;
import io.aelf.portkey.internal.model.google.GoogleAuthResult;
import io.aelf.portkey.internal.model.google.GoogleVerifyTokenParams;
import io.aelf.portkey.internal.model.guardian.GetRecommendGuardianResultDTO;
import io.aelf.portkey.internal.model.guardian.GetRecommendationVerifierParams;
import io.aelf.portkey.internal.model.guardian.GuardianInfoDTO;
import io.aelf.portkey.internal.model.recovery.RequestRecoveryParams;
import io.aelf.portkey.internal.model.register.RegisterHeader;
import io.aelf.portkey.internal.model.register.RegisterInfoDTO;
import io.aelf.portkey.internal.model.register.RequestRegisterParams;
import io.aelf.portkey.internal.model.verify.HeadVerifyCodeParams;
import io.aelf.portkey.internal.model.verify.HeadVerifyCodeResultDTO;
import io.aelf.portkey.internal.model.verify.SendVerificationCodeParams;
import io.aelf.portkey.internal.model.verify.SendVerificationCodeResultDTO;
import io.aelf.portkey.internal.model.wallet.RecoveryStatusDTO;
import io.aelf.portkey.internal.model.wallet.RegisterStatusDTO;
import io.aelf.portkey.internal.tools.GlobalConfig;
import io.aelf.portkey.network.retrofit.RetrofitProvider;
import io.aelf.portkey.network.slice.common.CommonAPIPath;
import io.aelf.portkey.network.slice.common.GoogleNetworkAPISlice;
import io.aelf.portkey.utils.log.GLogger;
import io.aelf.response.ResultCode;
import io.aelf.utils.AElfException;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.Buffer;
import org.apache.http.util.TextUtils;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;


public class NetworkService implements INetworkInterface {
    protected static volatile IRetrofitAPIs api;
    protected static volatile Gson gson;
    private static volatile NetworkService instance;

    private NetworkService() {
        if (api == null) {
            synchronized (NetworkService.class) {
                if (api == null) {
                    api = RetrofitProvider.getAPIService(IRetrofitAPIs.class);
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

    public static NetworkService getInstance() {
        if (instance == null) {
            synchronized (NetworkService.class) {
                if (instance == null) {
                    instance = new NetworkService();
                }
            }
        }
        return instance;
    }


    protected static <T> T realExecute(@NotNull Call<T> call) throws AElfException {
        try {
            GLogger.t("Network connection start, path:"
                    .concat(call.request().url().toString()));
            RequestBody body = call.request().body();
            if (body != null) {
                try (Buffer buffer = new Buffer()) {
                    body.writeTo(buffer);
                    GLogger.t("body : \n"
                            .concat(buffer.readUtf8()));
                }
            }
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
                            String.format("it seems that the peer has rejected your request. " +
                                            "\ncode:%d \nmsg: %s",
                                    response.code(),
                                    TextUtils.isEmpty(msg) ? "unknown" : msg
                            )
                    );
                }
            }

            AssertChecker.assertNotNull(response);
            T result = response.body();
            GLogger.t("Network connection end, path:"
                    .concat(call.request().url().toString())
                    .concat("\nresult: ")
                    .concat(GLogger.prettyJSON(result != null ? result : new JsonObject())));
            return result;
        } catch (Throwable e) {
            AElfException exception = new AElfException(e);
            GLogger.e("Network failure! path: " + call.request().url(), exception);
        }
        return null;
    }

    /**
     * Check the CountryCodeInfo config.
     *
     * @return CountryCodeInfoDTO
     */
    @Override
    public CountryCodeInfoDTO getPhoneCountryCode() throws AElfException {
        return realExecute(api.getPhoneCountryCode());
    }

    @Override
    public boolean checkGoogleRecaptcha(int scene) throws AElfException {
        CheckCaptchaParams params = new CheckCaptchaParams();
        params.setOperationType(scene);
        return Boolean.TRUE.equals(realExecute(api.checkGoogleRecaptcha(params)));
    }

    @Override
    public GuardianInfoDTO getGuardianInfo(String chainId, String guardianIdentifier) {
        return realExecute(api.getGuardianInfo(chainId, guardianIdentifier, guardianIdentifier));
    }

    @Override
    public GuardianInfoDTO getGuardianInfo(String chainId, String guardianIdentifier, String caHash) {
        return realExecute(api.getGuardianInfo(chainId, guardianIdentifier, guardianIdentifier, caHash));
    }

    @Override
    public RegisterInfoDTO getRegisterInfo(String loginGuardianIdentifier) {
        Call<RegisterInfoDTO> call = api.getRegisterInfo(loginGuardianIdentifier);
        try {
            Response<RegisterInfoDTO> response = call.execute();
            if (response.isSuccessful()) {
                return response.body();
            } else {
                try (ResponseBody errBody = response.errorBody()) {
                    if (errBody != null) {
                        JsonObject jsonObject = JsonParser.parseString(errBody.string()).getAsJsonObject();
                        if (!jsonObject.has("error")) return null;
                        jsonObject = jsonObject.getAsJsonObject("error");
                        if (jsonObject.has("code") && "3002".equals(jsonObject.get("code").getAsString())) {
                            return new RegisterInfoDTO()
                                    .setErrCodeMatchNotRegistered(true);
                        }
                    } else {
                        return null;
                    }
                }
            }
        } catch (Exception e) {
            GLogger.e("GetRegisterInfo failed!", new AElfException(e));
        }
        return null;

    }

    @Override
    public RegisterInfoDTO getRegisterInfo(String loginGuardianIdentifier, String caHash) {
        return realExecute(api.getRegisterInfo(loginGuardianIdentifier, caHash));
    }

    @Override
    public SendVerificationCodeResultDTO sendVerificationCode(@NonNull SendVerificationCodeParams params) throws AElfException {
        return realExecute(api.sendVerificationCode(params, null));
    }

    @Override
    public SendVerificationCodeResultDTO sendVerificationCode(@NotNull SendVerificationCodeParams params, @NotNull RegisterHeader headers) {
        return realExecute(api.sendVerificationCode(params, headers.getReCaptchaToken()));
    }

    @Override
    public HeadVerifyCodeResultDTO checkVerificationCode(@NonNull HeadVerifyCodeParams params) throws AElfException, IOException {
        Call<HeadVerifyCodeResultDTO> call = api.checkVerificationCode(params);
        Response<HeadVerifyCodeResultDTO> response = call.execute();
        if (response.isSuccessful()) {
            return response.body();
        } else {
            String errMsg = "Network Failed.";
            try (ResponseBody errorBody = response.errorBody()) {
                if (errorBody != null) {
                    errMsg = errorBody.string();
                }
            }
            GLogger.e("Check verify code failed! Reason:\n" + errMsg);
            return new HeadVerifyCodeResultDTO();
        }
    }

    @Override
    public RegisterOrRecoveryResultDTO requestRegister(@NonNull RequestRegisterParams params) throws AElfException {
        return realExecute(api.requestRegister(params));
    }

    @Override
    public RegisterOrRecoveryResultDTO requestRecovery(@NonNull RequestRecoveryParams params) throws AElfException {
        return realExecute(api.requestRecovery(params));
    }

    @Override
    public AppleExtraInfoResultDTO sendAppleUserExtraInfo(@NonNull AppleExtraInfoParams params) throws AElfException {
        return realExecute(api.sendAppleUserExtraInfo(params));
    }

    @Override
    public HeadVerifyCodeResultDTO verifyGoogleToken(@NonNull GoogleVerifyTokenParams params) throws AElfException, IOException {
        Call<HeadVerifyCodeResultDTO> call = api.verifyGoogleToken(params);
        Response<HeadVerifyCodeResultDTO> response = call.execute();
        if (response.isSuccessful()) {
            return response.body();
        } else {
            return new HeadVerifyCodeResultDTO();
        }
    }

    @Override
    public HeadVerifyCodeResultDTO verifyAppleToken(@NonNull AppleVerifyTokenParams params) throws AElfException {
        return realExecute(api.verifyAppleToken(params));
    }

    @Override
    public GetRecommendGuardianResultDTO getRecommendGuardian(GetRecommendationVerifierParams params) throws AElfException {
        return realExecute(api.getRecommendationGuardianInfo(params));
    }

    @Override
    public String getGoogleAccessToken(@NotNull String authorization) {
        GoogleNetworkAPISlice service = RetrofitProvider.getAPIService(GoogleNetworkAPISlice.class, GlobalConfig.GOOGLE_API_HOST);
        return realExecute(service.getGoogleAccessToken("Bearer ".concat(authorization)));
    }

    @Override
    public ChainInfoDTO getGlobalChainInfo() throws AElfException {
        return realExecute(api.getChainsInfo());
    }

    public GoogleAuthResult getGoogleAuthResult(String code) {
        GoogleAuthAPI service = RetrofitProvider.getAPIService(GoogleAuthAPI.class, GlobalConfig.GOOGLE_AUTH_HOST);
        try {
            Properties properties = new Properties();
            try {
                properties.load(Files.newInputStream(Paths.get("config.properties")));
            } catch (Throwable e) {
                properties.load(NetworkService.class.getClassLoader().getResourceAsStream("config.properties"));
            }
            return realExecute(service.getGoogleAccessToken(
                    code,
                    properties.getProperty("client_id"),
                    properties.getProperty("client_secret"),
                    properties.getProperty("redirect_uri"),
                    properties.getProperty("grant_type"),
                    "application/x-www-form-urlencoded"
            ));
        } catch (Exception e) {
            GLogger.e("Network failure! path: " + CommonAPIPath.GET_GOOGLE_AUTH_RESULT, new AElfException(e));
            return null;
        }
    }

    @Override
    public RecoveryStatusDTO getRecoveryStatus(String sessionId) throws AElfException {
        return realExecute(api.checkSocialRecoveryStatus("_id:".concat(sessionId)));
    }

    @Override
    public RegisterStatusDTO getRegisterStatus(String sessionId) throws AElfException {
        return realExecute(api.checkRegisterStatus("_id:".concat(sessionId)));
    }
}

interface GoogleAuthAPI {
    @FormUrlEncoded
    @POST(CommonAPIPath.GET_GOOGLE_AUTH_RESULT)
    Call<GoogleAuthResult> getGoogleAccessToken(
            @Field("code") String code,
            @Field("client_id") String clientId,
            @Field("client_secret") String clientSecret,
            @Field("redirect_uri") String redirectUri,
            @Field("grant_type") String grantType,
            @Header("Content-Type") String contentType
    );
}
