
# ShippingService

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**shippingServiceName** | **String** | A plain text representation of a carrier&#39;s shipping service. For example, \&quot;UPS Ground\&quot; or \&quot;FedEx Standard Overnight\&quot;.  | 
**carrierName** | **String** | The name of the carrier. | 
**shippingServiceId** | **String** |  | 
**shippingServiceOfferId** | **String** | An Amazon-defined shipping service offer identifier. | 
**shipDate** | **String** | The date that the carrier will ship the package. | 
**earliestEstimatedDeliveryDate** | **String** | The earliest date by which the shipment will be delivered. |  [optional]
**latestEstimatedDeliveryDate** | **String** | The latest date by which the shipment will be delivered. |  [optional]
**rate** | [**CurrencyAmount**](CurrencyAmount.md) | The amount that the carrier will charge for the shipment. | 
**shippingServiceOptions** | [**ShippingServiceOptions**](ShippingServiceOptions.md) | Extra services offered by the carrier. | 
**availableShippingServiceOptions** | [**AvailableShippingServiceOptions**](AvailableShippingServiceOptions.md) |  |  [optional]
**availableLabelFormats** | [**LabelFormatList**](LabelFormatList.md) |  |  [optional]
**availableFormatOptionsForLabel** | [**AvailableFormatOptionsForLabelList**](AvailableFormatOptionsForLabelList.md) |  |  [optional]
**requiresAdditionalSellerInputs** | **Boolean** | When true, additional seller inputs are required. | 



