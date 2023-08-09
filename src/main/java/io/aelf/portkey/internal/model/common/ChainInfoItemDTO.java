package io.aelf.portkey.internal.model.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.aelf.portkey.internal.tools.DataVerifyTools;

public class ChainInfoItemDTO {
    /**
     * @see io.aelf.portkey.internal.tools.GlobalConfig.ChainIds
     */
    @JsonProperty("chainId")
    private String chainId;
    @JsonProperty("chainName")
    private String chainName;
    @JsonProperty("endPoint")
    private String endPoint;
    @JsonProperty("explorerUrl")
    private String explorerUrl;
    @JsonProperty("caContractAddress")
    private String caContractAddress;
    @JsonProperty("lastModifyTime")
    private String lastModifyTime;
    @JsonProperty("id")
    private String id;
    @JsonProperty("defaultToken")
    private TokenDTO defaultToken;

    public String getChainId() {
        return chainId;
    }

    public ChainInfoItemDTO setChainId(String chainId) {
        DataVerifyTools.verifyChainId(chainId);
        this.chainId = chainId;
        return this;
    }

    public String getChainName() {
        return chainName;
    }

    public ChainInfoItemDTO setChainName(String chainName) {
        this.chainName = chainName;
        return this;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public ChainInfoItemDTO setEndPoint(String endPoint) {
        this.endPoint = endPoint;
        return this;
    }

    public String getExplorerUrl() {
        return explorerUrl;
    }

    public ChainInfoItemDTO setExplorerUrl(String explorerUrl) {
        this.explorerUrl = explorerUrl;
        return this;
    }

    public String getCaContractAddress() {
        return caContractAddress;
    }

    public ChainInfoItemDTO setCaContractAddress(String caContractAddress) {
        this.caContractAddress = caContractAddress;
        return this;
    }

    public String getLastModifyTime() {
        return lastModifyTime;
    }

    public ChainInfoItemDTO setLastModifyTime(String lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
        return this;
    }

    public String getId() {
        return id;
    }

    public ChainInfoItemDTO setId(String id) {
        this.id = id;
        return this;
    }

    public TokenDTO getDefaultToken() {
        return defaultToken;
    }

    public ChainInfoItemDTO setDefaultToken(TokenDTO defaultToken) {
        this.defaultToken = defaultToken;
        return this;
    }
}
