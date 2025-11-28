
# InboundShipmentPlanRequestItem

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**sellerSKU** | **String** | The seller SKU of the item. | 
**ASIN** | **String** | The Amazon Standard Identification Number (ASIN) of the item. | 
**condition** | [**Condition**](Condition.md) |  | 
**quantity** | **Integer** |  | 
**quantityInCase** | **Integer** | The item quantity in each case, for case-packed items. Note that QuantityInCase multiplied by the number of cases in the inbound shipment equals Quantity. Also note that all of the boxes of an inbound shipment must either be case packed or individually packed. For that reason, when you submit the createInboundShipmentPlan operation, the value of QuantityInCase must be provided for every item in the shipment or for none of the items in the shipment. |  [optional]
**prepDetailsList** | [**PrepDetailsList**](PrepDetailsList.md) |  |  [optional]



