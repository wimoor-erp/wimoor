
# RetrochargeEvent

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**retrochargeEventType** | **String** | The type of event.  Possible values:  * Retrocharge  * RetrochargeReversal |  [optional]
**amazonOrderId** | **String** | An Amazon-defined identifier for an order. |  [optional]
**postedDate** | **String** | The date and time when the financial event was posted. |  [optional]
**baseTax** | [**Currency**](Currency.md) | The base tax associated with the retrocharge event. |  [optional]
**shippingTax** | [**Currency**](Currency.md) | The shipping tax associated with the retrocharge event. |  [optional]
**marketplaceName** | **String** | The name of the marketplace where the retrocharge event occurred. |  [optional]
**retrochargeTaxWithheldList** | [**TaxWithheldComponentList**](TaxWithheldComponentList.md) | A list of information about taxes withheld. |  [optional]



