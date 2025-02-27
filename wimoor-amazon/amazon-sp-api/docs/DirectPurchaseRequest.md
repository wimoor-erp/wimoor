
# DirectPurchaseRequest

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**shipTo** | [**Address**](Address.md) | The address where the shipment will be delivered. For vendor orders, shipTo information is pulled directly from the Amazon order. |  [optional]
**shipFrom** | [**Address**](Address.md) | The address where the package will be picked up. |  [optional]
**returnTo** | [**Address**](Address.md) | The address where the package will be returned if it cannot be delivered. |  [optional]
**packages** | [**PackageList**](PackageList.md) |  |  [optional]
**channelDetails** | [**ChannelDetails**](ChannelDetails.md) |  | 
**labelSpecifications** | [**RequestedDocumentSpecification**](RequestedDocumentSpecification.md) | The document (label) specifications requested. The default label returned is PNG DPI 203 4x6 if no label specification is provided. Requesting an invalid file format results in a failure. |  [optional]



