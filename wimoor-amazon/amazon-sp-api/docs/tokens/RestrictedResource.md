
# RestrictedResource

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**method** | [**MethodEnum**](#MethodEnum) | The HTTP method used with the restricted resource. | 
**path** | **String** | The path from a restricted operation. This could be:  - A specific path containing a seller&#39;s order ID, for example &#x60;&#x60;&#x60;/orders/v0/orders/902-3159896-1390916/address&#x60;&#x60;&#x60;.  - A generic path that does not contain a seller&#39;s order ID, for example&#x60;&#x60;&#x60;/orders/v0/orders/{orderId}/address&#x60;&#x60;&#x60;). | 


<a name="MethodEnum"></a>
## Enum: MethodEnum
Name | Value
---- | -----
GET | &quot;GET&quot;
PUT | &quot;PUT&quot;
POST | &quot;POST&quot;
DELETE | &quot;DELETE&quot;



