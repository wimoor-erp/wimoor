# ShippingApi

All URIs are relative to *https://sellingpartnerapi-na.amazon.com*

Method | HTTP request | Description
------------- | ------------- | -------------
[**cancelShipment**](ShippingApi.md#cancelShipment) | **POST** /shipping/v1/shipments/{shipmentId}/cancel | 
[**createShipment**](ShippingApi.md#createShipment) | **POST** /shipping/v1/shipments | 
[**getAccount**](ShippingApi.md#getAccount) | **GET** /shipping/v1/account | 
[**getRates**](ShippingApi.md#getRates) | **POST** /shipping/v1/rates | 
[**getShipment**](ShippingApi.md#getShipment) | **GET** /shipping/v1/shipments/{shipmentId} | 
[**getTrackingInformation**](ShippingApi.md#getTrackingInformation) | **GET** /shipping/v1/tracking/{trackingId} | 
[**purchaseLabels**](ShippingApi.md#purchaseLabels) | **POST** /shipping/v1/shipments/{shipmentId}/purchaseLabels | 
[**purchaseShipment**](ShippingApi.md#purchaseShipment) | **POST** /shipping/v1/purchaseShipment | 
[**retrieveShippingLabel**](ShippingApi.md#retrieveShippingLabel) | **POST** /shipping/v1/shipments/{shipmentId}/containers/{trackingId}/label | 


<a name="cancelShipment"></a>
# **cancelShipment**
> CancelShipmentResponse cancelShipment(shipmentId)



Cancel a shipment by the given shipmentId.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 5 | 15 |  For more information, see \&quot;Usage Plans and Rate Limits\&quot; in the Selling Partner API documentation.

### Example
```java
// Import classes:
//import com.amazon.spapi.shipping.ApiException;
//import com.amazon.spapi.shipping.api.ShippingApi;


ShippingApi apiInstance = new ShippingApi();
String shipmentId = "shipmentId_example"; // String | 
try {
    CancelShipmentResponse result = apiInstance.cancelShipment(shipmentId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ShippingApi#cancelShipment");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **shipmentId** | **String**|  |

### Return type

[**CancelShipmentResponse**](CancelShipmentResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="createShipment"></a>
# **createShipment**
> CreateShipmentResponse createShipment(body)



Create a new shipment.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 5 | 15 |  For more information, see \&quot;Usage Plans and Rate Limits\&quot; in the Selling Partner API documentation.

### Example
```java
// Import classes:
//import com.amazon.spapi.shipping.ApiException;
//import com.amazon.spapi.shipping.api.ShippingApi;


ShippingApi apiInstance = new ShippingApi();
CreateShipmentRequest body = new CreateShipmentRequest(); // CreateShipmentRequest | 
try {
    CreateShipmentResponse result = apiInstance.createShipment(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ShippingApi#createShipment");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**CreateShipmentRequest**](CreateShipmentRequest.md)|  |

### Return type

[**CreateShipmentResponse**](CreateShipmentResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getAccount"></a>
# **getAccount**
> GetAccountResponse getAccount()



Verify if the current account is valid.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 5 | 15 |  For more information, see \&quot;Usage Plans and Rate Limits\&quot; in the Selling Partner API documentation.

### Example
```java
// Import classes:
//import com.amazon.spapi.shipping.ApiException;
//import com.amazon.spapi.shipping.api.ShippingApi;


ShippingApi apiInstance = new ShippingApi();
try {
    GetAccountResponse result = apiInstance.getAccount();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ShippingApi#getAccount");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**GetAccountResponse**](GetAccountResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getRates"></a>
# **getRates**
> GetRatesResponse getRates(body)



Get service rates.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 5 | 15 |  For more information, see \&quot;Usage Plans and Rate Limits\&quot; in the Selling Partner API documentation.

### Example
```java
// Import classes:
//import com.amazon.spapi.shipping.ApiException;
//import com.amazon.spapi.shipping.api.ShippingApi;


ShippingApi apiInstance = new ShippingApi();
GetRatesRequest body = new GetRatesRequest(); // GetRatesRequest | 
try {
    GetRatesResponse result = apiInstance.getRates(body);
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

### Return type

[**GetRatesResponse**](GetRatesResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getShipment"></a>
# **getShipment**
> GetShipmentResponse getShipment(shipmentId)



Return the entire shipment object for the shipmentId.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 5 | 15 |  For more information, see \&quot;Usage Plans and Rate Limits\&quot; in the Selling Partner API documentation.

### Example
```java
// Import classes:
//import com.amazon.spapi.shipping.ApiException;
//import com.amazon.spapi.shipping.api.ShippingApi;


ShippingApi apiInstance = new ShippingApi();
String shipmentId = "shipmentId_example"; // String | 
try {
    GetShipmentResponse result = apiInstance.getShipment(shipmentId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ShippingApi#getShipment");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **shipmentId** | **String**|  |

### Return type

[**GetShipmentResponse**](GetShipmentResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getTrackingInformation"></a>
# **getTrackingInformation**
> GetTrackingInformationResponse getTrackingInformation(trackingId)



Return the tracking information of a shipment.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 1 | 1 |  For more information, see \&quot;Usage Plans and Rate Limits\&quot; in the Selling Partner API documentation.

### Example
```java
// Import classes:
//import com.amazon.spapi.shipping.ApiException;
//import com.amazon.spapi.shipping.api.ShippingApi;


ShippingApi apiInstance = new ShippingApi();
String trackingId = "trackingId_example"; // String | 
try {
    GetTrackingInformationResponse result = apiInstance.getTrackingInformation(trackingId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ShippingApi#getTrackingInformation");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **trackingId** | **String**|  |

### Return type

[**GetTrackingInformationResponse**](GetTrackingInformationResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="purchaseLabels"></a>
# **purchaseLabels**
> PurchaseLabelsResponse purchaseLabels(shipmentId, body)



Purchase shipping labels based on a given rate.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 5 | 15 |  For more information, see \&quot;Usage Plans and Rate Limits\&quot; in the Selling Partner API documentation.

### Example
```java
// Import classes:
//import com.amazon.spapi.shipping.ApiException;
//import com.amazon.spapi.shipping.api.ShippingApi;


ShippingApi apiInstance = new ShippingApi();
String shipmentId = "shipmentId_example"; // String | 
PurchaseLabelsRequest body = new PurchaseLabelsRequest(); // PurchaseLabelsRequest | 
try {
    PurchaseLabelsResponse result = apiInstance.purchaseLabels(shipmentId, body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ShippingApi#purchaseLabels");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **shipmentId** | **String**|  |
 **body** | [**PurchaseLabelsRequest**](PurchaseLabelsRequest.md)|  |

### Return type

[**PurchaseLabelsResponse**](PurchaseLabelsResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="purchaseShipment"></a>
# **purchaseShipment**
> PurchaseShipmentResponse purchaseShipment(body)



Purchase shipping labels.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 5 | 15 |  For more information, see \&quot;Usage Plans and Rate Limits\&quot; in the Selling Partner API documentation.

### Example
```java
// Import classes:
//import com.amazon.spapi.shipping.ApiException;
//import com.amazon.spapi.shipping.api.ShippingApi;


ShippingApi apiInstance = new ShippingApi();
PurchaseShipmentRequest body = new PurchaseShipmentRequest(); // PurchaseShipmentRequest | 
try {
    PurchaseShipmentResponse result = apiInstance.purchaseShipment(body);
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

### Return type

[**PurchaseShipmentResponse**](PurchaseShipmentResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="retrieveShippingLabel"></a>
# **retrieveShippingLabel**
> RetrieveShippingLabelResponse retrieveShippingLabel(shipmentId, trackingId, body)



Retrieve shipping label based on the shipment id and tracking id.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 5 | 15 |  For more information, see \&quot;Usage Plans and Rate Limits\&quot; in the Selling Partner API documentation.

### Example
```java
// Import classes:
//import com.amazon.spapi.shipping.ApiException;
//import com.amazon.spapi.shipping.api.ShippingApi;


ShippingApi apiInstance = new ShippingApi();
String shipmentId = "shipmentId_example"; // String | 
String trackingId = "trackingId_example"; // String | 
RetrieveShippingLabelRequest body = new RetrieveShippingLabelRequest(); // RetrieveShippingLabelRequest | 
try {
    RetrieveShippingLabelResponse result = apiInstance.retrieveShippingLabel(shipmentId, trackingId, body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ShippingApi#retrieveShippingLabel");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **shipmentId** | **String**|  |
 **trackingId** | **String**|  |
 **body** | [**RetrieveShippingLabelRequest**](RetrieveShippingLabelRequest.md)|  |

### Return type

[**RetrieveShippingLabelResponse**](RetrieveShippingLabelResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

