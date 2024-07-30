
# AppointmentSlotReport

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**schedulingType** | [**SchedulingTypeEnum**](#SchedulingTypeEnum) | Defines the type of slots. |  [optional]
**startTime** | [**OffsetDateTime**](OffsetDateTime.md) | Start Time from which the appointment slots are generated in ISO 8601 format. |  [optional]
**endTime** | [**OffsetDateTime**](OffsetDateTime.md) | End Time up to which the appointment slots are generated in ISO 8601 format. |  [optional]
**appointmentSlots** | [**List&lt;AppointmentSlot&gt;**](AppointmentSlot.md) | A list of time windows along with associated capacity in which the service can be performed. |  [optional]


<a name="SchedulingTypeEnum"></a>
## Enum: SchedulingTypeEnum
Name | Value
---- | -----
REAL_TIME_SCHEDULING | &quot;REAL_TIME_SCHEDULING&quot;
NON_REAL_TIME_SCHEDULING | &quot;NON_REAL_TIME_SCHEDULING&quot;



