
# ShipmentItem

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**sellerSKU** | **String** | The seller SKU of the item. The seller SKU is qualified by the seller&#39;s seller ID, which is included with every call to the Selling Partner API. |  [optional]
**orderItemId** | **String** | An Amazon-defined order item identifier. |  [optional]
**orderAdjustmentItemId** | **String** | An Amazon-defined order adjustment identifier defined for refunds, guarantee claims, and chargeback events. |  [optional]
**quantityShipped** | **Integer** | The number of items shipped. |  [optional]
**itemChargeList** | [**ChargeComponentList**](ChargeComponentList.md) | A list of charges associated with the shipment item. |  [optional]
**itemChargeAdjustmentList** | [**ChargeComponentList**](ChargeComponentList.md) | A list of charge adjustments associated with the shipment item. This value is only returned for refunds, guarantee claims, and chargeback events. |  [optional]
**itemFeeList** | [**FeeComponentList**](FeeComponentList.md) | A list of fees associated with the shipment item. |  [optional]
**itemFeeAdjustmentList** | [**FeeComponentList**](FeeComponentList.md) | A list of fee adjustments associated with the shipment item. This value is only returned for refunds, guarantee claims, and chargeback events. |  [optional]
**itemTaxWithheldList** | [**TaxWithheldComponentList**](TaxWithheldComponentList.md) | A list of taxes withheld information for a shipment item. |  [optional]
**promotionList** | [**PromotionList**](PromotionList.md) |  |  [optional]
**promotionAdjustmentList** | [**PromotionList**](PromotionList.md) | A list of promotion adjustments associated with the shipment item. This value is only returned for refunds, guarantee claims, and chargeback events. |  [optional]
**costOfPointsGranted** | [**Currency**](Currency.md) | The cost of Amazon Points granted for a shipment item. |  [optional]
**costOfPointsReturned** | [**Currency**](Currency.md) | The cost of Amazon Points returned for a shipment item. This value is only returned for refunds, guarantee claims, and chargeback events. |  [optional]



