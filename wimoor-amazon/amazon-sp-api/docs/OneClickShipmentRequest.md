
# OneClickShipmentRequest

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**shipTo** | [**Address**](Address.md) | The ship to address. |  [optional]
**shipFrom** | [**Address**](Address.md) | The ship from address. | 
**returnTo** | [**Address**](Address.md) | The return to address. |  [optional]
**shipDate** | [**OffsetDateTime**](OffsetDateTime.md) | The ship date and time (the requested pickup). This defaults to the current date and time. |  [optional]
**packages** | [**PackageList**](PackageList.md) |  | 
**valueAddedServicesDetails** | [**OneClickShipmentValueAddedServiceDetails**](OneClickShipmentValueAddedServiceDetails.md) |  |  [optional]
**taxDetails** | [**TaxDetailList**](TaxDetailList.md) |  |  [optional]
**channelDetails** | [**ChannelDetails**](ChannelDetails.md) |  | 
**labelSpecifications** | [**RequestedDocumentSpecification**](RequestedDocumentSpecification.md) |  | 
**serviceSelection** | [**ServiceSelection**](ServiceSelection.md) |  | 
**shipperInstruction** | [**ShipperInstruction**](ShipperInstruction.md) | Optional field for shipper instruction. |  [optional]
**destinationAccessPointDetails** | [**AccessPointDetails**](AccessPointDetails.md) |  |  [optional]



