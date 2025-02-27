
# SegmentedFeaturedOffer

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**sellerId** | **String** | The seller identifier for the offer. | 
**condition** | [**Condition**](Condition.md) | Item Condition. | 
**fulfillmentType** | [**FulfillmentType**](FulfillmentType.md) | The fulfillment type for the offer. Possible values are AFN (Amazon Fulfillment Network) and MFN (Merchant Fulfillment Network). | 
**listingPrice** | [**MoneyType**](MoneyType.md) | Offer buying price. Does not include shipping, points, or applicable promotions. | 
**shippingOptions** | [**List&lt;ShippingOption&gt;**](ShippingOption.md) | A list of shipping options associated with this offer |  [optional]
**points** | [**Points**](Points.md) | The number of Amazon Points offered with the purchase of an item, and their monetary value. Note that the Points element is only returned in Japan (JP). |  [optional]
**featuredOfferSegments** | [**List&lt;FeaturedOfferSegment&gt;**](FeaturedOfferSegment.md) | The list of segment information in which the offer is featured. | 



