# FbaInboundApi

All URIs are relative to *https://sellingpartnerapi-na.amazon.com*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getItemEligibilityPreview**](FbaInboundApi.md#getItemEligibilityPreview) | **GET** /fba/inbound/v1/eligibility/itemPreview | 


<a name="getItemEligibilityPreview"></a>
# **getItemEligibilityPreview**
> GetItemEligibilityPreviewResponse getItemEligibilityPreview(asin, program, marketplaceIds)



This operation gets an eligibility preview for an item that you specify. You can specify the type of eligibility preview that you want (INBOUND or COMMINGLING). For INBOUND previews, you can specify the marketplace in which you want to determine the item&#39;s eligibility.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 1 | 1 |  The &#x60;x-amzn-RateLimit-Limit&#x60; response header returns the usage plan rate limits that were applied to the requested operation, when available. The table above indicates the default rate and burst values for this operation. Selling partners whose business demands require higher throughput may see higher rate and burst values than those shown here. For more information, see [Usage Plans and Rate Limits in the Selling Partner API](https://developer-docs.amazon.com/sp-api/docs/usage-plans-and-rate-limits-in-the-sp-api).

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.FbaInboundApi;


FbaInboundApi apiInstance = new FbaInboundApi();
String asin = "asin_example"; // String | The ASIN of the item for which you want an eligibility preview.
String program = "program_example"; // String | The program that you want to check eligibility against.
List<String> marketplaceIds = Arrays.asList("marketplaceIds_example"); // List<String> | The identifier for the marketplace in which you want to determine eligibility. Required only when program=INBOUND.
try {
    GetItemEligibilityPreviewResponse result = apiInstance.getItemEligibilityPreview(asin, program, marketplaceIds);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling FbaInboundApi#getItemEligibilityPreview");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **asin** | **String**| The ASIN of the item for which you want an eligibility preview. |
 **program** | **String**| The program that you want to check eligibility against. | [enum: INBOUND, COMMINGLING]
 **marketplaceIds** | [**List&lt;String&gt;**](String.md)| The identifier for the marketplace in which you want to determine eligibility. Required only when program&#x3D;INBOUND. | [optional]

### Return type

[**GetItemEligibilityPreviewResponse**](GetItemEligibilityPreviewResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

