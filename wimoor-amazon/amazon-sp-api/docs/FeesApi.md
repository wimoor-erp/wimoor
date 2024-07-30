# FeesApi

All URIs are relative to *https://sellingpartnerapi-na.amazon.com*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getMyFeesEstimateForASIN**](FeesApi.md#getMyFeesEstimateForASIN) | **POST** /products/fees/v0/items/{Asin}/feesEstimate | 
[**getMyFeesEstimateForSKU**](FeesApi.md#getMyFeesEstimateForSKU) | **POST** /products/fees/v0/listings/{SellerSKU}/feesEstimate | 
[**getMyFeesEstimates**](FeesApi.md#getMyFeesEstimates) | **POST** /products/fees/v0/feesEstimate | 


<a name="getMyFeesEstimateForASIN"></a>
# **getMyFeesEstimateForASIN**
> GetMyFeesEstimateResponse getMyFeesEstimateForASIN(body, asin)



Returns the estimated fees for the item indicated by the specified ASIN in the marketplace specified in the request body.  You can call &#x60;getMyFeesEstimateForASIN&#x60; for an item on behalf of a selling partner before the selling partner sets the item&#39;s price. The selling partner can then take estimated fees into account. Each fees request must include an original identifier. This identifier is included in the fees estimate so you can correlate a fees estimate with the original request.  **Note:** This identifier value is used to identify an estimate. Actual costs may vary. Search \&quot;fees\&quot; in [Seller Central](https://sellercentral.amazon.com/) and consult the store-specific fee schedule for the most up-to-date information.  **Note:** When using the &#x60;getMyFeesEstimateForASIN&#x60; operation with an ASIN, the fee estimates might be different. This is because these estimates use the item&#39;s catalog size, which might not always match the actual size of the item sent to Amazon.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 1 | 2 |  The &#x60;x-amzn-RateLimit-Limit&#x60; response header returns the usage plan rate limits that were applied to the requested operation, when available. The table above indicates the default rate and burst values for this operation. Selling partners whose business demands require higher throughput may see higher rate and burst values than those shown here. For more information, see [Usage Plans and Rate Limits in the Selling Partner API](doc:usage-plans-and-rate-limits-in-the-sp-api).

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.FeesApi;


FeesApi apiInstance = new FeesApi();
GetMyFeesEstimateRequest body = new GetMyFeesEstimateRequest(); // GetMyFeesEstimateRequest | 
String asin = "asin_example"; // String | The Amazon Standard Identification Number (ASIN) of the item.
try {
    GetMyFeesEstimateResponse result = apiInstance.getMyFeesEstimateForASIN(body, asin);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling FeesApi#getMyFeesEstimateForASIN");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**GetMyFeesEstimateRequest**](GetMyFeesEstimateRequest.md)|  |
 **asin** | **String**| The Amazon Standard Identification Number (ASIN) of the item. |

### Return type

[**GetMyFeesEstimateResponse**](GetMyFeesEstimateResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getMyFeesEstimateForSKU"></a>
# **getMyFeesEstimateForSKU**
> GetMyFeesEstimateResponse getMyFeesEstimateForSKU(body, sellerSKU)



Returns the estimated fees for the item indicated by the specified seller SKU in the marketplace specified in the request body.  **Note:** The parameters associated with this operation may contain special characters that require URL encoding to call the API. To avoid errors with SKUs when encoding URLs, refer to [URL Encoding](https://developer-docs.amazon.com/sp-api/docs/url-encoding).  You can call &#x60;getMyFeesEstimateForSKU&#x60; for an item on behalf of a selling partner before the selling partner sets the item&#39;s price. The selling partner can then take any estimated fees into account. Each fees estimate request must include an original identifier. This identifier is included in the fees estimate so that you can correlate a fees estimate with the original request.  **Note:** This identifier value is used to identify an estimate. Actual costs may vary. Search \&quot;fees\&quot; in [Seller Central](https://sellercentral.amazon.com/) and consult the store-specific fee schedule for the most up-to-date information.  **Note:** When sellers use the &#x60;getMyFeesEstimateForSKU&#x60; operation with their &#x60;SellerSKU&#x60;, they get accurate fees based on real item measurements, but only after they&#39;ve sent their items to Amazon.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 1 | 2 |  The &#x60;x-amzn-RateLimit-Limit&#x60; response header returns the usage plan rate limits that were applied to the requested operation, when available. The table above indicates the default rate and burst values for this operation. Selling partners whose business demands require higher throughput may see higher rate and burst values than those shown here. For more information, see [Usage Plans and Rate Limits in the Selling Partner API](doc:usage-plans-and-rate-limits-in-the-sp-api).

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.FeesApi;


FeesApi apiInstance = new FeesApi();
GetMyFeesEstimateRequest body = new GetMyFeesEstimateRequest(); // GetMyFeesEstimateRequest | 
String sellerSKU = "sellerSKU_example"; // String | Used to identify an item in the given marketplace. SellerSKU is qualified by the seller's SellerId, which is included with every operation that you submit.
try {
    GetMyFeesEstimateResponse result = apiInstance.getMyFeesEstimateForSKU(body, sellerSKU);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling FeesApi#getMyFeesEstimateForSKU");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**GetMyFeesEstimateRequest**](GetMyFeesEstimateRequest.md)|  |
 **sellerSKU** | **String**| Used to identify an item in the given marketplace. SellerSKU is qualified by the seller&#39;s SellerId, which is included with every operation that you submit. |

### Return type

[**GetMyFeesEstimateResponse**](GetMyFeesEstimateResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getMyFeesEstimates"></a>
# **getMyFeesEstimates**
> GetMyFeesEstimatesResponse getMyFeesEstimates(body)



Returns the estimated fees for a list of products.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 0.5 | 1 |  The &#x60;x-amzn-RateLimit-Limit&#x60; response header returns the usage plan rate limits that were applied to the requested operation, when available. The table above indicates the default rate and burst values for this operation. Selling partners whose business demands require higher throughput may see higher rate and burst values than those shown here. For more information, see [Usage Plans and Rate Limits in the Selling Partner API](doc:usage-plans-and-rate-limits-in-the-sp-api).

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.FeesApi;


FeesApi apiInstance = new FeesApi();
GetMyFeesEstimatesRequest body = new GetMyFeesEstimatesRequest(); // GetMyFeesEstimatesRequest | 
try {
    GetMyFeesEstimatesResponse result = apiInstance.getMyFeesEstimates(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling FeesApi#getMyFeesEstimates");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**GetMyFeesEstimatesRequest**](GetMyFeesEstimatesRequest.md)|  |

### Return type

[**GetMyFeesEstimatesResponse**](GetMyFeesEstimatesResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

