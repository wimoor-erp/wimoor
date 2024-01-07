# MerchantFulfillmentApi

All URIs are relative to *https://sellingpartnerapi-na.amazon.com*

Method | HTTP request | Description
------------- | ------------- | -------------
[**cancelShipment**](MerchantFulfillmentApi.md#cancelShipment) | **DELETE** /mfn/v0/shipments/{shipmentId} | 
[**cancelShipmentOld**](MerchantFulfillmentApi.md#cancelShipmentOld) | **PUT** /mfn/v0/shipments/{shipmentId}/cancel | 
[**createShipment**](MerchantFulfillmentApi.md#createShipment) | **POST** /mfn/v0/shipments | 
[**getAdditionalSellerInputs**](MerchantFulfillmentApi.md#getAdditionalSellerInputs) | **POST** /mfn/v0/additionalSellerInputs | 
[**getAdditionalSellerInputsOld**](MerchantFulfillmentApi.md#getAdditionalSellerInputsOld) | **POST** /mfn/v0/sellerInputs | 
[**getEligibleShipmentServices**](MerchantFulfillmentApi.md#getEligibleShipmentServices) | **POST** /mfn/v0/eligibleShippingServices | 
[**getEligibleShipmentServicesOld**](MerchantFulfillmentApi.md#getEligibleShipmentServicesOld) | **POST** /mfn/v0/eligibleServices | 
[**getShipment**](MerchantFulfillmentApi.md#getShipment) | **GET** /mfn/v0/shipments/{shipmentId} | 


<a name="cancelShipment"></a>
# **cancelShipment**
> CancelShipmentResponse cancelShipment(shipmentId)



Cancel the shipment indicated by the specified shipment identifier.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 1 | 1 |  For more information, see \&quot;Usage Plans and Rate Limits\&quot; in the Selling Partner API documentation.

### Example
```java
// Import classes:
//import com.amazon.spapi.merchantfulfillment.ApiException;
//import com.amazon.spapi.merchantfulfillment.api.MerchantFulfillmentApi;


MerchantFulfillmentApi apiInstance = new MerchantFulfillmentApi();
String shipmentId = "shipmentId_example"; // String | The Amazon-defined shipment identifier for the shipment to cancel.
try {
    CancelShipmentResponse result = apiInstance.cancelShipment(shipmentId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling MerchantFulfillmentApi#cancelShipment");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **shipmentId** | **String**| The Amazon-defined shipment identifier for the shipment to cancel. |

### Return type

[**CancelShipmentResponse**](CancelShipmentResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="cancelShipmentOld"></a>
# **cancelShipmentOld**
> CancelShipmentResponse cancelShipmentOld(shipmentId)



Cancel the shipment indicated by the specified shipment identifer.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 1 | 1 |  For more information, see \&quot;Usage Plans and Rate Limits\&quot; in the Selling Partner API documentation.

### Example
```java
// Import classes:
//import com.amazon.spapi.merchantfulfillment.ApiException;
//import com.amazon.spapi.merchantfulfillment.api.MerchantFulfillmentApi;


MerchantFulfillmentApi apiInstance = new MerchantFulfillmentApi();
String shipmentId = "shipmentId_example"; // String | The Amazon-defined shipment identifier for the shipment to cancel.
try {
    CancelShipmentResponse result = apiInstance.cancelShipmentOld(shipmentId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling MerchantFulfillmentApi#cancelShipmentOld");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **shipmentId** | **String**| The Amazon-defined shipment identifier for the shipment to cancel. |

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



Create a shipment with the information provided.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 1 | 1 |  For more information, see \&quot;Usage Plans and Rate Limits\&quot; in the Selling Partner API documentation.

### Example
```java
// Import classes:
//import com.amazon.spapi.merchantfulfillment.ApiException;
//import com.amazon.spapi.merchantfulfillment.api.MerchantFulfillmentApi;


MerchantFulfillmentApi apiInstance = new MerchantFulfillmentApi();
CreateShipmentRequest body = new CreateShipmentRequest(); // CreateShipmentRequest | 
try {
    CreateShipmentResponse result = apiInstance.createShipment(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling MerchantFulfillmentApi#createShipment");
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

<a name="getAdditionalSellerInputs"></a>
# **getAdditionalSellerInputs**
> GetAdditionalSellerInputsResponse getAdditionalSellerInputs(body)



Gets a list of additional seller inputs required for a ship method. This is generally used for international shipping.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 1 | 1 |  For more information, see \&quot;Usage Plans and Rate Limits\&quot; in the Selling Partner API documentation.

### Example
```java
// Import classes:
//import com.amazon.spapi.merchantfulfillment.ApiException;
//import com.amazon.spapi.merchantfulfillment.api.MerchantFulfillmentApi;


MerchantFulfillmentApi apiInstance = new MerchantFulfillmentApi();
GetAdditionalSellerInputsRequest body = new GetAdditionalSellerInputsRequest(); // GetAdditionalSellerInputsRequest | 
try {
    GetAdditionalSellerInputsResponse result = apiInstance.getAdditionalSellerInputs(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling MerchantFulfillmentApi#getAdditionalSellerInputs");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**GetAdditionalSellerInputsRequest**](GetAdditionalSellerInputsRequest.md)|  |

### Return type

[**GetAdditionalSellerInputsResponse**](GetAdditionalSellerInputsResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getAdditionalSellerInputsOld"></a>
# **getAdditionalSellerInputsOld**
> GetAdditionalSellerInputsResponse getAdditionalSellerInputsOld(body)



Get a list of additional seller inputs required for a ship method. This is generally used for international shipping.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 1 | 1 |  For more information, see \&quot;Usage Plans and Rate Limits\&quot; in the Selling Partner API documentation.

### Example
```java
// Import classes:
//import com.amazon.spapi.merchantfulfillment.ApiException;
//import com.amazon.spapi.merchantfulfillment.api.MerchantFulfillmentApi;


MerchantFulfillmentApi apiInstance = new MerchantFulfillmentApi();
GetAdditionalSellerInputsRequest body = new GetAdditionalSellerInputsRequest(); // GetAdditionalSellerInputsRequest | 
try {
    GetAdditionalSellerInputsResponse result = apiInstance.getAdditionalSellerInputsOld(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling MerchantFulfillmentApi#getAdditionalSellerInputsOld");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**GetAdditionalSellerInputsRequest**](GetAdditionalSellerInputsRequest.md)|  |

### Return type

[**GetAdditionalSellerInputsResponse**](GetAdditionalSellerInputsResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getEligibleShipmentServices"></a>
# **getEligibleShipmentServices**
> GetEligibleShipmentServicesResponse getEligibleShipmentServices(body)



Returns a list of shipping service offers that satisfy the specified shipment request details.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 1 | 1 |  For more information, see \&quot;Usage Plans and Rate Limits\&quot; in the Selling Partner API documentation.

### Example
```java
// Import classes:
//import com.amazon.spapi.merchantfulfillment.ApiException;
//import com.amazon.spapi.merchantfulfillment.api.MerchantFulfillmentApi;


MerchantFulfillmentApi apiInstance = new MerchantFulfillmentApi();
GetEligibleShipmentServicesRequest body = new GetEligibleShipmentServicesRequest(); // GetEligibleShipmentServicesRequest | 
try {
    GetEligibleShipmentServicesResponse result = apiInstance.getEligibleShipmentServices(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling MerchantFulfillmentApi#getEligibleShipmentServices");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**GetEligibleShipmentServicesRequest**](GetEligibleShipmentServicesRequest.md)|  |

### Return type

[**GetEligibleShipmentServicesResponse**](GetEligibleShipmentServicesResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getEligibleShipmentServicesOld"></a>
# **getEligibleShipmentServicesOld**
> GetEligibleShipmentServicesResponse getEligibleShipmentServicesOld(body)



Returns a list of shipping service offers that satisfy the specified shipment request details.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 1 | 1 |  For more information, see \&quot;Usage Plans and Rate Limits\&quot; in the Selling Partner API documentation.

### Example
```java
// Import classes:
//import com.amazon.spapi.merchantfulfillment.ApiException;
//import com.amazon.spapi.merchantfulfillment.api.MerchantFulfillmentApi;


MerchantFulfillmentApi apiInstance = new MerchantFulfillmentApi();
GetEligibleShipmentServicesRequest body = new GetEligibleShipmentServicesRequest(); // GetEligibleShipmentServicesRequest | 
try {
    GetEligibleShipmentServicesResponse result = apiInstance.getEligibleShipmentServicesOld(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling MerchantFulfillmentApi#getEligibleShipmentServicesOld");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**GetEligibleShipmentServicesRequest**](GetEligibleShipmentServicesRequest.md)|  |

### Return type

[**GetEligibleShipmentServicesResponse**](GetEligibleShipmentServicesResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getShipment"></a>
# **getShipment**
> GetShipmentResponse getShipment(shipmentId)



Returns the shipment information for an existing shipment.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 1 | 1 |  For more information, see \&quot;Usage Plans and Rate Limits\&quot; in the Selling Partner API documentation.

### Example
```java
// Import classes:
//import com.amazon.spapi.merchantfulfillment.ApiException;
//import com.amazon.spapi.merchantfulfillment.api.MerchantFulfillmentApi;


MerchantFulfillmentApi apiInstance = new MerchantFulfillmentApi();
String shipmentId = "shipmentId_example"; // String | The Amazon-defined shipment identifier for the shipment.
try {
    GetShipmentResponse result = apiInstance.getShipment(shipmentId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling MerchantFulfillmentApi#getShipment");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **shipmentId** | **String**| The Amazon-defined shipment identifier for the shipment. |

### Return type

[**GetShipmentResponse**](GetShipmentResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

