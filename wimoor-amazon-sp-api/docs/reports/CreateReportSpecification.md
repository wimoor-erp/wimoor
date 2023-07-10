
# CreateReportSpecification

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**reportOptions** | [**ReportOptions**](ReportOptions.md) |  |  [optional]
**reportType** | **String** | The report type. | 
**dataStartTime** | [**OffsetDateTime**](OffsetDateTime.md) | The start of a date and time range, in ISO 8601 date time format, used for selecting the data to report. The default is now. The value must be prior to or equal to the current date and time. Not all report types make use of this. |  [optional]
**dataEndTime** | [**OffsetDateTime**](OffsetDateTime.md) | The end of a date and time range, in ISO 8601 date time format, used for selecting the data to report. The default is now. The value must be prior to or equal to the current date and time. Not all report types make use of this. |  [optional]
**marketplaceIds** | **List&lt;String&gt;** | A list of marketplace identifiers. The report document&#39;s contents will contain data for all of the specified marketplaces, unless the report type indicates otherwise. | 



