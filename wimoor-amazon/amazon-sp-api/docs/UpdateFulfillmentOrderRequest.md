
# UpdateFulfillmentOrderRequest

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**marketplaceId** | **String** | The marketplace the fulfillment order is placed against. |  [optional]
**displayableOrderId** | **String** | A fulfillment order identifier that the seller creates. This value displays as the order identifier in recipient-facing materials such as the outbound shipment packing slip. The value of DisplayableOrderId should match the order identifier that the seller provides to the recipient. The seller can use the SellerFulfillmentOrderId for this value or they can specify an alternate value if they want the recipient to reference an alternate order identifier. |  [optional]
**displayableOrderDate** | **String** | The date and time of the fulfillment order. Displays as the order date in recipient-facing materials such as the outbound shipment packing slip. |  [optional]
**displayableOrderComment** | **String** | Order-specific text that appears in recipient-facing materials such as the outbound shipment packing slip. |  [optional]
**shippingSpeedCategory** | [**ShippingSpeedCategory**](ShippingSpeedCategory.md) |  |  [optional]
**destinationAddress** | [**Address**](Address.md) | The destination address for the fulfillment order. |  [optional]
**fulfillmentAction** | [**FulfillmentAction**](FulfillmentAction.md) |  |  [optional]
**fulfillmentPolicy** | [**FulfillmentPolicy**](FulfillmentPolicy.md) |  |  [optional]
**shipFromCountryCode** | **String** | The two-character country code for the country from which the fulfillment order ships. Must be in ISO 3166-1 alpha-2 format. |  [optional]
**notificationEmails** | [**NotificationEmailList**](NotificationEmailList.md) |  |  [optional]
**featureConstraints** | [**List&lt;FeatureSettings&gt;**](FeatureSettings.md) | A list of features and their fulfillment policies to apply to the order. |  [optional]
**items** | [**UpdateFulfillmentOrderItemList**](UpdateFulfillmentOrderItemList.md) | A list of items to include in the fulfillment order preview, including quantity. |  [optional]



