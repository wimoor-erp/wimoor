# CatalogApi

All URIs are relative to *https://sellingpartnerapi-na.amazon.com*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getCatalogItem**](CatalogApi.md#getCatalogItem) | **GET** /catalog/2022-04-01/items/{asin} | 
[**searchCatalogItems**](CatalogApi.md#searchCatalogItems) | **GET** /catalog/2022-04-01/items | 


<a name="getCatalogItem"></a>
# **getCatalogItem**
> Item getCatalogItem(asin, marketplaceIds, includedData, locale)



Retrieves details for an item in the Amazon catalog.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 2 | 2 |  The &#x60;x-amzn-RateLimit-Limit&#x60; response header returns the usage plan rate limits that were applied to the requested operation, when available. The table above indicates the default rate and burst values for this operation. Selling partners whose business demands require higher throughput may observe higher rate and burst values than those shown here. For more information, refer to the [Usage Plans and Rate Limits in the Selling Partner API](doc:usage-plans-and-rate-limits-in-the-sp-api).

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.CatalogApi;


CatalogApi apiInstance = new CatalogApi();
String asin = "asin_example"; // String | The Amazon Standard Identification Number (ASIN) of the item.
List<String> marketplaceIds = Arrays.asList("ATVPDKIKX0DER"); // List<String> | A comma-delimited list of Amazon marketplace identifiers. Data sets in the response contain data only for the specified marketplaces.
List<String> includedData = Arrays.asList("[\"summaries\"]"); // List<String> | A comma-delimited list of data sets to include in the response. Default: `summaries`.
String locale = "en_US"; // String | Locale for retrieving localized summaries. Defaults to the primary locale of the marketplace.
try {
    Item result = apiInstance.getCatalogItem(asin, marketplaceIds, includedData, locale);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling CatalogApi#getCatalogItem");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **asin** | **String**| The Amazon Standard Identification Number (ASIN) of the item. |
 **marketplaceIds** | [**List&lt;String&gt;**](String.md)| A comma-delimited list of Amazon marketplace identifiers. Data sets in the response contain data only for the specified marketplaces. |
 **includedData** | [**List&lt;String&gt;**](String.md)| A comma-delimited list of data sets to include in the response. Default: &#x60;summaries&#x60;. | [optional] [default to [&quot;summaries&quot;]] [enum: attributes, dimensions, identifiers, images, productTypes, relationships, salesRanks, summaries, vendorDetails]
 **locale** | **String**| Locale for retrieving localized summaries. Defaults to the primary locale of the marketplace. | [optional]

### Return type

[**Item**](Item.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="searchCatalogItems"></a>
# **searchCatalogItems**
> ItemSearchResults searchCatalogItems(marketplaceIds, identifiers, identifiersType, includedData, locale, sellerId, keywords, brandNames, classificationIds, pageSize, pageToken, keywordsLocale)



Search for and return a list of Amazon catalog items and associated information either by identifier or by keywords.  **Usage Plans:**  | Rate (requests per second) | Burst | | ---- | ---- | | 2 | 2 |  The &#x60;x-amzn-RateLimit-Limit&#x60; response header returns the usage plan rate limits that were applied to the requested operation, when available. The table above indicates the default rate and burst values for this operation. Selling partners whose business demands require higher throughput may observe higher rate and burst values than those shown here. For more information, refer to the [Usage Plans and Rate Limits in the Selling Partner API](doc:usage-plans-and-rate-limits-in-the-sp-api).

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.CatalogApi;


CatalogApi apiInstance = new CatalogApi();
List<String> marketplaceIds = Arrays.asList("ATVPDKIKX0DER"); // List<String> | A comma-delimited list of Amazon marketplace identifiers for the request.
List<String> identifiers = Arrays.asList("shoes"); // List<String> | A comma-delimited list of product identifiers to search the Amazon catalog for. **Note:** Cannot be used with `keywords`.
String identifiersType = "ASIN"; // String | Type of product identifiers to search the Amazon catalog for. **Note:** Required when `identifiers` are provided.
List<String> includedData = Arrays.asList("[\"summaries\"]"); // List<String> | A comma-delimited list of data sets to include in the response. Default: `summaries`.
String locale = "en_US"; // String | Locale for retrieving localized summaries. Defaults to the primary locale of the marketplace.
String sellerId = "sellerId_example"; // String | A selling partner identifier, such as a seller account or vendor code. **Note:** Required when `identifiersType` is `SKU`.
List<String> keywords = Arrays.asList("shoes"); // List<String> | A comma-delimited list of words to search the Amazon catalog for. **Note:** Cannot be used with `identifiers`.
List<String> brandNames = Arrays.asList("Beautiful Boats"); // List<String> | A comma-delimited list of brand names to limit the search for `keywords`-based queries. **Note:** Cannot be used with `identifiers`.
List<String> classificationIds = Arrays.asList("12345678"); // List<String> | A comma-delimited list of classification identifiers to limit the search for `keywords`-based queries. **Note:** Cannot be used with `identifiers`.
Integer pageSize = 10; // Integer | Number of results to be returned per page.
String pageToken = "sdlkj234lkj234lksjdflkjwdflkjsfdlkj234234234234"; // String | A token to fetch a certain page when there are multiple pages worth of results.
String keywordsLocale = "en_US"; // String | The language of the keywords provided for `keywords`-based queries. Defaults to the primary locale of the marketplace. **Note:** Cannot be used with `identifiers`.
try {
    ItemSearchResults result = apiInstance.searchCatalogItems(marketplaceIds, identifiers, identifiersType, includedData, locale, sellerId, keywords, brandNames, classificationIds, pageSize, pageToken, keywordsLocale);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling CatalogApi#searchCatalogItems");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **marketplaceIds** | [**List&lt;String&gt;**](String.md)| A comma-delimited list of Amazon marketplace identifiers for the request. |
 **identifiers** | [**List&lt;String&gt;**](String.md)| A comma-delimited list of product identifiers to search the Amazon catalog for. **Note:** Cannot be used with &#x60;keywords&#x60;. | [optional]
 **identifiersType** | **String**| Type of product identifiers to search the Amazon catalog for. **Note:** Required when &#x60;identifiers&#x60; are provided. | [optional] [enum: ASIN, EAN, GTIN, ISBN, JAN, MINSAN, SKU, UPC]
 **includedData** | [**List&lt;String&gt;**](String.md)| A comma-delimited list of data sets to include in the response. Default: &#x60;summaries&#x60;. | [optional] [default to [&quot;summaries&quot;]] [enum: attributes, dimensions, identifiers, images, productTypes, relationships, salesRanks, summaries, vendorDetails]
 **locale** | **String**| Locale for retrieving localized summaries. Defaults to the primary locale of the marketplace. | [optional]
 **sellerId** | **String**| A selling partner identifier, such as a seller account or vendor code. **Note:** Required when &#x60;identifiersType&#x60; is &#x60;SKU&#x60;. | [optional]
 **keywords** | [**List&lt;String&gt;**](String.md)| A comma-delimited list of words to search the Amazon catalog for. **Note:** Cannot be used with &#x60;identifiers&#x60;. | [optional]
 **brandNames** | [**List&lt;String&gt;**](String.md)| A comma-delimited list of brand names to limit the search for &#x60;keywords&#x60;-based queries. **Note:** Cannot be used with &#x60;identifiers&#x60;. | [optional]
 **classificationIds** | [**List&lt;String&gt;**](String.md)| A comma-delimited list of classification identifiers to limit the search for &#x60;keywords&#x60;-based queries. **Note:** Cannot be used with &#x60;identifiers&#x60;. | [optional]
 **pageSize** | **Integer**| Number of results to be returned per page. | [optional] [default to 10]
 **pageToken** | **String**| A token to fetch a certain page when there are multiple pages worth of results. | [optional]
 **keywordsLocale** | **String**| The language of the keywords provided for &#x60;keywords&#x60;-based queries. Defaults to the primary locale of the marketplace. **Note:** Cannot be used with &#x60;identifiers&#x60;. | [optional]

### Return type

[**ItemSearchResults**](ItemSearchResults.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

