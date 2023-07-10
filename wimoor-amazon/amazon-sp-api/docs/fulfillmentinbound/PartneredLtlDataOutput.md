
# PartneredLtlDataOutput

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**contact** | [**Contact**](Contact.md) | Contact information for the person in the seller&#39;s organization who is responsible for the shipment. Used by the carrier if they have questions about the shipment. | 
**boxCount** | **Integer** | The number of boxes in the shipment. | 
**sellerFreightClass** | [**SellerFreightClass**](SellerFreightClass.md) |  |  [optional]
**freightReadyDate** | **String** | The date that the shipment will be ready to be picked up by the carrier. Must be in YYYY-MM-DD format. | 
**palletList** | [**PalletList**](PalletList.md) |  | 
**totalWeight** | [**Weight**](Weight.md) | The total weight of the shipment. | 
**sellerDeclaredValue** | [**Amount**](Amount.md) | Your declaration of the total value of the inventory in the shipment. |  [optional]
**amazonCalculatedValue** | [**Amount**](Amount.md) | Estimate by Amazon of the total value of the inventory in the shipment. |  [optional]
**previewPickupDate** | **String** | The estimated date that the shipment will be picked up by the carrier, in YYYY-MM-DD format. | 
**previewDeliveryDate** | **String** | The estimated date that the shipment will be delivered to an Amazon fulfillment center, in YYYY-MM-DD format. | 
**previewFreightClass** | [**SellerFreightClass**](SellerFreightClass.md) | The freight class of the shipment as estimated by Amazon if you did not include a freight class when you called the putTransportDetails operation. | 
**amazonReferenceId** | **String** | A unique identifier created by Amazon that identifies this Amazon-partnered, Less Than Truckload/Full Truckload (LTL/FTL) shipment. | 
**isBillOfLadingAvailable** | **Boolean** | Indicates whether the bill of lading for the shipment is available. | 
**partneredEstimate** | [**PartneredEstimate**](PartneredEstimate.md) | The estimated shipping cost using an Amazon-partnered carrier. |  [optional]
**carrierName** | **String** | The carrier for the inbound shipment. | 



