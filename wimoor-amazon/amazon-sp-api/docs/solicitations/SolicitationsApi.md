# SolicitationsApi

All URIs are relative to *https://sellingpartnerapi-na.amazon.com*

Method | HTTP request | Description
------------- | ------------- | -------------
[**createProductReviewAndSellerFeedbackSolicitation**](SolicitationsApi.md#createProductReviewAndSellerFeedbackSolicitation) | **POST** /solicitations/v1/orders/{amazonOrderId}/solicitations/productReviewAndSellerFeedback | 
[**getSolicitationActionsForOrder**](SolicitationsApi.md#getSolicitationActionsForOrder) | **GET** /solicitations/v1/orders/{amazonOrderId} | 


<a name="createProductReviewAndSellerFeedbackSolicitation"></a>
# **createProductReviewAndSellerFeedbackSolicitation**
> CreateProductReviewAndSellerFeedbackSolicitationResponse createProductReviewAndSellerFeedbackSolicitation(amazonOrderId, marketplaceIds)



Sends a solicitation to a buyer asking for seller feedback and a product review for the specified order. Send only one productReviewAndSellerFeedback or free form proactive message per order.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 1 | 5 |  For more information, see \&quot;Usage Plans and Rate Limits\&quot; in the Selling Partner API documentation.

### Example
```java
// Import classes:
//import com.amazon.spapi.solicitations.ApiException;
//import com.amazon.spapi.solicitations.api.SolicitationsApi;


SolicitationsApi apiInstance = new SolicitationsApi();
String amazonOrderId = "amazonOrderId_example"; // String | An Amazon order identifier. This specifies the order for which a solicitation is sent.
List<String> marketplaceIds = Arrays.asList("marketplaceIds_example"); // List<String> | A marketplace identifier. This specifies the marketplace in which the order was placed. Only one marketplace can be specified.
try {
    CreateProductReviewAndSellerFeedbackSolicitationResponse result = apiInstance.createProductReviewAndSellerFeedbackSolicitation(amazonOrderId, marketplaceIds);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling SolicitationsApi#createProductReviewAndSellerFeedbackSolicitation");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **amazonOrderId** | **String**| An Amazon order identifier. This specifies the order for which a solicitation is sent. |
 **marketplaceIds** | [**List&lt;String&gt;**](String.md)| A marketplace identifier. This specifies the marketplace in which the order was placed. Only one marketplace can be specified. |

### Return type

[**CreateProductReviewAndSellerFeedbackSolicitationResponse**](CreateProductReviewAndSellerFeedbackSolicitationResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/hal+json

<a name="getSolicitationActionsForOrder"></a>
# **getSolicitationActionsForOrder**
> GetSolicitationActionsForOrderResponse getSolicitationActionsForOrder(amazonOrderId, marketplaceIds)



Returns a list of solicitation types that are available for an order that you specify. A solicitation type is represented by an actions object, which contains a path and query parameter(s). You can use the path and parameter(s) to call an operation that sends a solicitation. Currently only the productReviewAndSellerFeedbackSolicitation solicitation type is available.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 1 | 5 |  For more information, see \&quot;Usage Plans and Rate Limits\&quot; in the Selling Partner API documentation.

### Example
```java
// Import classes:
//import com.amazon.spapi.solicitations.ApiException;
//import com.amazon.spapi.solicitations.api.SolicitationsApi;


SolicitationsApi apiInstance = new SolicitationsApi();
String amazonOrderId = "amazonOrderId_example"; // String | An Amazon order identifier. This specifies the order for which you want a list of available solicitation types.
List<String> marketplaceIds = Arrays.asList("marketplaceIds_example"); // List<String> | A marketplace identifier. This specifies the marketplace in which the order was placed. Only one marketplace can be specified.
try {
    GetSolicitationActionsForOrderResponse result = apiInstance.getSolicitationActionsForOrder(amazonOrderId, marketplaceIds);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling SolicitationsApi#getSolicitationActionsForOrder");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **amazonOrderId** | **String**| An Amazon order identifier. This specifies the order for which you want a list of available solicitation types. |
 **marketplaceIds** | [**List&lt;String&gt;**](String.md)| A marketplace identifier. This specifies the marketplace in which the order was placed. Only one marketplace can be specified. |

### Return type

[**GetSolicitationActionsForOrderResponse**](GetSolicitationActionsForOrderResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/hal+json

