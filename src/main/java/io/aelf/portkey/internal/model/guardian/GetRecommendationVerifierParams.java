package io.aelf.portkey.internal.model.guardian;

import io.aelf.portkey.internal.tools.DataVerifyTools;

public class GetRecommendationVerifierParams {
    /**
     * @see DataVerifyTools#verifyChainId(String)
     */
    private String chainId;

    public String getChainId() {
        return chainId;
    }

    public GetRecommendationVerifierParams setChainId(String chainId) {
        DataVerifyTools.verifyChainId(chainId);
        this.chainId = chainId;
        return this;
    }
}
