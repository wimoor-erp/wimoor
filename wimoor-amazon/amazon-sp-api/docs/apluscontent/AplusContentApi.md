# AplusContentApi

All URIs are relative to *https://sellingpartnerapi-na.amazon.com*

Method | HTTP request | Description
------------- | ------------- | -------------
[**createContentDocument**](AplusContentApi.md#createContentDocument) | **POST** /aplus/2020-11-01/contentDocuments | 
[**getContentDocument**](AplusContentApi.md#getContentDocument) | **GET** /aplus/2020-11-01/contentDocuments/{contentReferenceKey} | 
[**listContentDocumentAsinRelations**](AplusContentApi.md#listContentDocumentAsinRelations) | **GET** /aplus/2020-11-01/contentDocuments/{contentReferenceKey}/asins | 
[**postContentDocumentApprovalSubmission**](AplusContentApi.md#postContentDocumentApprovalSubmission) | **POST** /aplus/2020-11-01/contentDocuments/{contentReferenceKey}/approvalSubmissions | 
[**postContentDocumentAsinRelations**](AplusContentApi.md#postContentDocumentAsinRelations) | **POST** /aplus/2020-11-01/contentDocuments/{contentReferenceKey}/asins | 
[**postContentDocumentSuspendSubmission**](AplusContentApi.md#postContentDocumentSuspendSubmission) | **POST** /aplus/2020-11-01/contentDocuments/{contentReferenceKey}/suspendSubmissions | 
[**searchContentDocuments**](AplusContentApi.md#searchContentDocuments) | **GET** /aplus/2020-11-01/contentDocuments | 
[**searchContentPublishRecords**](AplusContentApi.md#searchContentPublishRecords) | **GET** /aplus/2020-11-01/contentPublishRecords | 
[**updateContentDocument**](AplusContentApi.md#updateContentDocument) | **POST** /aplus/2020-11-01/contentDocuments/{contentReferenceKey} | 
[**validateContentDocumentAsinRelations**](AplusContentApi.md#validateContentDocumentAsinRelations) | **POST** /aplus/2020-11-01/contentAsinValidations | 


<a name="createContentDocument"></a>
# **createContentDocument**
> PostContentDocumentResponse createContentDocument(marketplaceId, postContentDocumentRequest)



Creates a new A+ Content document.  **Usage Plans:**  | Plan type | Rate (requests per second) | Burst | | ---- | ---- | ---- | |Default| 10 | 10 | |Selling partner specific| Variable | Variable |  The x-amzn-RateLimit-Limit response header returns the usage plan rate limits that were applied to the requested operation. Rate limits for some selling partners will vary from the default rate and burst shown in the table above. For more information, see \&quot;Usage Plans and Rate Limits\&quot; in the Selling Partner API documentation.

### Example
```java
// Import classes:
//import com.amazon.spapi.apluscontent.ApiException;
//import com.amazon.spapi.apluscontent.api.AplusContentApi;


AplusContentApi apiInstance = new AplusContentApi();
String marketplaceId = "marketplaceId_example"; // String | The identifier for the marketplace where the A+ Content is published.
PostContentDocumentRequest postContentDocumentRequest = new PostContentDocumentRequest(); // PostContentDocumentRequest | The content document request details.
try {
    PostContentDocumentResponse result = apiInstance.createContentDocument(marketplaceId, postContentDocumentRequest);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling AplusContentApi#createContentDocument");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **marketplaceId** | **String**| The identifier for the marketplace where the A+ Content is published. |
 **postContentDocumentRequest** | [**PostContentDocumentRequest**](PostContentDocumentRequest.md)| The content document request details. |

### Return type

[**PostContentDocumentResponse**](PostContentDocumentResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getContentDocument"></a>
# **getContentDocument**
> GetContentDocumentResponse getContentDocument(contentReferenceKey, marketplaceId, includedDataSet)



Returns an A+ Content document, if available.  **Usage Plans:**  | Plan type | Rate (requests per second) | Burst | | ---- | ---- | ---- | |Default| 10 | 10 | |Selling partner specific| Variable | Variable |  The x-amzn-RateLimit-Limit response header returns the usage plan rate limits that were applied to the requested operation. Rate limits for some selling partners will vary from the default rate and burst shown in the table above. For more information, see \&quot;Usage Plans and Rate Limits\&quot; in the Selling Partner API documentation.

### Example
```java
// Import classes:
//import com.amazon.spapi.apluscontent.ApiException;
//import com.amazon.spapi.apluscontent.api.AplusContentApi;


AplusContentApi apiInstance = new AplusContentApi();
String contentReferenceKey = "contentReferenceKey_example"; // String | The unique reference key for the A+ Content document. A content reference key cannot form a permalink and may change in the future. A content reference key is not guaranteed to match any A+ Content identifier.
String marketplaceId = "marketplaceId_example"; // String | The identifier for the marketplace where the A+ Content is published.
List<String> includedDataSet = Arrays.asList("includedDataSet_example"); // List<String> | The set of A+ Content data types to include in the response.
try {
    GetContentDocumentResponse result = apiInstance.getContentDocument(contentReferenceKey, marketplaceId, includedDataSet);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling AplusContentApi#getContentDocument");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **contentReferenceKey** | **String**| The unique reference key for the A+ Content document. A content reference key cannot form a permalink and may change in the future. A content reference key is not guaranteed to match any A+ Content identifier. |
 **marketplaceId** | **String**| The identifier for the marketplace where the A+ Content is published. |
 **includedDataSet** | [**List&lt;String&gt;**](String.md)| The set of A+ Content data types to include in the response. | [enum: CONTENTS, METADATA]

### Return type

[**GetContentDocumentResponse**](GetContentDocumentResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="listContentDocumentAsinRelations"></a>
# **listContentDocumentAsinRelations**
> ListContentDocumentAsinRelationsResponse listContentDocumentAsinRelations(contentReferenceKey, marketplaceId, includedDataSet, asinSet, pageToken)



Returns a list of ASINs related to the specified A+ Content document, if available. If you do not include the asinSet parameter, the operation returns all ASINs related to the content document.  **Usage Plans:**  | Plan type | Rate (requests per second) | Burst | | ---- | ---- | ---- | |Default| 10 | 10 | |Selling partner specific| Variable | Variable |  The x-amzn-RateLimit-Limit response header returns the usage plan rate limits that were applied to the requested operation. Rate limits for some selling partners will vary from the default rate and burst shown in the table above. For more information, see \&quot;Usage Plans and Rate Limits\&quot; in the Selling Partner API documentation.

### Example
```java
// Import classes:
//import com.amazon.spapi.apluscontent.ApiException;
//import com.amazon.spapi.apluscontent.api.AplusContentApi;


AplusContentApi apiInstance = new AplusContentApi();
String contentReferenceKey = "contentReferenceKey_example"; // String | The unique reference key for the A+ Content document. A content reference key cannot form a permalink and may change in the future. A content reference key is not guaranteed to match any A+ Content identifier.
String marketplaceId = "marketplaceId_example"; // String | The identifier for the marketplace where the A+ Content is published.
List<String> includedDataSet = Arrays.asList("includedDataSet_example"); // List<String> | The set of A+ Content data types to include in the response. If you do not include this parameter, the operation returns the related ASINs without metadata.
List<String> asinSet = Arrays.asList("asinSet_example"); // List<String> | The set of ASINs.
String pageToken = "pageToken_example"; // String | A page token from the nextPageToken response element returned by your previous call to this operation. nextPageToken is returned when the results of a call exceed the page size. To get the next page of results, call the operation and include pageToken as the only parameter. Specifying pageToken with any other parameter will cause the request to fail. When no nextPageToken value is returned there are no more pages to return. A pageToken value is not usable across different operations.
try {
    ListContentDocumentAsinRelationsResponse result = apiInstance.listContentDocumentAsinRelations(contentReferenceKey, marketplaceId, includedDataSet, asinSet, pageToken);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling AplusContentApi#listContentDocumentAsinRelations");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **contentReferenceKey** | **String**| The unique reference key for the A+ Content document. A content reference key cannot form a permalink and may change in the future. A content reference key is not guaranteed to match any A+ Content identifier. |
 **marketplaceId** | **String**| The identifier for the marketplace where the A+ Content is published. |
 **includedDataSet** | [**List&lt;String&gt;**](String.md)| The set of A+ Content data types to include in the response. If you do not include this parameter, the operation returns the related ASINs without metadata. | [optional] [enum: METADATA]
 **asinSet** | [**List&lt;String&gt;**](String.md)| The set of ASINs. | [optional]
 **pageToken** | **String**| A page token from the nextPageToken response element returned by your previous call to this operation. nextPageToken is returned when the results of a call exceed the page size. To get the next page of results, call the operation and include pageToken as the only parameter. Specifying pageToken with any other parameter will cause the request to fail. When no nextPageToken value is returned there are no more pages to return. A pageToken value is not usable across different operations. | [optional]

### Return type

[**ListContentDocumentAsinRelationsResponse**](ListContentDocumentAsinRelationsResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="postContentDocumentApprovalSubmission"></a>
# **postContentDocumentApprovalSubmission**
> PostContentDocumentApprovalSubmissionResponse postContentDocumentApprovalSubmission(contentReferenceKey, marketplaceId)



Submits an A+ Content document for review, approval, and publishing.  **Usage Plans:**  | Plan type | Rate (requests per second) | Burst | | ---- | ---- | ---- | |Default| 10 | 10 | |Selling partner specific| Variable | Variable |  The x-amzn-RateLimit-Limit response header returns the usage plan rate limits that were applied to the requested operation. Rate limits for some selling partners will vary from the default rate and burst shown in the table above. For more information, see \&quot;Usage Plans and Rate Limits\&quot; in the Selling Partner API documentation.

### Example
```java
// Import classes:
//import com.amazon.spapi.apluscontent.ApiException;
//import com.amazon.spapi.apluscontent.api.AplusContentApi;


AplusContentApi apiInstance = new AplusContentApi();
String contentReferenceKey = "contentReferenceKey_example"; // String | The unique reference key for the A+ Content document. A content reference key cannot form a permalink and may change in the future. A content reference key is not guaranteed to match any A+ content identifier.
String marketplaceId = "marketplaceId_example"; // String | The identifier for the marketplace where the A+ Content is published.
try {
    PostContentDocumentApprovalSubmissionResponse result = apiInstance.postContentDocumentApprovalSubmission(contentReferenceKey, marketplaceId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling AplusContentApi#postContentDocumentApprovalSubmission");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **contentReferenceKey** | **String**| The unique reference key for the A+ Content document. A content reference key cannot form a permalink and may change in the future. A content reference key is not guaranteed to match any A+ content identifier. |
 **marketplaceId** | **String**| The identifier for the marketplace where the A+ Content is published. |

### Return type

[**PostContentDocumentApprovalSubmissionResponse**](PostContentDocumentApprovalSubmissionResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="postContentDocumentAsinRelations"></a>
# **postContentDocumentAsinRelations**
> PostContentDocumentAsinRelationsResponse postContentDocumentAsinRelations(contentReferenceKey, marketplaceId, postContentDocumentAsinRelationsRequest)



Replaces all ASINs related to the specified A+ Content document, if available. This may add or remove ASINs, depending on the current set of related ASINs. Removing an ASIN has the side effect of suspending the content document from that ASIN.  **Usage Plans:**  | Plan type | Rate (requests per second) | Burst | | ---- | ---- | ---- | |Default| 10 | 10 | |Selling partner specific| Variable | Variable |  The x-amzn-RateLimit-Limit response header returns the usage plan rate limits that were applied to the requested operation. Rate limits for some selling partners will vary from the default rate and burst shown in the table above. For more information, see \&quot;Usage Plans and Rate Limits\&quot; in the Selling Partner API documentation.

### Example
```java
// Import classes:
//import com.amazon.spapi.apluscontent.ApiException;
//import com.amazon.spapi.apluscontent.api.AplusContentApi;


AplusContentApi apiInstance = new AplusContentApi();
String contentReferenceKey = "contentReferenceKey_example"; // String | The unique reference key for the A+ Content document. A content reference key cannot form a permalink and may change in the future. A content reference key is not guaranteed to match any A+ content identifier.
String marketplaceId = "marketplaceId_example"; // String | The identifier for the marketplace where the A+ Content is published.
PostContentDocumentAsinRelationsRequest postContentDocumentAsinRelationsRequest = new PostContentDocumentAsinRelationsRequest(); // PostContentDocumentAsinRelationsRequest | The content document ASIN relations request details.
try {
    PostContentDocumentAsinRelationsResponse result = apiInstance.postContentDocumentAsinRelations(contentReferenceKey, marketplaceId, postContentDocumentAsinRelationsRequest);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling AplusContentApi#postContentDocumentAsinRelations");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **contentReferenceKey** | **String**| The unique reference key for the A+ Content document. A content reference key cannot form a permalink and may change in the future. A content reference key is not guaranteed to match any A+ content identifier. |
 **marketplaceId** | **String**| The identifier for the marketplace where the A+ Content is published. |
 **postContentDocumentAsinRelationsRequest** | [**PostContentDocumentAsinRelationsRequest**](PostContentDocumentAsinRelationsRequest.md)| The content document ASIN relations request details. |

### Return type

[**PostContentDocumentAsinRelationsResponse**](PostContentDocumentAsinRelationsResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="postContentDocumentSuspendSubmission"></a>
# **postContentDocumentSuspendSubmission**
> PostContentDocumentSuspendSubmissionResponse postContentDocumentSuspendSubmission(contentReferenceKey, marketplaceId)



Submits a request to suspend visible A+ Content. This neither deletes the content document nor the ASIN relations.  **Usage Plans:**  | Plan type | Rate (requests per second) | Burst | | ---- | ---- | ---- | |Default| 10 | 10 | |Selling partner specific| Variable | Variable |  The x-amzn-RateLimit-Limit response header returns the usage plan rate limits that were applied to the requested operation. Rate limits for some selling partners will vary from the default rate and burst shown in the table above. For more information, see \&quot;Usage Plans and Rate Limits\&quot; in the Selling Partner API documentation.

### Example
```java
// Import classes:
//import com.amazon.spapi.apluscontent.ApiException;
//import com.amazon.spapi.apluscontent.api.AplusContentApi;


AplusContentApi apiInstance = new AplusContentApi();
String contentReferenceKey = "contentReferenceKey_example"; // String | The unique reference key for the A+ Content document. A content reference key cannot form a permalink and may change in the future. A content reference key is not guaranteed to match any A+ content identifier.
String marketplaceId = "marketplaceId_example"; // String | The identifier for the marketplace where the A+ Content is published.
try {
    PostContentDocumentSuspendSubmissionResponse result = apiInstance.postContentDocumentSuspendSubmission(contentReferenceKey, marketplaceId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling AplusContentApi#postContentDocumentSuspendSubmission");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **contentReferenceKey** | **String**| The unique reference key for the A+ Content document. A content reference key cannot form a permalink and may change in the future. A content reference key is not guaranteed to match any A+ content identifier. |
 **marketplaceId** | **String**| The identifier for the marketplace where the A+ Content is published. |

### Return type

[**PostContentDocumentSuspendSubmissionResponse**](PostContentDocumentSuspendSubmissionResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="searchContentDocuments"></a>
# **searchContentDocuments**
> SearchContentDocumentsResponse searchContentDocuments(marketplaceId, pageToken)



Returns a list of all A+ Content documents assigned to a selling partner. This operation returns only the metadata of the A+ Content documents. Call the getContentDocument operation to get the actual contents of the A+ Content documents.  **Usage Plans:**  | Plan type | Rate (requests per second) | Burst | | ---- | ---- | ---- | |Default| 10 | 10 | |Selling partner specific| Variable | Variable |  The x-amzn-RateLimit-Limit response header returns the usage plan rate limits that were applied to the requested operation. Rate limits for some selling partners will vary from the default rate and burst shown in the table above. For more information, see \&quot;Usage Plans and Rate Limits\&quot; in the Selling Partner API documentation.

### Example
```java
// Import classes:
//import com.amazon.spapi.apluscontent.ApiException;
//import com.amazon.spapi.apluscontent.api.AplusContentApi;


AplusContentApi apiInstance = new AplusContentApi();
String marketplaceId = "marketplaceId_example"; // String | The identifier for the marketplace where the A+ Content is published.
String pageToken = "pageToken_example"; // String | A page token from the nextPageToken response element returned by your previous call to this operation. nextPageToken is returned when the results of a call exceed the page size. To get the next page of results, call the operation and include pageToken as the only parameter. Specifying pageToken with any other parameter will cause the request to fail. When no nextPageToken value is returned there are no more pages to return. A pageToken value is not usable across different operations.
try {
    SearchContentDocumentsResponse result = apiInstance.searchContentDocuments(marketplaceId, pageToken);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling AplusContentApi#searchContentDocuments");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **marketplaceId** | **String**| The identifier for the marketplace where the A+ Content is published. |
 **pageToken** | **String**| A page token from the nextPageToken response element returned by your previous call to this operation. nextPageToken is returned when the results of a call exceed the page size. To get the next page of results, call the operation and include pageToken as the only parameter. Specifying pageToken with any other parameter will cause the request to fail. When no nextPageToken value is returned there are no more pages to return. A pageToken value is not usable across different operations. | [optional]

### Return type

[**SearchContentDocumentsResponse**](SearchContentDocumentsResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="searchContentPublishRecords"></a>
# **searchContentPublishRecords**
> SearchContentPublishRecordsResponse searchContentPublishRecords(marketplaceId, asin, pageToken)



Searches for A+ Content publishing records, if available.  **Usage Plans:**  | Plan type | Rate (requests per second) | Burst | | ---- | ---- | ---- | |Default| 10 | 10 | |Selling partner specific| Variable | Variable |  The x-amzn-RateLimit-Limit response header returns the usage plan rate limits that were applied to the requested operation. Rate limits for some selling partners will vary from the default rate and burst shown in the table above. For more information, see \&quot;Usage Plans and Rate Limits\&quot; in the Selling Partner API documentation.

### Example
```java
// Import classes:
//import com.amazon.spapi.apluscontent.ApiException;
//import com.amazon.spapi.apluscontent.api.AplusContentApi;


AplusContentApi apiInstance = new AplusContentApi();
String marketplaceId = "marketplaceId_example"; // String | The identifier for the marketplace where the A+ Content is published.
String asin = "asin_example"; // String | The Amazon Standard Identification Number (ASIN).
String pageToken = "pageToken_example"; // String | A page token from the nextPageToken response element returned by your previous call to this operation. nextPageToken is returned when the results of a call exceed the page size. To get the next page of results, call the operation and include pageToken as the only parameter. Specifying pageToken with any other parameter will cause the request to fail. When no nextPageToken value is returned there are no more pages to return. A pageToken value is not usable across different operations.
try {
    SearchContentPublishRecordsResponse result = apiInstance.searchContentPublishRecords(marketplaceId, asin, pageToken);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling AplusContentApi#searchContentPublishRecords");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **marketplaceId** | **String**| The identifier for the marketplace where the A+ Content is published. |
 **asin** | **String**| The Amazon Standard Identification Number (ASIN). |
 **pageToken** | **String**| A page token from the nextPageToken response element returned by your previous call to this operation. nextPageToken is returned when the results of a call exceed the page size. To get the next page of results, call the operation and include pageToken as the only parameter. Specifying pageToken with any other parameter will cause the request to fail. When no nextPageToken value is returned there are no more pages to return. A pageToken value is not usable across different operations. | [optional]

### Return type

[**SearchContentPublishRecordsResponse**](SearchContentPublishRecordsResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="updateContentDocument"></a>
# **updateContentDocument**
> PostContentDocumentResponse updateContentDocument(contentReferenceKey, marketplaceId, postContentDocumentRequest)



Updates an existing A+ Content document.  **Usage Plans:**  | Plan type | Rate (requests per second) | Burst | | ---- | ---- | ---- | |Default| 10 | 10 | |Selling partner specific| Variable | Variable |  The x-amzn-RateLimit-Limit response header returns the usage plan rate limits that were applied to the requested operation. Rate limits for some selling partners will vary from the default rate and burst shown in the table above. For more information, see \&quot;Usage Plans and Rate Limits\&quot; in the Selling Partner API documentation.

### Example
```java
// Import classes:
//import com.amazon.spapi.apluscontent.ApiException;
//import com.amazon.spapi.apluscontent.api.AplusContentApi;


AplusContentApi apiInstance = new AplusContentApi();
String contentReferenceKey = "contentReferenceKey_example"; // String | The unique reference key for the A+ Content document. A content reference key cannot form a permalink and may change in the future. A content reference key is not guaranteed to match any A+ Content identifier.
String marketplaceId = "marketplaceId_example"; // String | The identifier for the marketplace where the A+ Content is published.
PostContentDocumentRequest postContentDocumentRequest = new PostContentDocumentRequest(); // PostContentDocumentRequest | The content document request details.
try {
    PostContentDocumentResponse result = apiInstance.updateContentDocument(contentReferenceKey, marketplaceId, postContentDocumentRequest);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling AplusContentApi#updateContentDocument");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **contentReferenceKey** | **String**| The unique reference key for the A+ Content document. A content reference key cannot form a permalink and may change in the future. A content reference key is not guaranteed to match any A+ Content identifier. |
 **marketplaceId** | **String**| The identifier for the marketplace where the A+ Content is published. |
 **postContentDocumentRequest** | [**PostContentDocumentRequest**](PostContentDocumentRequest.md)| The content document request details. |

### Return type

[**PostContentDocumentResponse**](PostContentDocumentResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="validateContentDocumentAsinRelations"></a>
# **validateContentDocumentAsinRelations**
> ValidateContentDocumentAsinRelationsResponse validateContentDocumentAsinRelations(marketplaceId, postContentDocumentRequest, asinSet)



Checks if the A+ Content document is valid for use on a set of ASINs.  **Usage Plans:**  | Plan type | Rate (requests per second) | Burst | | ---- | ---- | ---- | |Default| 10 | 10 | |Selling partner specific| Variable | Variable |  The x-amzn-RateLimit-Limit response header returns the usage plan rate limits that were applied to the requested operation. Rate limits for some selling partners will vary from the default rate and burst shown in the table above. For more information, see \&quot;Usage Plans and Rate Limits\&quot; in the Selling Partner API documentation.

### Example
```java
// Import classes:
//import com.amazon.spapi.apluscontent.ApiException;
//import com.amazon.spapi.apluscontent.api.AplusContentApi;


AplusContentApi apiInstance = new AplusContentApi();
String marketplaceId = "marketplaceId_example"; // String | The identifier for the marketplace where the A+ Content is published.
PostContentDocumentRequest postContentDocumentRequest = new PostContentDocumentRequest(); // PostContentDocumentRequest | The content document request details.
List<String> asinSet = Arrays.asList("asinSet_example"); // List<String> | The set of ASINs.
try {
    ValidateContentDocumentAsinRelationsResponse result = apiInstance.validateContentDocumentAsinRelations(marketplaceId, postContentDocumentRequest, asinSet);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling AplusContentApi#validateContentDocumentAsinRelations");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **marketplaceId** | **String**| The identifier for the marketplace where the A+ Content is published. |
 **postContentDocumentRequest** | [**PostContentDocumentRequest**](PostContentDocumentRequest.md)| The content document request details. |
 **asinSet** | [**List&lt;String&gt;**](String.md)| The set of ASINs. | [optional]

### Return type

[**ValidateContentDocumentAsinRelationsResponse**](ValidateContentDocumentAsinRelationsResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

