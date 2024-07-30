
# Feed

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**feedId** | **String** | The identifier for the feed. This identifier is unique only in combination with a seller ID. | 
**feedType** | **String** | The feed type. | 
**marketplaceIds** | **List&lt;String&gt;** | A list of identifiers for the marketplaces that the feed is applied to. |  [optional]
**createdTime** | [**OffsetDateTime**](OffsetDateTime.md) | The date and time when the feed was created, in ISO 8601 date time format. | 
**processingStatus** | [**ProcessingStatusEnum**](#ProcessingStatusEnum) | The processing status of the feed. | 
**processingStartTime** | [**OffsetDateTime**](OffsetDateTime.md) | The date and time when feed processing started, in ISO 8601 date time format. |  [optional]
**processingEndTime** | [**OffsetDateTime**](OffsetDateTime.md) | The date and time when feed processing completed, in ISO 8601 date time format. |  [optional]
**resultFeedDocumentId** | **String** | The identifier for the feed document. This identifier is unique only in combination with a seller ID. |  [optional]


<a name="ProcessingStatusEnum"></a>
## Enum: ProcessingStatusEnum
Name | Value
---- | -----
CANCELLED | &quot;CANCELLED&quot;
DONE | &quot;DONE&quot;
FATAL | &quot;FATAL&quot;
IN_PROGRESS | &quot;IN_PROGRESS&quot;
IN_QUEUE | &quot;IN_QUEUE&quot;



