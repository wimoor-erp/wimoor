# FbaOutboundApi

All URIs are relative to *https://sellingpartnerapi-na.amazon.com*

Method | HTTP request | Description
------------- | ------------- | -------------
[**cancelFulfillmentOrder**](FbaOutboundApi.md#cancelFulfillmentOrder) | **PUT** /fba/outbound/2020-07-01/fulfillmentOrders/{sellerFulfillmentOrderId}/cancel | 
[**createFulfillmentOrder**](FbaOutboundApi.md#createFulfillmentOrder) | **POST** /fba/outbound/2020-07-01/fulfillmentOrders | 
[**createFulfillmentReturn**](FbaOutboundApi.md#createFulfillmentReturn) | **PUT** /fba/outbound/2020-07-01/fulfillmentOrders/{sellerFulfillmentOrderId}/return | 
[**getFeatureInventory**](FbaOutboundApi.md#getFeatureInventory) | **GET** /fba/outbound/2020-07-01/features/inventory/{featureName} | 
[**getFeatureSKU**](FbaOutboundApi.md#getFeatureSKU) | **GET** /fba/outbound/2020-07-01/features/inventory/{featureName}/{sellerSku} | 
[**getFeatures**](FbaOutboundApi.md#getFeatures) | **GET** /fba/outbound/2020-07-01/features | 
[**getFulfillmentOrder**](FbaOutboundApi.md#getFulfillmentOrder) | **GET** /fba/outbound/2020-07-01/fulfillmentOrders/{sellerFulfillmentOrderId} | 
[**getFulfillmentPreview**](FbaOutboundApi.md#getFulfillmentPreview) | **POST** /fba/outbound/2020-07-01/fulfillmentOrders/preview | 
[**getPackageTrackingDetails**](FbaOutboundApi.md#getPackageTrackingDetails) | **GET** /fba/outbound/2020-07-01/tracking | 
[**listAllFulfillmentOrders**](FbaOutboundApi.md#listAllFulfillmentOrders) | **GET** /fba/outbound/2020-07-01/fulfillmentOrders | 
[**listReturnReasonCodes**](FbaOutboundApi.md#listReturnReasonCodes) | **GET** /fba/outbound/2020-07-01/returnReasonCodes | 
[**updateFulfillmentOrder**](FbaOutboundApi.md#updateFulfillmentOrder) | **PUT** /fba/outbound/2020-07-01/fulfillmentOrders/{sellerFulfillmentOrderId} | 


<a name="cancelFulfillmentOrder"></a>
# **cancelFulfillmentOrder**
> CancelFulfillmentOrderResponse cancelFulfillmentOrder(sellerFulfillmentOrderId)



Requests that Amazon stop attempting to fulfill the fulfillment order indicated by the specified order identifier.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 2 | 30 |  For more information, see \&quot;Usage Plans and Rate Limits\&quot; in the Selling Partner API documentation.

### Example
```java
// Import classes:
//import com.amazon.spapi.fulfillmentoutbound.ApiException;
//import com.amazon.spapi.fulfillmentoutbound.api.FbaOutboundApi;


FbaOutboundApi apiInstance = new FbaOutboundApi();
String sellerFulfillmentOrderId = "sellerFulfillmentOrderId_example"; // String | The identifier assigned to the item by the seller when the fulfillment order was created.
try {
    CancelFulfillmentOrderResponse result = apiInstance.cancelFulfillmentOrder(sellerFulfillmentOrderId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling FbaOutboundApi#cancelFulfillmentOrder");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **sellerFulfillmentOrderId** | **String**| The identifier assigned to the item by the seller when the fulfillment order was created. |

### Return type

[**CancelFulfillmentOrderResponse**](CancelFulfillmentOrderResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="createFulfillmentOrder"></a>
# **createFulfillmentOrder**
> CreateFulfillmentOrderResponse createFulfillmentOrder(body)



Requests that Amazon ship items from the seller&#39;s inventory in Amazon&#39;s fulfillment network to a destination address.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 2 | 30 |  For more information, see \&quot;Usage Plans and Rate Limits\&quot; in the Selling Partner API documentation.

### Example
```java
// Import classes:
//import com.amazon.spapi.fulfillmentoutbound.ApiException;
//import com.amazon.spapi.fulfillmentoutbound.api.FbaOutboundApi;


FbaOutboundApi apiInstance = new FbaOutboundApi();
CreateFulfillmentOrderRequest body = new CreateFulfillmentOrderRequest(); // CreateFulfillmentOrderRequest | 
try {
    CreateFulfillmentOrderResponse result = apiInstance.createFulfillmentOrder(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling FbaOutboundApi#createFulfillmentOrder");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**CreateFulfillmentOrderRequest**](CreateFulfillmentOrderRequest.md)|  |

### Return type

[**CreateFulfillmentOrderResponse**](CreateFulfillmentOrderResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="createFulfillmentReturn"></a>
# **createFulfillmentReturn**
> CreateFulfillmentReturnResponse createFulfillmentReturn(body, sellerFulfillmentOrderId)



Creates a fulfillment return.   **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 2 | 30 |  For more information, see \&quot;Usage Plans and Rate Limits\&quot; in the Selling Partner API documentation.

### Example
```java
// Import classes:
//import com.amazon.spapi.fulfillmentoutbound.ApiException;
//import com.amazon.spapi.fulfillmentoutbound.api.FbaOutboundApi;


FbaOutboundApi apiInstance = new FbaOutboundApi();
CreateFulfillmentReturnRequest body = new CreateFulfillmentReturnRequest(); // CreateFulfillmentReturnRequest | 
String sellerFulfillmentOrderId = "sellerFulfillmentOrderId_example"; // String | An identifier assigned by the seller to the fulfillment order at the time it was created. The seller uses their own records to find the correct SellerFulfillmentOrderId value based on the buyer's request to return items.
try {
    CreateFulfillmentReturnResponse result = apiInstance.createFulfillmentReturn(body, sellerFulfillmentOrderId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling FbaOutboundApi#createFulfillmentReturn");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**CreateFulfillmentReturnRequest**](CreateFulfillmentReturnRequest.md)|  |
 **sellerFulfillmentOrderId** | **String**| An identifier assigned by the seller to the fulfillment order at the time it was created. The seller uses their own records to find the correct SellerFulfillmentOrderId value based on the buyer&#39;s request to return items. |

### Return type

[**CreateFulfillmentReturnResponse**](CreateFulfillmentReturnResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getFeatureInventory"></a>
# **getFeatureInventory**
> GetFeatureInventoryResponse getFeatureInventory(marketplaceId, featureName, nextToken)



Returns a list of inventory items that are eligible for the fulfillment feature you specify.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 2 | 30 |  For more information, see \&quot;Usage Plans and Rate Limits\&quot; in the Selling Partner API documentation.

### Example
```java
// Import classes:
//import com.amazon.spapi.fulfillmentoutbound.ApiException;
//import com.amazon.spapi.fulfillmentoutbound.api.FbaOutboundApi;


FbaOutboundApi apiInstance = new FbaOutboundApi();
String marketplaceId = "marketplaceId_example"; // String | The marketplace for which to return a list of the inventory that is eligible for the specified feature.
String featureName = "featureName_example"; // String | The name of the feature for which to return a list of eligible inventory.
String nextToken = "nextToken_example"; // String | A string token returned in the response to your previous request that is used to return the next response page. A value of null will return the first page.
try {
    GetFeatureInventoryResponse result = apiInstance.getFeatureInventory(marketplaceId, featureName, nextToken);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling FbaOutboundApi#getFeatureInventory");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **marketplaceId** | **String**| The marketplace for which to return a list of the inventory that is eligible for the specified feature. |
 **featureName** | **String**| The name of the feature for which to return a list of eligible inventory. |
 **nextToken** | **String**| A string token returned in the response to your previous request that is used to return the next response page. A value of null will return the first page. | [optional]

### Return type

[**GetFeatureInventoryResponse**](GetFeatureInventoryResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getFeatureSKU"></a>
# **getFeatureSKU**
> GetFeatureSkuResponse getFeatureSKU(marketplaceId, featureName, sellerSku)



Returns the number of items with the sellerSKU you specify that can have orders fulfilled using the specified feature. Note that if the sellerSKU isn&#39;t eligible, the response will contain an empty skuInfo object.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 2 | 30 |  For more information, see \&quot;Usage Plans and Rate Limits\&quot; in the Selling Partner API documentation.

### Example
```java
// Import classes:
//import com.amazon.spapi.fulfillmentoutbound.ApiException;
//import com.amazon.spapi.fulfillmentoutbound.api.FbaOutboundApi;


FbaOutboundApi apiInstance = new FbaOutboundApi();
String marketplaceId = "marketplaceId_example"; // String | The marketplace for which to return the count.
String featureName = "featureName_example"; // String | The name of the feature.
String sellerSku = "sellerSku_example"; // String | Used to identify an item in the given marketplace. SellerSKU is qualified by the seller's SellerId, which is included with every operation that you submit.
try {
    GetFeatureSkuResponse result = apiInstance.getFeatureSKU(marketplaceId, featureName, sellerSku);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling FbaOutboundApi#getFeatureSKU");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **marketplaceId** | **String**| The marketplace for which to return the count. |
 **featureName** | **String**| The name of the feature. |
 **sellerSku** | **String**| Used to identify an item in the given marketplace. SellerSKU is qualified by the seller&#39;s SellerId, which is included with every operation that you submit. |

### Return type

[**GetFeatureSkuResponse**](GetFeatureSkuResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getFeatures"></a>
# **getFeatures**
> GetFeaturesResponse getFeatures(marketplaceId)



Returns a list of features available for Multi-Channel Fulfillment orders in the marketplace you specify, and whether the seller for which you made the call is enrolled for each feature.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 2 | 30 |  For more information, see \&quot;Usage Plans and Rate Limits\&quot; in the Selling Partner API documentation.

### Example
```java
// Import classes:
//import com.amazon.spapi.fulfillmentoutbound.ApiException;
//import com.amazon.spapi.fulfillmentoutbound.api.FbaOutboundApi;


FbaOutboundApi apiInstance = new FbaOutboundApi();
String marketplaceId = "marketplaceId_example"; // String | The marketplace for which to return the list of features.
try {
    GetFeaturesResponse result = apiInstance.getFeatures(marketplaceId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling FbaOutboundApi#getFeatures");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **marketplaceId** | **String**| The marketplace for which to return the list of features. |

### Return type

[**GetFeaturesResponse**](GetFeaturesResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getFulfillmentOrder"></a>
# **getFulfillmentOrder**
> GetFulfillmentOrderResponse getFulfillmentOrder(sellerFulfillmentOrderId)



Returns the fulfillment order indicated by the specified order identifier.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 2 | 30 |  For more information, see \&quot;Usage Plans and Rate Limits\&quot; in the Selling Partner API documentation.

### Example
```java
// Import classes:
//import com.amazon.spapi.fulfillmentoutbound.ApiException;
//import com.amazon.spapi.fulfillmentoutbound.api.FbaOutboundApi;


FbaOutboundApi apiInstance = new FbaOutboundApi();
String sellerFulfillmentOrderId = "sellerFulfillmentOrderId_example"; // String | The identifier assigned to the item by the seller when the fulfillment order was created.
try {
    GetFulfillmentOrderResponse result = apiInstance.getFulfillmentOrder(sellerFulfillmentOrderId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling FbaOutboundApi#getFulfillmentOrder");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **sellerFulfillmentOrderId** | **String**| The identifier assigned to the item by the seller when the fulfillment order was created. |

### Return type

[**GetFulfillmentOrderResponse**](GetFulfillmentOrderResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getFulfillmentPreview"></a>
# **getFulfillmentPreview**
> GetFulfillmentPreviewResponse getFulfillmentPreview(body)



Returns a list of fulfillment order previews based on shipping criteria that you specify.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 2 | 30 |  For more information, see \&quot;Usage Plans and Rate Limits\&quot; in the Selling Partner API documentation.

### Example
```java
// Import classes:
//import com.amazon.spapi.fulfillmentoutbound.ApiException;
//import com.amazon.spapi.fulfillmentoutbound.api.FbaOutboundApi;


FbaOutboundApi apiInstance = new FbaOutboundApi();
GetFulfillmentPreviewRequest body = new GetFulfillmentPreviewRequest(); // GetFulfillmentPreviewRequest | 
try {
    GetFulfillmentPreviewResponse result = apiInstance.getFulfillmentPreview(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling FbaOutboundApi#getFulfillmentPreview");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**GetFulfillmentPreviewRequest**](GetFulfillmentPreviewRequest.md)|  |

### Return type

[**GetFulfillmentPreviewResponse**](GetFulfillmentPreviewResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getPackageTrackingDetails"></a>
# **getPackageTrackingDetails**
> GetPackageTrackingDetailsResponse getPackageTrackingDetails(packageNumber)



Returns delivery tracking information for a package in an outbound shipment for a Multi-Channel Fulfillment order.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 2 | 30 |  For more information, see \&quot;Usage Plans and Rate Limits\&quot; in the Selling Partner API documentation.

### Example
```java
// Import classes:
//import com.amazon.spapi.fulfillmentoutbound.ApiException;
//import com.amazon.spapi.fulfillmentoutbound.api.FbaOutboundApi;


FbaOutboundApi apiInstance = new FbaOutboundApi();
Integer packageNumber = 56; // Integer | The unencrypted package identifier returned by the getFulfillmentOrder operation.
try {
    GetPackageTrackingDetailsResponse result = apiInstance.getPackageTrackingDetails(packageNumber);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling FbaOutboundApi#getPackageTrackingDetails");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **packageNumber** | **Integer**| The unencrypted package identifier returned by the getFulfillmentOrder operation. |

### Return type

[**GetPackageTrackingDetailsResponse**](GetPackageTrackingDetailsResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="listAllFulfillmentOrders"></a>
# **listAllFulfillmentOrders**
> ListAllFulfillmentOrdersResponse listAllFulfillmentOrders(queryStartDate, nextToken)



Returns a list of fulfillment orders fulfilled after (or at) a specified date-time, or indicated by the next token parameter.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 2 | 30 |  For more information, see \&quot;Usage Plans and Rate Limits\&quot; in the Selling Partner API documentation.

### Example
```java
// Import classes:
//import com.amazon.spapi.fulfillmentoutbound.ApiException;
//import com.amazon.spapi.fulfillmentoutbound.api.FbaOutboundApi;


FbaOutboundApi apiInstance = new FbaOutboundApi();
OffsetDateTime queryStartDate = OffsetDateTime.now(); // OffsetDateTime | A date used to select fulfillment orders that were last updated after (or at) a specified time. An update is defined as any change in fulfillment order status, including the creation of a new fulfillment order.
String nextToken = "nextToken_example"; // String | A string token returned in the response to your previous request.
try {
    ListAllFulfillmentOrdersResponse result = apiInstance.listAllFulfillmentOrders(queryStartDate, nextToken);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling FbaOutboundApi#listAllFulfillmentOrders");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **queryStartDate** | **OffsetDateTime**| A date used to select fulfillment orders that were last updated after (or at) a specified time. An update is defined as any change in fulfillment order status, including the creation of a new fulfillment order. | [optional]
 **nextToken** | **String**| A string token returned in the response to your previous request. | [optional]

### Return type

[**ListAllFulfillmentOrdersResponse**](ListAllFulfillmentOrdersResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="listReturnReasonCodes"></a>
# **listReturnReasonCodes**
> ListReturnReasonCodesResponse listReturnReasonCodes(sellerSku, language, marketplaceId, sellerFulfillmentOrderId)



Returns a list of return reason codes for a seller SKU in a given marketplace.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 2 | 30 |  For more information, see \&quot;Usage Plans and Rate Limits\&quot; in the Selling Partner API documentation.

### Example
```java
// Import classes:
//import com.amazon.spapi.fulfillmentoutbound.ApiException;
//import com.amazon.spapi.fulfillmentoutbound.api.FbaOutboundApi;


FbaOutboundApi apiInstance = new FbaOutboundApi();
String sellerSku = "sellerSku_example"; // String | The seller SKU for which return reason codes are required.
String language = "language_example"; // String | The language that the TranslatedDescription property of the ReasonCodeDetails response object should be translated into.
String marketplaceId = "marketplaceId_example"; // String | The marketplace for which the seller wants return reason codes.
String sellerFulfillmentOrderId = "sellerFulfillmentOrderId_example"; // String | The identifier assigned to the item by the seller when the fulfillment order was created. The service uses this value to determine the marketplace for which the seller wants return reason codes.
try {
    ListReturnReasonCodesResponse result = apiInstance.listReturnReasonCodes(sellerSku, language, marketplaceId, sellerFulfillmentOrderId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling FbaOutboundApi#listReturnReasonCodes");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **sellerSku** | **String**| The seller SKU for which return reason codes are required. |
 **language** | **String**| The language that the TranslatedDescription property of the ReasonCodeDetails response object should be translated into. |
 **marketplaceId** | **String**| The marketplace for which the seller wants return reason codes. | [optional]
 **sellerFulfillmentOrderId** | **String**| The identifier assigned to the item by the seller when the fulfillment order was created. The service uses this value to determine the marketplace for which the seller wants return reason codes. | [optional]

### Return type

[**ListReturnReasonCodesResponse**](ListReturnReasonCodesResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="updateFulfillmentOrder"></a>
# **updateFulfillmentOrder**
> UpdateFulfillmentOrderResponse updateFulfillmentOrder(body, sellerFulfillmentOrderId)



Updates and/or requests shipment for a fulfillment order with an order hold on it.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 2 | 30 |  For more information, see \&quot;Usage Plans and Rate Limits\&quot; in the Selling Partner API documentation.

### Example
```java
// Import classes:
//import com.amazon.spapi.fulfillmentoutbound.ApiException;
//import com.amazon.spapi.fulfillmentoutbound.api.FbaOutboundApi;


FbaOutboundApi apiInstance = new FbaOutboundApi();
UpdateFulfillmentOrderRequest body = new UpdateFulfillmentOrderRequest(); // UpdateFulfillmentOrderRequest | 
String sellerFulfillmentOrderId = "sellerFulfillmentOrderId_example"; // String | The identifier assigned to the item by the seller when the fulfillment order was created.
try {
    UpdateFulfillmentOrderResponse result = apiInstance.updateFulfillmentOrder(body, sellerFulfillmentOrderId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling FbaOutboundApi#updateFulfillmentOrder");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**UpdateFulfillmentOrderRequest**](UpdateFulfillmentOrderRequest.md)|  |
 **sellerFulfillmentOrderId** | **String**| The identifier assigned to the item by the seller when the fulfillment order was created. |

### Return type

[**UpdateFulfillmentOrderResponse**](UpdateFulfillmentOrderResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

