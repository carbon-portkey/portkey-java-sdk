package io.aelf.portkey.network.slice.common;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface GoogleNetworkAPISlice {
    @GET(CommonAPIPath.GET_GOOGLE_ACCESS_TOKEN)
    Call<String> getGoogleAccessToken(@Header("Authorization") String authorization);
}
