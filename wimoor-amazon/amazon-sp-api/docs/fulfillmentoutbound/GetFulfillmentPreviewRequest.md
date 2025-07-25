
# GetFulfillmentPreviewRequest

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**marketplaceId** | **String** | The marketplace the fulfillment order is placed against. |  [optional]
**address** | [**Address**](Address.md) | The destination address for the fulfillment order preview. | 
**items** | [**GetFulfillmentPreviewItemList**](GetFulfillmentPreviewItemList.md) | Identifying information and quantity information for the items in the fulfillment order preview. | 
**shippingSpeedCategories** | [**ShippingSpeedCategoryList**](ShippingSpeedCategoryList.md) | A list of shipping methods used for creating fulfillment order previews.  Possible values:  * Standard - Standard shipping method. * Expedited - Expedited shipping method. * Priority - Priority shipping method. * ScheduledDelivery - Scheduled Delivery shipping method. Note: Shipping method service level agreements vary by marketplace. Sellers should see the Seller Central website in their marketplace for shipping method service level agreements and fulfillment fees. |  [optional]
**includeCODFulfillmentPreview** | **Boolean** | Specifies whether to return fulfillment order previews that are for COD (Cash On Delivery).  Possible values:  * true - Returns all fulfillment order previews (both for COD and not for COD). * false - Returns only fulfillment order previews that are not for COD. |  [optional]
**includeDeliveryWindows** | **Boolean** | Specifies whether to return the ScheduledDeliveryInfo response object, which contains the available delivery windows for a Scheduled Delivery. The ScheduledDeliveryInfo response object can only be returned for fulfillment order previews with ShippingSpeedCategories &#x3D; ScheduledDelivery. |  [optional]
**featureConstraints** | [**List&lt;FeatureSettings&gt;**](FeatureSettings.md) | A list of features and their fulfillment policies to apply to the order. |  [optional]



