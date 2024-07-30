
# CreateWarrantyRequest

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**attachments** | [**List&lt;Attachment&gt;**](Attachment.md) | Attachments to include in the message to the buyer. If any text is included in the attachment, the text must be written in the buyer&#39;s language of preference, which can be retrieved from the GetAttributes operation. |  [optional]
**coverageStartDate** | [**OffsetDateTime**](OffsetDateTime.md) | The start date of the warranty coverage to include in the message to the buyer. |  [optional]
**coverageEndDate** | [**OffsetDateTime**](OffsetDateTime.md) | The end date of the warranty coverage to include in the message to the buyer. |  [optional]



