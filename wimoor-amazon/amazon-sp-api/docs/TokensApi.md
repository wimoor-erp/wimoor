# TokensApi

All URIs are relative to *https://sellingpartnerapi-na.amazon.com*

Method | HTTP request | Description
------------- | ------------- | -------------
[**createRestrictedDataToken**](TokensApi.md#createRestrictedDataToken) | **POST** /tokens/2021-03-01/restrictedDataToken | 


<a name="createRestrictedDataToken"></a>
# **createRestrictedDataToken**
> CreateRestrictedDataTokenResponse createRestrictedDataToken(body)



Returns a Restricted Data Token (RDT) for one or more restricted resources that you specify. A restricted resource is the HTTP method and path from a restricted operation that returns Personally Identifiable Information (PII), plus a dataElements value that indicates the type of PII requested. See the Tokens API Use Case Guide for a list of restricted operations. Use the RDT returned here as the access token in subsequent calls to the corresponding restricted operations.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 1 | 10 |  The &#x60;x-amzn-RateLimit-Limit&#x60; response header returns the usage plan rate limits that were applied to the requested operation, when available. The table above indicates the default rate and burst values for this operation. Selling partners whose business demands require higher throughput may see higher rate and burst values than those shown here. For more information, see [Usage Plans and Rate Limits in the Selling Partner API](https://developer-docs.amazon.com/sp-api/docs/usage-plans-and-rate-limits-in-the-sp-api).

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.TokensApi;


TokensApi apiInstance = new TokensApi();
CreateRestrictedDataTokenRequest body = new CreateRestrictedDataTokenRequest(); // CreateRestrictedDataTokenRequest | The restricted data token request details.
try {
    CreateRestrictedDataTokenResponse result = apiInstance.createRestrictedDataToken(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling TokensApi#createRestrictedDataToken");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**CreateRestrictedDataTokenRequest**](CreateRestrictedDataTokenRequest.md)| The restricted data token request details. |

### Return type

[**CreateRestrictedDataTokenResponse**](CreateRestrictedDataTokenResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

