
# SellerDealPaymentEvent

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**postedDate** | **String** | The date and time when the financial event was posted. |  [optional]
**dealId** | **String** | The unique identifier of the deal. |  [optional]
**dealDescription** | **String** | The internal description of the deal. |  [optional]
**eventType** | **String** | The type of event: SellerDealComplete. |  [optional]
**feeType** | **String** | The type of fee: RunLightningDealFee. |  [optional]
**feeAmount** | [**Currency**](Currency.md) | The monetary amount of the fee. |  [optional]
**taxAmount** | [**Currency**](Currency.md) | The monetary amount of the tax applied. |  [optional]
**totalAmount** | [**Currency**](Currency.md) | The total monetary amount paid. |  [optional]



