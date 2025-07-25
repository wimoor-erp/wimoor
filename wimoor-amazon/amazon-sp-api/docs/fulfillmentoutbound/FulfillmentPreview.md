
# FulfillmentPreview

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**shippingSpeedCategory** | [**ShippingSpeedCategory**](ShippingSpeedCategory.md) |  | 
**scheduledDeliveryInfo** | [**ScheduledDeliveryInfo**](ScheduledDeliveryInfo.md) |  |  [optional]
**isFulfillable** | **Boolean** | When true, this fulfillment order preview is fulfillable. | 
**isCODCapable** | **Boolean** | When true, this fulfillment order preview is for COD (Cash On Delivery). | 
**estimatedShippingWeight** | [**Weight**](Weight.md) | Estimated shipping weight for this fulfillment order preview. |  [optional]
**estimatedFees** | [**FeeList**](FeeList.md) | The estimated fulfillment fees for this fulfillment order preview, if applicable. |  [optional]
**fulfillmentPreviewShipments** | [**FulfillmentPreviewShipmentList**](FulfillmentPreviewShipmentList.md) |  |  [optional]
**unfulfillablePreviewItems** | [**UnfulfillablePreviewItemList**](UnfulfillablePreviewItemList.md) |  |  [optional]
**orderUnfulfillableReasons** | [**StringList**](StringList.md) | Error codes associated with the fulfillment order preview that indicate why the order is not fulfillable.  Error code examples:  DeliverySLAUnavailable InvalidDestinationAddress |  [optional]
**marketplaceId** | **String** | The marketplace the fulfillment order is placed against. | 
**featureConstraints** | [**List&lt;FeatureSettings&gt;**](FeatureSettings.md) | A list of features and their fulfillment policies to apply to the order. |  [optional]



