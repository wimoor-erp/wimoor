
# Poa

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**appointmentTime** | [**AppointmentTime**](AppointmentTime.md) | The time of the appointment window. |  [optional]
**technicians** | [**List&lt;Technician&gt;**](Technician.md) | A list of technicians. |  [optional]
**uploadingTechnician** | **String** | The identifier of the technician who uploaded the POA. |  [optional]
**uploadTime** | [**OffsetDateTime**](OffsetDateTime.md) | The date and time when the POA was uploaded, in ISO 8601 format. |  [optional]
**poaType** | [**PoaTypeEnum**](#PoaTypeEnum) | The type of POA uploaded. |  [optional]


<a name="PoaTypeEnum"></a>
## Enum: PoaTypeEnum
Name | Value
---- | -----
NO_SIGNATURE_DUMMY_POS | &quot;NO_SIGNATURE_DUMMY_POS&quot;
CUSTOMER_SIGNATURE | &quot;CUSTOMER_SIGNATURE&quot;
DUMMY_RECEIPT | &quot;DUMMY_RECEIPT&quot;
POA_RECEIPT | &quot;POA_RECEIPT&quot;



