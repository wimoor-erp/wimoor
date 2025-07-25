
# ProductAdsPaymentEvent

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**postedDate** | **String** | The date and time when the financial event was posted. |  [optional]
**transactionType** | **String** | Indicates if the transaction is for a charge or a refund.  Possible values:  * charge - Charge  * refund - Refund |  [optional]
**invoiceId** | **String** | Identifier for the invoice that the transaction appears in. |  [optional]
**baseValue** | [**Currency**](Currency.md) | Base amount of the transaction, before tax. |  [optional]
**taxValue** | [**Currency**](Currency.md) | Tax amount of the transaction. |  [optional]
**transactionValue** | [**Currency**](Currency.md) | The total amount of the transaction. Equal to baseValue + taxValue. |  [optional]



