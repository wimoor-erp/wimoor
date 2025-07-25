
# ListingsItemSubmissionResponse

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**sku** | **String** | A selling partner provided identifier for an Amazon listing. | 
**status** | [**StatusEnum**](#StatusEnum) | The status of the listings item submission. | 
**submissionId** | **String** | The unique identifier of the listings item submission. | 
**issues** | [**List&lt;Issue&gt;**](Issue.md) | Listings item issues related to the listings item submission. |  [optional]


<a name="StatusEnum"></a>
## Enum: StatusEnum
Name | Value
---- | -----
ACCEPTED | &quot;ACCEPTED&quot;
INVALID | &quot;INVALID&quot;



