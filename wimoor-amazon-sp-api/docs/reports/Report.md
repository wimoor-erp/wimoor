
# Report

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**marketplaceIds** | **List&lt;String&gt;** | A list of marketplace identifiers for the report. |  [optional]
**reportId** | **String** | The identifier for the report. This identifier is unique only in combination with a seller ID. | 
**reportType** | **String** | The report type. | 
**dataStartTime** | [**OffsetDateTime**](OffsetDateTime.md) | The start of a date and time range used for selecting the data to report. |  [optional]
**dataEndTime** | [**OffsetDateTime**](OffsetDateTime.md) | The end of a date and time range used for selecting the data to report. |  [optional]
**reportScheduleId** | **String** | The identifier of the report schedule that created this report (if any). This identifier is unique only in combination with a seller ID. |  [optional]
**createdTime** | [**OffsetDateTime**](OffsetDateTime.md) | The date and time when the report was created. | 
**processingStatus** | [**ProcessingStatusEnum**](#ProcessingStatusEnum) | The processing status of the report. | 
**processingStartTime** | [**OffsetDateTime**](OffsetDateTime.md) | The date and time when the report processing started, in ISO 8601 date time format. |  [optional]
**processingEndTime** | [**OffsetDateTime**](OffsetDateTime.md) | The date and time when the report processing completed, in ISO 8601 date time format. |  [optional]
**reportDocumentId** | **String** | The identifier for the report document. Pass this into the getReportDocument operation to get the information you will need to retrieve and decrypt the report document&#39;s contents. |  [optional]


<a name="ProcessingStatusEnum"></a>
## Enum: ProcessingStatusEnum
Name | Value
---- | -----
CANCELLED | &quot;CANCELLED&quot;
DONE | &quot;DONE&quot;
FATAL | &quot;FATAL&quot;
IN_PROGRESS | &quot;IN_PROGRESS&quot;
IN_QUEUE | &quot;IN_QUEUE&quot;



