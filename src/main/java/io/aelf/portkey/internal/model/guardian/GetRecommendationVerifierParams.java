package io.aelf.portkey.internal.model.guardian;

import io.aelf.portkey.internal.behaviour.DataVerifyTools;

public class GetRecommendationVerifierParams {
    /**
     * @see DataVerifyTools#verifyChainIdParams(String)
     */
    private String chainId;

    public String getChainId() {
        return chainId;
    }

    public GetRecommendationVerifierParams setChainId(String chainId) {
        DataVerifyTools.verifyChainIdParams(chainId);
        this.chainId = chainId;
        return this;
    }
}
