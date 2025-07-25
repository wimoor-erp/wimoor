
# InboundShipmentInfo

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**shipmentId** | **String** | The shipment identifier submitted in the request. |  [optional]
**shipmentName** | **String** | The name for the inbound shipment. |  [optional]
**shipFromAddress** | [**Address**](Address.md) | The return address. | 
**destinationFulfillmentCenterId** | **String** | An Amazon fulfillment center identifier created by Amazon. |  [optional]
**shipmentStatus** | [**ShipmentStatus**](ShipmentStatus.md) |  |  [optional]
**labelPrepType** | [**LabelPrepType**](LabelPrepType.md) |  |  [optional]
**areCasesRequired** | **Boolean** | Indicates whether or not an inbound shipment contains case-packed boxes. When AreCasesRequired &#x3D; true for an inbound shipment, all items in the inbound shipment must be case packed. | 
**confirmedNeedByDate** | **String** | Date by which the shipment must arrive at the Amazon fulfillment center to avoid delivery promise breaks for pre-ordered items. |  [optional]
**boxContentsSource** | [**BoxContentsSource**](BoxContentsSource.md) |  |  [optional]
**estimatedBoxContentsFee** | [**BoxContentsFeeDetails**](BoxContentsFeeDetails.md) | An estimate of the manual processing fee charged by Amazon for boxes without box content information. This is only returned when BoxContentsSource is NONE. |  [optional]



