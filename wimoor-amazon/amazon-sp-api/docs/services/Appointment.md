
# Appointment

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**appointmentId** | **String** | The appointment identifier. |  [optional]
**appointmentStatus** | [**AppointmentStatusEnum**](#AppointmentStatusEnum) | The status of the appointment. |  [optional]
**appointmentTime** | [**AppointmentTime**](AppointmentTime.md) | The time of the appointment window. |  [optional]
**assignedTechnicians** | [**List&lt;Technician&gt;**](Technician.md) | A list of technicians assigned to the service job. |  [optional]
**rescheduledAppointmentId** | **String** | The identifier of a rescheduled appointment. |  [optional]
**poa** | [**Poa**](Poa.md) | Proof of Appointment (POA) details. |  [optional]


<a name="AppointmentStatusEnum"></a>
## Enum: AppointmentStatusEnum
Name | Value
---- | -----
ACTIVE | &quot;ACTIVE&quot;
CANCELLED | &quot;CANCELLED&quot;
COMPLETED | &quot;COMPLETED&quot;



