# ProductPricingApi

All URIs are relative to *https://sellingpartnerapi-na.amazon.com*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getCompetitivePricing**](ProductPricingApi.md#getCompetitivePricing) | **GET** /products/pricing/v0/competitivePrice | 
[**getItemOffers**](ProductPricingApi.md#getItemOffers) | **GET** /products/pricing/v0/items/{Asin}/offers | 
[**getListingOffers**](ProductPricingApi.md#getListingOffers) | **GET** /products/pricing/v0/listings/{SellerSKU}/offers | 
[**getPricing**](ProductPricingApi.md#getPricing) | **GET** /products/pricing/v0/price | 


<a name="getCompetitivePricing"></a>
# **getCompetitivePricing**
> GetPricingResponse getCompetitivePricing(marketplaceId, itemType, asins, skus)



Returns competitive pricing information for a seller&#39;s offer listings based on seller SKU or ASIN.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 1 | 1 |  For more information, see \&quot;Usage Plans and Rate Limits\&quot; in the Selling Partner API documentation.

### Example
```java
// Import classes:
//import com.amazon.spapi.productpricing.ApiException;
//import com.amazon.spapi.productpricing.api.ProductPricingApi;


ProductPricingApi apiInstance = new ProductPricingApi();
String marketplaceId = "marketplaceId_example"; // String | A marketplace identifier. Specifies the marketplace for which prices are returned.
String itemType = "itemType_example"; // String | Indicates whether ASIN values or seller SKU values are used to identify items. If you specify Asin, the information in the response will be dependent on the list of Asins you provide in the Asins parameter. If you specify Sku, the information in the response will be dependent on the list of Skus you provide in the Skus parameter. Possible values: Asin, Sku.
List<String> asins = Arrays.asList("asins_example"); // List<String> | A list of up to twenty Amazon Standard Identification Number (ASIN) values used to identify items in the given marketplace.
List<String> skus = Arrays.asList("skus_example"); // List<String> | A list of up to twenty seller SKU values used to identify items in the given marketplace.
try {
    GetPricingResponse result = apiInstance.getCompetitivePricing(marketplaceId, itemType, asins, skus);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ProductPricingApi#getCompetitivePricing");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **marketplaceId** | **String**| A marketplace identifier. Specifies the marketplace for which prices are returned. |
 **itemType** | **String**| Indicates whether ASIN values or seller SKU values are used to identify items. If you specify Asin, the information in the response will be dependent on the list of Asins you provide in the Asins parameter. If you specify Sku, the information in the response will be dependent on the list of Skus you provide in the Skus parameter. Possible values: Asin, Sku. | [enum: Asin, Sku]
 **asins** | [**List&lt;String&gt;**](String.md)| A list of up to twenty Amazon Standard Identification Number (ASIN) values used to identify items in the given marketplace. | [optional]
 **skus** | [**List&lt;String&gt;**](String.md)| A list of up to twenty seller SKU values used to identify items in the given marketplace. | [optional]

### Return type

[**GetPricingResponse**](GetPricingResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getItemOffers"></a>
# **getItemOffers**
> GetOffersResponse getItemOffers(marketplaceId, itemCondition, asin)



Returns the lowest priced offers for a single item based on ASIN.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 1 | 1 |  For more information, see \&quot;Usage Plans and Rate Limits\&quot; in the Selling Partner API documentation.

### Example
```java
// Import classes:
//import com.amazon.spapi.productpricing.ApiException;
//import com.amazon.spapi.productpricing.api.ProductPricingApi;


ProductPricingApi apiInstance = new ProductPricingApi();
String marketplaceId = "marketplaceId_example"; // String | A marketplace identifier. Specifies the marketplace for which prices are returned.
String itemCondition = "itemCondition_example"; // String | Filters the offer listings to be considered based on item condition. Possible values: New, Used, Collectible, Refurbished, Club.
String asin = "asin_example"; // String | The Amazon Standard Identification Number (ASIN) of the item.
try {
    GetOffersResponse result = apiInstance.getItemOffers(marketplaceId, itemCondition, asin);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ProductPricingApi#getItemOffers");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **marketplaceId** | **String**| A marketplace identifier. Specifies the marketplace for which prices are returned. |
 **itemCondition** | **String**| Filters the offer listings to be considered based on item condition. Possible values: New, Used, Collectible, Refurbished, Club. | [enum: New, Used, Collectible, Refurbished, Club]
 **asin** | **String**| The Amazon Standard Identification Number (ASIN) of the item. |

### Return type

[**GetOffersResponse**](GetOffersResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getListingOffers"></a>
# **getListingOffers**
> GetOffersResponse getListingOffers(marketplaceId, itemCondition, sellerSKU)



Returns the lowest priced offers for a single SKU listing.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 1 | 1 |  For more information, see \&quot;Usage Plans and Rate Limits\&quot; in the Selling Partner API documentation.

### Example
```java
// Import classes:
//import com.amazon.spapi.productpricing.ApiException;
//import com.amazon.spapi.productpricing.api.ProductPricingApi;


ProductPricingApi apiInstance = new ProductPricingApi();
String marketplaceId = "marketplaceId_example"; // String | A marketplace identifier. Specifies the marketplace for which prices are returned.
String itemCondition = "itemCondition_example"; // String | Filters the offer listings based on item condition. Possible values: New, Used, Collectible, Refurbished, Club.
String sellerSKU = "sellerSKU_example"; // String | Identifies an item in the given marketplace. SellerSKU is qualified by the seller's SellerId, which is included with every operation that you submit.
try {
    GetOffersResponse result = apiInstance.getListingOffers(marketplaceId, itemCondition, sellerSKU);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ProductPricingApi#getListingOffers");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **marketplaceId** | **String**| A marketplace identifier. Specifies the marketplace for which prices are returned. |
 **itemCondition** | **String**| Filters the offer listings based on item condition. Possible values: New, Used, Collectible, Refurbished, Club. | [enum: New, Used, Collectible, Refurbished, Club]
 **sellerSKU** | **String**| Identifies an item in the given marketplace. SellerSKU is qualified by the seller&#39;s SellerId, which is included with every operation that you submit. |

### Return type

[**GetOffersResponse**](GetOffersResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getPricing"></a>
# **getPricing**
> GetPricingResponse getPricing(marketplaceId, itemType, asins, skus, itemCondition)



Returns pricing information for a seller&#39;s offer listings based on seller SKU or ASIN.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 1 | 1 |  For more information, see \&quot;Usage Plans and Rate Limits\&quot; in the Selling Partner API documentation.

### Example
```java
// Import classes:
//import com.amazon.spapi.productpricing.ApiException;
//import com.amazon.spapi.productpricing.api.ProductPricingApi;


ProductPricingApi apiInstance = new ProductPricingApi();
String marketplaceId = "marketplaceId_example"; // String | A marketplace identifier. Specifies the marketplace for which prices are returned.
String itemType = "itemType_example"; // String | Indicates whether ASIN values or seller SKU values are used to identify items. If you specify Asin, the information in the response will be dependent on the list of Asins you provide in the Asins parameter. If you specify Sku, the information in the response will be dependent on the list of Skus you provide in the Skus parameter.
List<String> asins = Arrays.asList("asins_example"); // List<String> | A list of up to twenty Amazon Standard Identification Number (ASIN) values used to identify items in the given marketplace.
List<String> skus = Arrays.asList("skus_example"); // List<String> | A list of up to twenty seller SKU values used to identify items in the given marketplace.
String itemCondition = "itemCondition_example"; // String | Filters the offer listings based on item condition. Possible values: New, Used, Collectible, Refurbished, Club.
try {
    GetPricingResponse result = apiInstance.getPricing(marketplaceId, itemType, asins, skus, itemCondition);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ProductPricingApi#getPricing");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **marketplaceId** | **String**| A marketplace identifier. Specifies the marketplace for which prices are returned. |
 **itemType** | **String**| Indicates whether ASIN values or seller SKU values are used to identify items. If you specify Asin, the information in the response will be dependent on the list of Asins you provide in the Asins parameter. If you specify Sku, the information in the response will be dependent on the list of Skus you provide in the Skus parameter. | [enum: Asin, Sku]
 **asins** | [**List&lt;String&gt;**](String.md)| A list of up to twenty Amazon Standard Identification Number (ASIN) values used to identify items in the given marketplace. | [optional]
 **skus** | [**List&lt;String&gt;**](String.md)| A list of up to twenty seller SKU values used to identify items in the given marketplace. | [optional]
 **itemCondition** | **String**| Filters the offer listings based on item condition. Possible values: New, Used, Collectible, Refurbished, Club. | [optional] [enum: New, Used, Collectible, Refurbished, Club]

### Return type

[**GetPricingResponse**](GetPricingResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

