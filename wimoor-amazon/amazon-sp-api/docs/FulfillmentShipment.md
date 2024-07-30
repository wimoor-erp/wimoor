
# FulfillmentShipment

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**amazonShipmentId** | **String** | A shipment identifier assigned by Amazon. | 
**fulfillmentCenterId** | **String** | An identifier for the fulfillment center that the shipment will be sent from. | 
**fulfillmentShipmentStatus** | [**FulfillmentShipmentStatusEnum**](#FulfillmentShipmentStatusEnum) | The current status of the shipment. | 
**shippingDate** | **String** | The meaning of the shippingDate value depends on the current status of the shipment. If the current value of FulfillmentShipmentStatus is:  * Pending - shippingDate represents the estimated time that the shipment will leave the Amazon fulfillment center.  * Shipped - shippingDate represents the date that the shipment left the Amazon fulfillment center. If a shipment includes more than one package, shippingDate applies to all of the packages in the shipment. If the value of FulfillmentShipmentStatus is CancelledByFulfiller or CancelledBySeller, shippingDate is not returned. The value must be in ISO 8601 date time format. |  [optional]
**estimatedArrivalDate** | **String** | The estimated arrival date and time of the shipment, in ISO 8601 date time format. Note that this value can change over time. If a shipment includes more than one package, estimatedArrivalDate applies to all of the packages in the shipment. If the shipment has been cancelled, estimatedArrivalDate is not returned. |  [optional]
**shippingNotes** | **List&lt;String&gt;** | Provides additional insight into shipment timeline. Primairly used to communicate that actual delivery dates aren&#39;t available. |  [optional]
**fulfillmentShipmentItem** | [**FulfillmentShipmentItemList**](FulfillmentShipmentItemList.md) |  | 
**fulfillmentShipmentPackage** | [**FulfillmentShipmentPackageList**](FulfillmentShipmentPackageList.md) |  |  [optional]


<a name="FulfillmentShipmentStatusEnum"></a>
## Enum: FulfillmentShipmentStatusEnum
Name | Value
---- | -----
PENDING | &quot;PENDING&quot;
SHIPPED | &quot;SHIPPED&quot;
CANCELLED_BY_FULFILLER | &quot;CANCELLED_BY_FULFILLER&quot;
CANCELLED_BY_SELLER | &quot;CANCELLED_BY_SELLER&quot;



