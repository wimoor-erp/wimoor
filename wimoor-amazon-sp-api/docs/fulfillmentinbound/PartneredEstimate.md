
# PartneredEstimate

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**amount** | [**Amount**](Amount.md) | The amount that the Amazon-partnered carrier will charge to ship the inbound shipment. | 
**confirmDeadline** | **String** | The date in ISO 8601 date time format by which this estimate must be confirmed. After this date the estimate is no longer valid and cannot be confirmed.  Returned only if the TransportStatus value of the inbound shipment is ESTIMATED. |  [optional]
**voidDeadline** | **String** | The date in ISO 8601 date time format after which a confirmed transportation request can no longer be voided. This date is 24 hours after a Small Parcel shipment transportation request is confirmed or one hour after a Less Than Truckload/Full Truckload (LTL/FTL) shipment transportation request is confirmed. After the void deadline passes the seller&#39;s account will be charged for the shipping cost.  Returned only if the TransportStatus value of the inbound shipment is CONFIRMED. |  [optional]



