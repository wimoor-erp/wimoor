package com.amazon.spapi.SellingPartnerAPIAA;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
class LWAAccessTokenRequestMeta {
    @SerializedName("grant_type")
    private String grantType;

    @SerializedName("refresh_token")
    private String refreshToken;

    @SerializedName("client_id")
    private String clientId;

    @SerializedName("client_secret")
    private String clientSecret;

    @SerializedName("scope")
    private LWAClientScopes scopes;

}
