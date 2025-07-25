# MessagingApi

All URIs are relative to *https://sellingpartnerapi-na.amazon.com*

Method | HTTP request | Description
------------- | ------------- | -------------
[**confirmCustomizationDetails**](MessagingApi.md#confirmCustomizationDetails) | **POST** /messaging/v1/orders/{amazonOrderId}/messages/confirmCustomizationDetails | 
[**createAmazonMotors**](MessagingApi.md#createAmazonMotors) | **POST** /messaging/v1/orders/{amazonOrderId}/messages/amazonMotors | 
[**createConfirmDeliveryDetails**](MessagingApi.md#createConfirmDeliveryDetails) | **POST** /messaging/v1/orders/{amazonOrderId}/messages/confirmDeliveryDetails | 
[**createConfirmOrderDetails**](MessagingApi.md#createConfirmOrderDetails) | **POST** /messaging/v1/orders/{amazonOrderId}/messages/confirmOrderDetails | 
[**createConfirmServiceDetails**](MessagingApi.md#createConfirmServiceDetails) | **POST** /messaging/v1/orders/{amazonOrderId}/messages/confirmServiceDetails | 
[**createDigitalAccessKey**](MessagingApi.md#createDigitalAccessKey) | **POST** /messaging/v1/orders/{amazonOrderId}/messages/digitalAccessKey | 
[**createLegalDisclosure**](MessagingApi.md#createLegalDisclosure) | **POST** /messaging/v1/orders/{amazonOrderId}/messages/legalDisclosure | 
[**createNegativeFeedbackRemoval**](MessagingApi.md#createNegativeFeedbackRemoval) | **POST** /messaging/v1/orders/{amazonOrderId}/messages/negativeFeedbackRemoval | 
[**createUnexpectedProblem**](MessagingApi.md#createUnexpectedProblem) | **POST** /messaging/v1/orders/{amazonOrderId}/messages/unexpectedProblem | 
[**createWarranty**](MessagingApi.md#createWarranty) | **POST** /messaging/v1/orders/{amazonOrderId}/messages/warranty | 
[**getAttributes**](MessagingApi.md#getAttributes) | **GET** /messaging/v1/orders/{amazonOrderId}/attributes | 
[**getMessagingActionsForOrder**](MessagingApi.md#getMessagingActionsForOrder) | **GET** /messaging/v1/orders/{amazonOrderId} | 


<a name="confirmCustomizationDetails"></a>
# **confirmCustomizationDetails**
> CreateConfirmCustomizationDetailsResponse confirmCustomizationDetails(amazonOrderId, marketplaceIds, body)



Sends a message asking a buyer to provide or verify customization details such as name spelling, images, initials, etc.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 1 | 5 |  For more information, see \&quot;Usage Plans and Rate Limits\&quot; in the Selling Partner API documentation.

### Example
```java
// Import classes:
//import com.amazon.spapi.messaging.ApiException;
//import com.amazon.spapi.messaging.api.MessagingApi;


MessagingApi apiInstance = new MessagingApi();
String amazonOrderId = "amazonOrderId_example"; // String | An Amazon order identifier. This specifies the order for which a message is sent.
List<String> marketplaceIds = Arrays.asList("marketplaceIds_example"); // List<String> | A marketplace identifier. This specifies the marketplace in which the order was placed. Only one marketplace can be specified.
CreateConfirmCustomizationDetailsRequest body = new CreateConfirmCustomizationDetailsRequest(); // CreateConfirmCustomizationDetailsRequest | 
try {
    CreateConfirmCustomizationDetailsResponse result = apiInstance.confirmCustomizationDetails(amazonOrderId, marketplaceIds, body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling MessagingApi#confirmCustomizationDetails");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **amazonOrderId** | **String**| An Amazon order identifier. This specifies the order for which a message is sent. |
 **marketplaceIds** | [**List&lt;String&gt;**](String.md)| A marketplace identifier. This specifies the marketplace in which the order was placed. Only one marketplace can be specified. |
 **body** | [**CreateConfirmCustomizationDetailsRequest**](CreateConfirmCustomizationDetailsRequest.md)|  |

### Return type

[**CreateConfirmCustomizationDetailsResponse**](CreateConfirmCustomizationDetailsResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/hal+json

<a name="createAmazonMotors"></a>
# **createAmazonMotors**
> CreateAmazonMotorsResponse createAmazonMotors(amazonOrderId, marketplaceIds, body)



Sends a message to a buyer to provide details about an Amazon Motors order. This message can only be sent by Amazon Motors sellers.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 1 | 5 |  For more information, see \&quot;Usage Plans and Rate Limits\&quot; in the Selling Partner API documentation.

### Example
```java
// Import classes:
//import com.amazon.spapi.messaging.ApiException;
//import com.amazon.spapi.messaging.api.MessagingApi;


MessagingApi apiInstance = new MessagingApi();
String amazonOrderId = "amazonOrderId_example"; // String | An Amazon order identifier. This specifies the order for which a message is sent.
List<String> marketplaceIds = Arrays.asList("marketplaceIds_example"); // List<String> | A marketplace identifier. This specifies the marketplace in which the order was placed. Only one marketplace can be specified.
CreateAmazonMotorsRequest body = new CreateAmazonMotorsRequest(); // CreateAmazonMotorsRequest | 
try {
    CreateAmazonMotorsResponse result = apiInstance.createAmazonMotors(amazonOrderId, marketplaceIds, body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling MessagingApi#createAmazonMotors");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **amazonOrderId** | **String**| An Amazon order identifier. This specifies the order for which a message is sent. |
 **marketplaceIds** | [**List&lt;String&gt;**](String.md)| A marketplace identifier. This specifies the marketplace in which the order was placed. Only one marketplace can be specified. |
 **body** | [**CreateAmazonMotorsRequest**](CreateAmazonMotorsRequest.md)|  |

### Return type

[**CreateAmazonMotorsResponse**](CreateAmazonMotorsResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/hal+json

<a name="createConfirmDeliveryDetails"></a>
# **createConfirmDeliveryDetails**
> CreateConfirmDeliveryDetailsResponse createConfirmDeliveryDetails(amazonOrderId, marketplaceIds, body)



Sends a message to a buyer to arrange a delivery or to confirm contact information for making a delivery.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 1 | 5 |  For more information, see \&quot;Usage Plans and Rate Limits\&quot; in the Selling Partner API documentation.

### Example
```java
// Import classes:
//import com.amazon.spapi.messaging.ApiException;
//import com.amazon.spapi.messaging.api.MessagingApi;


MessagingApi apiInstance = new MessagingApi();
String amazonOrderId = "amazonOrderId_example"; // String | An Amazon order identifier. This specifies the order for which a message is sent.
List<String> marketplaceIds = Arrays.asList("marketplaceIds_example"); // List<String> | A marketplace identifier. This specifies the marketplace in which the order was placed. Only one marketplace can be specified.
CreateConfirmDeliveryDetailsRequest body = new CreateConfirmDeliveryDetailsRequest(); // CreateConfirmDeliveryDetailsRequest | 
try {
    CreateConfirmDeliveryDetailsResponse result = apiInstance.createConfirmDeliveryDetails(amazonOrderId, marketplaceIds, body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling MessagingApi#createConfirmDeliveryDetails");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **amazonOrderId** | **String**| An Amazon order identifier. This specifies the order for which a message is sent. |
 **marketplaceIds** | [**List&lt;String&gt;**](String.md)| A marketplace identifier. This specifies the marketplace in which the order was placed. Only one marketplace can be specified. |
 **body** | [**CreateConfirmDeliveryDetailsRequest**](CreateConfirmDeliveryDetailsRequest.md)|  |

### Return type

[**CreateConfirmDeliveryDetailsResponse**](CreateConfirmDeliveryDetailsResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/hal+json

<a name="createConfirmOrderDetails"></a>
# **createConfirmOrderDetails**
> CreateConfirmOrderDetailsResponse createConfirmOrderDetails(amazonOrderId, marketplaceIds, body)



Sends a message to ask a buyer an order-related question prior to shipping their order.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 1 | 5 |  For more information, see \&quot;Usage Plans and Rate Limits\&quot; in the Selling Partner API documentation.

### Example
```java
// Import classes:
//import com.amazon.spapi.messaging.ApiException;
//import com.amazon.spapi.messaging.api.MessagingApi;


MessagingApi apiInstance = new MessagingApi();
String amazonOrderId = "amazonOrderId_example"; // String | An Amazon order identifier. This specifies the order for which a message is sent.
List<String> marketplaceIds = Arrays.asList("marketplaceIds_example"); // List<String> | A marketplace identifier. This specifies the marketplace in which the order was placed. Only one marketplace can be specified.
CreateConfirmOrderDetailsRequest body = new CreateConfirmOrderDetailsRequest(); // CreateConfirmOrderDetailsRequest | 
try {
    CreateConfirmOrderDetailsResponse result = apiInstance.createConfirmOrderDetails(amazonOrderId, marketplaceIds, body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling MessagingApi#createConfirmOrderDetails");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **amazonOrderId** | **String**| An Amazon order identifier. This specifies the order for which a message is sent. |
 **marketplaceIds** | [**List&lt;String&gt;**](String.md)| A marketplace identifier. This specifies the marketplace in which the order was placed. Only one marketplace can be specified. |
 **body** | [**CreateConfirmOrderDetailsRequest**](CreateConfirmOrderDetailsRequest.md)|  |

### Return type

[**CreateConfirmOrderDetailsResponse**](CreateConfirmOrderDetailsResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/hal+json

<a name="createConfirmServiceDetails"></a>
# **createConfirmServiceDetails**
> CreateConfirmServiceDetailsResponse createConfirmServiceDetails(amazonOrderId, marketplaceIds, body)



Sends a message to contact a Home Service customer to arrange a service call or to gather information prior to a service call.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 1 | 5 |  For more information, see \&quot;Usage Plans and Rate Limits\&quot; in the Selling Partner API documentation.

### Example
```java
// Import classes:
//import com.amazon.spapi.messaging.ApiException;
//import com.amazon.spapi.messaging.api.MessagingApi;


MessagingApi apiInstance = new MessagingApi();
String amazonOrderId = "amazonOrderId_example"; // String | An Amazon order identifier. This specifies the order for which a message is sent.
List<String> marketplaceIds = Arrays.asList("marketplaceIds_example"); // List<String> | A marketplace identifier. This specifies the marketplace in which the order was placed. Only one marketplace can be specified.
CreateConfirmServiceDetailsRequest body = new CreateConfirmServiceDetailsRequest(); // CreateConfirmServiceDetailsRequest | 
try {
    CreateConfirmServiceDetailsResponse result = apiInstance.createConfirmServiceDetails(amazonOrderId, marketplaceIds, body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling MessagingApi#createConfirmServiceDetails");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **amazonOrderId** | **String**| An Amazon order identifier. This specifies the order for which a message is sent. |
 **marketplaceIds** | [**List&lt;String&gt;**](String.md)| A marketplace identifier. This specifies the marketplace in which the order was placed. Only one marketplace can be specified. |
 **body** | [**CreateConfirmServiceDetailsRequest**](CreateConfirmServiceDetailsRequest.md)|  |

### Return type

[**CreateConfirmServiceDetailsResponse**](CreateConfirmServiceDetailsResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/hal+json

<a name="createDigitalAccessKey"></a>
# **createDigitalAccessKey**
> CreateDigitalAccessKeyResponse createDigitalAccessKey(amazonOrderId, marketplaceIds, body)



Sends a message to a buyer to share a digital access key needed to utilize digital content in their order.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 1 | 5 |  For more information, see \&quot;Usage Plans and Rate Limits\&quot; in the Selling Partner API documentation.

### Example
```java
// Import classes:
//import com.amazon.spapi.messaging.ApiException;
//import com.amazon.spapi.messaging.api.MessagingApi;


MessagingApi apiInstance = new MessagingApi();
String amazonOrderId = "amazonOrderId_example"; // String | An Amazon order identifier. This specifies the order for which a message is sent.
List<String> marketplaceIds = Arrays.asList("marketplaceIds_example"); // List<String> | A marketplace identifier. This specifies the marketplace in which the order was placed. Only one marketplace can be specified.
CreateDigitalAccessKeyRequest body = new CreateDigitalAccessKeyRequest(); // CreateDigitalAccessKeyRequest | 
try {
    CreateDigitalAccessKeyResponse result = apiInstance.createDigitalAccessKey(amazonOrderId, marketplaceIds, body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling MessagingApi#createDigitalAccessKey");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **amazonOrderId** | **String**| An Amazon order identifier. This specifies the order for which a message is sent. |
 **marketplaceIds** | [**List&lt;String&gt;**](String.md)| A marketplace identifier. This specifies the marketplace in which the order was placed. Only one marketplace can be specified. |
 **body** | [**CreateDigitalAccessKeyRequest**](CreateDigitalAccessKeyRequest.md)|  |

### Return type

[**CreateDigitalAccessKeyResponse**](CreateDigitalAccessKeyResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/hal+json

<a name="createLegalDisclosure"></a>
# **createLegalDisclosure**
> CreateLegalDisclosureResponse createLegalDisclosure(amazonOrderId, marketplaceIds, body)



Sends a critical message that contains documents that a seller is legally obligated to provide to the buyer. This message should only be used to deliver documents that are required by law.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 1 | 5 |  For more information, see \&quot;Usage Plans and Rate Limits\&quot; in the Selling Partner API documentation.

### Example
```java
// Import classes:
//import com.amazon.spapi.messaging.ApiException;
//import com.amazon.spapi.messaging.api.MessagingApi;


MessagingApi apiInstance = new MessagingApi();
String amazonOrderId = "amazonOrderId_example"; // String | An Amazon order identifier. This specifies the order for which a message is sent.
List<String> marketplaceIds = Arrays.asList("marketplaceIds_example"); // List<String> | A marketplace identifier. This specifies the marketplace in which the order was placed. Only one marketplace can be specified.
CreateLegalDisclosureRequest body = new CreateLegalDisclosureRequest(); // CreateLegalDisclosureRequest | 
try {
    CreateLegalDisclosureResponse result = apiInstance.createLegalDisclosure(amazonOrderId, marketplaceIds, body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling MessagingApi#createLegalDisclosure");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **amazonOrderId** | **String**| An Amazon order identifier. This specifies the order for which a message is sent. |
 **marketplaceIds** | [**List&lt;String&gt;**](String.md)| A marketplace identifier. This specifies the marketplace in which the order was placed. Only one marketplace can be specified. |
 **body** | [**CreateLegalDisclosureRequest**](CreateLegalDisclosureRequest.md)|  |

### Return type

[**CreateLegalDisclosureResponse**](CreateLegalDisclosureResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/hal+json

<a name="createNegativeFeedbackRemoval"></a>
# **createNegativeFeedbackRemoval**
> CreateNegativeFeedbackRemovalResponse createNegativeFeedbackRemoval(amazonOrderId, marketplaceIds)



Sends a non-critical message that asks a buyer to remove their negative feedback. This message should only be sent after the seller has resolved the buyer&#39;s problem.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 1 | 5 |  For more information, see \&quot;Usage Plans and Rate Limits\&quot; in the Selling Partner API documentation.

### Example
```java
// Import classes:
//import com.amazon.spapi.messaging.ApiException;
//import com.amazon.spapi.messaging.api.MessagingApi;


MessagingApi apiInstance = new MessagingApi();
String amazonOrderId = "amazonOrderId_example"; // String | An Amazon order identifier. This specifies the order for which a message is sent.
List<String> marketplaceIds = Arrays.asList("marketplaceIds_example"); // List<String> | A marketplace identifier. This specifies the marketplace in which the order was placed. Only one marketplace can be specified.
try {
    CreateNegativeFeedbackRemovalResponse result = apiInstance.createNegativeFeedbackRemoval(amazonOrderId, marketplaceIds);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling MessagingApi#createNegativeFeedbackRemoval");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **amazonOrderId** | **String**| An Amazon order identifier. This specifies the order for which a message is sent. |
 **marketplaceIds** | [**List&lt;String&gt;**](String.md)| A marketplace identifier. This specifies the marketplace in which the order was placed. Only one marketplace can be specified. |

### Return type

[**CreateNegativeFeedbackRemovalResponse**](CreateNegativeFeedbackRemovalResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/hal+json

<a name="createUnexpectedProblem"></a>
# **createUnexpectedProblem**
> CreateUnexpectedProblemResponse createUnexpectedProblem(amazonOrderId, marketplaceIds, body)



Sends a critical message to a buyer that an unexpected problem was encountered affecting the completion of the order.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 1 | 5 |  For more information, see \&quot;Usage Plans and Rate Limits\&quot; in the Selling Partner API documentation.

### Example
```java
// Import classes:
//import com.amazon.spapi.messaging.ApiException;
//import com.amazon.spapi.messaging.api.MessagingApi;


MessagingApi apiInstance = new MessagingApi();
String amazonOrderId = "amazonOrderId_example"; // String | An Amazon order identifier. This specifies the order for which a message is sent.
List<String> marketplaceIds = Arrays.asList("marketplaceIds_example"); // List<String> | A marketplace identifier. This specifies the marketplace in which the order was placed. Only one marketplace can be specified.
CreateUnexpectedProblemRequest body = new CreateUnexpectedProblemRequest(); // CreateUnexpectedProblemRequest | 
try {
    CreateUnexpectedProblemResponse result = apiInstance.createUnexpectedProblem(amazonOrderId, marketplaceIds, body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling MessagingApi#createUnexpectedProblem");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **amazonOrderId** | **String**| An Amazon order identifier. This specifies the order for which a message is sent. |
 **marketplaceIds** | [**List&lt;String&gt;**](String.md)| A marketplace identifier. This specifies the marketplace in which the order was placed. Only one marketplace can be specified. |
 **body** | [**CreateUnexpectedProblemRequest**](CreateUnexpectedProblemRequest.md)|  |

### Return type

[**CreateUnexpectedProblemResponse**](CreateUnexpectedProblemResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/hal+json

<a name="createWarranty"></a>
# **createWarranty**
> CreateWarrantyResponse createWarranty(amazonOrderId, marketplaceIds, body)



Sends a message to a buyer to provide details about warranty information on a purchase in their order.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 1 | 5 |  For more information, see \&quot;Usage Plans and Rate Limits\&quot; in the Selling Partner API documentation.

### Example
```java
// Import classes:
//import com.amazon.spapi.messaging.ApiException;
//import com.amazon.spapi.messaging.api.MessagingApi;


MessagingApi apiInstance = new MessagingApi();
String amazonOrderId = "amazonOrderId_example"; // String | An Amazon order identifier. This specifies the order for which a message is sent.
List<String> marketplaceIds = Arrays.asList("marketplaceIds_example"); // List<String> | A marketplace identifier. This specifies the marketplace in which the order was placed. Only one marketplace can be specified.
CreateWarrantyRequest body = new CreateWarrantyRequest(); // CreateWarrantyRequest | 
try {
    CreateWarrantyResponse result = apiInstance.createWarranty(amazonOrderId, marketplaceIds, body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling MessagingApi#createWarranty");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **amazonOrderId** | **String**| An Amazon order identifier. This specifies the order for which a message is sent. |
 **marketplaceIds** | [**List&lt;String&gt;**](String.md)| A marketplace identifier. This specifies the marketplace in which the order was placed. Only one marketplace can be specified. |
 **body** | [**CreateWarrantyRequest**](CreateWarrantyRequest.md)|  |

### Return type

[**CreateWarrantyResponse**](CreateWarrantyResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/hal+json

<a name="getAttributes"></a>
# **getAttributes**
> GetAttributesResponse getAttributes(amazonOrderId, marketplaceIds)



Returns a response containing attributes related to an order. This includes buyer preferences.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 1 | 5 |

### Example
```java
// Import classes:
//import com.amazon.spapi.messaging.ApiException;
//import com.amazon.spapi.messaging.api.MessagingApi;


MessagingApi apiInstance = new MessagingApi();
String amazonOrderId = "amazonOrderId_example"; // String | An Amazon order identifier. This specifies the order for which a message is sent.
List<String> marketplaceIds = Arrays.asList("marketplaceIds_example"); // List<String> | A marketplace identifier. This specifies the marketplace in which the order was placed. Only one marketplace can be specified.
try {
    GetAttributesResponse result = apiInstance.getAttributes(amazonOrderId, marketplaceIds);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling MessagingApi#getAttributes");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **amazonOrderId** | **String**| An Amazon order identifier. This specifies the order for which a message is sent. |
 **marketplaceIds** | [**List&lt;String&gt;**](String.md)| A marketplace identifier. This specifies the marketplace in which the order was placed. Only one marketplace can be specified. |

### Return type

[**GetAttributesResponse**](GetAttributesResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/hal+json

<a name="getMessagingActionsForOrder"></a>
# **getMessagingActionsForOrder**
> GetMessagingActionsForOrderResponse getMessagingActionsForOrder(amazonOrderId, marketplaceIds)



Returns a list of message types that are available for an order that you specify. A message type is represented by an actions object, which contains a path and query parameter(s). You can use the path and parameter(s) to call an operation that sends a message.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 1 | 5 |  For more information, see \&quot;Usage Plans and Rate Limits\&quot; in the Selling Partner API documentation.

### Example
```java
// Import classes:
//import com.amazon.spapi.messaging.ApiException;
//import com.amazon.spapi.messaging.api.MessagingApi;


MessagingApi apiInstance = new MessagingApi();
String amazonOrderId = "amazonOrderId_example"; // String | An Amazon order identifier. This specifies the order for which you want a list of available message types.
List<String> marketplaceIds = Arrays.asList("marketplaceIds_example"); // List<String> | A marketplace identifier. This specifies the marketplace in which the order was placed. Only one marketplace can be specified.
try {
    GetMessagingActionsForOrderResponse result = apiInstance.getMessagingActionsForOrder(amazonOrderId, marketplaceIds);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling MessagingApi#getMessagingActionsForOrder");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **amazonOrderId** | **String**| An Amazon order identifier. This specifies the order for which you want a list of available message types. |
 **marketplaceIds** | [**List&lt;String&gt;**](String.md)| A marketplace identifier. This specifies the marketplace in which the order was placed. Only one marketplace can be specified. |

### Return type

[**GetMessagingActionsForOrderResponse**](GetMessagingActionsForOrderResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/hal+json

