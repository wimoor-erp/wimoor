
# FeesEstimateResult

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**status** | **String** | The status of the fee request. Possible values: Success, ClientError, ServiceError. |  [optional]
**feesEstimateIdentifier** | [**FeesEstimateIdentifier**](FeesEstimateIdentifier.md) | Information used to identify a fees estimate request. |  [optional]
**feesEstimate** | [**FeesEstimate**](FeesEstimate.md) | The total estimated fees for an item and a list of details. |  [optional]
**error** | [**FeesEstimateError**](FeesEstimateError.md) | An error object with a type, code, and message. |  [optional]



