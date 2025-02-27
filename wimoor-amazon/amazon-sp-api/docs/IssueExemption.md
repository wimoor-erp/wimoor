
# IssueExemption

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**status** | [**StatusEnum**](#StatusEnum) | This field indicates the current exemption status for the listed enforcement actions. It can take values such as &#x60;EXEMPT&#x60;, signifying permanent exemption, &#x60;EXEMPT_UNTIL_EXPIRY_DATE&#x60; indicating temporary exemption until a specified date, or &#x60;NOT_EXEMPT&#x60; signifying no exemptions, and enforcement actions were already applied. | 
**expiryDate** | [**OffsetDateTime**](OffsetDateTime.md) | This field represents the timestamp, following the ISO 8601 format, which specifies the date when temporary exemptions, if applicable, will expire, and Amazon will begin enforcing the listed actions. |  [optional]


<a name="StatusEnum"></a>
## Enum: StatusEnum
Name | Value
---- | -----
EXEMPT | &quot;EXEMPT&quot;
EXEMPT_UNTIL_EXPIRY_DATE | &quot;EXEMPT_UNTIL_EXPIRY_DATE&quot;
NOT_EXEMPT | &quot;NOT_EXEMPT&quot;



