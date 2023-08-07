package io.aelf.portkey.network.api.slice.community;

public interface CommunityRecoveryAPIPath {
    String CHECK_GOOGLE_RECAPTCHA = "/api/app/account/isGoogleRecaptchaOpen";
    String GET_GUARDIAN_INFO = "/api/app/account/guardianIdentifiers";
    String GET_REGISTER_INFO = "/api/app/account/registerInfo";
    String SEND_VERIFICATION_CODE = "/api/app/account/sendVerificationRequest";
    String CHECK_VERIFICATION_CODE = "/api/app/account/verifyCode";
    String REQUEST_REGISTER = "/api/app/account/register/request";
    String REQUEST_RECOVERY = "/api/app/account/recovery/request";
    String STUB_GET_GUARDIAN_INFO_BY_MANAGER = "#getHolderInfoByManager_stub";
    String SEND_APPLE_USER_EXTRA_INFO = "/api/app/userExtraInfo/appleUserExtraInfo";
    String VERIFY_GOOGLE_TOKEN = "/api/app/account/verifyGoogleToken";
    String VERIFY_APPLE_TOKEN = "/api/app/account/verifyAppleToken";
    String GET_PHONE_COUNTRY_CODE = "/api/app/phone/info";
    String GET_RECOMMEND_GUARDIAN = "/api/app/account/getVerifierServer";
}
