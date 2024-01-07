
# ShipmentRequestDetails

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**amazonOrderId** | **String** | An Amazon-defined order identifier in 3-7-7 format. | 
**sellerOrderId** | **String** | A seller-defined order identifier. |  [optional]
**itemList** | [**ItemList**](ItemList.md) |  | 
**shipFromAddress** | [**Address**](Address.md) | The address of the sender. | 
**packageDimensions** | [**PackageDimensions**](PackageDimensions.md) | The package dimensions. | 
**weight** | [**Weight**](Weight.md) | The package weight. | 
**mustArriveByDate** | **String** | The date by which the package must arrive to keep the promise to the customer, in ISO 8601 datetime format. If MustArriveByDate is specified, only shipping service offers that can be delivered by that date are returned. |  [optional]
**shipDate** | **String** | When used in a request, this is the date and time that the seller wants to ship the package. When used in a response, this is the date and time that the package can be shipped by the indicated method. |  [optional]
**shippingServiceOptions** | [**ShippingServiceOptions**](ShippingServiceOptions.md) | Extra services offered by the carrier. | 
**labelCustomization** | [**LabelCustomization**](LabelCustomization.md) | Label customization options. |  [optional]



