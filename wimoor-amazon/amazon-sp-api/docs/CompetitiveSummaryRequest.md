
# CompetitiveSummaryRequest

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**asin** | **String** | The Amazon identifier for the item. | 
**marketplaceId** | **String** | A marketplace identifier. | 
**includedData** | [**List&lt;CompetitiveSummaryIncludedData&gt;**](CompetitiveSummaryIncludedData.md) | The list of requested competitive pricing data for the product. | 
**method** | [**HttpMethod**](HttpMethod.md) | HTTP method type | 
**uri** | **String** | The URI associated with the individual APIs being called as part of the batch request. For &#x60;getCompetitiveSummary&#x60;, this should be &#x60;/products/pricing/2022-05-01/items/competitiveSummary&#x60;. | 



