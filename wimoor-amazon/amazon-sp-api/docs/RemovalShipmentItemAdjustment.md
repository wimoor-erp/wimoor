
# RemovalShipmentItemAdjustment

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**removalShipmentItemId** | **String** | An identifier for an item in a removal shipment. |  [optional]
**taxCollectionModel** | **String** | The tax collection model applied to the item.  Possible values:  * MarketplaceFacilitator - Tax is withheld and remitted to the taxing authority by Amazon on behalf of the seller.  * Standard - Tax is paid to the seller and not remitted to the taxing authority by Amazon. |  [optional]
**fulfillmentNetworkSKU** | **String** | The Amazon fulfillment network SKU for the item. |  [optional]
**adjustedQuantity** | **Integer** | Adjusted quantity of removal shipmentItemAdjustment items. |  [optional]
**revenueAdjustment** | [**Currency**](Currency.md) | The total amount adjusted for disputed items. |  [optional]
**taxAmountAdjustment** | [**Currency**](Currency.md) | Adjustment on the Tax collected amount on the adjusted revenue. |  [optional]
**taxWithheldAdjustment** | [**Currency**](Currency.md) | Adjustment the tax withheld and remitted to the taxing authority by Amazon on behalf of the seller. If TaxCollectionModel&#x3D;MarketplaceFacilitator, then TaxWithheld&#x3D;TaxAmount (except the TaxWithheld amount is a negative number). Otherwise TaxWithheld&#x3D;0. |  [optional]



