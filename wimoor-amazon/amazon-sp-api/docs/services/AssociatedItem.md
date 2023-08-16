
# AssociatedItem

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**asin** | **String** | The Amazon Standard Identification Number (ASIN) of the item. |  [optional]
**title** | **String** | The title of the item. |  [optional]
**quantity** | **Integer** | The total number of items included in the order. |  [optional]
**orderId** | **String** | The Amazon-defined identifier for an order placed by the buyer, in 3-7-7 format. |  [optional]
**itemStatus** | [**ItemStatusEnum**](#ItemStatusEnum) | The status of the item. |  [optional]
**brandName** | **String** | The brand name of the item. |  [optional]
**itemDelivery** | [**ItemDelivery**](ItemDelivery.md) | Delivery information for the item. |  [optional]


<a name="ItemStatusEnum"></a>
## Enum: ItemStatusEnum
Name | Value
---- | -----
ACTIVE | &quot;ACTIVE&quot;
CANCELLED | &quot;CANCELLED&quot;
SHIPPED | &quot;SHIPPED&quot;
DELIVERED | &quot;DELIVERED&quot;



