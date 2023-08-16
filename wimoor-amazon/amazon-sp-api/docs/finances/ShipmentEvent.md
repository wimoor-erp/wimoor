
# ShipmentEvent

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**amazonOrderId** | **String** | An Amazon-defined identifier for an order. |  [optional]
**sellerOrderId** | **String** | A seller-defined identifier for an order. |  [optional]
**marketplaceName** | **String** | The name of the marketplace where the event occurred. |  [optional]
**orderChargeList** | [**ChargeComponentList**](ChargeComponentList.md) | A list of order-level charges. These charges are applicable to Multi-Channel Fulfillment COD orders. |  [optional]
**orderChargeAdjustmentList** | [**ChargeComponentList**](ChargeComponentList.md) | A list of order-level charge adjustments. These adjustments are applicable to Multi-Channel Fulfillment COD orders. |  [optional]
**shipmentFeeList** | [**FeeComponentList**](FeeComponentList.md) | A list of shipment-level fees. |  [optional]
**shipmentFeeAdjustmentList** | [**FeeComponentList**](FeeComponentList.md) | A list of shipment-level fee adjustments. |  [optional]
**orderFeeList** | [**FeeComponentList**](FeeComponentList.md) | A list of order-level fees. These charges are applicable to Multi-Channel Fulfillment orders. |  [optional]
**orderFeeAdjustmentList** | [**FeeComponentList**](FeeComponentList.md) | A list of order-level fee adjustments. These adjustments are applicable to Multi-Channel Fulfillment orders. |  [optional]
**directPaymentList** | [**DirectPaymentList**](DirectPaymentList.md) | A list of transactions where buyers pay Amazon through one of the credit cards offered by Amazon or where buyers pay a seller directly through COD. |  [optional]
**postedDate** | **String** | The date and time when the financial event was posted. |  [optional]
**shipmentItemList** | [**ShipmentItemList**](ShipmentItemList.md) |  |  [optional]
**shipmentItemAdjustmentList** | [**ShipmentItemList**](ShipmentItemList.md) | A list of shipment item adjustments. |  [optional]



