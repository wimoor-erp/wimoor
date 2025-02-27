
# GetRatesRequest

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**shipTo** | [**Address**](Address.md) | The ship to address. |  [optional]
**shipFrom** | [**Address**](Address.md) | The ship from address. | 
**returnTo** | [**Address**](Address.md) | The return to address. |  [optional]
**shipDate** | [**OffsetDateTime**](OffsetDateTime.md) | The ship date and time (the requested pickup). This defaults to the current date and time. |  [optional]
**shipperInstruction** | [**ShipperInstruction**](ShipperInstruction.md) | This field describe shipper instruction. |  [optional]
**packages** | [**PackageList**](PackageList.md) |  | 
**valueAddedServices** | [**ValueAddedServiceDetails**](ValueAddedServiceDetails.md) |  |  [optional]
**taxDetails** | [**TaxDetailList**](TaxDetailList.md) |  |  [optional]
**channelDetails** | [**ChannelDetails**](ChannelDetails.md) |  | 
**clientReferenceDetails** | [**ClientReferenceDetails**](ClientReferenceDetails.md) |  |  [optional]
**shipmentType** | [**ShipmentType**](ShipmentType.md) |  |  [optional]
**destinationAccessPointDetails** | [**AccessPointDetails**](AccessPointDetails.md) |  |  [optional]



