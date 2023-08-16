
# CouponPaymentEvent

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**postedDate** | **String** | The date and time when the financial event was posted. |  [optional]
**couponId** | **String** | A coupon identifier. |  [optional]
**sellerCouponDescription** | **String** | The description provided by the seller when they created the coupon. |  [optional]
**clipOrRedemptionCount** | **Long** | The number of coupon clips or redemptions. |  [optional]
**paymentEventId** | **String** | A payment event identifier. |  [optional]
**feeComponent** | [**FeeComponent**](FeeComponent.md) |  |  [optional]
**chargeComponent** | [**ChargeComponent**](ChargeComponent.md) |  |  [optional]
**totalAmount** | [**Currency**](Currency.md) | The FeeComponent value plus the ChargeComponent value. |  [optional]



