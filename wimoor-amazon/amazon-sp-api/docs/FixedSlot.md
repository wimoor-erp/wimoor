
# FixedSlot

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**startDateTime** | [**OffsetDateTime**](OffsetDateTime.md) | Start date time of slot in ISO 8601 format with precision of seconds. |  [optional]
**scheduledCapacity** | **Integer** | Scheduled capacity corresponding to the slot. This capacity represents the originally allocated capacity as per resource schedule. |  [optional]
**availableCapacity** | **Integer** | Available capacity corresponding to the slot. This capacity represents the capacity available for allocation to reservations. |  [optional]
**encumberedCapacity** | **Integer** | Encumbered capacity corresponding to the slot. This capacity represents the capacity allocated for Amazon Jobs/Appointments/Orders. |  [optional]
**reservedCapacity** | **Integer** | Reserved capacity corresponding to the slot. This capacity represents the capacity made unavailable due to events like Breaks/Leaves/Lunch. |  [optional]



