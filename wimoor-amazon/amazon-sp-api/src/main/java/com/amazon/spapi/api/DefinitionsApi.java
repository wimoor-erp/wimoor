/*
 * Selling Partner API for Product Type Definitions
 * The Selling Partner API for Product Type Definitions provides programmatic access to attribute and data requirements for product types in the Amazon catalog. Use this API to return the JSON Schema for a product type that you can then use with other Selling Partner APIs, such as the Selling Partner API for Listings Items, the Selling Partner API for Catalog Items, and the Selling Partner API for Feeds (for JSON-based listing feeds).  For more information, see the [Product Type Definitions API Use Case Guide](doc:product-type-api-use-case-guide).
 *
 * OpenAPI spec version: 2020-09-01
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


package com.amazon.spapi.api;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazon.spapi.SellingPartnerAPIAA.LWAAccessTokenCache;
import com.amazon.spapi.SellingPartnerAPIAA.LWAAccessTokenCacheImpl;
import com.amazon.spapi.SellingPartnerAPIAA.LWAAuthorizationCredentials;
import com.amazon.spapi.SellingPartnerAPIAA.LWAAuthorizationSigner;
import com.amazon.spapi.SellingPartnerAPIAA.LWAException;
import com.amazon.spapi.SellingPartnerAPIAA.RateLimitConfiguration;
import com.amazon.spapi.client.ApiCallback;
import com.amazon.spapi.client.ApiClient;
import com.amazon.spapi.client.ApiException;
import com.amazon.spapi.client.ApiResponse;
import com.amazon.spapi.client.Configuration;
import com.amazon.spapi.client.Pair;
import com.amazon.spapi.client.ProgressRequestBody;
import com.amazon.spapi.client.ProgressResponseBody;
import com.amazon.spapi.client.StringUtil;
import com.amazon.spapi.model.definitions.ProductTypeDefinition;
import com.amazon.spapi.model.definitions.ProductTypeList;
import com.google.gson.reflect.TypeToken;
import okhttp3.Call;
import okhttp3.Interceptor;
import okhttp3.Response;

public class DefinitionsApi {
    private ApiClient apiClient;

    DefinitionsApi() {
        this(Configuration.getDefaultApiClient());
    }

    public DefinitionsApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Build call for getDefinitionsProductType
     * @param productType The Amazon product type name. (required)
     * @param marketplaceIds A comma-delimited list of Amazon marketplace identifiers for the request. Note: This parameter is limited to one marketplaceId at this time. (required)
     * @param sellerId A selling partner identifier. When provided, seller-specific requirements and values are populated within the product type definition schema, such as brand names associated with the selling partner. (optional)
     * @param productTypeVersion The version of the Amazon product type to retrieve. Defaults to \&quot;LATEST\&quot;,. Prerelease versions of product type definitions may be retrieved with \&quot;RELEASE_CANDIDATE\&quot;. If no prerelease version is currently available, the \&quot;LATEST\&quot; live version will be provided. (optional, default to LATEST)
     * @param requirements The name of the requirements set to retrieve requirements for. (optional, default to LISTING)
     * @param requirementsEnforced Identifies if the required attributes for a requirements set are enforced by the product type definition schema. Non-enforced requirements enable structural validation of individual attributes without all the required attributes being present (such as for partial updates). (optional, default to ENFORCED)
     * @param locale Locale for retrieving display labels and other presentation details. Defaults to the default language of the first marketplace in the request. (optional, default to DEFAULT)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     * @throws LWAException If calls to fetch LWA access token fails
     */
    public Call getDefinitionsProductTypeCall(String productType, List<String> marketplaceIds, String sellerId, String productTypeVersion, String requirements, String requirementsEnforced, String locale, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException, LWAException {
        Object localVarPostBody = null;

        // create path and map variables
        String localVarPath = "/definitions/2020-09-01/productTypes/{productType}"
            .replaceAll("\\{" + "productType" + "\\}", apiClient.escapeString(productType.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (sellerId != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sellerId", sellerId));
        if (marketplaceIds != null)
        localVarCollectionQueryParams.addAll(apiClient.parameterToPairs("csv", "marketplaceIds", marketplaceIds));
        if (productTypeVersion != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("productTypeVersion", productTypeVersion));
        if (requirements != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("requirements", requirements));
        if (requirementsEnforced != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("requirementsEnforced", requirementsEnforced));
        if (locale != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("locale", locale));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.addNetworkInterceptor(new Interceptor() {
                @Override
                public Response intercept(Interceptor.Chain chain) throws IOException {
                    Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] {  };
        return apiClient.buildCall(localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    @SuppressWarnings("rawtypes")
    private Call getDefinitionsProductTypeValidateBeforeCall(String productType, List<String> marketplaceIds, String sellerId, String productTypeVersion, String requirements, String requirementsEnforced, String locale, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException, LWAException {
        
        // verify the required parameter 'productType' is set
        if (productType == null) {
            throw new ApiException("Missing the required parameter 'productType' when calling getDefinitionsProductType(Async)");
        }
        
        // verify the required parameter 'marketplaceIds' is set
        if (marketplaceIds == null) {
            throw new ApiException("Missing the required parameter 'marketplaceIds' when calling getDefinitionsProductType(Async)");
        }
        

        Call call = getDefinitionsProductTypeCall(productType, marketplaceIds, sellerId, productTypeVersion, requirements, requirementsEnforced, locale, progressListener, progressRequestListener);
        return call;

    }

    /**
     * 
     * Retrieve an Amazon product type definition.  **Usage Plans:**  | Plan type | Rate (requests per second) | Burst | | ---- | ---- | ---- | |Default| 5 | 10 | |Selling partner specific| Variable | Variable |  The x-amzn-RateLimit-Limit response header returns the usage plan rate limits that were applied to the requested operation. Rate limits for some selling partners will vary from the default rate and burst shown in the table above. For more information, see [Usage Plans and Rate Limits in the Selling Partner API](doc:usage-plans-and-rate-limits-in-the-sp-api).
     * @param productType The Amazon product type name. (required)
     * @param marketplaceIds A comma-delimited list of Amazon marketplace identifiers for the request. Note: This parameter is limited to one marketplaceId at this time. (required)
     * @param sellerId A selling partner identifier. When provided, seller-specific requirements and values are populated within the product type definition schema, such as brand names associated with the selling partner. (optional)
     * @param productTypeVersion The version of the Amazon product type to retrieve. Defaults to \&quot;LATEST\&quot;,. Prerelease versions of product type definitions may be retrieved with \&quot;RELEASE_CANDIDATE\&quot;. If no prerelease version is currently available, the \&quot;LATEST\&quot; live version will be provided. (optional, default to LATEST)
     * @param requirements The name of the requirements set to retrieve requirements for. (optional, default to LISTING)
     * @param requirementsEnforced Identifies if the required attributes for a requirements set are enforced by the product type definition schema. Non-enforced requirements enable structural validation of individual attributes without all the required attributes being present (such as for partial updates). (optional, default to ENFORCED)
     * @param locale Locale for retrieving display labels and other presentation details. Defaults to the default language of the first marketplace in the request. (optional, default to DEFAULT)
     * @return ProductTypeDefinition
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     * @throws LWAException If calls to fetch LWA access token fails
     */
    public ProductTypeDefinition getDefinitionsProductType(String productType, List<String> marketplaceIds, String sellerId, String productTypeVersion, String requirements, String requirementsEnforced, String locale) throws ApiException,LWAException {
        ApiResponse<ProductTypeDefinition> resp = getDefinitionsProductTypeWithHttpInfo(productType, marketplaceIds, sellerId, productTypeVersion, requirements, requirementsEnforced, locale);
        return resp.getData();
    }

    /**
     * 
     * Retrieve an Amazon product type definition.  **Usage Plans:**  | Plan type | Rate (requests per second) | Burst | | ---- | ---- | ---- | |Default| 5 | 10 | |Selling partner specific| Variable | Variable |  The x-amzn-RateLimit-Limit response header returns the usage plan rate limits that were applied to the requested operation. Rate limits for some selling partners will vary from the default rate and burst shown in the table above. For more information, see [Usage Plans and Rate Limits in the Selling Partner API](doc:usage-plans-and-rate-limits-in-the-sp-api).
     * @param productType The Amazon product type name. (required)
     * @param marketplaceIds A comma-delimited list of Amazon marketplace identifiers for the request. Note: This parameter is limited to one marketplaceId at this time. (required)
     * @param sellerId A selling partner identifier. When provided, seller-specific requirements and values are populated within the product type definition schema, such as brand names associated with the selling partner. (optional)
     * @param productTypeVersion The version of the Amazon product type to retrieve. Defaults to \&quot;LATEST\&quot;,. Prerelease versions of product type definitions may be retrieved with \&quot;RELEASE_CANDIDATE\&quot;. If no prerelease version is currently available, the \&quot;LATEST\&quot; live version will be provided. (optional, default to LATEST)
     * @param requirements The name of the requirements set to retrieve requirements for. (optional, default to LISTING)
     * @param requirementsEnforced Identifies if the required attributes for a requirements set are enforced by the product type definition schema. Non-enforced requirements enable structural validation of individual attributes without all the required attributes being present (such as for partial updates). (optional, default to ENFORCED)
     * @param locale Locale for retrieving display labels and other presentation details. Defaults to the default language of the first marketplace in the request. (optional, default to DEFAULT)
     * @return ApiResponse&lt;ProductTypeDefinition&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     * @throws LWAException If calls to fetch LWA access token fails
     */
    public ApiResponse<ProductTypeDefinition> getDefinitionsProductTypeWithHttpInfo(String productType, List<String> marketplaceIds, String sellerId, String productTypeVersion, String requirements, String requirementsEnforced, String locale) throws ApiException,LWAException {
        Call call = getDefinitionsProductTypeValidateBeforeCall(productType, marketplaceIds, sellerId, productTypeVersion, requirements, requirementsEnforced, locale, null, null);
        Type localVarReturnType = new TypeToken<ProductTypeDefinition>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     *  (asynchronously)
     * Retrieve an Amazon product type definition.  **Usage Plans:**  | Plan type | Rate (requests per second) | Burst | | ---- | ---- | ---- | |Default| 5 | 10 | |Selling partner specific| Variable | Variable |  The x-amzn-RateLimit-Limit response header returns the usage plan rate limits that were applied to the requested operation. Rate limits for some selling partners will vary from the default rate and burst shown in the table above. For more information, see [Usage Plans and Rate Limits in the Selling Partner API](doc:usage-plans-and-rate-limits-in-the-sp-api).
     * @param productType The Amazon product type name. (required)
     * @param marketplaceIds A comma-delimited list of Amazon marketplace identifiers for the request. Note: This parameter is limited to one marketplaceId at this time. (required)
     * @param sellerId A selling partner identifier. When provided, seller-specific requirements and values are populated within the product type definition schema, such as brand names associated with the selling partner. (optional)
     * @param productTypeVersion The version of the Amazon product type to retrieve. Defaults to \&quot;LATEST\&quot;,. Prerelease versions of product type definitions may be retrieved with \&quot;RELEASE_CANDIDATE\&quot;. If no prerelease version is currently available, the \&quot;LATEST\&quot; live version will be provided. (optional, default to LATEST)
     * @param requirements The name of the requirements set to retrieve requirements for. (optional, default to LISTING)
     * @param requirementsEnforced Identifies if the required attributes for a requirements set are enforced by the product type definition schema. Non-enforced requirements enable structural validation of individual attributes without all the required attributes being present (such as for partial updates). (optional, default to ENFORCED)
     * @param locale Locale for retrieving display labels and other presentation details. Defaults to the default language of the first marketplace in the request. (optional, default to DEFAULT)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     * @throws LWAException If calls to fetch LWA access token fails
     */
    public Call getDefinitionsProductTypeAsync(String productType, List<String> marketplaceIds, String sellerId, String productTypeVersion, String requirements, String requirementsEnforced, String locale, final ApiCallback<ProductTypeDefinition> callback) throws ApiException, LWAException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        Call call = getDefinitionsProductTypeValidateBeforeCall(productType, marketplaceIds, sellerId, productTypeVersion, requirements, requirementsEnforced, locale, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<ProductTypeDefinition>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for searchDefinitionsProductTypes
     * @param marketplaceIds A comma-delimited list of Amazon marketplace identifiers for the request. (required)
     * @param keywords A comma-delimited list of keywords to search product types. **Note:** Cannot be used with &#x60;itemName&#x60;. (optional)
     * @param itemName The title of the ASIN to get the product type recommendation. **Note:** Cannot be used with &#x60;keywords&#x60;. (optional)
     * @param locale The locale for the display names in the response. Defaults to the primary locale of the marketplace. (optional)
     * @param searchLocale The locale used for the &#x60;keywords&#x60; and &#x60;itemName&#x60; parameters. Defaults to the primary locale of the marketplace. (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     * @throws LWAException If calls to fetch LWA access token fails
     */
    public Call searchDefinitionsProductTypesCall(List<String> marketplaceIds, List<String> keywords, String itemName, String locale, String searchLocale, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException, LWAException {
        Object localVarPostBody = null;

        // create path and map variables
        String localVarPath = "/definitions/2020-09-01/productTypes";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (keywords != null)
        localVarCollectionQueryParams.addAll(apiClient.parameterToPairs("csv", "keywords", keywords));
        if (marketplaceIds != null)
        localVarCollectionQueryParams.addAll(apiClient.parameterToPairs("csv", "marketplaceIds", marketplaceIds));
        if (itemName != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("itemName", itemName));
        if (locale != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("locale", locale));
        if (searchLocale != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("searchLocale", searchLocale));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.addNetworkInterceptor(new Interceptor() {
                @Override
                public Response intercept(Interceptor.Chain chain) throws IOException {
                    Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] {  };
        return apiClient.buildCall(localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    @SuppressWarnings("rawtypes")
    private Call searchDefinitionsProductTypesValidateBeforeCall(List<String> marketplaceIds, List<String> keywords, String itemName, String locale, String searchLocale, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException, LWAException {
        
        // verify the required parameter 'marketplaceIds' is set
        if (marketplaceIds == null) {
            throw new ApiException("Missing the required parameter 'marketplaceIds' when calling searchDefinitionsProductTypes(Async)");
        }
        

        Call call = searchDefinitionsProductTypesCall(marketplaceIds, keywords, itemName, locale, searchLocale, progressListener, progressRequestListener);
        return call;

    }

    /**
     * 
     * Search for and return a list of Amazon product types that have definitions available.  **Usage Plans:**  | Plan type | Rate (requests per second) | Burst | | ---- | ---- | ---- | |Default| 5 | 10 | |Selling partner specific| Variable | Variable |  The x-amzn-RateLimit-Limit response header returns the usage plan rate limits that were applied to the requested operation. Rate limits for some selling partners will vary from the default rate and burst shown in the table above. For more information, see [Usage Plans and Rate Limits in the Selling Partner API](doc:usage-plans-and-rate-limits-in-the-sp-api).
     * @param marketplaceIds A comma-delimited list of Amazon marketplace identifiers for the request. (required)
     * @param keywords A comma-delimited list of keywords to search product types. **Note:** Cannot be used with &#x60;itemName&#x60;. (optional)
     * @param itemName The title of the ASIN to get the product type recommendation. **Note:** Cannot be used with &#x60;keywords&#x60;. (optional)
     * @param locale The locale for the display names in the response. Defaults to the primary locale of the marketplace. (optional)
     * @param searchLocale The locale used for the &#x60;keywords&#x60; and &#x60;itemName&#x60; parameters. Defaults to the primary locale of the marketplace. (optional)
     * @return ProductTypeList
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     * @throws LWAException If calls to fetch LWA access token fails
     */
    public ProductTypeList searchDefinitionsProductTypes(List<String> marketplaceIds, List<String> keywords, String itemName, String locale, String searchLocale) throws ApiException,LWAException {
        ApiResponse<ProductTypeList> resp = searchDefinitionsProductTypesWithHttpInfo(marketplaceIds, keywords, itemName, locale, searchLocale);
        return resp.getData();
    }

    /**
     * 
     * Search for and return a list of Amazon product types that have definitions available.  **Usage Plans:**  | Plan type | Rate (requests per second) | Burst | | ---- | ---- | ---- | |Default| 5 | 10 | |Selling partner specific| Variable | Variable |  The x-amzn-RateLimit-Limit response header returns the usage plan rate limits that were applied to the requested operation. Rate limits for some selling partners will vary from the default rate and burst shown in the table above. For more information, see [Usage Plans and Rate Limits in the Selling Partner API](doc:usage-plans-and-rate-limits-in-the-sp-api).
     * @param marketplaceIds A comma-delimited list of Amazon marketplace identifiers for the request. (required)
     * @param keywords A comma-delimited list of keywords to search product types. **Note:** Cannot be used with &#x60;itemName&#x60;. (optional)
     * @param itemName The title of the ASIN to get the product type recommendation. **Note:** Cannot be used with &#x60;keywords&#x60;. (optional)
     * @param locale The locale for the display names in the response. Defaults to the primary locale of the marketplace. (optional)
     * @param searchLocale The locale used for the &#x60;keywords&#x60; and &#x60;itemName&#x60; parameters. Defaults to the primary locale of the marketplace. (optional)
     * @return ApiResponse&lt;ProductTypeList&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     * @throws LWAException If calls to fetch LWA access token fails
     */
    public ApiResponse<ProductTypeList> searchDefinitionsProductTypesWithHttpInfo(List<String> marketplaceIds, List<String> keywords, String itemName, String locale, String searchLocale) throws ApiException,LWAException {
        Call call = searchDefinitionsProductTypesValidateBeforeCall(marketplaceIds, keywords, itemName, locale, searchLocale, null, null);
        Type localVarReturnType = new TypeToken<ProductTypeList>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     *  (asynchronously)
     * Search for and return a list of Amazon product types that have definitions available.  **Usage Plans:**  | Plan type | Rate (requests per second) | Burst | | ---- | ---- | ---- | |Default| 5 | 10 | |Selling partner specific| Variable | Variable |  The x-amzn-RateLimit-Limit response header returns the usage plan rate limits that were applied to the requested operation. Rate limits for some selling partners will vary from the default rate and burst shown in the table above. For more information, see [Usage Plans and Rate Limits in the Selling Partner API](doc:usage-plans-and-rate-limits-in-the-sp-api).
     * @param marketplaceIds A comma-delimited list of Amazon marketplace identifiers for the request. (required)
     * @param keywords A comma-delimited list of keywords to search product types. **Note:** Cannot be used with &#x60;itemName&#x60;. (optional)
     * @param itemName The title of the ASIN to get the product type recommendation. **Note:** Cannot be used with &#x60;keywords&#x60;. (optional)
     * @param locale The locale for the display names in the response. Defaults to the primary locale of the marketplace. (optional)
     * @param searchLocale The locale used for the &#x60;keywords&#x60; and &#x60;itemName&#x60; parameters. Defaults to the primary locale of the marketplace. (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     * @throws LWAException If calls to fetch LWA access token fails
     */
    public Call searchDefinitionsProductTypesAsync(List<String> marketplaceIds, List<String> keywords, String itemName, String locale, String searchLocale, final ApiCallback<ProductTypeList> callback) throws ApiException, LWAException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        Call call = searchDefinitionsProductTypesValidateBeforeCall(marketplaceIds, keywords, itemName, locale, searchLocale, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<ProductTypeList>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }

    public static class Builder {
        private LWAAuthorizationCredentials lwaAuthorizationCredentials;
        private String endpoint;
        private LWAAccessTokenCache lwaAccessTokenCache;
        private Boolean disableAccessTokenCache = false;
        private RateLimitConfiguration rateLimitConfiguration;


        public Builder lwaAuthorizationCredentials(LWAAuthorizationCredentials lwaAuthorizationCredentials) {
            this.lwaAuthorizationCredentials = lwaAuthorizationCredentials;
            return this;
        }

        public Builder endpoint(String endpoint) {
            this.endpoint = endpoint;
            return this;
        }
        
        public Builder lwaAccessTokenCache(LWAAccessTokenCache lwaAccessTokenCache) {
            this.lwaAccessTokenCache = lwaAccessTokenCache;
            return this;
        }
		
	   public Builder disableAccessTokenCache() {
            this.disableAccessTokenCache = true;
            return this;
        }

        public Builder rateLimitConfigurationOnRequests(RateLimitConfiguration rateLimitConfiguration){
            this.rateLimitConfiguration = rateLimitConfiguration;
            return this;
        }
        
        public Builder disableRateLimitOnRequests() {
            this.rateLimitConfiguration = null;
            return this;
        }

        public DefinitionsApi build() {
            if (lwaAuthorizationCredentials == null) {
                throw new RuntimeException("LWAAuthorizationCredentials not set");
            }

            if (StringUtil.isEmpty(endpoint)) {
                throw new RuntimeException("Endpoint not set");
            }
            
            LWAAuthorizationSigner lwaAuthorizationSigner = null;            
            if (disableAccessTokenCache) {
                lwaAuthorizationSigner = new LWAAuthorizationSigner(lwaAuthorizationCredentials);
            }
            else {
                if (lwaAccessTokenCache == null) {
                    lwaAccessTokenCache = new LWAAccessTokenCacheImpl();                  
                 }
                 lwaAuthorizationSigner = new LWAAuthorizationSigner(lwaAuthorizationCredentials,lwaAccessTokenCache);
            }

            return new DefinitionsApi(new ApiClient(rateLimitConfiguration)
                .setLWAAuthorizationSigner(lwaAuthorizationSigner)
                .setBasePath(endpoint)
                );
        }
    }
}
