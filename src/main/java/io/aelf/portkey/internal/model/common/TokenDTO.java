package io.aelf.portkey.internal.model.common;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TokenDTO {
    @JsonProperty("name")
    private String name;
    @JsonProperty("address")
    private String address;
    @JsonProperty("imageUrl")
    private String imageUrl;
    @JsonProperty("symbol")
    private String symbol;
    @JsonProperty("decimals")
    private String decimals;

    public String getName() {
        return name;
    }

    public TokenDTO setName(String name) {
        this.name = name;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public TokenDTO setAddress(String address) {
        this.address = address;
        return this;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public TokenDTO setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public String getSymbol() {
        return symbol;
    }

    public TokenDTO setSymbol(String symbol) {
        this.symbol = symbol;
        return this;
    }

    public String getDecimals() {
        return decimals;
    }

    public TokenDTO setDecimals(String decimals) {
        this.decimals = decimals;
        return this;
    }
}
