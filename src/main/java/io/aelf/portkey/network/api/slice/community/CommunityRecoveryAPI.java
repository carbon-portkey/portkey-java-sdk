package io.aelf.portkey.network.api.slice.community;

import io.aelf.portkey.internal.model.common.CheckCaptchaParams;
import io.aelf.portkey.internal.model.common.CountryCodeInfoDTO;
import io.aelf.portkey.internal.model.guardian.GuardianInfoDTO;
import io.aelf.portkey.internal.model.register.RegisterInfoDTO;
import io.aelf.portkey.internal.model.register.SendVerificationCodeParams;
import io.aelf.portkey.internal.model.register.VerifyCodeResultDTO;
import retrofit2.Call;
import retrofit2.http.*;

public interface CommunityRecoveryAPI {


    /**
     * Check the CountryCodeInfo config.
     *
     * @return CountryCodeInfoDTO
     */
    @GET(CommunityRecoveryAPIPath.GET_PHONE_COUNTRY_CODE)
    Call<CountryCodeInfoDTO> getPhoneCountryCode();

    /**
     * Check if Google recaptcha is open.
     *
     * @return Boolean true if open, false if not.
     */
    @POST(CommunityRecoveryAPIPath.CHECK_GOOGLE_RECAPTCHA)
    Call<Boolean> checkGoogleRecaptcha(@Body CheckCaptchaParams body);

    @GET(CommunityRecoveryAPIPath.GET_GUARDIAN_INFO)
    Call<GuardianInfoDTO> getGuardianInfo(@Query("chainId") String chainId,
                                          @Query("caHash") String caHash,
                                          @Query("guardianIdentifier") String guardianIdentifier);

    @GET(CommunityRecoveryAPIPath.GET_REGISTER_INFO)
    Call<RegisterInfoDTO> getRegisterInfo(@Query("loginGuardianIdentifier") String loginGuardianIdentifier,
                                          @Query("caHash") String caHash);

    @GET(CommunityRecoveryAPIPath.GET_VERIFICATION_CODE)
    Call<VerifyCodeResultDTO> getVerificationCode(@Body SendVerificationCodeParams params, @Header("reCaptchaToken") String reCaptchaToken);

}

