
# DirectPayment

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**directPaymentType** | **String** | The type of payment.  Possible values:  * StoredValueCardRevenue - The amount that is deducted from the seller&#39;s account because the seller received money through a stored value card.  * StoredValueCardRefund - The amount that Amazon returns to the seller if the order that is bought using a stored value card is refunded.  * PrivateLabelCreditCardRevenue - The amount that is deducted from the seller&#39;s account because the seller received money through a private label credit card offered by Amazon.  * PrivateLabelCreditCardRefund - The amount that Amazon returns to the seller if the order that is bought using a private label credit card offered by Amazon is refunded.  * CollectOnDeliveryRevenue - The COD amount that the seller collected directly from the buyer.  * CollectOnDeliveryRefund - The amount that Amazon refunds to the buyer if an order paid for by COD is refunded. |  [optional]
**directPaymentAmount** | [**Currency**](Currency.md) | The amount of the direct payment. |  [optional]



