# CatalogApi

All URIs are relative to *https://sellingpartnerapi-na.amazon.com*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getCatalogItem**](CatalogApi.md#getCatalogItem) | **GET** /catalog/v0/items/{asin} | 
[**listCatalogCategories**](CatalogApi.md#listCatalogCategories) | **GET** /catalog/v0/categories | 
[**listCatalogItems**](CatalogApi.md#listCatalogItems) | **GET** /catalog/v0/items | 


<a name="getCatalogItem"></a>
# **getCatalogItem**
> GetCatalogItemResponse getCatalogItem(marketplaceId, asin)



Returns a specified item and its attributes.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 1 | 1 |  For more information, see \&quot;Usage Plans and Rate Limits\&quot; in the Selling Partner API documentation.

### Example
```java
// Import classes:
//import com.amazon.spapi.catalogitems.ApiException;
//import com.amazon.spapi.catalogitems.api.CatalogApi;


CatalogApi apiInstance = new CatalogApi();
String marketplaceId = "marketplaceId_example"; // String | A marketplace identifier. Specifies the marketplace for the item.
String asin = "asin_example"; // String | The Amazon Standard Identification Number (ASIN) of the item.
try {
    GetCatalogItemResponse result = apiInstance.getCatalogItem(marketplaceId, asin);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling CatalogApi#getCatalogItem");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **marketplaceId** | **String**| A marketplace identifier. Specifies the marketplace for the item. |
 **asin** | **String**| The Amazon Standard Identification Number (ASIN) of the item. |

### Return type

[**GetCatalogItemResponse**](GetCatalogItemResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="listCatalogCategories"></a>
# **listCatalogCategories**
> ListCatalogCategoriesResponse listCatalogCategories(marketplaceId, ASIN, sellerSKU)



Returns the parent categories to which an item belongs, based on the specified ASIN or SellerSKU.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 1 | 1 |  For more information, see \&quot;Usage Plans and Rate Limits\&quot; in the Selling Partner API documentation.

### Example
```java
// Import classes:
//import com.amazon.spapi.catalogitems.ApiException;
//import com.amazon.spapi.catalogitems.api.CatalogApi;


CatalogApi apiInstance = new CatalogApi();
String marketplaceId = "marketplaceId_example"; // String | A marketplace identifier. Specifies the marketplace for the item.
String ASIN = "ASIN_example"; // String | The Amazon Standard Identification Number (ASIN) of the item.
String sellerSKU = "sellerSKU_example"; // String | Used to identify items in the given marketplace. SellerSKU is qualified by the seller's SellerId, which is included with every operation that you submit.
try {
    ListCatalogCategoriesResponse result = apiInstance.listCatalogCategories(marketplaceId, ASIN, sellerSKU);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling CatalogApi#listCatalogCategories");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **marketplaceId** | **String**| A marketplace identifier. Specifies the marketplace for the item. |
 **ASIN** | **String**| The Amazon Standard Identification Number (ASIN) of the item. | [optional]
 **sellerSKU** | **String**| Used to identify items in the given marketplace. SellerSKU is qualified by the seller&#39;s SellerId, which is included with every operation that you submit. | [optional]

### Return type

[**ListCatalogCategoriesResponse**](ListCatalogCategoriesResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="listCatalogItems"></a>
# **listCatalogItems**
> ListCatalogItemsResponse listCatalogItems(marketplaceId, query, queryContextId, sellerSKU, UPC, EAN, ISBN, JAN)



Returns a list of items and their attributes, based on a search query or item identifiers that you specify. When based on a search query, provide the Query parameter and optionally, the QueryContextId parameter. When based on item identifiers, provide a single appropriate parameter based on the identifier type, and specify the associated item value. MarketplaceId is always required.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 1 | 1 |  For more information, see \&quot;Usage Plans and Rate Limits\&quot; in the Selling Partner API documentation.

### Example
```java
// Import classes:
//import com.amazon.spapi.catalogitems.ApiException;
//import com.amazon.spapi.catalogitems.api.CatalogApi;


CatalogApi apiInstance = new CatalogApi();
String marketplaceId = "marketplaceId_example"; // String | A marketplace identifier. Specifies the marketplace for which items are returned.
String query = "query_example"; // String | Keyword(s) to use to search for items in the catalog. Example: 'harry potter books'.
String queryContextId = "queryContextId_example"; // String | An identifier for the context within which the given search will be performed. A marketplace might provide mechanisms for constraining a search to a subset of potential items. For example, the retail marketplace allows queries to be constrained to a specific category. The QueryContextId parameter specifies such a subset. If it is omitted, the search will be performed using the default context for the marketplace, which will typically contain the largest set of items.
String sellerSKU = "sellerSKU_example"; // String | Used to identify an item in the given marketplace. SellerSKU is qualified by the seller's SellerId, which is included with every operation that you submit.
String UPC = "UPC_example"; // String | A 12-digit bar code used for retail packaging.
String EAN = "EAN_example"; // String | A European article number that uniquely identifies the catalog item, manufacturer, and its attributes.
String ISBN = "ISBN_example"; // String | The unique commercial book identifier used to identify books internationally.
String JAN = "JAN_example"; // String | A Japanese article number that uniquely identifies the product, manufacturer, and its attributes.
try {
    ListCatalogItemsResponse result = apiInstance.listCatalogItems(marketplaceId, query, queryContextId, sellerSKU, UPC, EAN, ISBN, JAN);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling CatalogApi#listCatalogItems");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **marketplaceId** | **String**| A marketplace identifier. Specifies the marketplace for which items are returned. |
 **query** | **String**| Keyword(s) to use to search for items in the catalog. Example: &#39;harry potter books&#39;. | [optional]
 **queryContextId** | **String**| An identifier for the context within which the given search will be performed. A marketplace might provide mechanisms for constraining a search to a subset of potential items. For example, the retail marketplace allows queries to be constrained to a specific category. The QueryContextId parameter specifies such a subset. If it is omitted, the search will be performed using the default context for the marketplace, which will typically contain the largest set of items. | [optional]
 **sellerSKU** | **String**| Used to identify an item in the given marketplace. SellerSKU is qualified by the seller&#39;s SellerId, which is included with every operation that you submit. | [optional]
 **UPC** | **String**| A 12-digit bar code used for retail packaging. | [optional]
 **EAN** | **String**| A European article number that uniquely identifies the catalog item, manufacturer, and its attributes. | [optional]
 **ISBN** | **String**| The unique commercial book identifier used to identify books internationally. | [optional]
 **JAN** | **String**| A Japanese article number that uniquely identifies the product, manufacturer, and its attributes. | [optional]

### Return type

[**ListCatalogItemsResponse**](ListCatalogItemsResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

