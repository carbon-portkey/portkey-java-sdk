package io.aelf.portkey.network.connecter;

import io.aelf.portkey.internal.model.guardian.GuardianInfoDTO;
import io.aelf.portkey.internal.model.register.RegisterHeader;
import io.aelf.portkey.internal.model.register.RegisterInfoDTO;
import io.aelf.portkey.internal.model.register.SendVerificationCodeParams;
import io.aelf.portkey.internal.model.register.VerifyCodeResultDTO;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.IOException;

public interface IConnector {
    boolean checkGoogleRecaptcha(int scene) throws IOException;

    GuardianInfoDTO getGuardianInfo(String chainId, String caHash, String guardianIdentifier);

    RegisterInfoDTO getRegisterInfo(String loginGuardianIdentifier, String caHash);

    VerifyCodeResultDTO getVerificationCode(@NonNull SendVerificationCodeParams params,@NonNull RegisterHeader headers);
}
