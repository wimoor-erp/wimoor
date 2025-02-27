
# FeaturedBuyingOption

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**buyingOptionType** | [**BuyingOptionTypeEnum**](#BuyingOptionTypeEnum) | The buying option type of the featured offer. This field represents the buying options that a customer sees on the detail page. For example, B2B, Fresh, and Subscribe n Save. Currently supports &#x60;NEW&#x60; | 
**segmentedFeaturedOffers** | [**List&lt;SegmentedFeaturedOffer&gt;**](SegmentedFeaturedOffer.md) | A list of segmented featured offers for the current buying option type. A segment can be considered as a group of regional contexts that all have the same featured offer. A regional context is a combination of factors such as customer type, region or postal code and buying option. | 


<a name="BuyingOptionTypeEnum"></a>
## Enum: BuyingOptionTypeEnum
Name | Value
---- | -----
NEW | &quot;New&quot;



