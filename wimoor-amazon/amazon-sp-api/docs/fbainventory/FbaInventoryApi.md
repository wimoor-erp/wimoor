# FbaInventoryApi

All URIs are relative to *https://sellingpartnerapi-na.amazon.com*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getInventorySummaries**](FbaInventoryApi.md#getInventorySummaries) | **GET** /fba/inventory/v1/summaries | 


<a name="getInventorySummaries"></a>
# **getInventorySummaries**
> GetInventorySummariesResponse getInventorySummaries(granularityType, granularityId, marketplaceIds, details, startDateTime, sellerSkus, nextToken)



Returns a list of inventory summaries. The summaries returned depend on the presence or absence of the startDateTime and sellerSkus parameters:  - All inventory summaries with available details are returned when the startDateTime and sellerSkus parameters are omitted. - When startDateTime is provided, the operation returns inventory summaries that have had changes after the date and time specified. The sellerSkus parameter is ignored. - When the sellerSkus parameter is provided, the operation returns inventory summaries for only the specified sellerSkus.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 90 | 150 |  For more information, see \&quot;Usage Plans and Rate Limits\&quot; in the Selling Partner API documentation.

### Example
```java
// Import classes:
//import com.amazon.spapi.fbainventory.ApiException;
//import com.amazon.spapi.fbainventory.api.FbaInventoryApi;


FbaInventoryApi apiInstance = new FbaInventoryApi();
String granularityType = "granularityType_example"; // String | The granularity type for the inventory aggregation level.
String granularityId = "granularityId_example"; // String | The granularity ID for the inventory aggregation level.
List<String> marketplaceIds = Arrays.asList("marketplaceIds_example"); // List<String> | The marketplace ID for the marketplace for which to return inventory summaries.
Boolean details = false; // Boolean | true to return inventory summaries with additional summarized inventory details and quantities. Otherwise, returns inventory summaries only (default value).
OffsetDateTime startDateTime = OffsetDateTime.now(); // OffsetDateTime | A start date and time in ISO8601 format. If specified, all inventory summaries that have changed since then are returned. You must specify a date and time that is no earlier than 18 months prior to the date and time when you call the API. Note: Changes in inboundWorkingQuantity, inboundShippedQuantity and inboundReceivingQuantity are not detected.
List<String> sellerSkus = Arrays.asList("sellerSkus_example"); // List<String> | A list of seller SKUs for which to return inventory summaries. You may specify up to 50 SKUs.
String nextToken = "nextToken_example"; // String | String token returned in the response of your previous request.
try {
    GetInventorySummariesResponse result = apiInstance.getInventorySummaries(granularityType, granularityId, marketplaceIds, details, startDateTime, sellerSkus, nextToken);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling FbaInventoryApi#getInventorySummaries");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **granularityType** | **String**| The granularity type for the inventory aggregation level. | [enum: Marketplace]
 **granularityId** | **String**| The granularity ID for the inventory aggregation level. |
 **marketplaceIds** | [**List&lt;String&gt;**](String.md)| The marketplace ID for the marketplace for which to return inventory summaries. |
 **details** | **Boolean**| true to return inventory summaries with additional summarized inventory details and quantities. Otherwise, returns inventory summaries only (default value). | [optional] [default to false]
 **startDateTime** | **OffsetDateTime**| A start date and time in ISO8601 format. If specified, all inventory summaries that have changed since then are returned. You must specify a date and time that is no earlier than 18 months prior to the date and time when you call the API. Note: Changes in inboundWorkingQuantity, inboundShippedQuantity and inboundReceivingQuantity are not detected. | [optional]
 **sellerSkus** | [**List&lt;String&gt;**](String.md)| A list of seller SKUs for which to return inventory summaries. You may specify up to 50 SKUs. | [optional]
 **nextToken** | **String**| String token returned in the response of your previous request. | [optional]

### Return type

[**GetInventorySummariesResponse**](GetInventorySummariesResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

