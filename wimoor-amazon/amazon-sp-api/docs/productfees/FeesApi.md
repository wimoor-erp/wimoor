# FeesApi

All URIs are relative to *https://sellingpartnerapi-na.amazon.com*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getMyFeesEstimateForASIN**](FeesApi.md#getMyFeesEstimateForASIN) | **POST** /products/fees/v0/items/{Asin}/feesEstimate | 
[**getMyFeesEstimateForSKU**](FeesApi.md#getMyFeesEstimateForSKU) | **POST** /products/fees/v0/listings/{SellerSKU}/feesEstimate | 


<a name="getMyFeesEstimateForASIN"></a>
# **getMyFeesEstimateForASIN**
> GetMyFeesEstimateResponse getMyFeesEstimateForASIN(body, asin)



Returns the estimated fees for the item indicated by the specified Asin in the marketplace specified in the request body.  You can call getMyFeesEstimateForASIN for an item on behalf of a seller before the seller sets the item&#39;s price. They can then take estimated fees into account. With each product fees request, you must include an original identifier. This identifier is included in the fees estimate so you can correlate a fees estimate with the original request.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 1 | 1 |  For more information, see \&quot;Usage Plans and Rate Limits\&quot; in the Selling Partner API documentation.

### Example
```java
// Import classes:
//import com.amazon.spapi.productfees.ApiException;
//import com.amazon.spapi.productfees.api.FeesApi;


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



Returns the estimated fees for the item indicated by the specified seller SKU in the marketplace specified in the request body.  You can call getMyFeesEstimateForSKU for an item on behalf of a seller before the seller sets the item&#39;s price. They can then take estimated fees into account. With each fees estimate request, you must include an original identifier. This identifier is included in the fees estimate so you can correlate a fees estimate with the original request.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 1 | 1 |  For more information, see \&quot;Usage Plans and Rate Limits\&quot; in the Selling Partner API documentation.

### Example
```java
// Import classes:
//import com.amazon.spapi.productfees.ApiException;
//import com.amazon.spapi.productfees.api.FeesApi;


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

