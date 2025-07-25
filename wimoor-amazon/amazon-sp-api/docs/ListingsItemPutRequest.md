
# ListingsItemPutRequest

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**productType** | **String** | The Amazon product type of the listings item. | 
**requirements** | [**RequirementsEnum**](#RequirementsEnum) | The name of the requirements set for the provided data. |  [optional]
**attributes** | **Object** | A JSON object containing structured listings item attribute data keyed by attribute name. | 


<a name="RequirementsEnum"></a>
## Enum: RequirementsEnum
Name | Value
---- | -----
LISTING | &quot;LISTING&quot;
LISTING_PRODUCT_ONLY | &quot;LISTING_PRODUCT_ONLY&quot;
LISTING_OFFER_ONLY | &quot;LISTING_OFFER_ONLY&quot;



