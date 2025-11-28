
# ItemEligibilityPreview

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**asin** | **String** | The ASIN for which eligibility was determined. | 
**marketplaceId** | **String** | The marketplace for which eligibility was determined. |  [optional]
**program** | [**ProgramEnum**](#ProgramEnum) | The program for which eligibility was determined. | 
**isEligibleForProgram** | **Boolean** | Indicates if the item is eligible for the program. | 
**ineligibilityReasonList** | [**List&lt;IneligibilityReasonListEnum&gt;**](#List&lt;IneligibilityReasonListEnum&gt;) | Potential Ineligibility Reason Codes. |  [optional]


<a name="ProgramEnum"></a>
## Enum: ProgramEnum
Name | Value
---- | -----
INBOUND | &quot;INBOUND&quot;
COMMINGLING | &quot;COMMINGLING&quot;


<a name="List<IneligibilityReasonListEnum>"></a>
## Enum: List&lt;IneligibilityReasonListEnum&gt;
Name | Value
---- | -----
FBA_INB_0004 | &quot;FBA_INB_0004&quot;
FBA_INB_0006 | &quot;FBA_INB_0006&quot;
FBA_INB_0007 | &quot;FBA_INB_0007&quot;
FBA_INB_0008 | &quot;FBA_INB_0008&quot;
FBA_INB_0009 | &quot;FBA_INB_0009&quot;
FBA_INB_0010 | &quot;FBA_INB_0010&quot;
FBA_INB_0011 | &quot;FBA_INB_0011&quot;
FBA_INB_0012 | &quot;FBA_INB_0012&quot;
FBA_INB_0013 | &quot;FBA_INB_0013&quot;
FBA_INB_0014 | &quot;FBA_INB_0014&quot;
FBA_INB_0015 | &quot;FBA_INB_0015&quot;
FBA_INB_0016 | &quot;FBA_INB_0016&quot;
FBA_INB_0017 | &quot;FBA_INB_0017&quot;
FBA_INB_0018 | &quot;FBA_INB_0018&quot;
FBA_INB_0019 | &quot;FBA_INB_0019&quot;
FBA_INB_0034 | &quot;FBA_INB_0034&quot;
FBA_INB_0035 | &quot;FBA_INB_0035&quot;
FBA_INB_0036 | &quot;FBA_INB_0036&quot;
FBA_INB_0037 | &quot;FBA_INB_0037&quot;
FBA_INB_0038 | &quot;FBA_INB_0038&quot;
FBA_INB_0050 | &quot;FBA_INB_0050&quot;
FBA_INB_0051 | &quot;FBA_INB_0051&quot;
FBA_INB_0053 | &quot;FBA_INB_0053&quot;
FBA_INB_0055 | &quot;FBA_INB_0055&quot;
FBA_INB_0056 | &quot;FBA_INB_0056&quot;
FBA_INB_0059 | &quot;FBA_INB_0059&quot;
FBA_INB_0065 | &quot;FBA_INB_0065&quot;
FBA_INB_0066 | &quot;FBA_INB_0066&quot;
FBA_INB_0067 | &quot;FBA_INB_0067&quot;
FBA_INB_0068 | &quot;FBA_INB_0068&quot;
FBA_INB_0095 | &quot;FBA_INB_0095&quot;
FBA_INB_0097 | &quot;FBA_INB_0097&quot;
FBA_INB_0098 | &quot;FBA_INB_0098&quot;
FBA_INB_0099 | &quot;FBA_INB_0099&quot;
FBA_INB_0100 | &quot;FBA_INB_0100&quot;
FBA_INB_0103 | &quot;FBA_INB_0103&quot;
FBA_INB_0104 | &quot;FBA_INB_0104&quot;
UNKNOWN_INB_ERROR_CODE | &quot;UNKNOWN_INB_ERROR_CODE&quot;



