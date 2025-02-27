
# AccessPoint

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**accessPointId** | **String** |  |  [optional]
**name** | **String** | Name of entity (store/hub etc) where this access point is located |  [optional]
**timezone** | **String** | Timezone of access point |  [optional]
**type** | [**AccessPointType**](AccessPointType.md) |  |  [optional]
**accessibilityAttributes** | [**AccessibilityAttributes**](AccessibilityAttributes.md) |  |  [optional]
**address** | [**Address**](Address.md) |  |  [optional]
**exceptionOperatingHours** | [**List&lt;ExceptionOperatingHours&gt;**](ExceptionOperatingHours.md) |  |  [optional]
**assistanceType** | [**AssistanceTypeEnum**](#AssistanceTypeEnum) |  |  [optional]
**score** | **String** | The score of access point, based on proximity to postal code and sorting preference. This can be used to sort access point results on shipper&#39;s end. |  [optional]
**standardOperatingHours** | [**DayOfWeekTimeMap**](DayOfWeekTimeMap.md) |  |  [optional]


<a name="AssistanceTypeEnum"></a>
## Enum: AssistanceTypeEnum
Name | Value
---- | -----
STAFF_ASSISTED | &quot;STAFF_ASSISTED&quot;
SELF_ASSISTED | &quot;SELF_ASSISTED&quot;



