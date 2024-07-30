
# PackageDetail

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**packageReferenceId** | **String** |  | 
**carrierCode** | **String** | Identifies the carrier that will deliver the package. This field is required for all marketplaces, see [reference](https://developer-docs.amazon.com/sp-api/changelog/carriercode-value-required-in-shipment-confirmations-for-br-mx-ca-sg-au-in-jp-marketplaces). | 
**carrierName** | **String** | Carrier Name that will deliver the package. Required when carrierCode is \&quot;Others\&quot;  |  [optional]
**shippingMethod** | **String** | Ship method to be used for shipping the order. |  [optional]
**trackingNumber** | **String** | The tracking number used to obtain tracking and delivery information. | 
**shipDate** | [**OffsetDateTime**](OffsetDateTime.md) | The shipping date for the package. Must be in &lt;a href&#x3D;&#39;https://developer-docs.amazon.com/sp-api/docs/iso-8601&#39;&gt;ISO 8601&lt;/a&gt; date/time format. | 
**shipFromSupplySourceId** | **String** | The unique identifier of the supply source. |  [optional]
**orderItems** | [**ConfirmShipmentOrderItemsList**](ConfirmShipmentOrderItemsList.md) | The list of order items and quantities to be updated. | 



