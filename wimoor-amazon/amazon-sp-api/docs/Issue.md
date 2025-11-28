
# Issue

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**code** | **String** | An issue code that identifies the type of issue. | 
**message** | **String** | A message that describes the issue. | 
**severity** | [**SeverityEnum**](#SeverityEnum) | The severity of the issue. | 
**attributeNames** | **List&lt;String&gt;** | The names of the attributes associated with the issue, if applicable. |  [optional]
**categories** | **List&lt;String&gt;** | List of issue categories.   Possible vales:   * &#x60;INVALID_ATTRIBUTE&#x60; - Indicating an invalid attribute in the listing.   * &#x60;MISSING_ATTRIBUTE&#x60; - Highlighting a missing attribute in the listing.   * &#x60;INVALID_IMAGE&#x60; - Signifying an invalid image in the listing.   * &#x60;MISSING_IMAGE&#x60; - Noting the absence of an image in the listing.   * &#x60;INVALID_PRICE&#x60; - Pertaining to issues with the listing&#39;s price-related attributes.   * &#x60;MISSING_PRICE&#x60; - Pointing out the absence of a price attribute in the listing.   * &#x60;DUPLICATE&#x60; - Identifying listings with potential duplicate problems, such as this ASIN potentially being a duplicate of another ASIN.   * &#x60;QUALIFICATION_REQUIRED&#x60; - Indicating that the listing requires qualification-related approval. | 
**enforcements** | [**IssueEnforcements**](IssueEnforcements.md) | This field provides information about the enforcement actions taken by Amazon that affect the publishing or status of a listing. It also includes details about any associated exemptions. |  [optional]


<a name="SeverityEnum"></a>
## Enum: SeverityEnum
Name | Value
---- | -----
ERROR | &quot;ERROR&quot;
WARNING | &quot;WARNING&quot;
INFO | &quot;INFO&quot;



