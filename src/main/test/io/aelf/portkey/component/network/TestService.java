package io.aelf.portkey.component.network;

import com.google.gson.JsonElement;
import retrofit2.Call;
import retrofit2.http.GET;

interface TestService {
    @GET("/api/blockChain/chainStatus")
    Call<JsonElement> networkStatus();
}
