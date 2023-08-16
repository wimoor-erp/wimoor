
# PackageTrackingDetails

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**packageNumber** | **Integer** | The package identifier. | 
**trackingNumber** | **String** | The tracking number for the package. |  [optional]
**customerTrackingLink** | **String** | Link on swiship.com that allows customers to track the package. |  [optional]
**carrierCode** | **String** | The name of the carrier. |  [optional]
**carrierPhoneNumber** | **String** | The phone number of the carrier. |  [optional]
**carrierURL** | **String** | The URL of the carrier’s website. |  [optional]
**shipDate** | **String** | The shipping date for the package. |  [optional]
**estimatedArrivalDate** | **String** | The estimated arrival date. |  [optional]
**shipToAddress** | [**TrackingAddress**](TrackingAddress.md) | The destination city for the package. |  [optional]
**currentStatus** | [**CurrentStatus**](CurrentStatus.md) |  |  [optional]
**currentStatusDescription** | **String** | Description corresponding to the CurrentStatus value. |  [optional]
**signedForBy** | **String** | The name of the person who signed for the package. |  [optional]
**additionalLocationInfo** | [**AdditionalLocationInfo**](AdditionalLocationInfo.md) |  |  [optional]
**trackingEvents** | [**TrackingEventList**](TrackingEventList.md) |  |  [optional]



