
# DetailedShippingTimeType

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**minimumHours** | **Long** | The minimum time, in hours, that the item will likely be shipped after the order has been placed. |  [optional]
**maximumHours** | **Long** | The maximum time, in hours, that the item will likely be shipped after the order has been placed. |  [optional]
**availableDate** | [**BigDecimal**](BigDecimal.md) | The date when the item will be available for shipping. Only displayed for items that are not currently available for shipping. |  [optional]
**availabilityType** | [**AvailabilityTypeEnum**](#AvailabilityTypeEnum) | Indicates whether the item is available for shipping now, or on a known or an unknown date in the future. If known, the availableDate property indicates the date that the item will be available for shipping. Possible values: NOW, FUTURE_WITHOUT_DATE, FUTURE_WITH_DATE. |  [optional]


<a name="AvailabilityTypeEnum"></a>
## Enum: AvailabilityTypeEnum
Name | Value
---- | -----
NOW | &quot;NOW&quot;
FUTURE_WITHOUT_DATE | &quot;FUTURE_WITHOUT_DATE&quot;
FUTURE_WITH_DATE | &quot;FUTURE_WITH_DATE&quot;



