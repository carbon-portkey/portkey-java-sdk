package io.aelf.portkey.network.api;

import com.google.gson.JsonElement;
import io.aelf.portkey.internal.model.common.CheckCaptchaParams;
import io.aelf.portkey.internal.model.guardian.GuardianInfoDTO;
import io.aelf.portkey.internal.model.register.RegisterHeader;
import io.aelf.portkey.internal.model.register.RegisterInfoDTO;
import io.aelf.portkey.internal.model.register.SendVerificationCodeParams;
import io.aelf.portkey.internal.model.register.VerifyCodeResultDTO;
import retrofit2.Call;
import retrofit2.http.*;

public interface PresetAPIs {
    /**
     * Check if Google recaptcha is open.
     *
     * @return JsonElement - actually it is boolean
     */
    @POST(PresetAPIPath.CHECK_GOOGLE_RECAPTCHA)
    Call<Boolean> checkGoogleRecaptcha(@Body CheckCaptchaParams body);

    @GET(PresetAPIPath.GET_GUARDIAN_INFO)
    Call<GuardianInfoDTO> getGuardianInfo(@Query("chainId") String chainId,
                                              @Query("caHash") String caHash,
                                              @Query("guardianIdentifier") String guardianIdentifier);

    @GET(PresetAPIPath.GET_REGISTER_INFO)
    Call<RegisterInfoDTO> getRegisterInfo(@Query("loginGuardianIdentifier") String loginGuardianIdentifier,
                                              @Query("caHash") String caHash);

    @GET(PresetAPIPath.GET_VERIFICATION_CODE)
    Call<VerifyCodeResultDTO> getVerificationCode(@Body SendVerificationCodeParams params, @Header("reCaptchaToken") String reCaptchaToken);

}

