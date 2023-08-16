
# CreateShipmentRequest

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**shipmentRequestDetails** | [**ShipmentRequestDetails**](ShipmentRequestDetails.md) | Shipment information required for creating a shipment. | 
**shippingServiceId** | **String** |  | 
**shippingServiceOfferId** | **String** | Identifies a shipping service order made by a carrier. |  [optional]
**hazmatType** | [**HazmatType**](HazmatType.md) | Hazardous materials options for a package. Consult the terms and conditions for each carrier for more information about hazardous materials. |  [optional]
**labelFormatOption** | [**LabelFormatOptionRequest**](LabelFormatOptionRequest.md) |  |  [optional]
**shipmentLevelSellerInputsList** | [**AdditionalSellerInputsList**](AdditionalSellerInputsList.md) | A list of additional seller inputs required to ship this shipment. |  [optional]



