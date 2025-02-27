
# RegulatedOrderVerificationStatus

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**status** | [**VerificationStatus**](VerificationStatus.md) | The verification status of the order. | 
**requiresMerchantAction** | **Boolean** | When true, the regulated information provided in the order requires a review by the merchant. | 
**validRejectionReasons** | [**List&lt;RejectionReason&gt;**](RejectionReason.md) | A list of valid rejection reasons that may be used to reject the order&#39;s regulated information. | 
**rejectionReason** | [**RejectionReason**](RejectionReason.md) | The reason for rejecting the order&#39;s regulated information. Not present if the order isn&#39;t rejected. |  [optional]
**reviewDate** | **String** | The date the order was reviewed. In &lt;a href&#x3D;&#39;https://developer-docs.amazon.com/sp-api/docs/iso-8601&#39;&gt;ISO 8601&lt;/a&gt; date time format. |  [optional]
**externalReviewerId** | **String** | The identifier for the order&#39;s regulated information reviewer. |  [optional]



