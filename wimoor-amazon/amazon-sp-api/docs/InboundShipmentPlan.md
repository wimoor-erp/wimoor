
# InboundShipmentPlan

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**shipmentId** | **String** | A shipment identifier originally returned by the createInboundShipmentPlan operation. | 
**destinationFulfillmentCenterId** | **String** | An Amazon fulfillment center identifier created by Amazon. | 
**shipToAddress** | [**Address**](Address.md) | The address of the Amazon fulfillment center to which to ship the items. | 
**labelPrepType** | [**LabelPrepType**](LabelPrepType.md) |  | 
**items** | [**InboundShipmentPlanItemList**](InboundShipmentPlanItemList.md) | SKU and quantity information for the items in the shipment. | 
**estimatedBoxContentsFee** | [**BoxContentsFeeDetails**](BoxContentsFeeDetails.md) |  |  [optional]



