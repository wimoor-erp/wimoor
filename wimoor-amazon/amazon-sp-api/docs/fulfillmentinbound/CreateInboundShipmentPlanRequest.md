
# CreateInboundShipmentPlanRequest

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**shipFromAddress** | [**Address**](Address.md) | The address from which the inbound shipment will be sent. | 
**labelPrepPreference** | [**LabelPrepPreference**](LabelPrepPreference.md) | The seller&#39;s preference for label preparation for an inbound shipment. | 
**shipToCountryCode** | **String** | The two-character country code for the country where the inbound shipment is to be sent.  Note: Not required. Specifying both ShipToCountryCode and ShipToCountrySubdivisionCode returns an error.   Values:   ShipToCountryCode values for North America:  * CA – Canada  * MX - Mexico  * US - United States  ShipToCountryCode values for MCI sellers in Europe:  * DE – Germany  * ES – Spain  * FR – France  * GB – United Kingdom  * IT – Italy  Default: The country code for the seller&#39;s home marketplace. |  [optional]
**shipToCountrySubdivisionCode** | **String** | The two-character country code, followed by a dash and then up to three characters that represent the subdivision of the country where the inbound shipment is to be sent. For example, \&quot;IN-MH\&quot;. In full ISO 3166-2 format.  Note: Not required. Specifying both ShipToCountryCode and ShipToCountrySubdivisionCode returns an error. |  [optional]
**inboundShipmentPlanRequestItems** | [**InboundShipmentPlanRequestItemList**](InboundShipmentPlanRequestItemList.md) |  | 



