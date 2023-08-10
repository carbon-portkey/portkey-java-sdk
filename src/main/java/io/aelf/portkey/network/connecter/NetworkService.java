package io.aelf.portkey.network.connecter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import io.aelf.internal.sdkv2.AElfClientV2;
import io.aelf.portkey.assertion.AssertChecker;
import io.aelf.portkey.internal.model.apple.AppleExtraInfoParams;
import io.aelf.portkey.internal.model.apple.AppleExtraInfoResultDTO;
import io.aelf.portkey.internal.model.apple.AppleVerifyTokenParams;
import io.aelf.portkey.internal.model.common.*;
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
import io.aelf.portkey.internal.tools.GlobalConfig;
import io.aelf.portkey.network.retrofit.RetrofitProvider;
import io.aelf.portkey.network.slice.common.GoogleNetworkAPISlice;
import io.aelf.portkey.utils.log.GLogger;
import io.aelf.response.ResultCode;
import io.aelf.schemas.ChainstatusDto;
import io.aelf.utils.AElfException;
import okhttp3.ResponseBody;
import org.apache.http.util.TextUtils;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.Arrays;


public class NetworkService implements INetworkInterface {
    protected static volatile IRetrofitAPIs api;
    protected static volatile Gson gson;
    private static volatile NetworkService instance;
    protected volatile AElfClientV2 aelfClientV2;

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

    protected static NetworkService getInstance() {
        if (instance == null) {
            synchronized (NetworkService.class) {
                if (instance == null) {
                    instance = new NetworkService();
                }
            }
        }
        return instance;
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
            assert result != null;
            GLogger.t("Network connection end, path:"
                    .concat(call.request().url().toString())
                    .concat("\nresult: ")
                    .concat(GLogger.prettyJSON(result)));
            return result;
        } catch (Throwable e) {
            AElfException exception = new AElfException(e);
            GLogger.e("Network failure! path: " + call.request().url(), exception);
            throw exception;
        }
    }

    protected synchronized void syncAElfChainInfo(@NotNull ChainInfoDTO data, @NotNull String targetChainId) {
        Arrays.stream(data.getItems())
                .filter(item -> targetChainId.equals(item.getChainId()))
                .findFirst()
                .ifPresent(this::initAElfClient);
    }

    protected synchronized void initAElfClient(ChainInfoItemDTO item) {
        String url = item.getEndPoint();
        AssertChecker.assertNotBlank(url, new AElfException(ResultCode.INTERNAL_ERROR, "Chain url is blank."));
        aelfClientV2 = new AElfClientV2(url);
    }

    public AElfClientV2 getAElfClient() throws AElfException {
        AssertChecker.assertNotNull(
                aelfClientV2,
                new AElfException(
                        ResultCode.INTERNAL_ERROR,
                        "AElfClient is null, maybe you forgot to call initAElfClient()?"
                )
        );
        return aelfClientV2;
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
        return realExecute(api.checkGoogleRecaptcha(params));
    }

    @Override
    public GuardianInfoDTO getGuardianInfo(String chainId, String guardianIdentifier) {
        return realExecute(api.getGuardianInfo(chainId, guardianIdentifier));
    }

    @Override
    public GuardianInfoDTO getGuardianInfo(String chainId, String guardianIdentifier, String caHash) {
        return realExecute(api.getGuardianInfo(chainId, guardianIdentifier, caHash));
    }

    @Override
    public RegisterInfoDTO getRegisterInfo(String loginGuardianIdentifier) {
        return realExecute(api.getRegisterInfo(loginGuardianIdentifier));
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
    public HeadVerifyCodeResultDTO checkVerificationCode(@NonNull HeadVerifyCodeParams params) throws AElfException {
        return realExecute(api.checkVerificationCode(params));
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
    public HeadVerifyCodeResultDTO verifyGoogleToken(@NonNull GoogleVerifyTokenParams params) throws AElfException {
        return realExecute(api.verifyGoogleToken(params));
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
        GoogleNetworkAPISlice service = RetrofitProvider.getAPIService(GoogleNetworkAPISlice.class, GlobalConfig.GOOGLE_HOST);
        return realExecute(service.getGoogleAccessToken("Bearer ".concat(authorization)));
    }

    @Override
    public ChainInfoDTO getGlobalChainInfo() throws AElfException {
        ChainInfoDTO result = realExecute(api.getChainsInfo());
        syncAElfChainInfo(result, GlobalConfig.getCurrentChainId());
        return result;
    }

    @Override
    public ChainstatusDto getParticularChainInfo() throws IOException {
        return getAElfClient().getChainStatus();
    }
}
