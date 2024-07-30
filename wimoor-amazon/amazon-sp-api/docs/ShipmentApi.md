# ShipmentApi

All URIs are relative to *https://sellingpartnerapi-na.amazon.com*

Method | HTTP request | Description
------------- | ------------- | -------------
[**updateShipmentStatus**](ShipmentApi.md#updateShipmentStatus) | **POST** /orders/v0/orders/{orderId}/shipment | 


<a name="updateShipmentStatus"></a>
# **updateShipmentStatus**
> updateShipmentStatus(orderId, payload)



Update the shipment status for an order that you specify.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 5 | 15 |  The &#x60;x-amzn-RateLimit-Limit&#x60; response header returns the usage plan rate limits that were applied to the requested operation, when available. The table above indicates the default rate and burst values for this operation. Selling partners whose business demands require higher throughput may see higher rate and burst values then those shown here. For more information, see [Usage Plans and Rate Limits in the Selling Partner API](doc:usage-plans-and-rate-limits-in-the-sp-api).

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.ShipmentApi;


ShipmentApi apiInstance = new ShipmentApi();
String orderId = "orderId_example"; // String | An Amazon-defined order identifier, in 3-7-7 format.
UpdateShipmentStatusRequest payload = new UpdateShipmentStatusRequest(); // UpdateShipmentStatusRequest | The request body for the updateShipmentStatus operation.
try {
    apiInstance.updateShipmentStatus(orderId, payload);
} catch (ApiException e) {
    System.err.println("Exception when calling ShipmentApi#updateShipmentStatus");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **orderId** | **String**| An Amazon-defined order identifier, in 3-7-7 format. |
 **payload** | [**UpdateShipmentStatusRequest**](UpdateShipmentStatusRequest.md)| The request body for the updateShipmentStatus operation. |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

