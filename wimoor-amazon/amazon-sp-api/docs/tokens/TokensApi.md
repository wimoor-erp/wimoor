# TokensApi

All URIs are relative to *https://sellingpartnerapi-na.amazon.com*

Method | HTTP request | Description
------------- | ------------- | -------------
[**createRestrictedDataToken**](TokensApi.md#createRestrictedDataToken) | **POST** /tokens/2021-03-01/restrictedDataToken | 


<a name="createRestrictedDataToken"></a>
# **createRestrictedDataToken**
> CreateRestrictedDataTokenResponse createRestrictedDataToken(body)



Returns a Restricted Data Token (RDT) for one or more restricted resources that you specify. A restricted resource is the HTTP method and path from a restricted operation that returns Personally Identifiable Information (PII). See the Tokens API Use Case Guide for a list of restricted operations. Use the RDT returned here as the access token in subsequent calls to the corresponding restricted operations.  The path of a restricted resource can be: - A specific path containing a seller&#39;s order ID, for example &#x60;&#x60;&#x60;/orders/v0/orders/902-3159896-1390916/address&#x60;&#x60;&#x60;. The returned RDT authorizes a subsequent call to the getOrderAddress operation of the Orders API for that specific order only. For example, &#x60;&#x60;&#x60;GET /orders/v0/orders/902-3159896-1390916/address&#x60;&#x60;&#x60;. - A generic path that does not contain a seller&#39;s order ID, for example&#x60;&#x60;&#x60;/orders/v0/orders/{orderId}/address&#x60;&#x60;&#x60;). The returned RDT authorizes subsequent calls to the getOrderAddress operation for *any* of a seller&#39;s order IDs. For example, &#x60;&#x60;&#x60;GET /orders/v0/orders/902-3159896-1390916/address&#x60;&#x60;&#x60; and &#x60;&#x60;&#x60;GET /orders/v0/orders/483-3488972-0896720/address&#x60;&#x60;&#x60;  **Usage Plans:**  | Plan type | Rate (requests per second) | Burst | | ---- | ---- | ---- | |Default| 1 | 10 | |Selling partner specific| Variable | Variable |  The x-amzn-RateLimit-Limit response header returns the usage plan rate limits that were applied to the requested operation. Rate limits for some selling partners will vary from the default rate and burst shown in the table above. For more information, see \&quot;Usage Plans and Rate Limits\&quot; in the Selling Partner API documentation.

### Example
```java
// Import classes:
//import com.amazon.spapi.tokens.ApiException;
//import com.amazon.spapi.tokens.api.TokensApi;


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

