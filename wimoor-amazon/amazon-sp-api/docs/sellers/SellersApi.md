# SellersApi

All URIs are relative to *https://sellingpartnerapi-na.amazon.com*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getMarketplaceParticipations**](SellersApi.md#getMarketplaceParticipations) | **GET** /sellers/v1/marketplaceParticipations | 


<a name="getMarketplaceParticipations"></a>
# **getMarketplaceParticipations**
> GetMarketplaceParticipationsResponse getMarketplaceParticipations()



Returns a list of marketplaces that the seller submitting the request can sell in and information about the seller&#39;s participation in those marketplaces.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | .016 | 15 |  For more information, see \&quot;Usage Plans and Rate Limits\&quot; in the Selling Partner API documentation.

### Example
```java
// Import classes:
//import com.amazon.spapi.sellers.ApiException;
//import com.amazon.spapi.sellers.api.SellersApi;


SellersApi apiInstance = new SellersApi();
try {
    GetMarketplaceParticipationsResponse result = apiInstance.getMarketplaceParticipations();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling SellersApi#getMarketplaceParticipations");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**GetMarketplaceParticipationsResponse**](GetMarketplaceParticipationsResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

