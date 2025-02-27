# FeedsApi

All URIs are relative to *https://sellingpartnerapi-na.amazon.com*

Method | HTTP request | Description
------------- | ------------- | -------------
[**cancelFeed**](FeedsApi.md#cancelFeed) | **DELETE** /feeds/2021-06-30/feeds/{feedId} | 
[**createFeed**](FeedsApi.md#createFeed) | **POST** /feeds/2021-06-30/feeds | 
[**createFeedDocument**](FeedsApi.md#createFeedDocument) | **POST** /feeds/2021-06-30/documents | 
[**getFeed**](FeedsApi.md#getFeed) | **GET** /feeds/2021-06-30/feeds/{feedId} | 
[**getFeedDocument**](FeedsApi.md#getFeedDocument) | **GET** /feeds/2021-06-30/documents/{feedDocumentId} | 
[**getFeeds**](FeedsApi.md#getFeeds) | **GET** /feeds/2021-06-30/feeds | 


<a name="cancelFeed"></a>
# **cancelFeed**
> cancelFeed(feedId)



Cancels the feed that you specify. Only feeds with processingStatus&#x3D;IN_QUEUE can be cancelled. Cancelled feeds are returned in subsequent calls to the getFeed and getFeeds operations.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 2 | 15 |  The &#x60;x-amzn-RateLimit-Limit&#x60; response header returns the usage plan rate limits that were applied to the requested operation, when available. The table above indicates the default rate and burst values for this operation. Selling partners whose business demands require higher throughput may see higher rate and burst values than those shown here. For more information, see [Usage Plans and Rate Limits in the Selling Partner API](https://developer-docs.amazon.com/sp-api/docs/usage-plans-and-rate-limits-in-the-sp-api).

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.FeedsApi;


FeedsApi apiInstance = new FeedsApi();
String feedId = "feedId_example"; // String | The identifier for the feed. This identifier is unique only in combination with a seller ID.
try {
    apiInstance.cancelFeed(feedId);
} catch (ApiException e) {
    System.err.println("Exception when calling FeedsApi#cancelFeed");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **feedId** | **String**| The identifier for the feed. This identifier is unique only in combination with a seller ID. |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="createFeed"></a>
# **createFeed**
> CreateFeedResponse createFeed(body)



Creates a feed. Upload the contents of the feed document before calling this operation.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 0.0083 | 15 |  The &#x60;x-amzn-RateLimit-Limit&#x60; response header returns the usage plan rate limits that were applied to the requested operation, when available. The table above indicates the default rate and burst values for this operation. Selling partners whose business demands require higher throughput may see higher rate and burst values than those shown here. For more information, see [Usage Plans and Rate Limits in the Selling Partner API](https://developer-docs.amazon.com/sp-api/docs/usage-plans-and-rate-limits-in-the-sp-api).

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.FeedsApi;


FeedsApi apiInstance = new FeedsApi();
CreateFeedSpecification body = new CreateFeedSpecification(); // CreateFeedSpecification | 
try {
    CreateFeedResponse result = apiInstance.createFeed(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling FeedsApi#createFeed");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**CreateFeedSpecification**](CreateFeedSpecification.md)|  |

### Return type

[**CreateFeedResponse**](CreateFeedResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="createFeedDocument"></a>
# **createFeedDocument**
> CreateFeedDocumentResponse createFeedDocument(body)



Creates a feed document for the feed type that you specify. This operation returns a presigned URL for uploading the feed document contents. It also returns a feedDocumentId value that you can pass in with a subsequent call to the createFeed operation.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 0.5 | 15 |  The &#x60;x-amzn-RateLimit-Limit&#x60; response header returns the usage plan rate limits that were applied to the requested operation, when available. The table above indicates the default rate and burst values for this operation. Selling partners whose business demands require higher throughput may see higher rate and burst values than those shown here. For more information, see [Usage Plans and Rate Limits in the Selling Partner API](https://developer-docs.amazon.com/sp-api/docs/usage-plans-and-rate-limits-in-the-sp-api).

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.FeedsApi;


FeedsApi apiInstance = new FeedsApi();
CreateFeedDocumentSpecification body = new CreateFeedDocumentSpecification(); // CreateFeedDocumentSpecification | 
try {
    CreateFeedDocumentResponse result = apiInstance.createFeedDocument(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling FeedsApi#createFeedDocument");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**CreateFeedDocumentSpecification**](CreateFeedDocumentSpecification.md)|  |

### Return type

[**CreateFeedDocumentResponse**](CreateFeedDocumentResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getFeed"></a>
# **getFeed**
> Feed getFeed(feedId)



Returns feed details (including the resultDocumentId, if available) for the feed that you specify.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 2 | 15 |  The &#x60;x-amzn-RateLimit-Limit&#x60; response header returns the usage plan rate limits that were applied to the requested operation, when available. The table above indicates the default rate and burst values for this operation. Selling partners whose business demands require higher throughput may see higher rate and burst values than those shown here. For more information, see [Usage Plans and Rate Limits in the Selling Partner API](https://developer-docs.amazon.com/sp-api/docs/usage-plans-and-rate-limits-in-the-sp-api).

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.FeedsApi;


FeedsApi apiInstance = new FeedsApi();
String feedId = "feedId_example"; // String | The identifier for the feed. This identifier is unique only in combination with a seller ID.
try {
    Feed result = apiInstance.getFeed(feedId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling FeedsApi#getFeed");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **feedId** | **String**| The identifier for the feed. This identifier is unique only in combination with a seller ID. |

### Return type

[**Feed**](Feed.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getFeedDocument"></a>
# **getFeedDocument**
> FeedDocument getFeedDocument(feedDocumentId)



Returns the information required for retrieving a feed document&#39;s contents.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 0.0222 | 10 |  The &#x60;x-amzn-RateLimit-Limit&#x60; response header returns the usage plan rate limits that were applied to the requested operation, when available. The table above indicates the default rate and burst values for this operation. Selling partners whose business demands require higher throughput may see higher rate and burst values than those shown here. For more information, see [Usage Plans and Rate Limits in the Selling Partner API](https://developer-docs.amazon.com/sp-api/docs/usage-plans-and-rate-limits-in-the-sp-api).

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.FeedsApi;


FeedsApi apiInstance = new FeedsApi();
String feedDocumentId = "feedDocumentId_example"; // String | The identifier of the feed document.
try {
    FeedDocument result = apiInstance.getFeedDocument(feedDocumentId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling FeedsApi#getFeedDocument");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **feedDocumentId** | **String**| The identifier of the feed document. |

### Return type

[**FeedDocument**](FeedDocument.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getFeeds"></a>
# **getFeeds**
> GetFeedsResponse getFeeds(feedTypes, marketplaceIds, pageSize, processingStatuses, createdSince, createdUntil, nextToken)



Returns feed details for the feeds that match the filters that you specify.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 0.0222 | 10 |  The &#x60;x-amzn-RateLimit-Limit&#x60; response header returns the usage plan rate limits that were applied to the requested operation, when available. The table above indicates the default rate and burst values for this operation. Selling partners whose business demands require higher throughput may see higher rate and burst values than those shown here. For more information, see [Usage Plans and Rate Limits in the Selling Partner API](https://developer-docs.amazon.com/sp-api/docs/usage-plans-and-rate-limits-in-the-sp-api).

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.FeedsApi;


FeedsApi apiInstance = new FeedsApi();
List<String> feedTypes = Arrays.asList("feedTypes_example"); // List<String> | A list of feed types used to filter feeds. When feedTypes is provided, the other filter parameters (processingStatuses, marketplaceIds, createdSince, createdUntil) and pageSize may also be provided. Either feedTypes or nextToken is required.
List<String> marketplaceIds = Arrays.asList("marketplaceIds_example"); // List<String> | A list of marketplace identifiers used to filter feeds. The feeds returned will match at least one of the marketplaces that you specify.
Integer pageSize = 10; // Integer | The maximum number of feeds to return in a single call.
List<String> processingStatuses = Arrays.asList("processingStatuses_example"); // List<String> | A list of processing statuses used to filter feeds.
OffsetDateTime createdSince = OffsetDateTime.now(); // OffsetDateTime | The earliest feed creation date and time for feeds included in the response, in ISO 8601 format. The default is 90 days ago. Feeds are retained for a maximum of 90 days.
OffsetDateTime createdUntil = OffsetDateTime.now(); // OffsetDateTime | The latest feed creation date and time for feeds included in the response, in ISO 8601 format. The default is now.
String nextToken = "nextToken_example"; // String | A string token returned in the response to your previous request. nextToken is returned when the number of results exceeds the specified pageSize value. To get the next page of results, call the getFeeds operation and include this token as the only parameter. Specifying nextToken with any other parameters will cause the request to fail.
try {
    GetFeedsResponse result = apiInstance.getFeeds(feedTypes, marketplaceIds, pageSize, processingStatuses, createdSince, createdUntil, nextToken);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling FeedsApi#getFeeds");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **feedTypes** | [**List&lt;String&gt;**](String.md)| A list of feed types used to filter feeds. When feedTypes is provided, the other filter parameters (processingStatuses, marketplaceIds, createdSince, createdUntil) and pageSize may also be provided. Either feedTypes or nextToken is required. | [optional]
 **marketplaceIds** | [**List&lt;String&gt;**](String.md)| A list of marketplace identifiers used to filter feeds. The feeds returned will match at least one of the marketplaces that you specify. | [optional]
 **pageSize** | **Integer**| The maximum number of feeds to return in a single call. | [optional] [default to 10]
 **processingStatuses** | [**List&lt;String&gt;**](String.md)| A list of processing statuses used to filter feeds. | [optional] [enum: CANCELLED, DONE, FATAL, IN_PROGRESS, IN_QUEUE]
 **createdSince** | **OffsetDateTime**| The earliest feed creation date and time for feeds included in the response, in ISO 8601 format. The default is 90 days ago. Feeds are retained for a maximum of 90 days. | [optional]
 **createdUntil** | **OffsetDateTime**| The latest feed creation date and time for feeds included in the response, in ISO 8601 format. The default is now. | [optional]
 **nextToken** | **String**| A string token returned in the response to your previous request. nextToken is returned when the number of results exceeds the specified pageSize value. To get the next page of results, call the getFeeds operation and include this token as the only parameter. Specifying nextToken with any other parameters will cause the request to fail. | [optional]

### Return type

[**GetFeedsResponse**](GetFeedsResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

