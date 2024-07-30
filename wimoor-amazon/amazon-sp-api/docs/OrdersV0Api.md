# OrdersV0Api

All URIs are relative to *https://sellingpartnerapi-na.amazon.com*

Method | HTTP request | Description
------------- | ------------- | -------------
[**confirmShipment**](OrdersV0Api.md#confirmShipment) | **POST** /orders/v0/orders/{orderId}/shipmentConfirmation | 
[**getOrder**](OrdersV0Api.md#getOrder) | **GET** /orders/v0/orders/{orderId} | 
[**getOrderAddress**](OrdersV0Api.md#getOrderAddress) | **GET** /orders/v0/orders/{orderId}/address | 
[**getOrderBuyerInfo**](OrdersV0Api.md#getOrderBuyerInfo) | **GET** /orders/v0/orders/{orderId}/buyerInfo | 
[**getOrderItems**](OrdersV0Api.md#getOrderItems) | **GET** /orders/v0/orders/{orderId}/orderItems | 
[**getOrderItemsBuyerInfo**](OrdersV0Api.md#getOrderItemsBuyerInfo) | **GET** /orders/v0/orders/{orderId}/orderItems/buyerInfo | 
[**getOrderRegulatedInfo**](OrdersV0Api.md#getOrderRegulatedInfo) | **GET** /orders/v0/orders/{orderId}/regulatedInfo | 
[**getOrders**](OrdersV0Api.md#getOrders) | **GET** /orders/v0/orders | 
[**updateVerificationStatus**](OrdersV0Api.md#updateVerificationStatus) | **PATCH** /orders/v0/orders/{orderId}/regulatedInfo | 


<a name="confirmShipment"></a>
# **confirmShipment**
> confirmShipment(orderId, payload)



Updates the shipment confirmation status for a specified order.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 2 | 10 |  The &#x60;x-amzn-RateLimit-Limit&#x60; response header returns the usage plan rate limits that were applied to the requested operation, when available. The table above indicates the default rate and burst values for this operation. Selling partners whose business demands require higher throughput may see higher rate and burst values then those shown here. For more information, see [Usage Plans and Rate Limits in the Selling Partner API](doc:usage-plans-and-rate-limits-in-the-sp-api).

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.OrdersV0Api;


OrdersV0Api apiInstance = new OrdersV0Api();
String orderId = "orderId_example"; // String | An Amazon-defined order identifier, in 3-7-7 format.
ConfirmShipmentRequest payload = new ConfirmShipmentRequest(); // ConfirmShipmentRequest | Request body of confirmShipment.
try {
    apiInstance.confirmShipment(orderId, payload);
} catch (ApiException e) {
    System.err.println("Exception when calling OrdersV0Api#confirmShipment");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **orderId** | **String**| An Amazon-defined order identifier, in 3-7-7 format. |
 **payload** | [**ConfirmShipmentRequest**](ConfirmShipmentRequest.md)| Request body of confirmShipment. |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getOrder"></a>
# **getOrder**
> GetOrderResponse getOrder(orderId)



Returns the order that you specify.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 0.0167 | 20 |  The &#x60;x-amzn-RateLimit-Limit&#x60; response header returns the usage plan rate limits that were applied to the requested operation, when available. The table above indicates the default rate and burst values for this operation. Selling partners whose business demands require higher throughput may see higher rate and burst values then those shown here. For more information, see [Usage Plans and Rate Limits in the Selling Partner API](doc:usage-plans-and-rate-limits-in-the-sp-api).

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.OrdersV0Api;


OrdersV0Api apiInstance = new OrdersV0Api();
String orderId = "orderId_example"; // String | An Amazon-defined order identifier, in 3-7-7 format.
try {
    GetOrderResponse result = apiInstance.getOrder(orderId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling OrdersV0Api#getOrder");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **orderId** | **String**| An Amazon-defined order identifier, in 3-7-7 format. |

### Return type

[**GetOrderResponse**](GetOrderResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getOrderAddress"></a>
# **getOrderAddress**
> GetOrderAddressResponse getOrderAddress(orderId)



Returns the shipping address for the order that you specify.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 0.0167 | 20 |  The &#x60;x-amzn-RateLimit-Limit&#x60; response header returns the usage plan rate limits that were applied to the requested operation, when available. The table above indicates the default rate and burst values for this operation. Selling partners whose business demands require higher throughput may see higher rate and burst values then those shown here. For more information, see [Usage Plans and Rate Limits in the Selling Partner API](doc:usage-plans-and-rate-limits-in-the-sp-api).

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.OrdersV0Api;


OrdersV0Api apiInstance = new OrdersV0Api();
String orderId = "orderId_example"; // String | An orderId is an Amazon-defined order identifier, in 3-7-7 format.
try {
    GetOrderAddressResponse result = apiInstance.getOrderAddress(orderId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling OrdersV0Api#getOrderAddress");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **orderId** | **String**| An orderId is an Amazon-defined order identifier, in 3-7-7 format. |

### Return type

[**GetOrderAddressResponse**](GetOrderAddressResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getOrderBuyerInfo"></a>
# **getOrderBuyerInfo**
> GetOrderBuyerInfoResponse getOrderBuyerInfo(orderId)



Returns buyer information for the order that you specify.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 0.0167 | 20 |  The &#x60;x-amzn-RateLimit-Limit&#x60; response header returns the usage plan rate limits that were applied to the requested operation, when available. The table above indicates the default rate and burst values for this operation. Selling partners whose business demands require higher throughput may see higher rate and burst values then those shown here. For more information, see [Usage Plans and Rate Limits in the Selling Partner API](doc:usage-plans-and-rate-limits-in-the-sp-api).

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.OrdersV0Api;


OrdersV0Api apiInstance = new OrdersV0Api();
String orderId = "orderId_example"; // String | An orderId is an Amazon-defined order identifier, in 3-7-7 format.
try {
    GetOrderBuyerInfoResponse result = apiInstance.getOrderBuyerInfo(orderId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling OrdersV0Api#getOrderBuyerInfo");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **orderId** | **String**| An orderId is an Amazon-defined order identifier, in 3-7-7 format. |

### Return type

[**GetOrderBuyerInfoResponse**](GetOrderBuyerInfoResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getOrderItems"></a>
# **getOrderItems**
> GetOrderItemsResponse getOrderItems(orderId, nextToken)



Returns detailed order item information for the order that you specify. If NextToken is provided, it&#39;s used to retrieve the next page of order items.  __Note__: When an order is in the Pending state (the order has been placed but payment has not been authorized), the getOrderItems operation does not return information about pricing, taxes, shipping charges, gift status or promotions for the order items in the order. After an order leaves the Pending state (this occurs when payment has been authorized) and enters the Unshipped, Partially Shipped, or Shipped state, the getOrderItems operation returns information about pricing, taxes, shipping charges, gift status and promotions for the order items in the order.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 0.5 | 30 |  The &#x60;x-amzn-RateLimit-Limit&#x60; response header returns the usage plan rate limits that were applied to the requested operation, when available. The table above indicates the default rate and burst values for this operation. Selling partners whose business demands require higher throughput may see higher rate and burst values then those shown here. For more information, see [Usage Plans and Rate Limits in the Selling Partner API](doc:usage-plans-and-rate-limits-in-the-sp-api).

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.OrdersV0Api;


OrdersV0Api apiInstance = new OrdersV0Api();
String orderId = "orderId_example"; // String | An Amazon-defined order identifier, in 3-7-7 format.
String nextToken = "nextToken_example"; // String | A string token returned in the response of your previous request.
try {
    GetOrderItemsResponse result = apiInstance.getOrderItems(orderId, nextToken);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling OrdersV0Api#getOrderItems");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **orderId** | **String**| An Amazon-defined order identifier, in 3-7-7 format. |
 **nextToken** | **String**| A string token returned in the response of your previous request. | [optional]

### Return type

[**GetOrderItemsResponse**](GetOrderItemsResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getOrderItemsBuyerInfo"></a>
# **getOrderItemsBuyerInfo**
> GetOrderItemsBuyerInfoResponse getOrderItemsBuyerInfo(orderId, nextToken)



Returns buyer information for the order items in the order that you specify.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 0.5 | 30 |  The &#x60;x-amzn-RateLimit-Limit&#x60; response header returns the usage plan rate limits that were applied to the requested operation, when available. The table above indicates the default rate and burst values for this operation. Selling partners whose business demands require higher throughput may see higher rate and burst values then those shown here. For more information, see [Usage Plans and Rate Limits in the Selling Partner API](doc:usage-plans-and-rate-limits-in-the-sp-api).

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.OrdersV0Api;


OrdersV0Api apiInstance = new OrdersV0Api();
String orderId = "orderId_example"; // String | An Amazon-defined order identifier, in 3-7-7 format.
String nextToken = "nextToken_example"; // String | A string token returned in the response of your previous request.
try {
    GetOrderItemsBuyerInfoResponse result = apiInstance.getOrderItemsBuyerInfo(orderId, nextToken);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling OrdersV0Api#getOrderItemsBuyerInfo");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **orderId** | **String**| An Amazon-defined order identifier, in 3-7-7 format. |
 **nextToken** | **String**| A string token returned in the response of your previous request. | [optional]

### Return type

[**GetOrderItemsBuyerInfoResponse**](GetOrderItemsBuyerInfoResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getOrderRegulatedInfo"></a>
# **getOrderRegulatedInfo**
> GetOrderRegulatedInfoResponse getOrderRegulatedInfo(orderId)



Returns regulated information for the order that you specify.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 0.5 | 30 |  The &#x60;x-amzn-RateLimit-Limit&#x60; response header returns the usage plan rate limits that were applied to the requested operation, when available. The table above indicates the default rate and burst values for this operation. Selling partners whose business demands require higher throughput may see higher rate and burst values then those shown here. For more information, see [Usage Plans and Rate Limits in the Selling Partner API](doc:usage-plans-and-rate-limits-in-the-sp-api).

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.OrdersV0Api;


OrdersV0Api apiInstance = new OrdersV0Api();
String orderId = "orderId_example"; // String | An orderId is an Amazon-defined order identifier, in 3-7-7 format.
try {
    GetOrderRegulatedInfoResponse result = apiInstance.getOrderRegulatedInfo(orderId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling OrdersV0Api#getOrderRegulatedInfo");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **orderId** | **String**| An orderId is an Amazon-defined order identifier, in 3-7-7 format. |

### Return type

[**GetOrderRegulatedInfoResponse**](GetOrderRegulatedInfoResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getOrders"></a>
# **getOrders**
> GetOrdersResponse getOrders(marketplaceIds, createdAfter, createdBefore, lastUpdatedAfter, lastUpdatedBefore, orderStatuses, fulfillmentChannels, paymentMethods, buyerEmail, sellerOrderId, maxResultsPerPage, easyShipShipmentStatuses, electronicInvoiceStatuses, nextToken, amazonOrderIds, actualFulfillmentSupplySourceId, isISPU, storeChainStoreId, earliestDeliveryDateBefore, earliestDeliveryDateAfter, latestDeliveryDateBefore, latestDeliveryDateAfter)



Returns orders created or updated during the time frame indicated by the specified parameters. You can also apply a range of filtering criteria to narrow the list of orders returned. If NextToken is present, that will be used to retrieve the orders instead of other criteria.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 0.0167 | 20 |  The &#x60;x-amzn-RateLimit-Limit&#x60; response header returns the usage plan rate limits that were applied to the requested operation, when available. The table above indicates the default rate and burst values for this operation. Selling partners whose business demands require higher throughput may see higher rate and burst values then those shown here. For more information, see [Usage Plans and Rate Limits in the Selling Partner API](doc:usage-plans-and-rate-limits-in-the-sp-api).

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.OrdersV0Api;


OrdersV0Api apiInstance = new OrdersV0Api();
List<String> marketplaceIds = Arrays.asList("marketplaceIds_example"); // List<String> | A list of MarketplaceId values. Used to select orders that were placed in the specified marketplaces.  Refer to [Marketplace IDs](https://developer-docs.amazon.com/sp-api/docs/marketplace-ids) for a complete list of marketplaceId values.
String createdAfter = "createdAfter_example"; // String | A date used for selecting orders created after (or at) a specified time. Only orders placed after the specified time are returned. Either the CreatedAfter parameter or the LastUpdatedAfter parameter is required. Both cannot be empty. The date must be in <a href='https://developer-docs.amazon.com/sp-api/docs/iso-8601'>ISO 8601</a> format.
String createdBefore = "createdBefore_example"; // String | A date used for selecting orders created before (or at) a specified time. Only orders placed before the specified time are returned. The date must be in <a href='https://developer-docs.amazon.com/sp-api/docs/iso-8601'>ISO 8601</a> format.
String lastUpdatedAfter = "lastUpdatedAfter_example"; // String | A date used for selecting orders that were last updated after (or at) a specified time. An update is defined as any change in order status, including the creation of a new order. Includes updates made by Amazon and by the seller. The date must be in <a href='https://developer-docs.amazon.com/sp-api/docs/iso-8601'>ISO 8601</a> format.
String lastUpdatedBefore = "lastUpdatedBefore_example"; // String | A date used for selecting orders that were last updated before (or at) a specified time. An update is defined as any change in order status, including the creation of a new order. Includes updates made by Amazon and by the seller. The date must be in <a href='https://developer-docs.amazon.com/sp-api/docs/iso-8601'>ISO 8601</a> format.
List<String> orderStatuses = Arrays.asList("orderStatuses_example"); // List<String> | A list of `OrderStatus` values used to filter the results.  **Possible values:** - `PendingAvailability` (This status is available for pre-orders only. The order has been placed, payment has not been authorized, and the release date of the item is in the future.) - `Pending` (The order has been placed but payment has not been authorized.) - `Unshipped` (Payment has been authorized and the order is ready for shipment, but no items in the order have been shipped.) - `PartiallyShipped` (One or more, but not all, items in the order have been shipped.) - `Shipped` (All items in the order have been shipped.) - `InvoiceUnconfirmed` (All items in the order have been shipped. The seller has not yet given confirmation to Amazon that the invoice has been shipped to the buyer.) - `Canceled` (The order has been canceled.) - `Unfulfillable` (The order cannot be fulfilled. This state applies only to Multi-Channel Fulfillment orders.)
List<String> fulfillmentChannels = Arrays.asList("fulfillmentChannels_example"); // List<String> | A list that indicates how an order was fulfilled. Filters the results by fulfillment channel. Possible values: AFN (Fulfillment by Amazon); MFN (Fulfilled by the seller).
List<String> paymentMethods = Arrays.asList("paymentMethods_example"); // List<String> | A list of payment method values. Used to select orders paid using the specified payment methods. Possible values: COD (Cash on delivery); CVS (Convenience store payment); Other (Any payment method other than COD or CVS).
String buyerEmail = "buyerEmail_example"; // String | The email address of a buyer. Used to select orders that contain the specified email address.
String sellerOrderId = "sellerOrderId_example"; // String | An order identifier that is specified by the seller. Used to select only the orders that match the order identifier. If SellerOrderId is specified, then FulfillmentChannels, OrderStatuses, PaymentMethod, LastUpdatedAfter, LastUpdatedBefore, and BuyerEmail cannot be specified.
Integer maxResultsPerPage = 56; // Integer | A number that indicates the maximum number of orders that can be returned per page. Value must be 1 - 100. Default 100.
List<String> easyShipShipmentStatuses = Arrays.asList("easyShipShipmentStatuses_example"); // List<String> | A list of `EasyShipShipmentStatus` values. Used to select Easy Ship orders with statuses that match the specified values. If `EasyShipShipmentStatus` is specified, only Amazon Easy Ship orders are returned.  **Possible values:** - `PendingSchedule` (The package is awaiting the schedule for pick-up.) - `PendingPickUp` (Amazon has not yet picked up the package from the seller.) - `PendingDropOff` (The seller will deliver the package to the carrier.) - `LabelCanceled` (The seller canceled the pickup.) - `PickedUp` (Amazon has picked up the package from the seller.) - `DroppedOff` (The package is delivered to the carrier by the seller.) - `AtOriginFC` (The packaged is at the origin fulfillment center.) - `AtDestinationFC` (The package is at the destination fulfillment center.) - `Delivered` (The package has been delivered.) - `RejectedByBuyer` (The package has been rejected by the buyer.) - `Undeliverable` (The package cannot be delivered.) - `ReturningToSeller` (The package was not delivered and is being returned to the seller.) - `ReturnedToSeller` (The package was not delivered and was returned to the seller.) - `Lost` (The package is lost.) - `OutForDelivery` (The package is out for delivery.) - `Damaged` (The package was damaged by the carrier.)
List<String> electronicInvoiceStatuses = Arrays.asList("electronicInvoiceStatuses_example"); // List<String> | A list of `ElectronicInvoiceStatus` values. Used to select orders with electronic invoice statuses that match the specified values.  **Possible values:** - `NotRequired` (Electronic invoice submission is not required for this order.) - `NotFound` (The electronic invoice was not submitted for this order.) - `Processing` (The electronic invoice is being processed for this order.) - `Errored` (The last submitted electronic invoice was rejected for this order.) - `Accepted` (The last submitted electronic invoice was submitted and accepted.)
String nextToken = "nextToken_example"; // String | A string token returned in the response of your previous request.
List<String> amazonOrderIds = Arrays.asList("amazonOrderIds_example"); // List<String> | A list of AmazonOrderId values. An AmazonOrderId is an Amazon-defined order identifier, in 3-7-7 format.
String actualFulfillmentSupplySourceId = "actualFulfillmentSupplySourceId_example"; // String | Denotes the recommended sourceId where the order should be fulfilled from.
Boolean isISPU = true; // Boolean | When true, this order is marked to be picked up from a store rather than delivered.
String storeChainStoreId = "storeChainStoreId_example"; // String | The store chain store identifier. Linked to a specific store in a store chain.
String earliestDeliveryDateBefore = "earliestDeliveryDateBefore_example"; // String | A date used for selecting orders with a earliest delivery date before (or at) a specified time. The date must be in <a href='https://developer-docs.amazon.com/sp-api/docs/iso-8601'>ISO 8601</a> format.
String earliestDeliveryDateAfter = "earliestDeliveryDateAfter_example"; // String | A date used for selecting orders with a earliest delivery date after (or at) a specified time. The date must be in <a href='https://developer-docs.amazon.com/sp-api/docs/iso-8601'>ISO 8601</a> format.
String latestDeliveryDateBefore = "latestDeliveryDateBefore_example"; // String | A date used for selecting orders with a latest delivery date before (or at) a specified time. The date must be in <a href='https://developer-docs.amazon.com/sp-api/docs/iso-8601'>ISO 8601</a> format.
String latestDeliveryDateAfter = "latestDeliveryDateAfter_example"; // String | A date used for selecting orders with a latest delivery date after (or at) a specified time. The date must be in <a href='https://developer-docs.amazon.com/sp-api/docs/iso-8601'>ISO 8601</a> format.
try {
    GetOrdersResponse result = apiInstance.getOrders(marketplaceIds, createdAfter, createdBefore, lastUpdatedAfter, lastUpdatedBefore, orderStatuses, fulfillmentChannels, paymentMethods, buyerEmail, sellerOrderId, maxResultsPerPage, easyShipShipmentStatuses, electronicInvoiceStatuses, nextToken, amazonOrderIds, actualFulfillmentSupplySourceId, isISPU, storeChainStoreId, earliestDeliveryDateBefore, earliestDeliveryDateAfter, latestDeliveryDateBefore, latestDeliveryDateAfter);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling OrdersV0Api#getOrders");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **marketplaceIds** | [**List&lt;String&gt;**](String.md)| A list of MarketplaceId values. Used to select orders that were placed in the specified marketplaces.  Refer to [Marketplace IDs](https://developer-docs.amazon.com/sp-api/docs/marketplace-ids) for a complete list of marketplaceId values. |
 **createdAfter** | **String**| A date used for selecting orders created after (or at) a specified time. Only orders placed after the specified time are returned. Either the CreatedAfter parameter or the LastUpdatedAfter parameter is required. Both cannot be empty. The date must be in &lt;a href&#x3D;&#39;https://developer-docs.amazon.com/sp-api/docs/iso-8601&#39;&gt;ISO 8601&lt;/a&gt; format. | [optional]
 **createdBefore** | **String**| A date used for selecting orders created before (or at) a specified time. Only orders placed before the specified time are returned. The date must be in &lt;a href&#x3D;&#39;https://developer-docs.amazon.com/sp-api/docs/iso-8601&#39;&gt;ISO 8601&lt;/a&gt; format. | [optional]
 **lastUpdatedAfter** | **String**| A date used for selecting orders that were last updated after (or at) a specified time. An update is defined as any change in order status, including the creation of a new order. Includes updates made by Amazon and by the seller. The date must be in &lt;a href&#x3D;&#39;https://developer-docs.amazon.com/sp-api/docs/iso-8601&#39;&gt;ISO 8601&lt;/a&gt; format. | [optional]
 **lastUpdatedBefore** | **String**| A date used for selecting orders that were last updated before (or at) a specified time. An update is defined as any change in order status, including the creation of a new order. Includes updates made by Amazon and by the seller. The date must be in &lt;a href&#x3D;&#39;https://developer-docs.amazon.com/sp-api/docs/iso-8601&#39;&gt;ISO 8601&lt;/a&gt; format. | [optional]
 **orderStatuses** | [**List&lt;String&gt;**](String.md)| A list of &#x60;OrderStatus&#x60; values used to filter the results.  **Possible values:** - &#x60;PendingAvailability&#x60; (This status is available for pre-orders only. The order has been placed, payment has not been authorized, and the release date of the item is in the future.) - &#x60;Pending&#x60; (The order has been placed but payment has not been authorized.) - &#x60;Unshipped&#x60; (Payment has been authorized and the order is ready for shipment, but no items in the order have been shipped.) - &#x60;PartiallyShipped&#x60; (One or more, but not all, items in the order have been shipped.) - &#x60;Shipped&#x60; (All items in the order have been shipped.) - &#x60;InvoiceUnconfirmed&#x60; (All items in the order have been shipped. The seller has not yet given confirmation to Amazon that the invoice has been shipped to the buyer.) - &#x60;Canceled&#x60; (The order has been canceled.) - &#x60;Unfulfillable&#x60; (The order cannot be fulfilled. This state applies only to Multi-Channel Fulfillment orders.) | [optional]
 **fulfillmentChannels** | [**List&lt;String&gt;**](String.md)| A list that indicates how an order was fulfilled. Filters the results by fulfillment channel. Possible values: AFN (Fulfillment by Amazon); MFN (Fulfilled by the seller). | [optional]
 **paymentMethods** | [**List&lt;String&gt;**](String.md)| A list of payment method values. Used to select orders paid using the specified payment methods. Possible values: COD (Cash on delivery); CVS (Convenience store payment); Other (Any payment method other than COD or CVS). | [optional]
 **buyerEmail** | **String**| The email address of a buyer. Used to select orders that contain the specified email address. | [optional]
 **sellerOrderId** | **String**| An order identifier that is specified by the seller. Used to select only the orders that match the order identifier. If SellerOrderId is specified, then FulfillmentChannels, OrderStatuses, PaymentMethod, LastUpdatedAfter, LastUpdatedBefore, and BuyerEmail cannot be specified. | [optional]
 **maxResultsPerPage** | **Integer**| A number that indicates the maximum number of orders that can be returned per page. Value must be 1 - 100. Default 100. | [optional]
 **easyShipShipmentStatuses** | [**List&lt;String&gt;**](String.md)| A list of &#x60;EasyShipShipmentStatus&#x60; values. Used to select Easy Ship orders with statuses that match the specified values. If &#x60;EasyShipShipmentStatus&#x60; is specified, only Amazon Easy Ship orders are returned.  **Possible values:** - &#x60;PendingSchedule&#x60; (The package is awaiting the schedule for pick-up.) - &#x60;PendingPickUp&#x60; (Amazon has not yet picked up the package from the seller.) - &#x60;PendingDropOff&#x60; (The seller will deliver the package to the carrier.) - &#x60;LabelCanceled&#x60; (The seller canceled the pickup.) - &#x60;PickedUp&#x60; (Amazon has picked up the package from the seller.) - &#x60;DroppedOff&#x60; (The package is delivered to the carrier by the seller.) - &#x60;AtOriginFC&#x60; (The packaged is at the origin fulfillment center.) - &#x60;AtDestinationFC&#x60; (The package is at the destination fulfillment center.) - &#x60;Delivered&#x60; (The package has been delivered.) - &#x60;RejectedByBuyer&#x60; (The package has been rejected by the buyer.) - &#x60;Undeliverable&#x60; (The package cannot be delivered.) - &#x60;ReturningToSeller&#x60; (The package was not delivered and is being returned to the seller.) - &#x60;ReturnedToSeller&#x60; (The package was not delivered and was returned to the seller.) - &#x60;Lost&#x60; (The package is lost.) - &#x60;OutForDelivery&#x60; (The package is out for delivery.) - &#x60;Damaged&#x60; (The package was damaged by the carrier.) | [optional]
 **electronicInvoiceStatuses** | [**List&lt;String&gt;**](String.md)| A list of &#x60;ElectronicInvoiceStatus&#x60; values. Used to select orders with electronic invoice statuses that match the specified values.  **Possible values:** - &#x60;NotRequired&#x60; (Electronic invoice submission is not required for this order.) - &#x60;NotFound&#x60; (The electronic invoice was not submitted for this order.) - &#x60;Processing&#x60; (The electronic invoice is being processed for this order.) - &#x60;Errored&#x60; (The last submitted electronic invoice was rejected for this order.) - &#x60;Accepted&#x60; (The last submitted electronic invoice was submitted and accepted.) | [optional]
 **nextToken** | **String**| A string token returned in the response of your previous request. | [optional]
 **amazonOrderIds** | [**List&lt;String&gt;**](String.md)| A list of AmazonOrderId values. An AmazonOrderId is an Amazon-defined order identifier, in 3-7-7 format. | [optional]
 **actualFulfillmentSupplySourceId** | **String**| Denotes the recommended sourceId where the order should be fulfilled from. | [optional]
 **isISPU** | **Boolean**| When true, this order is marked to be picked up from a store rather than delivered. | [optional]
 **storeChainStoreId** | **String**| The store chain store identifier. Linked to a specific store in a store chain. | [optional]
 **earliestDeliveryDateBefore** | **String**| A date used for selecting orders with a earliest delivery date before (or at) a specified time. The date must be in &lt;a href&#x3D;&#39;https://developer-docs.amazon.com/sp-api/docs/iso-8601&#39;&gt;ISO 8601&lt;/a&gt; format. | [optional]
 **earliestDeliveryDateAfter** | **String**| A date used for selecting orders with a earliest delivery date after (or at) a specified time. The date must be in &lt;a href&#x3D;&#39;https://developer-docs.amazon.com/sp-api/docs/iso-8601&#39;&gt;ISO 8601&lt;/a&gt; format. | [optional]
 **latestDeliveryDateBefore** | **String**| A date used for selecting orders with a latest delivery date before (or at) a specified time. The date must be in &lt;a href&#x3D;&#39;https://developer-docs.amazon.com/sp-api/docs/iso-8601&#39;&gt;ISO 8601&lt;/a&gt; format. | [optional]
 **latestDeliveryDateAfter** | **String**| A date used for selecting orders with a latest delivery date after (or at) a specified time. The date must be in &lt;a href&#x3D;&#39;https://developer-docs.amazon.com/sp-api/docs/iso-8601&#39;&gt;ISO 8601&lt;/a&gt; format. | [optional]

### Return type

[**GetOrdersResponse**](GetOrdersResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="updateVerificationStatus"></a>
# **updateVerificationStatus**
> updateVerificationStatus(orderId, payload)



Updates (approves or rejects) the verification status of an order containing regulated products.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 0.5 | 30 |  The &#x60;x-amzn-RateLimit-Limit&#x60; response header returns the usage plan rate limits that were applied to the requested operation, when available. The table above indicates the default rate and burst values for this operation. Selling partners whose business demands require higher throughput may see higher rate and burst values then those shown here. For more information, see [Usage Plans and Rate Limits in the Selling Partner API](doc:usage-plans-and-rate-limits-in-the-sp-api).

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.OrdersV0Api;


OrdersV0Api apiInstance = new OrdersV0Api();
String orderId = "orderId_example"; // String | An orderId is an Amazon-defined order identifier, in 3-7-7 format.
UpdateVerificationStatusRequest payload = new UpdateVerificationStatusRequest(); // UpdateVerificationStatusRequest | The request body for the updateVerificationStatus operation.
try {
    apiInstance.updateVerificationStatus(orderId, payload);
} catch (ApiException e) {
    System.err.println("Exception when calling OrdersV0Api#updateVerificationStatus");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **orderId** | **String**| An orderId is an Amazon-defined order identifier, in 3-7-7 format. |
 **payload** | [**UpdateVerificationStatusRequest**](UpdateVerificationStatusRequest.md)| The request body for the updateVerificationStatus operation. |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

