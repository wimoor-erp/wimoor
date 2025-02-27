
# RestrictedResource

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**method** | [**MethodEnum**](#MethodEnum) | The HTTP method in the restricted resource. | 
**path** | **String** | The path in the restricted resource. Here are some path examples: - &#x60;&#x60;&#x60;/orders/v0/orders&#x60;&#x60;&#x60;. For getting an RDT for the getOrders operation of the Orders API. For bulk orders. - &#x60;&#x60;&#x60;/orders/v0/orders/123-1234567-1234567&#x60;&#x60;&#x60;. For getting an RDT for the getOrder operation of the Orders API. For a specific order. - &#x60;&#x60;&#x60;/orders/v0/orders/123-1234567-1234567/orderItems&#x60;&#x60;&#x60;. For getting an RDT for the getOrderItems operation of the Orders API. For the order items in a specific order. - &#x60;&#x60;&#x60;/mfn/v0/shipments/FBA1234ABC5D&#x60;&#x60;&#x60;. For getting an RDT for the getShipment operation of the Shipping API. For a specific shipment. - &#x60;&#x60;&#x60;/mfn/v0/shipments/{shipmentId}&#x60;&#x60;&#x60;. For getting an RDT for the getShipment operation of the Shipping API. For any of a selling partner&#39;s shipments that you specify when you call the getShipment operation. | 
**dataElements** | **List&lt;String&gt;** | Indicates the type of Personally Identifiable Information requested. This parameter is required only when getting an RDT for use with the getOrder, getOrders, or getOrderItems operation of the Orders API. For more information, see the [Tokens API Use Case Guide](doc:tokens-api-use-case-guide). Possible values include: - **buyerInfo**. On the order level this includes general identifying information about the buyer and tax-related information. On the order item level this includes gift wrap information and custom order information, if available. - **shippingAddress**. This includes information for fulfilling orders. - **buyerTaxInformation**. This includes information for issuing tax invoices. |  [optional]


<a name="MethodEnum"></a>
## Enum: MethodEnum
Name | Value
---- | -----
GET | &quot;GET&quot;
PUT | &quot;PUT&quot;
POST | &quot;POST&quot;
DELETE | &quot;DELETE&quot;



