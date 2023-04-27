
# OfferDetail

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**myOffer** | **Boolean** | When true, this is the seller&#39;s offer. |  [optional]
**subCondition** | **String** | The subcondition of the item. Subcondition values: New, Mint, Very Good, Good, Acceptable, Poor, Club, OEM, Warranty, Refurbished Warranty, Refurbished, Open Box, or Other. | 
**sellerFeedbackRating** | [**SellerFeedbackType**](SellerFeedbackType.md) | Information about the seller&#39;s feedback, including the percentage of positive feedback, and the total number of ratings received. |  [optional]
**shippingTime** | [**DetailedShippingTimeType**](DetailedShippingTimeType.md) | The maximum time within which the item will likely be shipped once an order has been placed. | 
**listingPrice** | [**MoneyType**](MoneyType.md) | The price of the item. | 
**points** | [**Points**](Points.md) | The number of Amazon Points offered with the purchase of an item. |  [optional]
**shipping** | [**MoneyType**](MoneyType.md) | The shipping cost. | 
**shipsFrom** | [**ShipsFromType**](ShipsFromType.md) | The state and country from where the item is shipped. |  [optional]
**isFulfilledByAmazon** | **Boolean** | When true, the offer is fulfilled by Amazon. | 
**isBuyBoxWinner** | **Boolean** | When true, the offer is currently in the Buy Box. There can be up to two Buy Box winners at any time per ASIN, one that is eligible for Prime and one that is not eligible for Prime. |  [optional]
**isFeaturedMerchant** | **Boolean** | When true, the seller of the item is eligible to win the Buy Box. |  [optional]



