
# Shipment

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**shipmentId** | **String** |  | 
**amazonOrderId** | **String** |  | 
**sellerOrderId** | **String** |  |  [optional]
**itemList** | [**ItemList**](ItemList.md) |  | 
**shipFromAddress** | [**Address**](Address.md) | The address of the sender. | 
**shipToAddress** | [**Address**](Address.md) | The destination address for the shipment. | 
**packageDimensions** | [**PackageDimensions**](PackageDimensions.md) |  | 
**weight** | [**Weight**](Weight.md) | The package weight. | 
**insurance** | [**CurrencyAmount**](CurrencyAmount.md) | If DeclaredValue was specified in a previous call to the createShipment operation, then Insurance indicates the amount that the carrier will use to insure the shipment. If DeclaredValue was not specified with a previous call to the createShipment operation, then the shipment will be insured for the carrier&#39;s minimum insurance amount, or the combined sale prices that the items are listed for in the shipment, whichever is less. | 
**shippingService** | [**ShippingService**](ShippingService.md) |  | 
**label** | [**Label**](Label.md) | Data for creating a shipping label and dimensions for printing the label. If the shipment is canceled, an empty Label is returned. | 
**status** | [**ShipmentStatus**](ShipmentStatus.md) | The shipment status. | 
**trackingId** | **String** |  |  [optional]
**createdDate** | **String** | The date and time the shipment was created. | 
**lastUpdatedDate** | **String** | The date and time of the last update. |  [optional]



