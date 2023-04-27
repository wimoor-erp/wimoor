
# FulfillmentOrderItem

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**sellerSku** | **String** | The seller SKU of the item. | 
**sellerFulfillmentOrderItemId** | **String** | A fulfillment order item identifier submitted with a call to the createFulfillmentOrder operation. | 
**quantity** | **Integer** |  | 
**giftMessage** | **String** | A message to the gift recipient, if applicable. |  [optional]
**displayableComment** | **String** | Item-specific text that displays in recipient-facing materials such as the outbound shipment packing slip. |  [optional]
**fulfillmentNetworkSku** | **String** | Amazon&#39;s fulfillment network SKU of the item. |  [optional]
**orderItemDisposition** | **String** | Indicates whether the item is sellable or unsellable. |  [optional]
**cancelledQuantity** | **Integer** | The item quantity that was cancelled by the seller. | 
**unfulfillableQuantity** | **Integer** | The item quantity that is unfulfillable. | 
**estimatedShipDate** | **String** | The estimated date and time that the item quantity is scheduled to ship from the fulfillment center. Note that this value can change over time. If the shipment that contains the item quantity has been cancelled, estimatedShipDate is not returned. |  [optional]
**estimatedArrivalDate** | **String** | The estimated arrival date and time of the item quantity. Note that this value can change over time. If the shipment that contains the item quantity has been cancelled, estimatedArrivalDate is not returned. |  [optional]
**perUnitPrice** | [**Money**](Money.md) | The amount to be collected from the recipient for this item in a COD (Cash On Delivery) order. |  [optional]
**perUnitTax** | [**Money**](Money.md) | The tax on the amount to be collected from the recipient for this item in a COD (Cash On Delivery) order. |  [optional]
**perUnitDeclaredValue** | [**Money**](Money.md) | The monetary value assigned by the seller to this item. |  [optional]



