# FbaInventoryApi

All URIs are relative to *https://sellingpartnerapi-na.amazon.com*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getInventorySummaries**](FbaInventoryApi.md#getInventorySummaries) | **GET** /fba/inventory/v1/summaries | 


<a name="getInventorySummaries"></a>
# **getInventorySummaries**
> GetInventorySummariesResponse getInventorySummaries(granularityType, granularityId, marketplaceIds, details, startDateTime, sellerSkus, sellerSku, nextToken)



Returns a list of inventory summaries. The summaries returned depend on the presence or absence of the &#x60;startDateTime&#x60;, &#x60;sellerSkus&#x60; and &#x60;sellerSku&#x60; parameters:  - All inventory summaries with available details are returned when the &#x60;startDateTime&#x60;, &#x60;sellerSkus&#x60; and &#x60;sellerSku&#x60; parameters are omitted. - When &#x60;startDateTime&#x60; is provided, the operation returns inventory summaries that have had changes after the date and time specified. The &#x60;sellerSkus&#x60; and &#x60;sellerSku&#x60; parameters are ignored. **Important:** To avoid errors, use both &#x60;startDateTime&#x60; and &#x60;nextToken&#x60; to get the next page of inventory summaries that have changed after the date and time specified. - When the &#x60;sellerSkus&#x60; parameter is provided, the operation returns inventory summaries for only the specified &#x60;sellerSkus&#x60;. The &#x60;sellerSku&#x60; parameter is ignored. - When the &#x60;sellerSku&#x60; parameter is provided, the operation returns inventory summaries for only the specified &#x60;sellerSku&#x60;.  **Note:** The parameters associated with this operation may contain special characters that must be encoded to successfully call the API. To avoid errors with SKUs when encoding URLs, refer to [URL Encoding](https://developer-docs.amazon.com/sp-api/docs/url-encoding).  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 2 | 2 |  The &#x60;x-amzn-RateLimit-Limit&#x60; response header returns the usage plan rate limits that were applied to the requested operation, when available. The table above indicates the default rate and burst values for this operation. Selling partners whose business demands require higher throughput may see higher rate and burst values than those shown here. For more information, see [Usage Plans and Rate Limits in the Selling Partner API](doc:usage-plans-and-rate-limits-in-the-sp-api).

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.FbaInventoryApi;


FbaInventoryApi apiInstance = new FbaInventoryApi();
String granularityType = "granularityType_example"; // String | The granularity type for the inventory aggregation level.
String granularityId = "granularityId_example"; // String | The granularity ID for the inventory aggregation level.
List<String> marketplaceIds = Arrays.asList("marketplaceIds_example"); // List<String> | The marketplace ID for the marketplace for which to return inventory summaries.
Boolean details = false; // Boolean | true to return inventory summaries with additional summarized inventory details and quantities. Otherwise, returns inventory summaries only (default value).
OffsetDateTime startDateTime = OffsetDateTime.now(); // OffsetDateTime | A start date and time in ISO8601 format. If specified, all inventory summaries that have changed since then are returned. You must specify a date and time that is no earlier than 18 months prior to the date and time when you call the API. Note: Changes in inboundWorkingQuantity, inboundShippedQuantity and inboundReceivingQuantity are not detected.
List<String> sellerSkus = Arrays.asList("sellerSkus_example"); // List<String> | A list of seller SKUs for which to return inventory summaries. You may specify up to 50 SKUs.
String sellerSku = "sellerSku_example"; // String | A single seller SKU used for querying the specified seller SKU inventory summaries.
String nextToken = "nextToken_example"; // String | String token returned in the response of your previous request. The string token will expire 30 seconds after being created.
try {
    GetInventorySummariesResponse result = apiInstance.getInventorySummaries(granularityType, granularityId, marketplaceIds, details, startDateTime, sellerSkus, sellerSku, nextToken);
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
 **sellerSku** | **String**| A single seller SKU used for querying the specified seller SKU inventory summaries. | [optional]
 **nextToken** | **String**| String token returned in the response of your previous request. The string token will expire 30 seconds after being created. | [optional]

### Return type

[**GetInventorySummariesResponse**](GetInventorySummariesResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

