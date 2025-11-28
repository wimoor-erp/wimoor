# ShippingApi

All URIs are relative to *https://sellingpartnerapi-eu.amazon.com*

Method | HTTP request | Description
------------- | ------------- | -------------
[**cancelShipment**](ShippingApi.md#cancelShipment) | **PUT** /shipping/v2/shipments/{shipmentId}/cancel | 
[**directPurchaseShipment**](ShippingApi.md#directPurchaseShipment) | **POST** /shipping/v2/shipments/directPurchase | 
[**generateCollectionForm**](ShippingApi.md#generateCollectionForm) | **POST** /shipping/v2/collectionForms | 
[**getAccessPoints**](ShippingApi.md#getAccessPoints) | **GET** /shipping/v2/accessPoints | 
[**getAdditionalInputs**](ShippingApi.md#getAdditionalInputs) | **GET** /shipping/v2/shipments/additionalInputs/schema | 
[**getCarrierAccountFormInputs**](ShippingApi.md#getCarrierAccountFormInputs) | **GET** /shipping/v2/carrierAccountFormInputs | 
[**getCarrierAccounts**](ShippingApi.md#getCarrierAccounts) | **PUT** /shipping/v2/carrierAccounts | 
[**getCollectionForm**](ShippingApi.md#getCollectionForm) | **GET** /shipping/v2/collectionForms/{collectionFormId} | 
[**getCollectionFormHistory**](ShippingApi.md#getCollectionFormHistory) | **PUT** /shipping/v2/collectionForms/history | 
[**getRates**](ShippingApi.md#getRates) | **POST** /shipping/v2/shipments/rates | 
[**getShipmentDocuments**](ShippingApi.md#getShipmentDocuments) | **GET** /shipping/v2/shipments/{shipmentId}/documents | 
[**getTracking**](ShippingApi.md#getTracking) | **GET** /shipping/v2/tracking | 
[**getUnmanifestedShipments**](ShippingApi.md#getUnmanifestedShipments) | **PUT** /shipping/v2/unmanifestedShipments | 
[**linkCarrierAccount**](ShippingApi.md#linkCarrierAccount) | **PUT** /shipping/v2/carrierAccounts/{carrierId} | 
[**oneClickShipment**](ShippingApi.md#oneClickShipment) | **POST** /shipping/v2/oneClickShipment | 
[**purchaseShipment**](ShippingApi.md#purchaseShipment) | **POST** /shipping/v2/shipments | 
[**unlinkCarrierAccount**](ShippingApi.md#unlinkCarrierAccount) | **PUT** /shipping/v2/carrierAccounts/{carrierId}/unlink | 


<a name="cancelShipment"></a>
# **cancelShipment**
> CancelShipmentResponse cancelShipment(shipmentId, xAmznShippingBusinessId)



Cancels a purchased shipment. Returns an empty object if the shipment is successfully cancelled.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 80 | 100 |  The &#x60;x-amzn-RateLimit-Limit&#x60; response header returns the usage plan rate limits that were applied to the requested operation, when available. The table above indicates the default rate and burst values for this operation. Selling partners whose business demands require higher throughput may see higher rate and burst values then those shown here. For more information, see [Usage Plans and Rate Limits in the Selling Partner API](https://developer-docs.amazon.com/sp-api/docs/usage-plans-and-rate-limits-in-the-sp-api).

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.ShippingApi;


ShippingApi apiInstance = new ShippingApi();
String shipmentId = "shipmentId_example"; // String | The shipment identifier originally returned by the purchaseShipment operation.
String xAmznShippingBusinessId = "xAmznShippingBusinessId_example"; // String | Amazon shipping business to assume for this request. The default is AmazonShipping_UK.
try {
    CancelShipmentResponse result = apiInstance.cancelShipment(shipmentId, xAmznShippingBusinessId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ShippingApi#cancelShipment");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **shipmentId** | **String**| The shipment identifier originally returned by the purchaseShipment operation. |
 **xAmznShippingBusinessId** | **String**| Amazon shipping business to assume for this request. The default is AmazonShipping_UK. | [optional] [enum: AmazonShipping_US, AmazonShipping_IN, AmazonShipping_UK, AmazonShipping_UAE, AmazonShipping_SA, AmazonShipping_EG, AmazonShipping_IT, AmazonShipping_ES, AmazonShipping_FR, AmazonShipping_JP]

### Return type

[**CancelShipmentResponse**](CancelShipmentResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="directPurchaseShipment"></a>
# **directPurchaseShipment**
> DirectPurchaseResponse directPurchaseShipment(body, xAmznIdempotencyKey, locale, xAmznShippingBusinessId)



Purchases the shipping service for a shipment using the best fit service offering. Returns purchase related details and documents.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 80 | 100 |  The &#x60;x-amzn-RateLimit-Limit&#x60; response header returns the usage plan rate limits that were applied to the requested operation, when available. The table above indicates the default rate and burst values for this operation. Selling partners whose business demands require higher throughput may see higher rate and burst values then those shown here. For more information, see [Usage Plans and Rate Limits in the Selling Partner API](https://developer-docs.amazon.com/sp-api/docs/usage-plans-and-rate-limits-in-the-sp-api).

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.ShippingApi;


ShippingApi apiInstance = new ShippingApi();
DirectPurchaseRequest body = new DirectPurchaseRequest(); // DirectPurchaseRequest | 
String xAmznIdempotencyKey = "xAmznIdempotencyKey_example"; // String | A unique value which the server uses to recognize subsequent retries of the same request.
String locale = "locale_example"; // String | The IETF Language Tag. Note that this only supports the primary language subtag with one secondary language subtag (i.e. en-US, fr-CA). The secondary language subtag is almost always a regional designation. This does not support additional subtags beyond the primary and secondary language subtags. 
String xAmznShippingBusinessId = "xAmznShippingBusinessId_example"; // String | Amazon shipping business to assume for this request. The default is AmazonShipping_UK.
try {
    DirectPurchaseResponse result = apiInstance.directPurchaseShipment(body, xAmznIdempotencyKey, locale, xAmznShippingBusinessId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ShippingApi#directPurchaseShipment");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**DirectPurchaseRequest**](DirectPurchaseRequest.md)|  |
 **xAmznIdempotencyKey** | **String**| A unique value which the server uses to recognize subsequent retries of the same request. | [optional]
 **locale** | **String**| The IETF Language Tag. Note that this only supports the primary language subtag with one secondary language subtag (i.e. en-US, fr-CA). The secondary language subtag is almost always a regional designation. This does not support additional subtags beyond the primary and secondary language subtags.  | [optional]
 **xAmznShippingBusinessId** | **String**| Amazon shipping business to assume for this request. The default is AmazonShipping_UK. | [optional] [enum: AmazonShipping_US, AmazonShipping_IN, AmazonShipping_UK, AmazonShipping_UAE, AmazonShipping_SA, AmazonShipping_EG, AmazonShipping_IT, AmazonShipping_ES, AmazonShipping_FR, AmazonShipping_JP]

### Return type

[**DirectPurchaseResponse**](DirectPurchaseResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="generateCollectionForm"></a>
# **generateCollectionForm**
> GenerateCollectionFormResponse generateCollectionForm(body, xAmznIdempotencyKey, xAmznShippingBusinessId)



This API  Call to generate the collection form.   **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 80 | 100 |  The &#x60;x-amzn-RateLimit-Limit&#x60; response header returns the usage plan rate limits that were applied to the requested operation, when available. The table above indicates the default rate and burst values for this operation. Selling partners whose business demands require higher throughput may see higher rate and burst values then those shown here. For more information, see [Usage Plans and Rate Limits in the Selling Partner API](https://developer-docs.amazon.com/sp-api/docs/usage-plans-and-rate-limits-in-the-sp-api).

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.ShippingApi;


ShippingApi apiInstance = new ShippingApi();
GenerateCollectionFormRequest body = new GenerateCollectionFormRequest(); // GenerateCollectionFormRequest | 
String xAmznIdempotencyKey = "xAmznIdempotencyKey_example"; // String | A unique value which the server uses to recognize subsequent retries of the same request.
String xAmznShippingBusinessId = "xAmznShippingBusinessId_example"; // String | Amazon shipping business to assume for this request. The default is AmazonShipping_UK.
try {
    GenerateCollectionFormResponse result = apiInstance.generateCollectionForm(body, xAmznIdempotencyKey, xAmznShippingBusinessId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ShippingApi#generateCollectionForm");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**GenerateCollectionFormRequest**](GenerateCollectionFormRequest.md)|  |
 **xAmznIdempotencyKey** | **String**| A unique value which the server uses to recognize subsequent retries of the same request. | [optional]
 **xAmznShippingBusinessId** | **String**| Amazon shipping business to assume for this request. The default is AmazonShipping_UK. | [optional] [enum: AmazonShipping_US, AmazonShipping_IN, AmazonShipping_UK, AmazonShipping_UAE, AmazonShipping_SA, AmazonShipping_EG, AmazonShipping_IT, AmazonShipping_ES, AmazonShipping_FR, AmazonShipping_JP]

### Return type

[**GenerateCollectionFormResponse**](GenerateCollectionFormResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getAccessPoints"></a>
# **getAccessPoints**
> GetAccessPointsResponse getAccessPoints(accessPointTypes, countryCode, postalCode, xAmznShippingBusinessId)



Returns a list of access points in proximity of input postal code.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 80 | 100 |  The &#x60;x-amzn-RateLimit-Limit&#x60; response header returns the usage plan rate limits that were applied to the requested operation, when available. The table above indicates the default rate and burst values for this operation. Selling partners whose business demands require higher throughput may see higher rate and burst values then those shown here. For more information, see [Usage Plans and Rate Limits in the Selling Partner API](https://developer-docs.amazon.com/sp-api/docs/usage-plans-and-rate-limits-in-the-sp-api).

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.ShippingApi;


ShippingApi apiInstance = new ShippingApi();
List<String> accessPointTypes = Arrays.asList("HELIX"); // List<String> | 
String countryCode = "US"; // String | 
String postalCode = "EX332JL"; // String | 
String xAmznShippingBusinessId = "xAmznShippingBusinessId_example"; // String | Amazon shipping business to assume for this request. The default is AmazonShipping_UK.
try {
    GetAccessPointsResponse result = apiInstance.getAccessPoints(accessPointTypes, countryCode, postalCode, xAmznShippingBusinessId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ShippingApi#getAccessPoints");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **accessPointTypes** | [**List&lt;String&gt;**](String.md)|  | [enum: HELIX, CAMPUS_LOCKER, OMNI_LOCKER, ODIN_LOCKER, DOBBY_LOCKER, CORE_LOCKER, 3P, CAMPUS_ROOM]
 **countryCode** | **String**|  |
 **postalCode** | **String**|  |
 **xAmznShippingBusinessId** | **String**| Amazon shipping business to assume for this request. The default is AmazonShipping_UK. | [optional] [enum: AmazonShipping_US, AmazonShipping_IN, AmazonShipping_UK, AmazonShipping_UAE, AmazonShipping_SA, AmazonShipping_EG, AmazonShipping_IT, AmazonShipping_ES, AmazonShipping_FR, AmazonShipping_JP]

### Return type

[**GetAccessPointsResponse**](GetAccessPointsResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getAdditionalInputs"></a>
# **getAdditionalInputs**
> GetAdditionalInputsResponse getAdditionalInputs(requestToken, rateId, xAmznShippingBusinessId)



Returns the JSON schema to use for providing additional inputs when needed to purchase a shipping offering. Call the getAdditionalInputs operation when the response to a previous call to the getRates operation indicates that additional inputs are required for the rate (shipping offering) that you want to purchase.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 80 | 100 |  The &#x60;x-amzn-RateLimit-Limit&#x60; response header returns the usage plan rate limits that were applied to the requested operation, when available. The table above indicates the default rate and burst values for this operation. Selling partners whose business demands require higher throughput may see higher rate and burst values then those shown here. For more information, see [Usage Plans and Rate Limits in the Selling Partner API](https://developer-docs.amazon.com/sp-api/docs/usage-plans-and-rate-limits-in-the-sp-api).

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.ShippingApi;


ShippingApi apiInstance = new ShippingApi();
String requestToken = "requestToken_example"; // String | The request token returned in the response to the getRates operation.
String rateId = "rateId_example"; // String | The rate identifier for the shipping offering (rate) returned in the response to the getRates operation.
String xAmznShippingBusinessId = "xAmznShippingBusinessId_example"; // String | Amazon shipping business to assume for this request. The default is AmazonShipping_UK.
try {
    GetAdditionalInputsResponse result = apiInstance.getAdditionalInputs(requestToken, rateId, xAmznShippingBusinessId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ShippingApi#getAdditionalInputs");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **requestToken** | **String**| The request token returned in the response to the getRates operation. |
 **rateId** | **String**| The rate identifier for the shipping offering (rate) returned in the response to the getRates operation. |
 **xAmznShippingBusinessId** | **String**| Amazon shipping business to assume for this request. The default is AmazonShipping_UK. | [optional] [enum: AmazonShipping_US, AmazonShipping_IN, AmazonShipping_UK, AmazonShipping_UAE, AmazonShipping_SA, AmazonShipping_EG, AmazonShipping_IT, AmazonShipping_ES, AmazonShipping_FR, AmazonShipping_JP]

### Return type

[**GetAdditionalInputsResponse**](GetAdditionalInputsResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getCarrierAccountFormInputs"></a>
# **getCarrierAccountFormInputs**
> GetCarrierAccountFormInputsResponse getCarrierAccountFormInputs(xAmznShippingBusinessId)



This API will return a list of input schema required to register a shipper account with the carrier.   **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 80 | 100 |  The &#x60;x-amzn-RateLimit-Limit&#x60; response header returns the usage plan rate limits that were applied to the requested operation, when available. The table above indicates the default rate and burst values for this operation. Selling partners whose business demands require higher throughput may see higher rate and burst values then those shown here. For more information, see [Usage Plans and Rate Limits in the Selling Partner API](https://developer-docs.amazon.com/sp-api/docs/usage-plans-and-rate-limits-in-the-sp-api).

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.ShippingApi;


ShippingApi apiInstance = new ShippingApi();
String xAmznShippingBusinessId = "xAmznShippingBusinessId_example"; // String | Amazon shipping business to assume for this request. The default is AmazonShipping_UK.
try {
    GetCarrierAccountFormInputsResponse result = apiInstance.getCarrierAccountFormInputs(xAmznShippingBusinessId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ShippingApi#getCarrierAccountFormInputs");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **xAmznShippingBusinessId** | **String**| Amazon shipping business to assume for this request. The default is AmazonShipping_UK. | [optional] [enum: AmazonShipping_US, AmazonShipping_IN, AmazonShipping_UK, AmazonShipping_UAE, AmazonShipping_SA, AmazonShipping_EG, AmazonShipping_IT, AmazonShipping_ES, AmazonShipping_FR, AmazonShipping_JP]

### Return type

[**GetCarrierAccountFormInputsResponse**](GetCarrierAccountFormInputsResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getCarrierAccounts"></a>
# **getCarrierAccounts**
> GetCarrierAccountsResponse getCarrierAccounts(body, xAmznShippingBusinessId)



This API will return Get all carrier accounts for a merchant.   **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 80 | 100 |  The &#x60;x-amzn-RateLimit-Limit&#x60; response header returns the usage plan rate limits that were applied to the requested operation, when available. The table above indicates the default rate and burst values for this operation. Selling partners whose business demands require higher throughput may see higher rate and burst values then those shown here. For more information, see [Usage Plans and Rate Limits in the Selling Partner API](https://developer-docs.amazon.com/sp-api/docs/usage-plans-and-rate-limits-in-the-sp-api).

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.ShippingApi;


ShippingApi apiInstance = new ShippingApi();
GetCarrierAccountsRequest body = new GetCarrierAccountsRequest(); // GetCarrierAccountsRequest | 
String xAmznShippingBusinessId = "xAmznShippingBusinessId_example"; // String | Amazon shipping business to assume for this request. The default is AmazonShipping_UK.
try {
    GetCarrierAccountsResponse result = apiInstance.getCarrierAccounts(body, xAmznShippingBusinessId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ShippingApi#getCarrierAccounts");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**GetCarrierAccountsRequest**](GetCarrierAccountsRequest.md)|  |
 **xAmznShippingBusinessId** | **String**| Amazon shipping business to assume for this request. The default is AmazonShipping_UK. | [optional] [enum: AmazonShipping_US, AmazonShipping_IN, AmazonShipping_UK, AmazonShipping_UAE, AmazonShipping_SA, AmazonShipping_EG, AmazonShipping_IT, AmazonShipping_ES, AmazonShipping_FR, AmazonShipping_JP]

### Return type

[**GetCarrierAccountsResponse**](GetCarrierAccountsResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getCollectionForm"></a>
# **getCollectionForm**
> GetCollectionFormResponse getCollectionForm(collectionFormId, xAmznShippingBusinessId)



This API reprint a collection form.   **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 80 | 100 |  The &#x60;x-amzn-RateLimit-Limit&#x60; response header returns the usage plan rate limits that were applied to the requested operation, when available. The table above indicates the default rate and burst values for this operation. Selling partners whose business demands require higher throughput may see higher rate and burst values then those shown here. For more information, see [Usage Plans and Rate Limits in the Selling Partner API](https://developer-docs.amazon.com/sp-api/docs/usage-plans-and-rate-limits-in-the-sp-api).

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.ShippingApi;


ShippingApi apiInstance = new ShippingApi();
String collectionFormId = "collectionFormId_example"; // String | collection form Id to reprint a collection.
String xAmznShippingBusinessId = "xAmznShippingBusinessId_example"; // String | Amazon shipping business to assume for this request. The default is AmazonShipping_UK.
try {
    GetCollectionFormResponse result = apiInstance.getCollectionForm(collectionFormId, xAmznShippingBusinessId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ShippingApi#getCollectionForm");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **collectionFormId** | **String**| collection form Id to reprint a collection. |
 **xAmznShippingBusinessId** | **String**| Amazon shipping business to assume for this request. The default is AmazonShipping_UK. | [optional] [enum: AmazonShipping_US, AmazonShipping_IN, AmazonShipping_UK, AmazonShipping_UAE, AmazonShipping_SA, AmazonShipping_EG, AmazonShipping_IT, AmazonShipping_ES, AmazonShipping_FR, AmazonShipping_JP]

### Return type

[**GetCollectionFormResponse**](GetCollectionFormResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getCollectionFormHistory"></a>
# **getCollectionFormHistory**
> GetCollectionFormHistoryResponse getCollectionFormHistory(body, xAmznShippingBusinessId)



This API Call to get the history of the previously generated collection forms.   **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 80 | 100 |  The &#x60;x-amzn-RateLimit-Limit&#x60; response header returns the usage plan rate limits that were applied to the requested operation, when available. The table above indicates the default rate and burst values for this operation. Selling partners whose business demands require higher throughput may see higher rate and burst values then those shown here. For more information, see [Usage Plans and Rate Limits in the Selling Partner API](https://developer-docs.amazon.com/sp-api/docs/usage-plans-and-rate-limits-in-the-sp-api).

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.ShippingApi;


ShippingApi apiInstance = new ShippingApi();
GetCollectionFormHistoryRequest body = new GetCollectionFormHistoryRequest(); // GetCollectionFormHistoryRequest | 
String xAmznShippingBusinessId = "xAmznShippingBusinessId_example"; // String | Amazon shipping business to assume for this request. The default is AmazonShipping_UK.
try {
    GetCollectionFormHistoryResponse result = apiInstance.getCollectionFormHistory(body, xAmznShippingBusinessId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ShippingApi#getCollectionFormHistory");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**GetCollectionFormHistoryRequest**](GetCollectionFormHistoryRequest.md)|  |
 **xAmznShippingBusinessId** | **String**| Amazon shipping business to assume for this request. The default is AmazonShipping_UK. | [optional] [enum: AmazonShipping_US, AmazonShipping_IN, AmazonShipping_UK, AmazonShipping_UAE, AmazonShipping_SA, AmazonShipping_EG, AmazonShipping_IT, AmazonShipping_ES, AmazonShipping_FR, AmazonShipping_JP]

### Return type

[**GetCollectionFormHistoryResponse**](GetCollectionFormHistoryResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getRates"></a>
# **getRates**
> GetRatesResponse getRates(body, xAmznShippingBusinessId)



Returns the available shipping service offerings.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 80 | 100 |  The &#x60;x-amzn-RateLimit-Limit&#x60; response header returns the usage plan rate limits that were applied to the requested operation, when available. The table above indicates the default rate and burst values for this operation. Selling partners whose business demands require higher throughput may see higher rate and burst values then those shown here. For more information, see [Usage Plans and Rate Limits in the Selling Partner API](https://developer-docs.amazon.com/sp-api/docs/usage-plans-and-rate-limits-in-the-sp-api).

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.ShippingApi;


ShippingApi apiInstance = new ShippingApi();
GetRatesRequest body = new GetRatesRequest(); // GetRatesRequest | 
String xAmznShippingBusinessId = "xAmznShippingBusinessId_example"; // String | Amazon shipping business to assume for this request. The default is AmazonShipping_UK.
try {
    GetRatesResponse result = apiInstance.getRates(body, xAmznShippingBusinessId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ShippingApi#getRates");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**GetRatesRequest**](GetRatesRequest.md)|  |
 **xAmznShippingBusinessId** | **String**| Amazon shipping business to assume for this request. The default is AmazonShipping_UK. | [optional] [enum: AmazonShipping_US, AmazonShipping_IN, AmazonShipping_UK, AmazonShipping_UAE, AmazonShipping_SA, AmazonShipping_EG, AmazonShipping_IT, AmazonShipping_ES, AmazonShipping_FR, AmazonShipping_JP]

### Return type

[**GetRatesResponse**](GetRatesResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getShipmentDocuments"></a>
# **getShipmentDocuments**
> GetShipmentDocumentsResponse getShipmentDocuments(shipmentId, packageClientReferenceId, format, dpi, xAmznShippingBusinessId)



Returns the shipping documents associated with a package in a shipment.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 80 | 100 |  The &#x60;x-amzn-RateLimit-Limit&#x60; response header returns the usage plan rate limits that were applied to the requested operation, when available. The table above indicates the default rate and burst values for this operation. Selling partners whose business demands require higher throughput may see higher rate and burst values then those shown here. For more information, see [Usage Plans and Rate Limits in the Selling Partner API](https://developer-docs.amazon.com/sp-api/docs/usage-plans-and-rate-limits-in-the-sp-api).

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.ShippingApi;


ShippingApi apiInstance = new ShippingApi();
String shipmentId = "shipmentId_example"; // String | The shipment identifier originally returned by the purchaseShipment operation.
String packageClientReferenceId = "packageClientReferenceId_example"; // String | The package client reference identifier originally provided in the request body parameter for the getRates operation.
String format = "format_example"; // String | The file format of the document. Must be one of the supported formats returned by the getRates operation.
BigDecimal dpi = new BigDecimal(); // BigDecimal | The resolution of the document (for example, 300 means 300 dots per inch). Must be one of the supported resolutions returned in the response to the getRates operation.
String xAmznShippingBusinessId = "xAmznShippingBusinessId_example"; // String | Amazon shipping business to assume for this request. The default is AmazonShipping_UK.
try {
    GetShipmentDocumentsResponse result = apiInstance.getShipmentDocuments(shipmentId, packageClientReferenceId, format, dpi, xAmznShippingBusinessId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ShippingApi#getShipmentDocuments");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **shipmentId** | **String**| The shipment identifier originally returned by the purchaseShipment operation. |
 **packageClientReferenceId** | **String**| The package client reference identifier originally provided in the request body parameter for the getRates operation. |
 **format** | **String**| The file format of the document. Must be one of the supported formats returned by the getRates operation. | [optional]
 **dpi** | **BigDecimal**| The resolution of the document (for example, 300 means 300 dots per inch). Must be one of the supported resolutions returned in the response to the getRates operation. | [optional]
 **xAmznShippingBusinessId** | **String**| Amazon shipping business to assume for this request. The default is AmazonShipping_UK. | [optional] [enum: AmazonShipping_US, AmazonShipping_IN, AmazonShipping_UK, AmazonShipping_UAE, AmazonShipping_SA, AmazonShipping_EG, AmazonShipping_IT, AmazonShipping_ES, AmazonShipping_FR, AmazonShipping_JP]

### Return type

[**GetShipmentDocumentsResponse**](GetShipmentDocumentsResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getTracking"></a>
# **getTracking**
> GetTrackingResponse getTracking(trackingId, carrierId, xAmznShippingBusinessId)



Returns tracking information for a purchased shipment.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 80 | 100 |  The &#x60;x-amzn-RateLimit-Limit&#x60; response header returns the usage plan rate limits that were applied to the requested operation, when available. The table above indicates the default rate and burst values for this operation. Selling partners whose business demands require higher throughput may see higher rate and burst values then those shown here. For more information, see [Usage Plans and Rate Limits in the Selling Partner API](https://developer-docs.amazon.com/sp-api/docs/usage-plans-and-rate-limits-in-the-sp-api).

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.ShippingApi;


ShippingApi apiInstance = new ShippingApi();
String trackingId = "trackingId_example"; // String | A carrier-generated tracking identifier originally returned by the purchaseShipment operation.
String carrierId = "carrierId_example"; // String | A carrier identifier originally returned by the getRates operation for the selected rate.
String xAmznShippingBusinessId = "xAmznShippingBusinessId_example"; // String | Amazon shipping business to assume for this request. The default is AmazonShipping_UK.
try {
    GetTrackingResponse result = apiInstance.getTracking(trackingId, carrierId, xAmznShippingBusinessId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ShippingApi#getTracking");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **trackingId** | **String**| A carrier-generated tracking identifier originally returned by the purchaseShipment operation. |
 **carrierId** | **String**| A carrier identifier originally returned by the getRates operation for the selected rate. |
 **xAmznShippingBusinessId** | **String**| Amazon shipping business to assume for this request. The default is AmazonShipping_UK. | [optional] [enum: AmazonShipping_US, AmazonShipping_IN, AmazonShipping_UK, AmazonShipping_UAE, AmazonShipping_SA, AmazonShipping_EG, AmazonShipping_IT, AmazonShipping_ES, AmazonShipping_FR, AmazonShipping_JP]

### Return type

[**GetTrackingResponse**](GetTrackingResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getUnmanifestedShipments"></a>
# **getUnmanifestedShipments**
> GetUnmanifestedShipmentsResponse getUnmanifestedShipments(body, xAmznShippingBusinessId)



This API Get all unmanifested carriers with shipment locations. Any locations which has unmanifested shipments         with an eligible carrier for manifesting shall be returned.   **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 80 | 100 |  The &#x60;x-amzn-RateLimit-Limit&#x60; response header returns the usage plan rate limits that were applied to the requested operation, when available. The table above indicates the default rate and burst values for this operation. Selling partners whose business demands require higher throughput may see higher rate and burst values then those shown here. For more information, see [Usage Plans and Rate Limits in the Selling Partner API](https://developer-docs.amazon.com/sp-api/docs/usage-plans-and-rate-limits-in-the-sp-api).

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.ShippingApi;


ShippingApi apiInstance = new ShippingApi();
GetUnmanifestedShipmentsRequest body = new GetUnmanifestedShipmentsRequest(); // GetUnmanifestedShipmentsRequest | 
String xAmznShippingBusinessId = "xAmznShippingBusinessId_example"; // String | Amazon shipping business to assume for this request. The default is AmazonShipping_UK.
try {
    GetUnmanifestedShipmentsResponse result = apiInstance.getUnmanifestedShipments(body, xAmznShippingBusinessId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ShippingApi#getUnmanifestedShipments");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**GetUnmanifestedShipmentsRequest**](GetUnmanifestedShipmentsRequest.md)|  |
 **xAmznShippingBusinessId** | **String**| Amazon shipping business to assume for this request. The default is AmazonShipping_UK. | [optional] [enum: AmazonShipping_US, AmazonShipping_IN, AmazonShipping_UK, AmazonShipping_UAE, AmazonShipping_SA, AmazonShipping_EG, AmazonShipping_IT, AmazonShipping_ES, AmazonShipping_FR, AmazonShipping_JP]

### Return type

[**GetUnmanifestedShipmentsResponse**](GetUnmanifestedShipmentsResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="linkCarrierAccount"></a>
# **linkCarrierAccount**
> LinkCarrierAccountResponse linkCarrierAccount(carrierId, body, xAmznShippingBusinessId)



This API associates/links the specified carrier account with the merchant.   **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 80 | 100 |  The &#x60;x-amzn-RateLimit-Limit&#x60; response header returns the usage plan rate limits that were applied to the requested operation, when available. The table above indicates the default rate and burst values for this operation. Selling partners whose business demands require higher throughput may see higher rate and burst values then those shown here. For more information, see [Usage Plans and Rate Limits in the Selling Partner API](https://developer-docs.amazon.com/sp-api/docs/usage-plans-and-rate-limits-in-the-sp-api).

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.ShippingApi;


ShippingApi apiInstance = new ShippingApi();
String carrierId = "carrierId_example"; // String | The unique identifier associated with the carrier account.
LinkCarrierAccountRequest body = new LinkCarrierAccountRequest(); // LinkCarrierAccountRequest | 
String xAmznShippingBusinessId = "xAmznShippingBusinessId_example"; // String | Amazon shipping business to assume for this request. The default is AmazonShipping_UK.
try {
    LinkCarrierAccountResponse result = apiInstance.linkCarrierAccount(carrierId, body, xAmznShippingBusinessId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ShippingApi#linkCarrierAccount");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **carrierId** | **String**| The unique identifier associated with the carrier account. |
 **body** | [**LinkCarrierAccountRequest**](LinkCarrierAccountRequest.md)|  |
 **xAmznShippingBusinessId** | **String**| Amazon shipping business to assume for this request. The default is AmazonShipping_UK. | [optional] [enum: AmazonShipping_US, AmazonShipping_IN, AmazonShipping_UK, AmazonShipping_UAE, AmazonShipping_SA, AmazonShipping_EG, AmazonShipping_IT, AmazonShipping_ES, AmazonShipping_FR, AmazonShipping_JP]

### Return type

[**LinkCarrierAccountResponse**](LinkCarrierAccountResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="oneClickShipment"></a>
# **oneClickShipment**
> OneClickShipmentResponse oneClickShipment(body, xAmznShippingBusinessId)



Purchases a shipping service identifier and returns purchase-related details and documents.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 80 | 100 |  The &#x60;x-amzn-RateLimit-Limit&#x60; response header returns the usage plan rate limits that were applied to the requested operation, when available. The table above indicates the default rate and burst values for this operation. Selling partners whose business demands require higher throughput may see higher rate and burst values then those shown here. For more information, see [Usage Plans and Rate Limits in the Selling Partner API](https://developer-docs.amazon.com/sp-api/docs/usage-plans-and-rate-limits-in-the-sp-api).

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.ShippingApi;


ShippingApi apiInstance = new ShippingApi();
OneClickShipmentRequest body = new OneClickShipmentRequest(); // OneClickShipmentRequest | 
String xAmznShippingBusinessId = "xAmznShippingBusinessId_example"; // String | Amazon shipping business to assume for this request. The default is AmazonShipping_UK.
try {
    OneClickShipmentResponse result = apiInstance.oneClickShipment(body, xAmznShippingBusinessId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ShippingApi#oneClickShipment");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**OneClickShipmentRequest**](OneClickShipmentRequest.md)|  |
 **xAmznShippingBusinessId** | **String**| Amazon shipping business to assume for this request. The default is AmazonShipping_UK. | [optional] [enum: AmazonShipping_US, AmazonShipping_IN, AmazonShipping_UK, AmazonShipping_UAE, AmazonShipping_SA, AmazonShipping_EG, AmazonShipping_IT, AmazonShipping_ES, AmazonShipping_FR, AmazonShipping_JP]

### Return type

[**OneClickShipmentResponse**](OneClickShipmentResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="purchaseShipment"></a>
# **purchaseShipment**
> PurchaseShipmentResponse purchaseShipment(body, xAmznIdempotencyKey, xAmznShippingBusinessId)



Purchases a shipping service and returns purchase related details and documents.  Note: You must complete the purchase within 10 minutes of rate creation by the shipping service provider. If you make the request after the 10 minutes have expired, you will receive an error response with the error code equal to \&quot;TOKEN_EXPIRED\&quot;. If you receive this error response, you must get the rates for the shipment again.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 80 | 100 |  The &#x60;x-amzn-RateLimit-Limit&#x60; response header returns the usage plan rate limits that were applied to the requested operation, when available. The table above indicates the default rate and burst values for this operation. Selling partners whose business demands require higher throughput may see higher rate and burst values then those shown here. For more information, see [Usage Plans and Rate Limits in the Selling Partner API](https://developer-docs.amazon.com/sp-api/docs/usage-plans-and-rate-limits-in-the-sp-api).

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.ShippingApi;


ShippingApi apiInstance = new ShippingApi();
PurchaseShipmentRequest body = new PurchaseShipmentRequest(); // PurchaseShipmentRequest | 
String xAmznIdempotencyKey = "xAmznIdempotencyKey_example"; // String | A unique value which the server uses to recognize subsequent retries of the same request.
String xAmznShippingBusinessId = "xAmznShippingBusinessId_example"; // String | Amazon shipping business to assume for this request. The default is AmazonShipping_UK.
try {
    PurchaseShipmentResponse result = apiInstance.purchaseShipment(body, xAmznIdempotencyKey, xAmznShippingBusinessId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ShippingApi#purchaseShipment");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**PurchaseShipmentRequest**](PurchaseShipmentRequest.md)|  |
 **xAmznIdempotencyKey** | **String**| A unique value which the server uses to recognize subsequent retries of the same request. | [optional]
 **xAmznShippingBusinessId** | **String**| Amazon shipping business to assume for this request. The default is AmazonShipping_UK. | [optional] [enum: AmazonShipping_US, AmazonShipping_IN, AmazonShipping_UK, AmazonShipping_UAE, AmazonShipping_SA, AmazonShipping_EG, AmazonShipping_IT, AmazonShipping_ES, AmazonShipping_FR, AmazonShipping_JP]

### Return type

[**PurchaseShipmentResponse**](PurchaseShipmentResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="unlinkCarrierAccount"></a>
# **unlinkCarrierAccount**
> UnlinkCarrierAccountResponse unlinkCarrierAccount(carrierId, body, xAmznShippingBusinessId)



This API Unlink the specified carrier account with the merchant.   **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 80 | 100 |  The &#x60;x-amzn-RateLimit-Limit&#x60; response header returns the usage plan rate limits that were applied to the requested operation, when available. The table above indicates the default rate and burst values for this operation. Selling partners whose business demands require higher throughput may see higher rate and burst values then those shown here. For more information, see [Usage Plans and Rate Limits in the Selling Partner API](https://developer-docs.amazon.com/sp-api/docs/usage-plans-and-rate-limits-in-the-sp-api).

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.ShippingApi;


ShippingApi apiInstance = new ShippingApi();
String carrierId = "carrierId_example"; // String | carrier Id to unlink with merchant.
UnlinkCarrierAccountRequest body = new UnlinkCarrierAccountRequest(); // UnlinkCarrierAccountRequest | 
String xAmznShippingBusinessId = "xAmznShippingBusinessId_example"; // String | Amazon shipping business to assume for this request. The default is AmazonShipping_UK.
try {
    UnlinkCarrierAccountResponse result = apiInstance.unlinkCarrierAccount(carrierId, body, xAmznShippingBusinessId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ShippingApi#unlinkCarrierAccount");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **carrierId** | **String**| carrier Id to unlink with merchant. |
 **body** | [**UnlinkCarrierAccountRequest**](UnlinkCarrierAccountRequest.md)|  |
 **xAmznShippingBusinessId** | **String**| Amazon shipping business to assume for this request. The default is AmazonShipping_UK. | [optional] [enum: AmazonShipping_US, AmazonShipping_IN, AmazonShipping_UK, AmazonShipping_UAE, AmazonShipping_SA, AmazonShipping_EG, AmazonShipping_IT, AmazonShipping_ES, AmazonShipping_FR, AmazonShipping_JP]

### Return type

[**UnlinkCarrierAccountResponse**](UnlinkCarrierAccountResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

