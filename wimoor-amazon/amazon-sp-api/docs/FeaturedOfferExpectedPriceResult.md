
# FeaturedOfferExpectedPriceResult

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**featuredOfferExpectedPrice** | [**FeaturedOfferExpectedPrice**](FeaturedOfferExpectedPrice.md) |  |  [optional]
**resultStatus** | **String** | The status of the featured offer expected price computation. Possible values include &#x60;VALID_FOEP&#x60;, &#x60;NO_COMPETING_OFFER&#x60;, &#x60;OFFER_NOT_ELIGIBLE&#x60;, &#x60;OFFER_NOT_FOUND&#x60;, &#x60;ASIN_NOT_ELIGIBLE&#x60;. Additional values may be added in the future. | 
**competingFeaturedOffer** | [**FeaturedOffer**](FeaturedOffer.md) | The offer that will likely be the featured offer if the target offer is priced above its featured offer expected price. If the target offer is currently the featured offer, this property will be different than &#x60;currentFeaturedOffer&#x60;. |  [optional]
**currentFeaturedOffer** | [**FeaturedOffer**](FeaturedOffer.md) | The offer that is currently the featured offer. If the target offer is not currently featured, then this property will be equal to &#x60;competingFeaturedOffer&#x60;. |  [optional]



