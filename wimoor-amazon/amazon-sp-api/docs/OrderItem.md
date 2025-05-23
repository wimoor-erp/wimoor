
# OrderItem

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**ASIN** | **String** | The Amazon Standard Identification Number (ASIN) of the item. | 
**sellerSKU** | **String** | The seller stock keeping unit (SKU) of the item. |  [optional]
**orderItemId** | **String** | An Amazon-defined order item identifier. | 
**associatedItems** | [**List&lt;AssociatedItem&gt;**](AssociatedItem.md) | A list of associated items that a customer has purchased with a product. For example, a tire installation service purchased with tires. |  [optional]
**title** | **String** | The name of the item. |  [optional]
**quantityOrdered** | **Integer** | The number of items in the order.  | 
**quantityShipped** | **Integer** | The number of items shipped. |  [optional]
**productInfo** | [**ProductInfoDetail**](ProductInfoDetail.md) | Product information for the item. |  [optional]
**pointsGranted** | [**PointsGrantedDetail**](PointsGrantedDetail.md) | The number and value of Amazon Points granted with the purchase of an item. |  [optional]
**itemPrice** | [**Money**](Money.md) | The selling price of the order item. Note that an order item is an item and a quantity. This means that the value of ItemPrice is equal to the selling price of the item multiplied by the quantity ordered. Note that ItemPrice excludes ShippingPrice and GiftWrapPrice. |  [optional]
**shippingPrice** | [**Money**](Money.md) | The shipping price of the item. |  [optional]
**itemTax** | [**Money**](Money.md) | The tax on the item price. |  [optional]
**shippingTax** | [**Money**](Money.md) | The tax on the shipping price. |  [optional]
**shippingDiscount** | [**Money**](Money.md) | The discount on the shipping price. |  [optional]
**shippingDiscountTax** | [**Money**](Money.md) | The tax on the discount on the shipping price. |  [optional]
**promotionDiscount** | [**Money**](Money.md) | The total of all promotional discounts in the offer. |  [optional]
**promotionDiscountTax** | [**Money**](Money.md) | The tax on the total of all promotional discounts in the offer. |  [optional]
**promotionIds** | [**PromotionIdList**](PromotionIdList.md) |  |  [optional]
**coDFee** | [**Money**](Money.md) | The fee charged for COD service. |  [optional]
**coDFeeDiscount** | [**Money**](Money.md) | The discount on the COD fee. |  [optional]
**isGift** | **Boolean** | When true, the item is a gift. |  [optional]
**conditionNote** | **String** | The condition of the item as described by the seller. |  [optional]
**conditionId** | **String** | The condition of the item.  Possible values: New, Used, Collectible, Refurbished, Preorder, Club. |  [optional]
**conditionSubtypeId** | **String** | The subcondition of the item.  Possible values: New, Mint, Very Good, Good, Acceptable, Poor, Club, OEM, Warranty, Refurbished Warranty, Refurbished, Open Box, Any, Other. |  [optional]
**scheduledDeliveryStartDate** | **String** | The start date of the scheduled delivery window in the time zone of the order destination. In &lt;a href&#x3D;&#39;https://developer-docs.amazon.com/sp-api/docs/iso-8601&#39;&gt;ISO 8601&lt;/a&gt; date time format. |  [optional]
**scheduledDeliveryEndDate** | **String** | The end date of the scheduled delivery window in the time zone of the order destination. In &lt;a href&#x3D;&#39;https://developer-docs.amazon.com/sp-api/docs/iso-8601&#39;&gt;ISO 8601&lt;/a&gt; date time format. |  [optional]
**priceDesignation** | **String** | Indicates that the selling price is a special price that is available only for Amazon Business orders. For more information about the Amazon Business Seller Program, see the [Amazon Business website](https://www.amazon.com/b2b/info/amazon-business).   Possible values: BusinessPrice - A special price that is available only for Amazon Business orders. |  [optional]
**taxCollection** | [**TaxCollection**](TaxCollection.md) | Information about withheld taxes. |  [optional]
**serialNumberRequired** | **Boolean** | When true, the product type for this item has a serial number.  Returned only for Amazon Easy Ship orders. |  [optional]
**isTransparency** | **Boolean** | When true, the ASIN is enrolled in Transparency and the Transparency serial number that needs to be submitted can be determined by the following:  **1D or 2D Barcode:** This has a **T** logo. Submit either the 29-character alpha-numeric identifier beginning with **AZ** or **ZA**, or the 38-character Serialized Global Trade Item Number (SGTIN). **2D Barcode SN:** Submit the 7- to 20-character serial number barcode, which likely has the prefix **SN**. The serial number will be applied to the same side of the packaging as the GTIN (UPC/EAN/ISBN) barcode. **QR code SN:** Submit the URL that the QR code generates. |  [optional]
**iossNumber** | **String** | The IOSS number for the marketplace. Sellers shipping to the European Union (EU) from outside of the EU must provide this IOSS number to their carrier when Amazon has collected the VAT on the sale. |  [optional]
**storeChainStoreId** | **String** | The store chain store identifier. Linked to a specific store in a store chain. |  [optional]
**deemedResellerCategory** | [**DeemedResellerCategoryEnum**](#DeemedResellerCategoryEnum) | The category of deemed reseller. This applies to selling partners that are not based in the EU and is used to help them meet the VAT Deemed Reseller tax laws in the EU and UK. |  [optional]
**buyerInfo** | [**ItemBuyerInfo**](ItemBuyerInfo.md) |  |  [optional]
**buyerRequestedCancel** | [**BuyerRequestedCancel**](BuyerRequestedCancel.md) | Information about whether or not a buyer requested cancellation. |  [optional]
**serialNumbers** | **List&lt;String&gt;** | A list of serial numbers for electronic products that are shipped to customers. Returned for FBA orders only. |  [optional]
**substitutionPreferences** | [**SubstitutionPreferences**](SubstitutionPreferences.md) | Substitution preferences for the order item. This is an optional field and will only be present if seller supports substitutions like in case of some grocery sellers. |  [optional]
**measurement** | [**Measurement**](Measurement.md) | Measurement information for the order item. |  [optional]


<a name="DeemedResellerCategoryEnum"></a>
## Enum: DeemedResellerCategoryEnum
Name | Value
---- | -----
IOSS | &quot;IOSS&quot;
UOSS | &quot;UOSS&quot;



