package io.aelf.portkey.network.api.slice.community;

import io.aelf.portkey.internal.model.common.CheckCaptchaParams;
import io.aelf.portkey.internal.model.common.CountryCodeInfoDTO;
import io.aelf.portkey.internal.model.common.RegisterOrRecoveryResultDTO;
import io.aelf.portkey.internal.model.guardian.GuardianInfoDTO;
import io.aelf.portkey.internal.model.recovery.RequestRecoveryParams;
import io.aelf.portkey.internal.model.register.*;
import io.aelf.portkey.internal.model.verify.HeadVerifyCodeParams;
import io.aelf.portkey.internal.model.verify.HeadVerifyCodeResultDTO;
import io.aelf.portkey.internal.model.verify.SendVerificationCodeParams;
import io.aelf.portkey.internal.model.verify.SendVerificationCodeResultDTO;
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

    @POST(CommunityRecoveryAPIPath.SEND_VERIFICATION_CODE)
    Call<SendVerificationCodeResultDTO> sendVerificationCode(@Body SendVerificationCodeParams params, @Header("reCaptchaToken") String reCaptchaToken);

    @POST(CommunityRecoveryAPIPath.CHECK_VERIFICATION_CODE)
    Call<HeadVerifyCodeResultDTO> checkVerificationCode(@Body HeadVerifyCodeParams params);

    @POST(CommunityRecoveryAPIPath.REQUEST_REGISTER)
    Call<RegisterOrRecoveryResultDTO> requestRegister(@Body RequestRegisterParams params);

    @POST(CommunityRecoveryAPIPath.REQUEST_RECOVERY)
    Call<RegisterOrRecoveryResultDTO> requestRecovery(@Body RequestRecoveryParams params);

}

