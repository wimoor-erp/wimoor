
# ReturnItem

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**sellerReturnItemId** | **String** | An identifier assigned by the seller to the return item. | 
**sellerFulfillmentOrderItemId** | **String** | The identifier assigned to the item by the seller when the fulfillment order was created. | 
**amazonShipmentId** | **String** | The identifier for the shipment that is associated with the return item. | 
**sellerReturnReasonCode** | **String** | The return reason code assigned to the return item by the seller. | 
**returnComment** | **String** | An optional comment about the return item. |  [optional]
**amazonReturnReasonCode** | **String** | The return reason code that the Amazon fulfillment center assigned to the return item. |  [optional]
**status** | [**FulfillmentReturnItemStatus**](FulfillmentReturnItemStatus.md) | Indicates if the return item has been processed by an Amazon fulfillment center. | 
**statusChangedDate** | **String** | Indicates when the status last changed. | 
**returnAuthorizationId** | **String** | Identifies the return authorization used to return this item. See ReturnAuthorization. |  [optional]
**returnReceivedCondition** | [**ReturnItemDisposition**](ReturnItemDisposition.md) |  |  [optional]
**fulfillmentCenterId** | **String** | The identifier for the Amazon fulfillment center that processed the return item. |  [optional]



