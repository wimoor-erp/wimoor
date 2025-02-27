
# FixedSlotCapacityQuery

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**capacityTypes** | [**List&lt;CapacityType&gt;**](CapacityType.md) | An array of capacity types which are being requested. Default value is &#x60;[SCHEDULED_CAPACITY]&#x60;. |  [optional]
**slotDuration** | [**BigDecimal**](BigDecimal.md) | Size in which slots are being requested. This value should be a multiple of 5 and fall in the range: 5 &lt;&#x3D; &#x60;slotDuration&#x60; &lt;&#x3D; 360. |  [optional]
**startDateTime** | [**OffsetDateTime**](OffsetDateTime.md) | Start date time from which the capacity slots are being requested in ISO 8601 format. | 
**endDateTime** | [**OffsetDateTime**](OffsetDateTime.md) | End date time up to which the capacity slots are being requested in ISO 8601 format. | 



