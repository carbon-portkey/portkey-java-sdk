package io.aelf.portkey.network.slice.account;

import io.aelf.portkey.internal.model.apple.AppleExtraInfoParams;
import io.aelf.portkey.internal.model.apple.AppleExtraInfoResultDTO;
import io.aelf.portkey.internal.model.apple.AppleVerifyTokenParams;
import io.aelf.portkey.internal.model.common.CheckCaptchaParams;
import io.aelf.portkey.internal.model.common.CountryCodeInfoDTO;
import io.aelf.portkey.internal.model.common.RegisterOrRecoveryResultDTO;
import io.aelf.portkey.internal.model.google.GoogleVerifyTokenParams;
import io.aelf.portkey.internal.model.guardian.GetRecommendGuardianResultDTO;
import io.aelf.portkey.internal.model.guardian.GetRecommendationVerifierParams;
import io.aelf.portkey.internal.model.guardian.GuardianInfoDTO;
import io.aelf.portkey.internal.model.recovery.RequestRecoveryParams;
import io.aelf.portkey.internal.model.register.RegisterInfoDTO;
import io.aelf.portkey.internal.model.register.RequestRegisterParams;
import io.aelf.portkey.internal.model.verify.HeadVerifyCodeParams;
import io.aelf.portkey.internal.model.verify.HeadVerifyCodeResultDTO;
import io.aelf.portkey.internal.model.verify.SendVerificationCodeParams;
import io.aelf.portkey.internal.model.verify.SendVerificationCodeResultDTO;
import retrofit2.Call;
import retrofit2.http.*;

public interface AccountNetworkAPISlice {


    /**
     * Check the CountryCodeInfo config.
     *
     * @return CountryCodeInfoDTO
     */
    @GET(AccountAPIPath.GET_PHONE_COUNTRY_CODE)
    Call<CountryCodeInfoDTO> getPhoneCountryCode();

    /**
     * Check if Google recaptcha is open.
     *
     * @return Boolean true if open, false if not.
     */
    @POST(AccountAPIPath.CHECK_GOOGLE_RECAPTCHA)
    Call<Boolean> checkGoogleRecaptcha(@Body CheckCaptchaParams body);

    @GET(AccountAPIPath.GET_GUARDIAN_INFO)
    Call<GuardianInfoDTO> getGuardianInfo(@Query("chainId") String chainId,
                                          @Query("guardianIdentifier") String guardianIdentifier);

    @GET(AccountAPIPath.GET_GUARDIAN_INFO)
    Call<GuardianInfoDTO> getGuardianInfo(@Query("chainId") String chainId,
                                          @Query("guardianIdentifier") String guardianIdentifier,
                                          @Query("caHash") String caHash);

    @GET(AccountAPIPath.GET_REGISTER_INFO)
    Call<RegisterInfoDTO> getRegisterInfo(@Query("loginGuardianIdentifier") String loginGuardianIdentifier);

    @GET(AccountAPIPath.GET_REGISTER_INFO)
    Call<RegisterInfoDTO> getRegisterInfo(@Query("loginGuardianIdentifier") String loginGuardianIdentifier,
                                          @Query("caHash") String caHash);

    @POST(AccountAPIPath.SEND_VERIFICATION_CODE)
    Call<SendVerificationCodeResultDTO> sendVerificationCode(@Body SendVerificationCodeParams params, @Header("reCaptchaToken") String reCaptchaToken);

    @POST(AccountAPIPath.CHECK_VERIFICATION_CODE)
    Call<HeadVerifyCodeResultDTO> checkVerificationCode(@Body HeadVerifyCodeParams params);

    @POST(AccountAPIPath.REQUEST_REGISTER)
    Call<RegisterOrRecoveryResultDTO> requestRegister(@Body RequestRegisterParams params);

    @POST(AccountAPIPath.REQUEST_RECOVERY)
    Call<RegisterOrRecoveryResultDTO> requestRecovery(@Body RequestRecoveryParams params);

    @POST(AccountAPIPath.SEND_APPLE_USER_EXTRA_INFO)
    Call<AppleExtraInfoResultDTO> sendAppleUserExtraInfo(@Body AppleExtraInfoParams params);

    @POST(AccountAPIPath.VERIFY_GOOGLE_TOKEN)
    Call<HeadVerifyCodeResultDTO> verifyGoogleToken(@Body GoogleVerifyTokenParams token);

    @POST(AccountAPIPath.VERIFY_APPLE_TOKEN)
    Call<HeadVerifyCodeResultDTO> verifyAppleToken(@Body AppleVerifyTokenParams token);

    @POST(AccountAPIPath.GET_RECOMMEND_GUARDIAN)
    Call<GetRecommendGuardianResultDTO> getRecommendationGuardianInfo(@Body GetRecommendationVerifierParams params);
}

