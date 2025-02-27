
# RemovalShipmentAdjustmentEvent

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**postedDate** | **String** | The date when the financial event was posted. |  [optional]
**adjustmentEventId** | **String** | The unique identifier for the adjustment event. |  [optional]
**merchantOrderId** | **String** | The merchant removal orderId. |  [optional]
**orderId** | **String** | The orderId for shipping inventory. |  [optional]
**transactionType** | **String** | The type of removal order.  Possible values:  * WHOLESALE_LIQUIDATION. |  [optional]
**removalShipmentItemAdjustmentList** | [**List&lt;RemovalShipmentItemAdjustment&gt;**](RemovalShipmentItemAdjustment.md) | A comma-delimited list of Removal shipmentItemAdjustment details for FBA inventory. |  [optional]



