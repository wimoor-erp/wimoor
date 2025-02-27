package com.amazon.spapi.SellingPartnerAPIAA;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.EnumUtils;

class LWAClient {
    private static final String ACCESS_TOKEN_KEY = "access_token";
    private static final String ACCESS_TOKEN_EXPIRES_IN = "expires_in";
    private static final MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");
    @Getter
    private String endpoint;

    @Setter(AccessLevel.PACKAGE)
    private OkHttpClient okHttpClient;
    private LWAAccessTokenCache lwaAccessTokenCache;

    /**
     * Sets cache to store access token until token is expired
     */
    public void setLWAAccessTokenCache(LWAAccessTokenCache tokenCache) {
        this.lwaAccessTokenCache = tokenCache;
    }

    LWAClient(String endpoint) {
        okHttpClient = new OkHttpClient();
        this.endpoint = endpoint;
    }

    String getAccessToken(LWAAccessTokenRequestMeta lwaAccessTokenRequestMeta) throws LWAException {
        if (lwaAccessTokenCache != null) {
            return getAccessTokenFromCache(lwaAccessTokenRequestMeta);
        } else {
            return getAccessTokenFromEndpoint(lwaAccessTokenRequestMeta);
        }
    }

    String getAccessTokenFromCache(LWAAccessTokenRequestMeta lwaAccessTokenRequestMeta) throws LWAException {
        String accessTokenCacheData = (String) lwaAccessTokenCache.get(lwaAccessTokenRequestMeta);
        if (accessTokenCacheData != null) {
            return accessTokenCacheData;
        } else {
            return getAccessTokenFromEndpoint(lwaAccessTokenRequestMeta);
        }
    }

    String getAccessTokenFromEndpoint(LWAAccessTokenRequestMeta lwaAccessTokenRequestMeta) throws LWAException {
        RequestBody requestBody = RequestBody.create(JSON_MEDIA_TYPE, new Gson().toJson(lwaAccessTokenRequestMeta));
        Request accessTokenRequest = new Request.Builder().url(endpoint).post(requestBody).build();
        LWAExceptionErrorCode lwaErrorCode = null;
        String accessToken;
        try {
            Response response = okHttpClient.newCall(accessTokenRequest).execute();
            JsonObject responseJson = new JsonParser().parse(response.body().string()).getAsJsonObject();
            if (!response.isSuccessful()) {
                // Check if response has element error and is a known LWA error code
                if (responseJson.has("error") &&
                        EnumUtils.isValidEnum(LWAExceptionErrorCode.class, responseJson.get("error").getAsString())) {
                    throw new LWAException(responseJson.get("error").getAsString(),
                            responseJson.get("error_description").getAsString(), "Error getting LWA Token");
                } else { // else throw other LWA error
                    throw new LWAException(LWAExceptionErrorCode.other.toString(), "Other LWA Exception",
                            "Error getting LWA Token");
                }
            }
            accessToken = responseJson.get(ACCESS_TOKEN_KEY).getAsString();
            if (lwaAccessTokenCache != null) {
                long timeToTokenexpiry = responseJson.get(ACCESS_TOKEN_EXPIRES_IN).getAsLong();
                lwaAccessTokenCache.put(lwaAccessTokenRequestMeta, accessToken, timeToTokenexpiry);
            }
        } catch (LWAException e) { // throw LWA exception
            throw new LWAException(e.getErrorCode(), e.getErrorMessage(), e.getMessage());
        } catch (Exception e) { // throw other runtime exceptions
            throw new RuntimeException("Error getting LWA Token");
        }
        return accessToken;
    }
}
