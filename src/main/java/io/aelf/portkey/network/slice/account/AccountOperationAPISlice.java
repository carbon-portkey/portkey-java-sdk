package io.aelf.portkey.network.slice.account;

import io.aelf.portkey.internal.model.apple.AppleExtraInfoParams;
import io.aelf.portkey.internal.model.apple.AppleExtraInfoResultDTO;
import io.aelf.portkey.internal.model.apple.AppleVerifyTokenParams;
import io.aelf.portkey.internal.model.common.CountryCodeInfoDTO;
import io.aelf.portkey.internal.model.common.RegisterOrRecoveryResultDTO;
import io.aelf.portkey.internal.model.google.GoogleVerifyTokenParams;
import io.aelf.portkey.internal.model.guardian.GetRecommendGuardianResultDTO;
import io.aelf.portkey.internal.model.guardian.GetRecommendationVerifierParams;
import io.aelf.portkey.internal.model.guardian.GuardianInfoDTO;
import io.aelf.portkey.internal.model.recovery.RequestRecoveryParams;
import io.aelf.portkey.internal.model.register.*;
import io.aelf.portkey.internal.model.verify.HeadVerifyCodeParams;
import io.aelf.portkey.internal.model.verify.HeadVerifyCodeResultDTO;
import io.aelf.portkey.internal.model.verify.SendVerificationCodeParams;
import io.aelf.portkey.internal.model.verify.SendVerificationCodeResultDTO;
import io.aelf.utils.AElfException;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface AccountOperationAPISlice {

    /**
     * Check the CountryCodeInfo config.
     *
     * @return CountryCodeInfoDTO
     */
    CountryCodeInfoDTO getPhoneCountryCode() throws AElfException;

    /**
     * Check if Google recaptcha is open.
     *
     * @return true if open, false if not.
     */
    boolean checkGoogleRecaptcha(int scene) throws AElfException;

    GuardianInfoDTO getGuardianInfo(String chainId, String caHash, String guardianIdentifier);

    RegisterInfoDTO getRegisterInfo(String loginGuardianIdentifier, String caHash);
    SendVerificationCodeResultDTO sendVerificationCode(@NonNull SendVerificationCodeParams params) throws AElfException;

    SendVerificationCodeResultDTO sendVerificationCode(@NonNull SendVerificationCodeParams params,
                                                       @NonNull RegisterHeader headers) throws AElfException;

    HeadVerifyCodeResultDTO checkVerificationCode(@NonNull HeadVerifyCodeParams params) throws AElfException;

    RegisterOrRecoveryResultDTO requestRegister(@NonNull RequestRegisterParams params) throws AElfException;

    RegisterOrRecoveryResultDTO requestRecovery(@NonNull RequestRecoveryParams params) throws AElfException;

    AppleExtraInfoResultDTO sendAppleUserExtraInfo(@NonNull AppleExtraInfoParams params) throws AElfException;

    HeadVerifyCodeResultDTO verifyGoogleToken(@NonNull GoogleVerifyTokenParams params) throws AElfException;

    HeadVerifyCodeResultDTO verifyAppleToken(@NonNull AppleVerifyTokenParams params) throws AElfException;

    GetRecommendGuardianResultDTO getRecommendGuardian(GetRecommendationVerifierParams params) throws AElfException;
}
