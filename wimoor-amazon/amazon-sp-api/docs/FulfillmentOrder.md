
# FulfillmentOrder

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**sellerFulfillmentOrderId** | **String** | The fulfillment order identifier submitted with the createFulfillmentOrder operation. | 
**marketplaceId** | **String** | The identifier for the marketplace the fulfillment order is placed against. | 
**displayableOrderId** | **String** | A fulfillment order identifier submitted with the createFulfillmentOrder operation. Displays as the order identifier in recipient-facing materials such as the packing slip. | 
**displayableOrderDate** | **String** | A date and time submitted with the createFulfillmentOrder operation. Displays as the order date in recipient-facing materials such as the packing slip. | 
**displayableOrderComment** | **String** | A text block submitted with the createFulfillmentOrder operation. Displays in recipient-facing materials such as the packing slip. | 
**shippingSpeedCategory** | [**ShippingSpeedCategory**](ShippingSpeedCategory.md) |  | 
**deliveryWindow** | [**DeliveryWindow**](DeliveryWindow.md) |  |  [optional]
**destinationAddress** | [**Address**](Address.md) | The destination address submitted with the createFulfillmentOrder operation. | 
**fulfillmentAction** | [**FulfillmentAction**](FulfillmentAction.md) |  |  [optional]
**fulfillmentPolicy** | [**FulfillmentPolicy**](FulfillmentPolicy.md) |  |  [optional]
**codSettings** | [**CODSettings**](CODSettings.md) |  |  [optional]
**receivedDate** | **String** | The date and time that the fulfillment order was received by an Amazon fulfillment center. | 
**fulfillmentOrderStatus** | [**FulfillmentOrderStatus**](FulfillmentOrderStatus.md) |  | 
**statusUpdatedDate** | **String** | The date and time that the status of the fulfillment order last changed, in ISO 8601 date time format. | 
**notificationEmails** | [**NotificationEmailList**](NotificationEmailList.md) |  |  [optional]
**featureConstraints** | [**List&lt;FeatureSettings&gt;**](FeatureSettings.md) | A list of features and their fulfillment policies to apply to the order. |  [optional]



