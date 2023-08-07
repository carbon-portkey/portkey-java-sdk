package io.aelf.portkey.internal.model.apple;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.Nullable;

public class AppleExtraInfoParams {
    @JsonProperty("identityToken")
    private String identityToken;
    @JsonProperty("userInfo")
    private UserInfo userInfo;

    public String getIdentityToken() {
        return identityToken;
    }

    public AppleExtraInfoParams setIdentityToken(String identityToken) {
        this.identityToken = identityToken;
        return this;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public AppleExtraInfoParams setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
        return this;
    }

    public static class UserInfo {
        @JsonProperty("name")
        private Name name;
        @JsonProperty("email")
        private @Nullable String email;

        public Name getName() {
            return name;
        }

        public UserInfo setName(Name name) {
            this.name = name;
            return this;
        }

        public @Nullable String getEmail() {
            return email;
        }

        public UserInfo setEmail(@Nullable String email) {
            this.email = email;
            return this;
        }

        public static class Name {
            @JsonProperty("firstName")
            private String firstName;
            @JsonProperty("lastName")
            private @Nullable String lastName;

            public String getFirstName() {
                return firstName;
            }

            public Name setFirstName(String firstName) {
                this.firstName = firstName;
                return this;
            }

            public @Nullable String getLastName() {
                return lastName;
            }

            public Name setLastName(@Nullable String lastName) {
                this.lastName = lastName;
                return this;
            }
        }
    }
}
