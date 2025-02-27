
# Reservation

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**reservationId** | **String** | Unique identifier for a reservation. If present, it is treated as an update reservation request and will update the corresponding reservation. Otherwise, it is treated as a new create reservation request. |  [optional]
**type** | [**TypeEnum**](#TypeEnum) | Type of reservation. | 
**availability** | [**AvailabilityRecord**](AvailabilityRecord.md) | &#x60;AvailabilityRecord&#x60; to represent the capacity of a resource over a time range. | 


<a name="TypeEnum"></a>
## Enum: TypeEnum
Name | Value
---- | -----
APPOINTMENT | &quot;APPOINTMENT&quot;
TRAVEL | &quot;TRAVEL&quot;
VACATION | &quot;VACATION&quot;
BREAK | &quot;BREAK&quot;
TRAINING | &quot;TRAINING&quot;



