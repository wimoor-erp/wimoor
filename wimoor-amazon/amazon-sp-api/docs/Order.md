
# Order

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**amazonOrderId** | **String** | An Amazon-defined order identifier, in 3-7-7 format. | 
**sellerOrderId** | **String** | A seller-defined order identifier. |  [optional]
**purchaseDate** | **String** | The date when the order was created. | 
**lastUpdateDate** | **String** | The date when the order was last updated.  __Note__: LastUpdateDate is returned with an incorrect date for orders that were last updated before 2009-04-01. | 
**orderStatus** | [**OrderStatusEnum**](#OrderStatusEnum) | The current order status. | 
**fulfillmentChannel** | [**FulfillmentChannelEnum**](#FulfillmentChannelEnum) | Whether the order was fulfilled by Amazon (AFN) or by the seller (MFN). |  [optional]
**salesChannel** | **String** | The sales channel of the first item in the order. |  [optional]
**orderChannel** | **String** | The order channel of the first item in the order. |  [optional]
**shipServiceLevel** | **String** | The shipment service level of the order. |  [optional]
**orderTotal** | [**Money**](Money.md) | The total charge for this order. |  [optional]
**numberOfItemsShipped** | **Integer** | The number of items shipped. |  [optional]
**numberOfItemsUnshipped** | **Integer** | The number of items unshipped. |  [optional]
**paymentExecutionDetail** | [**PaymentExecutionDetailItemList**](PaymentExecutionDetailItemList.md) | Information about sub-payment methods for a Cash On Delivery (COD) order.  __Note__: For a COD order that is paid for using one sub-payment method, one PaymentExecutionDetailItem object is returned, with PaymentExecutionDetailItem/PaymentMethod &#x3D; COD. For a COD order that is paid for using multiple sub-payment methods, two or more PaymentExecutionDetailItem objects are returned. |  [optional]
**paymentMethod** | [**PaymentMethodEnum**](#PaymentMethodEnum) | The payment method for the order. This property is limited to Cash On Delivery (COD) and Convenience Store (CVS) payment methods. Unless you need the specific COD payment information provided by the PaymentExecutionDetailItem object, we recommend using the PaymentMethodDetails property to get payment method information. |  [optional]
**paymentMethodDetails** | [**PaymentMethodDetailItemList**](PaymentMethodDetailItemList.md) | A list of payment methods for the order. |  [optional]
**marketplaceId** | **String** | The identifier for the marketplace where the order was placed. |  [optional]
**shipmentServiceLevelCategory** | **String** | The shipment service level category of the order.  Possible values: Expedited, FreeEconomy, NextDay, Priority, SameDay, SecondDay, Scheduled, Standard. |  [optional]
**easyShipShipmentStatus** | [**EasyShipShipmentStatus**](EasyShipShipmentStatus.md) | The status of the Amazon Easy Ship order. This property is included only for Amazon Easy Ship orders. |  [optional]
**cbaDisplayableShippingLabel** | **String** | Custom ship label for Checkout by Amazon (CBA). |  [optional]
**orderType** | [**OrderTypeEnum**](#OrderTypeEnum) | The type of the order. |  [optional]
**earliestShipDate** | **String** | The start of the time period within which you have committed to ship the order. In &lt;a href&#x3D;&#39;https://developer-docs.amazon.com/sp-api/docs/iso-8601&#39;&gt;ISO 8601&lt;/a&gt; date time format. Returned only for seller-fulfilled orders.  __Note__: EarliestShipDate might not be returned for orders placed before February 1, 2013. |  [optional]
**latestShipDate** | **String** | The end of the time period within which you have committed to ship the order. In &lt;a href&#x3D;&#39;https://developer-docs.amazon.com/sp-api/docs/iso-8601&#39;&gt;ISO 8601&lt;/a&gt; date time format. Returned only for seller-fulfilled orders.  __Note__: LatestShipDate might not be returned for orders placed before February 1, 2013. |  [optional]
**earliestDeliveryDate** | **String** | The start of the time period within which you have committed to fulfill the order. In &lt;a href&#x3D;&#39;https://developer-docs.amazon.com/sp-api/docs/iso-8601&#39;&gt;ISO 8601&lt;/a&gt; date time format. Returned only for seller-fulfilled orders. |  [optional]
**latestDeliveryDate** | **String** | The end of the time period within which you have committed to fulfill the order. In &lt;a href&#x3D;&#39;https://developer-docs.amazon.com/sp-api/docs/iso-8601&#39;&gt;ISO 8601&lt;/a&gt; date time format. Returned only for seller-fulfilled orders that do not have a PendingAvailability, Pending, or Canceled status. |  [optional]
**isBusinessOrder** | **Boolean** | When true, the order is an Amazon Business order. An Amazon Business order is an order where the buyer is a Verified Business Buyer. |  [optional]
**isPrime** | **Boolean** | When true, the order is a seller-fulfilled Amazon Prime order. |  [optional]
**isPremiumOrder** | **Boolean** | When true, the order has a Premium Shipping Service Level Agreement. For more information about Premium Shipping orders, see \&quot;Premium Shipping Options\&quot; in the Seller Central Help for your marketplace. |  [optional]
**isGlobalExpressEnabled** | **Boolean** | When true, the order is a GlobalExpress order. |  [optional]
**replacedOrderId** | **String** | The order ID value for the order that is being replaced. Returned only if IsReplacementOrder &#x3D; true. |  [optional]
**isReplacementOrder** | **Boolean** | When true, this is a replacement order. |  [optional]
**promiseResponseDueDate** | **String** | Indicates the date by which the seller must respond to the buyer with an estimated ship date. Returned only for Sourcing on Demand orders. |  [optional]
**isEstimatedShipDateSet** | **Boolean** | When true, the estimated ship date is set for the order. Returned only for Sourcing on Demand orders. |  [optional]
**isSoldByAB** | **Boolean** | When true, the item within this order was bought and re-sold by Amazon Business EU SARL (ABEU). By buying and instantly re-selling your items, ABEU becomes the seller of record, making your inventory available for sale to customers who would not otherwise purchase from a third-party seller. |  [optional]
**isIBA** | **Boolean** | When true, the item within this order was bought and re-sold by Amazon Business EU SARL (ABEU). By buying and instantly re-selling your items, ABEU becomes the seller of record, making your inventory available for sale to customers who would not otherwise purchase from a third-party seller. |  [optional]
**defaultShipFromLocationAddress** | [**Address**](Address.md) | The recommended location for the seller to ship the items from. It is calculated at checkout. The seller may or may not choose to ship from this location. |  [optional]
**buyerInvoicePreference** | [**BuyerInvoicePreferenceEnum**](#BuyerInvoicePreferenceEnum) | The buyer&#39;s invoicing preference. Available only in the TR marketplace. |  [optional]
**buyerTaxInformation** | [**BuyerTaxInformation**](BuyerTaxInformation.md) | Contains the business invoice tax information. |  [optional]
**fulfillmentInstruction** | [**FulfillmentInstruction**](FulfillmentInstruction.md) | Contains the instructions about the fulfillment like where should it be fulfilled from. |  [optional]
**isISPU** | **Boolean** | When true, this order is marked to be picked up from a store rather than delivered. |  [optional]
**isAccessPointOrder** | **Boolean** | When true, this order is marked to be delivered to an Access Point. The access location is chosen by the customer. Access Points include Amazon Hub Lockers, Amazon Hub Counters, and pickup points operated by carriers. |  [optional]
**marketplaceTaxInfo** | [**MarketplaceTaxInfo**](MarketplaceTaxInfo.md) | Tax information about the marketplace. |  [optional]
**sellerDisplayName** | **String** | The seller’s friendly name registered in the marketplace. |  [optional]
**shippingAddress** | [**Address**](Address.md) |  |  [optional]
**buyerInfo** | [**BuyerInfo**](BuyerInfo.md) |  |  [optional]
**automatedShippingSettings** | [**AutomatedShippingSettings**](AutomatedShippingSettings.md) | Contains information regarding the Shipping Settings Automaton program, such as whether the order&#39;s shipping settings were generated automatically, and what those settings are. |  [optional]
**hasRegulatedItems** | **Boolean** | Whether the order contains regulated items which may require additional approval steps before being fulfilled. |  [optional]
**electronicInvoiceStatus** | [**ElectronicInvoiceStatus**](ElectronicInvoiceStatus.md) | The status of the electronic invoice. |  [optional]


<a name="OrderStatusEnum"></a>
## Enum: OrderStatusEnum
Name | Value
---- | -----
PENDING | &quot;Pending&quot;
UNSHIPPED | &quot;Unshipped&quot;
PARTIALLYSHIPPED | &quot;PartiallyShipped&quot;
SHIPPED | &quot;Shipped&quot;
CANCELED | &quot;Canceled&quot;
UNFULFILLABLE | &quot;Unfulfillable&quot;
INVOICEUNCONFIRMED | &quot;InvoiceUnconfirmed&quot;
PENDINGAVAILABILITY | &quot;PendingAvailability&quot;


<a name="FulfillmentChannelEnum"></a>
## Enum: FulfillmentChannelEnum
Name | Value
---- | -----
MFN | &quot;MFN&quot;
AFN | &quot;AFN&quot;


<a name="PaymentMethodEnum"></a>
## Enum: PaymentMethodEnum
Name | Value
---- | -----
COD | &quot;COD&quot;
CVS | &quot;CVS&quot;
OTHER | &quot;Other&quot;


<a name="OrderTypeEnum"></a>
## Enum: OrderTypeEnum
Name | Value
---- | -----
STANDARDORDER | &quot;StandardOrder&quot;
LONGLEADTIMEORDER | &quot;LongLeadTimeOrder&quot;
PREORDER | &quot;Preorder&quot;
BACKORDER | &quot;BackOrder&quot;
SOURCINGONDEMANDORDER | &quot;SourcingOnDemandOrder&quot;


<a name="BuyerInvoicePreferenceEnum"></a>
## Enum: BuyerInvoicePreferenceEnum
Name | Value
---- | -----
INDIVIDUAL | &quot;INDIVIDUAL&quot;
BUSINESS | &quot;BUSINESS&quot;



