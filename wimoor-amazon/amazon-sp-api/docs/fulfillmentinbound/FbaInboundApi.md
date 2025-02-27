# FbaInboundApi

All URIs are relative to *https://sellingpartnerapi-na.amazon.com*

Method | HTTP request | Description
------------- | ------------- | -------------
[**confirmPreorder**](FbaInboundApi.md#confirmPreorder) | **PUT** /fba/inbound/v0/shipments/{shipmentId}/preorder/confirm | 
[**confirmTransport**](FbaInboundApi.md#confirmTransport) | **POST** /fba/inbound/v0/shipments/{shipmentId}/transport/confirm | 
[**createInboundShipment**](FbaInboundApi.md#createInboundShipment) | **POST** /fba/inbound/v0/shipments/{shipmentId} | 
[**createInboundShipmentPlan**](FbaInboundApi.md#createInboundShipmentPlan) | **POST** /fba/inbound/v0/plans | 
[**estimateTransport**](FbaInboundApi.md#estimateTransport) | **POST** /fba/inbound/v0/shipments/{shipmentId}/transport/estimate | 
[**getBillOfLading**](FbaInboundApi.md#getBillOfLading) | **GET** /fba/inbound/v0/shipments/{shipmentId}/billOfLading | 
[**getInboundGuidance**](FbaInboundApi.md#getInboundGuidance) | **GET** /fba/inbound/v0/itemsGuidance | 
[**getLabels**](FbaInboundApi.md#getLabels) | **GET** /fba/inbound/v0/shipments/{shipmentId}/labels | 
[**getPreorderInfo**](FbaInboundApi.md#getPreorderInfo) | **GET** /fba/inbound/v0/shipments/{shipmentId}/preorder | 
[**getPrepInstructions**](FbaInboundApi.md#getPrepInstructions) | **GET** /fba/inbound/v0/prepInstructions | 
[**getShipmentItems**](FbaInboundApi.md#getShipmentItems) | **GET** /fba/inbound/v0/shipmentItems | 
[**getShipmentItemsByShipmentId**](FbaInboundApi.md#getShipmentItemsByShipmentId) | **GET** /fba/inbound/v0/shipments/{shipmentId}/items | 
[**getShipments**](FbaInboundApi.md#getShipments) | **GET** /fba/inbound/v0/shipments | 
[**getTransportDetails**](FbaInboundApi.md#getTransportDetails) | **GET** /fba/inbound/v0/shipments/{shipmentId}/transport | 
[**putTransportDetails**](FbaInboundApi.md#putTransportDetails) | **PUT** /fba/inbound/v0/shipments/{shipmentId}/transport | 
[**updateInboundShipment**](FbaInboundApi.md#updateInboundShipment) | **PUT** /fba/inbound/v0/shipments/{shipmentId} | 
[**voidTransport**](FbaInboundApi.md#voidTransport) | **POST** /fba/inbound/v0/shipments/{shipmentId}/transport/void | 


<a name="confirmPreorder"></a>
# **confirmPreorder**
> ConfirmPreorderResponse confirmPreorder(shipmentId, needByDate, marketplaceId)



Returns information needed to confirm a shipment for pre-order. Call this operation after calling the getPreorderInfo operation to get the NeedByDate value and other pre-order information about the shipment.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 2 | 30 |  For more information, see \&quot;Usage Plans and Rate Limits\&quot; in the Selling Partner API documentation.

### Example
```java
// Import classes:
//import com.amazon.spapi.fulfillmentinbound.ApiException;
//import com.amazon.spapi.fulfillmentinbound.api.FbaInboundApi;


FbaInboundApi apiInstance = new FbaInboundApi();
String shipmentId = "shipmentId_example"; // String | A shipment identifier originally returned by the createInboundShipmentPlan operation.
LocalDate needByDate = LocalDate.now(); // LocalDate | Date that the shipment must arrive at the Amazon fulfillment center to avoid delivery promise breaks for pre-ordered items. Must be in YYYY-MM-DD format. The response to the getPreorderInfo operation returns this value.
String marketplaceId = "marketplaceId_example"; // String | A marketplace identifier. Specifies the marketplace the shipment is tied to.
try {
    ConfirmPreorderResponse result = apiInstance.confirmPreorder(shipmentId, needByDate, marketplaceId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling FbaInboundApi#confirmPreorder");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **shipmentId** | **String**| A shipment identifier originally returned by the createInboundShipmentPlan operation. |
 **needByDate** | **LocalDate**| Date that the shipment must arrive at the Amazon fulfillment center to avoid delivery promise breaks for pre-ordered items. Must be in YYYY-MM-DD format. The response to the getPreorderInfo operation returns this value. |
 **marketplaceId** | **String**| A marketplace identifier. Specifies the marketplace the shipment is tied to. |

### Return type

[**ConfirmPreorderResponse**](ConfirmPreorderResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="confirmTransport"></a>
# **confirmTransport**
> ConfirmTransportResponse confirmTransport(shipmentId)



Confirms that the seller accepts the Amazon-partnered shipping estimate, agrees to allow Amazon to charge their account for the shipping cost, and requests that the Amazon-partnered carrier ship the inbound shipment.  Prior to calling the confirmTransport operation, you should call the getTransportDetails operation to get the Amazon-partnered shipping estimate.  Important: After confirming the transportation request, if the seller decides that they do not want the Amazon-partnered carrier to ship the inbound shipment, you can call the voidTransport operation to cancel the transportation request. Note that for a Small Parcel shipment, the seller has 24 hours after confirming a transportation request to void the transportation request. For a Less Than Truckload/Full Truckload (LTL/FTL) shipment, the seller has one hour after confirming a transportation request to void it. After the grace period has expired the seller&#39;s account will be charged for the shipping cost.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 2 | 30 |  For more information, see \&quot;Usage Plans and Rate Limits\&quot; in the Selling Partner API documentation.

### Example
```java
// Import classes:
//import com.amazon.spapi.fulfillmentinbound.ApiException;
//import com.amazon.spapi.fulfillmentinbound.api.FbaInboundApi;


FbaInboundApi apiInstance = new FbaInboundApi();
String shipmentId = "shipmentId_example"; // String | A shipment identifier originally returned by the createInboundShipmentPlan operation.
try {
    ConfirmTransportResponse result = apiInstance.confirmTransport(shipmentId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling FbaInboundApi#confirmTransport");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **shipmentId** | **String**| A shipment identifier originally returned by the createInboundShipmentPlan operation. |

### Return type

[**ConfirmTransportResponse**](ConfirmTransportResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="createInboundShipment"></a>
# **createInboundShipment**
> InboundShipmentResponse createInboundShipment(body, shipmentId)



Returns a new inbound shipment based on the specified shipmentId that was returned by the createInboundShipmentPlan operation.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 2 | 30 |  For more information, see \&quot;Usage Plans and Rate Limits\&quot; in the Selling Partner API documentation.

### Example
```java
// Import classes:
//import com.amazon.spapi.fulfillmentinbound.ApiException;
//import com.amazon.spapi.fulfillmentinbound.api.FbaInboundApi;


FbaInboundApi apiInstance = new FbaInboundApi();
InboundShipmentRequest body = new InboundShipmentRequest(); // InboundShipmentRequest | 
String shipmentId = "shipmentId_example"; // String | A shipment identifier originally returned by the createInboundShipmentPlan operation.
try {
    InboundShipmentResponse result = apiInstance.createInboundShipment(body, shipmentId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling FbaInboundApi#createInboundShipment");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**InboundShipmentRequest**](InboundShipmentRequest.md)|  |
 **shipmentId** | **String**| A shipment identifier originally returned by the createInboundShipmentPlan operation. |

### Return type

[**InboundShipmentResponse**](InboundShipmentResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="createInboundShipmentPlan"></a>
# **createInboundShipmentPlan**
> CreateInboundShipmentPlanResponse createInboundShipmentPlan(body)



Returns one or more inbound shipment plans, which provide the information you need to create one or more inbound shipments for a set of items that you specify. Multiple inbound shipment plans might be required so that items can be optimally placed in Amazon&#39;s fulfillment network—for example, positioning inventory closer to the customer. Alternatively, two inbound shipment plans might be created with the same Amazon fulfillment center destination if the two shipment plans require different processing—for example, items that require labels must be shipped separately from stickerless, commingled inventory.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 2 | 30 |  For more information, see \&quot;Usage Plans and Rate Limits\&quot; in the Selling Partner API documentation.

### Example
```java
// Import classes:
//import com.amazon.spapi.fulfillmentinbound.ApiException;
//import com.amazon.spapi.fulfillmentinbound.api.FbaInboundApi;


FbaInboundApi apiInstance = new FbaInboundApi();
CreateInboundShipmentPlanRequest body = new CreateInboundShipmentPlanRequest(); // CreateInboundShipmentPlanRequest | 
try {
    CreateInboundShipmentPlanResponse result = apiInstance.createInboundShipmentPlan(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling FbaInboundApi#createInboundShipmentPlan");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**CreateInboundShipmentPlanRequest**](CreateInboundShipmentPlanRequest.md)|  |

### Return type

[**CreateInboundShipmentPlanResponse**](CreateInboundShipmentPlanResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="estimateTransport"></a>
# **estimateTransport**
> EstimateTransportResponse estimateTransport(shipmentId)



Initiates the process of estimating the shipping cost for an inbound shipment by an Amazon-partnered carrier.  Prior to calling the estimateTransport operation, you must call the putTransportDetails operation to provide Amazon with the transportation information for the inbound shipment.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 2 | 30 |  For more information, see \&quot;Usage Plans and Rate Limits\&quot; in the Selling Partner API documentation.

### Example
```java
// Import classes:
//import com.amazon.spapi.fulfillmentinbound.ApiException;
//import com.amazon.spapi.fulfillmentinbound.api.FbaInboundApi;


FbaInboundApi apiInstance = new FbaInboundApi();
String shipmentId = "shipmentId_example"; // String | A shipment identifier originally returned by the createInboundShipmentPlan operation.
try {
    EstimateTransportResponse result = apiInstance.estimateTransport(shipmentId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling FbaInboundApi#estimateTransport");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **shipmentId** | **String**| A shipment identifier originally returned by the createInboundShipmentPlan operation. |

### Return type

[**EstimateTransportResponse**](EstimateTransportResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getBillOfLading"></a>
# **getBillOfLading**
> GetBillOfLadingResponse getBillOfLading(shipmentId)



Returns a bill of lading for a Less Than Truckload/Full Truckload (LTL/FTL) shipment. The getBillOfLading operation returns PDF document data for printing a bill of lading for an Amazon-partnered Less Than Truckload/Full Truckload (LTL/FTL) inbound shipment.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 2 | 30 |  For more information, see \&quot;Usage Plans and Rate Limits\&quot; in the Selling Partner API documentation.

### Example
```java
// Import classes:
//import com.amazon.spapi.fulfillmentinbound.ApiException;
//import com.amazon.spapi.fulfillmentinbound.api.FbaInboundApi;


FbaInboundApi apiInstance = new FbaInboundApi();
String shipmentId = "shipmentId_example"; // String | A shipment identifier originally returned by the createInboundShipmentPlan operation.
try {
    GetBillOfLadingResponse result = apiInstance.getBillOfLading(shipmentId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling FbaInboundApi#getBillOfLading");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **shipmentId** | **String**| A shipment identifier originally returned by the createInboundShipmentPlan operation. |

### Return type

[**GetBillOfLadingResponse**](GetBillOfLadingResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getInboundGuidance"></a>
# **getInboundGuidance**
> GetInboundGuidanceResponse getInboundGuidance(marketplaceId, sellerSKUList, asINList)



Returns information that lets a seller know if Amazon recommends sending an item to a given marketplace. In some cases, Amazon provides guidance for why a given SellerSKU or ASIN is not recommended for shipment to Amazon&#39;s fulfillment network. Sellers may still ship items that are not recommended, at their discretion.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 2 | 30 |  For more information, see \&quot;Usage Plans and Rate Limits\&quot; in the Selling Partner API documentation.

### Example
```java
// Import classes:
//import com.amazon.spapi.fulfillmentinbound.ApiException;
//import com.amazon.spapi.fulfillmentinbound.api.FbaInboundApi;


FbaInboundApi apiInstance = new FbaInboundApi();
String marketplaceId = "marketplaceId_example"; // String | A marketplace identifier. Specifies the marketplace where the product would be stored.
List<String> sellerSKUList = Arrays.asList("sellerSKUList_example"); // List<String> | A list of SellerSKU values. Used to identify items for which you want inbound guidance for shipment to Amazon's fulfillment network. Note: SellerSKU is qualified by the SellerId, which is included with every Selling Partner API operation that you submit. If you specify a SellerSKU that identifies a variation parent ASIN, this operation returns an error. A variation parent ASIN represents a generic product that cannot be sold. Variation child ASINs represent products that have specific characteristics (such as size and color) and can be sold. 
List<String> asINList = Arrays.asList("asINList_example"); // List<String> | A list of ASIN values. Used to identify items for which you want inbound guidance for shipment to Amazon's fulfillment network. Note: If you specify a ASIN that identifies a variation parent ASIN, this operation returns an error. A variation parent ASIN represents a generic product that cannot be sold. Variation child ASINs represent products that have specific characteristics (such as size and color) and can be sold.
try {
    GetInboundGuidanceResponse result = apiInstance.getInboundGuidance(marketplaceId, sellerSKUList, asINList);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling FbaInboundApi#getInboundGuidance");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **marketplaceId** | **String**| A marketplace identifier. Specifies the marketplace where the product would be stored. |
 **sellerSKUList** | [**List&lt;String&gt;**](String.md)| A list of SellerSKU values. Used to identify items for which you want inbound guidance for shipment to Amazon&#39;s fulfillment network. Note: SellerSKU is qualified by the SellerId, which is included with every Selling Partner API operation that you submit. If you specify a SellerSKU that identifies a variation parent ASIN, this operation returns an error. A variation parent ASIN represents a generic product that cannot be sold. Variation child ASINs represent products that have specific characteristics (such as size and color) and can be sold.  | [optional]
 **asINList** | [**List&lt;String&gt;**](String.md)| A list of ASIN values. Used to identify items for which you want inbound guidance for shipment to Amazon&#39;s fulfillment network. Note: If you specify a ASIN that identifies a variation parent ASIN, this operation returns an error. A variation parent ASIN represents a generic product that cannot be sold. Variation child ASINs represent products that have specific characteristics (such as size and color) and can be sold. | [optional]

### Return type

[**GetInboundGuidanceResponse**](GetInboundGuidanceResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getLabels"></a>
# **getLabels**
> GetLabelsResponse getLabels(shipmentId, pageType, labelType, numberOfPackages, packageLabelsToPrint, numberOfPallets)



Returns package/pallet labels for faster and more accurate shipment processing at the Amazon fulfillment center.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 2 | 30 |  For more information, see \&quot;Usage Plans and Rate Limits\&quot; in the Selling Partner API documentation.

### Example
```java
// Import classes:
//import com.amazon.spapi.fulfillmentinbound.ApiException;
//import com.amazon.spapi.fulfillmentinbound.api.FbaInboundApi;


FbaInboundApi apiInstance = new FbaInboundApi();
String shipmentId = "shipmentId_example"; // String | A shipment identifier originally returned by the createInboundShipmentPlan operation.
String pageType = "pageType_example"; // String | The page type to use to print the labels. Submitting a PageType value that is not supported in your marketplace returns an error.
String labelType = "labelType_example"; // String | The type of labels requested. 
Integer numberOfPackages = 56; // Integer | The number of packages in the shipment.
List<String> packageLabelsToPrint = Arrays.asList("packageLabelsToPrint_example"); // List<String> | A list of identifiers that specify packages for which you want package labels printed.  Must match CartonId values previously passed using the FBA Inbound Shipment Carton Information Feed. If not, the operation returns the IncorrectPackageIdentifier error code.
Integer numberOfPallets = 56; // Integer | The number of pallets in the shipment. This returns four identical labels for each pallet.
try {
    GetLabelsResponse result = apiInstance.getLabels(shipmentId, pageType, labelType, numberOfPackages, packageLabelsToPrint, numberOfPallets);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling FbaInboundApi#getLabels");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **shipmentId** | **String**| A shipment identifier originally returned by the createInboundShipmentPlan operation. |
 **pageType** | **String**| The page type to use to print the labels. Submitting a PageType value that is not supported in your marketplace returns an error. | [enum: PackageLabel_Letter_2, PackageLabel_Letter_4, PackageLabel_Letter_6, PackageLabel_Letter_6_CarrierLeft, PackageLabel_A4_2, PackageLabel_A4_4, PackageLabel_Plain_Paper, PackageLabel_Plain_Paper_CarrierBottom, PackageLabel_Thermal, PackageLabel_Thermal_Unified, PackageLabel_Thermal_NonPCP, PackageLabel_Thermal_No_Carrier_Rotation]
 **labelType** | **String**| The type of labels requested.  | [enum: DEFAULT, UNIQUE, PALLET]
 **numberOfPackages** | **Integer**| The number of packages in the shipment. | [optional]
 **packageLabelsToPrint** | [**List&lt;String&gt;**](String.md)| A list of identifiers that specify packages for which you want package labels printed.  Must match CartonId values previously passed using the FBA Inbound Shipment Carton Information Feed. If not, the operation returns the IncorrectPackageIdentifier error code. | [optional]
 **numberOfPallets** | **Integer**| The number of pallets in the shipment. This returns four identical labels for each pallet. | [optional]

### Return type

[**GetLabelsResponse**](GetLabelsResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getPreorderInfo"></a>
# **getPreorderInfo**
> GetPreorderInfoResponse getPreorderInfo(shipmentId, marketplaceId)



Returns pre-order information, including dates, that a seller needs before confirming a shipment for pre-order.   **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 2 | 30 |  For more information, see \&quot;Usage Plans and Rate Limits\&quot; in the Selling Partner API documentation.

### Example
```java
// Import classes:
//import com.amazon.spapi.fulfillmentinbound.ApiException;
//import com.amazon.spapi.fulfillmentinbound.api.FbaInboundApi;


FbaInboundApi apiInstance = new FbaInboundApi();
String shipmentId = "shipmentId_example"; // String | A shipment identifier originally returned by the createInboundShipmentPlan operation.
String marketplaceId = "marketplaceId_example"; // String | A marketplace identifier. Specifies the marketplace the shipment is tied to.
try {
    GetPreorderInfoResponse result = apiInstance.getPreorderInfo(shipmentId, marketplaceId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling FbaInboundApi#getPreorderInfo");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **shipmentId** | **String**| A shipment identifier originally returned by the createInboundShipmentPlan operation. |
 **marketplaceId** | **String**| A marketplace identifier. Specifies the marketplace the shipment is tied to. |

### Return type

[**GetPreorderInfoResponse**](GetPreorderInfoResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getPrepInstructions"></a>
# **getPrepInstructions**
> GetPrepInstructionsResponse getPrepInstructions(shipToCountryCode, sellerSKUList, asINList)



Returns labeling requirements and item preparation instructions to help prepare items for shipment to Amazon&#39;s fulfillment network.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 2 | 30 |  For more information, see \&quot;Usage Plans and Rate Limits\&quot; in the Selling Partner API documentation.

### Example
```java
// Import classes:
//import com.amazon.spapi.fulfillmentinbound.ApiException;
//import com.amazon.spapi.fulfillmentinbound.api.FbaInboundApi;


FbaInboundApi apiInstance = new FbaInboundApi();
String shipToCountryCode = "shipToCountryCode_example"; // String | The country code of the country to which the items will be shipped. Note that labeling requirements and item preparation instructions can vary by country.
List<String> sellerSKUList = Arrays.asList("sellerSKUList_example"); // List<String> | A list of SellerSKU values. Used to identify items for which you want labeling requirements and item preparation instructions for shipment to Amazon's fulfillment network. The SellerSKU is qualified by the Seller ID, which is included with every call to the Seller Partner API.  Note: Include seller SKUs that you have used to list items on Amazon's retail website. If you include a seller SKU that you have never used to list an item on Amazon's retail website, the seller SKU is returned in the InvalidSKUList property in the response.
List<String> asINList = Arrays.asList("asINList_example"); // List<String> | A list of ASIN values. Used to identify items for which you want item preparation instructions to help with item sourcing decisions.  Note: ASINs must be included in the product catalog for at least one of the marketplaces that the seller  participates in. Any ASIN that is not included in the product catalog for at least one of the marketplaces that the seller participates in is returned in the InvalidASINList property in the response. You can find out which marketplaces a seller participates in by calling the getMarketplaceParticipations operation in the Selling Partner API for Sellers.
try {
    GetPrepInstructionsResponse result = apiInstance.getPrepInstructions(shipToCountryCode, sellerSKUList, asINList);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling FbaInboundApi#getPrepInstructions");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **shipToCountryCode** | **String**| The country code of the country to which the items will be shipped. Note that labeling requirements and item preparation instructions can vary by country. |
 **sellerSKUList** | [**List&lt;String&gt;**](String.md)| A list of SellerSKU values. Used to identify items for which you want labeling requirements and item preparation instructions for shipment to Amazon&#39;s fulfillment network. The SellerSKU is qualified by the Seller ID, which is included with every call to the Seller Partner API.  Note: Include seller SKUs that you have used to list items on Amazon&#39;s retail website. If you include a seller SKU that you have never used to list an item on Amazon&#39;s retail website, the seller SKU is returned in the InvalidSKUList property in the response. | [optional]
 **asINList** | [**List&lt;String&gt;**](String.md)| A list of ASIN values. Used to identify items for which you want item preparation instructions to help with item sourcing decisions.  Note: ASINs must be included in the product catalog for at least one of the marketplaces that the seller  participates in. Any ASIN that is not included in the product catalog for at least one of the marketplaces that the seller participates in is returned in the InvalidASINList property in the response. You can find out which marketplaces a seller participates in by calling the getMarketplaceParticipations operation in the Selling Partner API for Sellers. | [optional]

### Return type

[**GetPrepInstructionsResponse**](GetPrepInstructionsResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getShipmentItems"></a>
# **getShipmentItems**
> GetShipmentItemsResponse getShipmentItems(queryType, marketplaceId, lastUpdatedAfter, lastUpdatedBefore, nextToken)



Returns a list of items in a specified inbound shipment, or a list of items that were updated within a specified time frame.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 2 | 30 |  For more information, see \&quot;Usage Plans and Rate Limits\&quot; in the Selling Partner API documentation.

### Example
```java
// Import classes:
//import com.amazon.spapi.fulfillmentinbound.ApiException;
//import com.amazon.spapi.fulfillmentinbound.api.FbaInboundApi;


FbaInboundApi apiInstance = new FbaInboundApi();
String queryType = "queryType_example"; // String | Indicates whether items are returned using a date range (by providing the LastUpdatedAfter and LastUpdatedBefore parameters), or using NextToken, which continues returning items specified in a previous request.
String marketplaceId = "marketplaceId_example"; // String | A marketplace identifier. Specifies the marketplace where the product would be stored.
OffsetDateTime lastUpdatedAfter = OffsetDateTime.now(); // OffsetDateTime | A date used for selecting inbound shipment items that were last updated after (or at) a specified time. The selection includes updates made by Amazon and by the seller.
OffsetDateTime lastUpdatedBefore = OffsetDateTime.now(); // OffsetDateTime | A date used for selecting inbound shipment items that were last updated before (or at) a specified time. The selection includes updates made by Amazon and by the seller.
String nextToken = "nextToken_example"; // String | A string token returned in the response to your previous request.
try {
    GetShipmentItemsResponse result = apiInstance.getShipmentItems(queryType, marketplaceId, lastUpdatedAfter, lastUpdatedBefore, nextToken);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling FbaInboundApi#getShipmentItems");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **queryType** | **String**| Indicates whether items are returned using a date range (by providing the LastUpdatedAfter and LastUpdatedBefore parameters), or using NextToken, which continues returning items specified in a previous request. | [enum: DATE_RANGE, NEXT_TOKEN]
 **marketplaceId** | **String**| A marketplace identifier. Specifies the marketplace where the product would be stored. |
 **lastUpdatedAfter** | **OffsetDateTime**| A date used for selecting inbound shipment items that were last updated after (or at) a specified time. The selection includes updates made by Amazon and by the seller. | [optional]
 **lastUpdatedBefore** | **OffsetDateTime**| A date used for selecting inbound shipment items that were last updated before (or at) a specified time. The selection includes updates made by Amazon and by the seller. | [optional]
 **nextToken** | **String**| A string token returned in the response to your previous request. | [optional]

### Return type

[**GetShipmentItemsResponse**](GetShipmentItemsResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getShipmentItemsByShipmentId"></a>
# **getShipmentItemsByShipmentId**
> GetShipmentItemsResponse getShipmentItemsByShipmentId(shipmentId, marketplaceId)



Returns a list of items in a specified inbound shipment.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 2 | 30 |  For more information, see \&quot;Usage Plans and Rate Limits\&quot; in the Selling Partner API documentation.

### Example
```java
// Import classes:
//import com.amazon.spapi.fulfillmentinbound.ApiException;
//import com.amazon.spapi.fulfillmentinbound.api.FbaInboundApi;


FbaInboundApi apiInstance = new FbaInboundApi();
String shipmentId = "shipmentId_example"; // String | A shipment identifier used for selecting items in a specific inbound shipment.
String marketplaceId = "marketplaceId_example"; // String | A marketplace identifier. Specifies the marketplace where the product would be stored.
try {
    GetShipmentItemsResponse result = apiInstance.getShipmentItemsByShipmentId(shipmentId, marketplaceId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling FbaInboundApi#getShipmentItemsByShipmentId");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **shipmentId** | **String**| A shipment identifier used for selecting items in a specific inbound shipment. |
 **marketplaceId** | **String**| A marketplace identifier. Specifies the marketplace where the product would be stored. |

### Return type

[**GetShipmentItemsResponse**](GetShipmentItemsResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getShipments"></a>
# **getShipments**
> GetShipmentsResponse getShipments(queryType, marketplaceId, shipmentStatusList, shipmentIdList, lastUpdatedAfter, lastUpdatedBefore, nextToken)



Returns a list of inbound shipments based on criteria that you specify.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 2 | 30 |  For more information, see \&quot;Usage Plans and Rate Limits\&quot; in the Selling Partner API documentation.

### Example
```java
// Import classes:
//import com.amazon.spapi.fulfillmentinbound.ApiException;
//import com.amazon.spapi.fulfillmentinbound.api.FbaInboundApi;


FbaInboundApi apiInstance = new FbaInboundApi();
String queryType = "queryType_example"; // String | Indicates whether shipments are returned using shipment information (by providing the ShipmentStatusList or ShipmentIdList parameters), using a date range (by providing the LastUpdatedAfter and LastUpdatedBefore parameters), or by using NextToken to continue returning items specified in a previous request.
String marketplaceId = "marketplaceId_example"; // String | A marketplace identifier. Specifies the marketplace where the product would be stored.
List<String> shipmentStatusList = Arrays.asList("shipmentStatusList_example"); // List<String> | A list of ShipmentStatus values. Used to select shipments with a current status that matches the status values that you specify.
List<String> shipmentIdList = Arrays.asList("shipmentIdList_example"); // List<String> | A list of shipment IDs used to select the shipments that you want. If both ShipmentStatusList and ShipmentIdList are specified, only shipments that match both parameters are returned.
OffsetDateTime lastUpdatedAfter = OffsetDateTime.now(); // OffsetDateTime | A date used for selecting inbound shipments that were last updated after (or at) a specified time. The selection includes updates made by Amazon and by the seller.
OffsetDateTime lastUpdatedBefore = OffsetDateTime.now(); // OffsetDateTime | A date used for selecting inbound shipments that were last updated before (or at) a specified time. The selection includes updates made by Amazon and by the seller.
String nextToken = "nextToken_example"; // String | A string token returned in the response to your previous request.
try {
    GetShipmentsResponse result = apiInstance.getShipments(queryType, marketplaceId, shipmentStatusList, shipmentIdList, lastUpdatedAfter, lastUpdatedBefore, nextToken);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling FbaInboundApi#getShipments");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **queryType** | **String**| Indicates whether shipments are returned using shipment information (by providing the ShipmentStatusList or ShipmentIdList parameters), using a date range (by providing the LastUpdatedAfter and LastUpdatedBefore parameters), or by using NextToken to continue returning items specified in a previous request. | [enum: SHIPMENT, DATE_RANGE, NEXT_TOKEN]
 **marketplaceId** | **String**| A marketplace identifier. Specifies the marketplace where the product would be stored. |
 **shipmentStatusList** | [**List&lt;String&gt;**](String.md)| A list of ShipmentStatus values. Used to select shipments with a current status that matches the status values that you specify. | [optional] [enum: WORKING, SHIPPED, RECEIVING, CANCELLED, DELETED, CLOSED, ERROR, IN_TRANSIT, DELIVERED, CHECKED_IN]
 **shipmentIdList** | [**List&lt;String&gt;**](String.md)| A list of shipment IDs used to select the shipments that you want. If both ShipmentStatusList and ShipmentIdList are specified, only shipments that match both parameters are returned. | [optional]
 **lastUpdatedAfter** | **OffsetDateTime**| A date used for selecting inbound shipments that were last updated after (or at) a specified time. The selection includes updates made by Amazon and by the seller. | [optional]
 **lastUpdatedBefore** | **OffsetDateTime**| A date used for selecting inbound shipments that were last updated before (or at) a specified time. The selection includes updates made by Amazon and by the seller. | [optional]
 **nextToken** | **String**| A string token returned in the response to your previous request. | [optional]

### Return type

[**GetShipmentsResponse**](GetShipmentsResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getTransportDetails"></a>
# **getTransportDetails**
> GetTransportDetailsResponse getTransportDetails(shipmentId)



Returns current transportation information about an inbound shipment.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 2 | 30 |  For more information, see \&quot;Usage Plans and Rate Limits\&quot; in the Selling Partner API documentation.

### Example
```java
// Import classes:
//import com.amazon.spapi.fulfillmentinbound.ApiException;
//import com.amazon.spapi.fulfillmentinbound.api.FbaInboundApi;


FbaInboundApi apiInstance = new FbaInboundApi();
String shipmentId = "shipmentId_example"; // String | A shipment identifier originally returned by the createInboundShipmentPlan operation.
try {
    GetTransportDetailsResponse result = apiInstance.getTransportDetails(shipmentId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling FbaInboundApi#getTransportDetails");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **shipmentId** | **String**| A shipment identifier originally returned by the createInboundShipmentPlan operation. |

### Return type

[**GetTransportDetailsResponse**](GetTransportDetailsResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="putTransportDetails"></a>
# **putTransportDetails**
> PutTransportDetailsResponse putTransportDetails(shipmentId, body)



Sends transportation information to Amazon about an inbound shipment.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 2 | 30 |  For more information, see \&quot;Usage Plans and Rate Limits\&quot; in the Selling Partner API documentation.

### Example
```java
// Import classes:
//import com.amazon.spapi.fulfillmentinbound.ApiException;
//import com.amazon.spapi.fulfillmentinbound.api.FbaInboundApi;


FbaInboundApi apiInstance = new FbaInboundApi();
String shipmentId = "shipmentId_example"; // String | A shipment identifier originally returned by the createInboundShipmentPlan operation.
PutTransportDetailsRequest body = new PutTransportDetailsRequest(); // PutTransportDetailsRequest | 
try {
    PutTransportDetailsResponse result = apiInstance.putTransportDetails(shipmentId, body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling FbaInboundApi#putTransportDetails");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **shipmentId** | **String**| A shipment identifier originally returned by the createInboundShipmentPlan operation. |
 **body** | [**PutTransportDetailsRequest**](PutTransportDetailsRequest.md)|  |

### Return type

[**PutTransportDetailsResponse**](PutTransportDetailsResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="updateInboundShipment"></a>
# **updateInboundShipment**
> InboundShipmentResponse updateInboundShipment(body, shipmentId)



Adds, updates, or removes items from the inbound shipment identified by the specified shipment identifier.   **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 2 | 30 |  For more information, see \&quot;Usage Plans and Rate Limits\&quot; in the Selling Partner API documentation.

### Example
```java
// Import classes:
//import com.amazon.spapi.fulfillmentinbound.ApiException;
//import com.amazon.spapi.fulfillmentinbound.api.FbaInboundApi;


FbaInboundApi apiInstance = new FbaInboundApi();
InboundShipmentRequest body = new InboundShipmentRequest(); // InboundShipmentRequest | 
String shipmentId = "shipmentId_example"; // String | A shipment identifier originally returned by the createInboundShipmentPlan operation.
try {
    InboundShipmentResponse result = apiInstance.updateInboundShipment(body, shipmentId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling FbaInboundApi#updateInboundShipment");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**InboundShipmentRequest**](InboundShipmentRequest.md)|  |
 **shipmentId** | **String**| A shipment identifier originally returned by the createInboundShipmentPlan operation. |

### Return type

[**InboundShipmentResponse**](InboundShipmentResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="voidTransport"></a>
# **voidTransport**
> VoidTransportResponse voidTransport(shipmentId)



Cancels a previously-confirmed request to ship an inbound shipment using an Amazon-partnered carrier.  To be successful, you must call this operation before the VoidDeadline date that is returned by the getTransportDetails operation.  Important: The VoidDeadline date is 24 hours after you confirm a Small Parcel shipment transportation request or one hour after you confirm a Less Than Truckload/Full Truckload (LTL/FTL) shipment transportation request. After the void deadline passes, your account will be charged for the shipping cost.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 2 | 30 |  For more information, see \&quot;Usage Plans and Rate Limits\&quot; in the Selling Partner API documentation.

### Example
```java
// Import classes:
//import com.amazon.spapi.fulfillmentinbound.ApiException;
//import com.amazon.spapi.fulfillmentinbound.api.FbaInboundApi;


FbaInboundApi apiInstance = new FbaInboundApi();
String shipmentId = "shipmentId_example"; // String | A shipment identifier originally returned by the createInboundShipmentPlan operation.
try {
    VoidTransportResponse result = apiInstance.voidTransport(shipmentId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling FbaInboundApi#voidTransport");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **shipmentId** | **String**| A shipment identifier originally returned by the createInboundShipmentPlan operation. |

### Return type

[**VoidTransportResponse**](VoidTransportResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

