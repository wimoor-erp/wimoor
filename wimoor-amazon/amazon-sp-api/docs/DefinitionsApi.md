# DefinitionsApi

All URIs are relative to *https://sellingpartnerapi-na.amazon.com*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getDefinitionsProductType**](DefinitionsApi.md#getDefinitionsProductType) | **GET** /definitions/2020-09-01/productTypes/{productType} | 
[**searchDefinitionsProductTypes**](DefinitionsApi.md#searchDefinitionsProductTypes) | **GET** /definitions/2020-09-01/productTypes | 


<a name="getDefinitionsProductType"></a>
# **getDefinitionsProductType**
> ProductTypeDefinition getDefinitionsProductType(productType, marketplaceIds, sellerId, productTypeVersion, requirements, requirementsEnforced, locale)



Retrieve an Amazon product type definition.  **Usage Plans:**  | Plan type | Rate (requests per second) | Burst | | ---- | ---- | ---- | |Default| 5 | 10 | |Selling partner specific| Variable | Variable |  The x-amzn-RateLimit-Limit response header returns the usage plan rate limits that were applied to the requested operation. Rate limits for some selling partners will vary from the default rate and burst shown in the table above. For more information, see [Usage Plans and Rate Limits in the Selling Partner API](doc:usage-plans-and-rate-limits-in-the-sp-api).

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.DefinitionsApi;


DefinitionsApi apiInstance = new DefinitionsApi();
String productType = "LUGGAGE"; // String | The Amazon product type name.
List<String> marketplaceIds = Arrays.asList("ATVPDKIKX0DER"); // List<String> | A comma-delimited list of Amazon marketplace identifiers for the request. Note: This parameter is limited to one marketplaceId at this time.
String sellerId = "sellerId_example"; // String | A selling partner identifier. When provided, seller-specific requirements and values are populated within the product type definition schema, such as brand names associated with the selling partner.
String productTypeVersion = "LATEST"; // String | The version of the Amazon product type to retrieve. Defaults to \"LATEST\",. Prerelease versions of product type definitions may be retrieved with \"RELEASE_CANDIDATE\". If no prerelease version is currently available, the \"LATEST\" live version will be provided.
String requirements = "LISTING"; // String | The name of the requirements set to retrieve requirements for.
String requirementsEnforced = "ENFORCED"; // String | Identifies if the required attributes for a requirements set are enforced by the product type definition schema. Non-enforced requirements enable structural validation of individual attributes without all the required attributes being present (such as for partial updates).
String locale = "DEFAULT"; // String | Locale for retrieving display labels and other presentation details. Defaults to the default language of the first marketplace in the request.
try {
    ProductTypeDefinition result = apiInstance.getDefinitionsProductType(productType, marketplaceIds, sellerId, productTypeVersion, requirements, requirementsEnforced, locale);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefinitionsApi#getDefinitionsProductType");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **productType** | **String**| The Amazon product type name. |
 **marketplaceIds** | [**List&lt;String&gt;**](String.md)| A comma-delimited list of Amazon marketplace identifiers for the request. Note: This parameter is limited to one marketplaceId at this time. |
 **sellerId** | **String**| A selling partner identifier. When provided, seller-specific requirements and values are populated within the product type definition schema, such as brand names associated with the selling partner. | [optional]
 **productTypeVersion** | **String**| The version of the Amazon product type to retrieve. Defaults to \&quot;LATEST\&quot;,. Prerelease versions of product type definitions may be retrieved with \&quot;RELEASE_CANDIDATE\&quot;. If no prerelease version is currently available, the \&quot;LATEST\&quot; live version will be provided. | [optional] [default to LATEST]
 **requirements** | **String**| The name of the requirements set to retrieve requirements for. | [optional] [default to LISTING] [enum: LISTING, LISTING_PRODUCT_ONLY, LISTING_OFFER_ONLY]
 **requirementsEnforced** | **String**| Identifies if the required attributes for a requirements set are enforced by the product type definition schema. Non-enforced requirements enable structural validation of individual attributes without all the required attributes being present (such as for partial updates). | [optional] [default to ENFORCED] [enum: ENFORCED, NOT_ENFORCED]
 **locale** | **String**| Locale for retrieving display labels and other presentation details. Defaults to the default language of the first marketplace in the request. | [optional] [default to DEFAULT] [enum: DEFAULT, ar, ar_AE, de, de_DE, en, en_AE, en_AU, en_CA, en_GB, en_IN, en_SG, en_US, es, es_ES, es_MX, es_US, fr, fr_CA, fr_FR, it, it_IT, ja, ja_JP, nl, nl_NL, pl, pl_PL, pt, pt_BR, pt_PT, sv, sv_SE, tr, tr_TR, zh, zh_CN, zh_TW]

### Return type

[**ProductTypeDefinition**](ProductTypeDefinition.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="searchDefinitionsProductTypes"></a>
# **searchDefinitionsProductTypes**
> ProductTypeList searchDefinitionsProductTypes(marketplaceIds, keywords, itemName, locale, searchLocale)



Search for and return a list of Amazon product types that have definitions available.  **Usage Plans:**  | Plan type | Rate (requests per second) | Burst | | ---- | ---- | ---- | |Default| 5 | 10 | |Selling partner specific| Variable | Variable |  The x-amzn-RateLimit-Limit response header returns the usage plan rate limits that were applied to the requested operation. Rate limits for some selling partners will vary from the default rate and burst shown in the table above. For more information, see [Usage Plans and Rate Limits in the Selling Partner API](doc:usage-plans-and-rate-limits-in-the-sp-api).

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.DefinitionsApi;


DefinitionsApi apiInstance = new DefinitionsApi();
List<String> marketplaceIds = Arrays.asList("ATVPDKIKX0DER"); // List<String> | A comma-delimited list of Amazon marketplace identifiers for the request.
List<String> keywords = Arrays.asList("LUGGAGE"); // List<String> | A comma-delimited list of keywords to search product types. **Note:** Cannot be used with `itemName`.
String itemName = "Running shoes"; // String | The title of the ASIN to get the product type recommendation. **Note:** Cannot be used with `keywords`.
String locale = "en_US"; // String | The locale for the display names in the response. Defaults to the primary locale of the marketplace.
String searchLocale = "en_US"; // String | The locale used for the `keywords` and `itemName` parameters. Defaults to the primary locale of the marketplace.
try {
    ProductTypeList result = apiInstance.searchDefinitionsProductTypes(marketplaceIds, keywords, itemName, locale, searchLocale);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefinitionsApi#searchDefinitionsProductTypes");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **marketplaceIds** | [**List&lt;String&gt;**](String.md)| A comma-delimited list of Amazon marketplace identifiers for the request. |
 **keywords** | [**List&lt;String&gt;**](String.md)| A comma-delimited list of keywords to search product types. **Note:** Cannot be used with &#x60;itemName&#x60;. | [optional]
 **itemName** | **String**| The title of the ASIN to get the product type recommendation. **Note:** Cannot be used with &#x60;keywords&#x60;. | [optional]
 **locale** | **String**| The locale for the display names in the response. Defaults to the primary locale of the marketplace. | [optional]
 **searchLocale** | **String**| The locale used for the &#x60;keywords&#x60; and &#x60;itemName&#x60; parameters. Defaults to the primary locale of the marketplace. | [optional]

### Return type

[**ProductTypeList**](ProductTypeList.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

