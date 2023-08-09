package io.aelf.portkey.network.slice.common;

import io.aelf.portkey.internal.model.common.ChainInfoDTO;
import retrofit2.Call;
import retrofit2.http.GET;

public interface CommonNetworkAPISlice {
    @GET(CommonAPIPath.GET_CHAINS_INFO)
    Call<ChainInfoDTO> getChainsInfo();
}
