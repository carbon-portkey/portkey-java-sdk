package io.aelf.portkey.network.connecter;

import io.aelf.portkey.internal.model.common.CountryCodeInfoDTO;
import io.aelf.portkey.internal.model.guardian.GuardianInfoDTO;
import io.aelf.portkey.internal.model.register.*;
import io.aelf.portkey.internal.model.verify.HeadVerifyCodeParams;
import io.aelf.portkey.internal.model.verify.HeadVerifyCodeResultDTO;
import io.aelf.portkey.internal.model.verify.SendVerificationCodeParams;
import io.aelf.portkey.internal.model.verify.SendVerificationCodeResultDTO;
import io.aelf.utils.AElfException;
import org.checkerframework.checker.nullness.qual.NonNull;

@SuppressWarnings("UnusedReturnValue")
public interface IConnector {

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

    SendVerificationCodeResultDTO getVerificationCode(@NonNull SendVerificationCodeParams params,
                                                      @NonNull RegisterHeader headers) throws AElfException;

    HeadVerifyCodeResultDTO checkVerificationCode(@NonNull HeadVerifyCodeParams params) throws AElfException;
}
