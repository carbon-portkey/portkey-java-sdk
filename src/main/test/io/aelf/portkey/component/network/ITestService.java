package io.aelf.portkey.component.network;

import com.google.gson.JsonElement;
import io.aelf.portkey.internal.model.common.CountryCodeInfoDTO;
import io.aelf.portkey.network.api.slice.community.CommunityRecoveryAPIPath;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

interface ITestService {
    @GET("/api/blockChain/chainStatus")
    Call<JsonElement> networkStatus();

    @GET(CommunityRecoveryAPIPath.GET_PHONE_COUNTRY_CODE)
    Call<CountryCodeInfoDTO> getPhoneCountryCode();

    @GET(CommunityRecoveryAPIPath.GET_PHONE_COUNTRY_CODE)
    Call<CountryCodeInfoDTO> getPhoneCountryCode(@Query("mock") int mock);


    @GET(CommunityRecoveryAPIPath.GET_PHONE_COUNTRY_CODE)
    Call<CountryCodeInfoDTO> getPhoneCountryCode(@Header("Content-Type") String contentType);

    @POST(CommunityRecoveryAPIPath.CHECK_GOOGLE_RECAPTCHA)
    Call<Boolean> checkGoogleRecaptcha(@Query("mock") String mock);
}
