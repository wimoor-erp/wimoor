
# PriceType

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**landedPrice** | [**MoneyType**](MoneyType.md) | The value calculated by adding ListingPrice + Shipping - Points. Note that if the landed price is not returned, the listing price represents the product with the lowest landed price. |  [optional]
**listingPrice** | [**MoneyType**](MoneyType.md) | The listing price of the item including any promotions that apply. | 
**shipping** | [**MoneyType**](MoneyType.md) | The shipping cost of the product. Note that the shipping cost is not always available. |  [optional]
**points** | [**Points**](Points.md) | The number of Amazon Points offered with the purchase of an item, and their monetary value. |  [optional]



