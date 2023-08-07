package io.aelf.portkey.internal.model.guardian;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GetRecommendGuardianResultDTO {
    @JsonProperty("id")
    private String id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("imageUrl")
    private String imageUrl;

    public String getId() {
        return id;
    }

    public GetRecommendGuardianResultDTO setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public GetRecommendGuardianResultDTO setName(String name) {
        this.name = name;
        return this;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public GetRecommendGuardianResultDTO setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }
}
