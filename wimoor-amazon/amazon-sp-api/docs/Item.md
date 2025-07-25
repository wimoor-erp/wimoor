
# Item

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**itemValue** | [**Currency**](Currency.md) |  |  [optional]
**description** | **String** | The product description of the item. |  [optional]
**itemIdentifier** | **String** | A unique identifier for an item provided by the client. |  [optional]
**quantity** | **Integer** | The number of units. This value is required. | 
**weight** | [**Weight**](Weight.md) |  |  [optional]
**liquidVolume** | [**LiquidVolume**](LiquidVolume.md) |  |  [optional]
**isHazmat** | **Boolean** | When true, the item qualifies as hazardous materials (hazmat). Defaults to false. |  [optional]
**dangerousGoodsDetails** | [**DangerousGoodsDetails**](DangerousGoodsDetails.md) |  |  [optional]
**productType** | **String** | The product type of the item. |  [optional]
**invoiceDetails** | [**InvoiceDetails**](InvoiceDetails.md) |  |  [optional]
**serialNumbers** | **List&lt;String&gt;** | A list of unique serial numbers in an Amazon package that can be used to guarantee non-fraudulent items. The number of serial numbers in the list must be less than or equal to the quantity of items being shipped. Only applicable when channel source is Amazon. |  [optional]
**directFulfillmentItemIdentifiers** | [**DirectFulfillmentItemIdentifiers**](DirectFulfillmentItemIdentifiers.md) |  |  [optional]



