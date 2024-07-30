
# InboundShipmentHeader

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**shipmentName** | **String** | The name for the shipment. Use a naming convention that helps distinguish between shipments over time, such as the date the shipment was created. | 
**shipFromAddress** | [**Address**](Address.md) | The return address. | 
**destinationFulfillmentCenterId** | **String** | The identifier for the fulfillment center to which the shipment will be shipped. Get this value from the InboundShipmentPlan object in the response returned by the createInboundShipmentPlan operation. | 
**areCasesRequired** | **Boolean** | Indicates whether or not an inbound shipment contains case-packed boxes. Note: A shipment must contain either all case-packed boxes or all individually packed boxes.  Possible values:  true - All boxes in the shipment must be case packed.  false - All boxes in the shipment must be individually packed.  Note: If AreCasesRequired &#x3D; true for an inbound shipment, then the value of QuantityInCase must be greater than zero for every item in the shipment. Otherwise the service returns an error. |  [optional]
**shipmentStatus** | [**ShipmentStatus**](ShipmentStatus.md) |  | 
**labelPrepPreference** | [**LabelPrepPreference**](LabelPrepPreference.md) | The preference for label preparation for an inbound shipment. | 
**intendedBoxContentsSource** | [**IntendedBoxContentsSource**](IntendedBoxContentsSource.md) |  |  [optional]



