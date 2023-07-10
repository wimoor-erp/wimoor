# ReportsApi

All URIs are relative to *https://sellingpartnerapi-na.amazon.com*

Method | HTTP request | Description
------------- | ------------- | -------------
[**cancelReport**](ReportsApi.md#cancelReport) | **DELETE** /reports/2020-09-04/reports/{reportId} | 
[**cancelReportSchedule**](ReportsApi.md#cancelReportSchedule) | **DELETE** /reports/2020-09-04/schedules/{reportScheduleId} | 
[**createReport**](ReportsApi.md#createReport) | **POST** /reports/2020-09-04/reports | 
[**createReportSchedule**](ReportsApi.md#createReportSchedule) | **POST** /reports/2020-09-04/schedules | 
[**getReport**](ReportsApi.md#getReport) | **GET** /reports/2020-09-04/reports/{reportId} | 
[**getReportDocument**](ReportsApi.md#getReportDocument) | **GET** /reports/2020-09-04/documents/{reportDocumentId} | 
[**getReportSchedule**](ReportsApi.md#getReportSchedule) | **GET** /reports/2020-09-04/schedules/{reportScheduleId} | 
[**getReportSchedules**](ReportsApi.md#getReportSchedules) | **GET** /reports/2020-09-04/schedules | 
[**getReports**](ReportsApi.md#getReports) | **GET** /reports/2020-09-04/reports | 


<a name="cancelReport"></a>
# **cancelReport**
> CancelReportResponse cancelReport(reportId)



Cancels the report that you specify. Only reports with processingStatus&#x3D;IN_QUEUE can be cancelled. Cancelled reports are returned in subsequent calls to the getReport and getReports operations.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 0.0222 | 10 |  For more information, see \&quot;Usage Plans and Rate Limits\&quot; in the Selling Partner API documentation.

### Example
```java
// Import classes:
//import com.amazon.spapi.reports.ApiException;
//import com.amazon.spapi.reports.api.ReportsApi;


ReportsApi apiInstance = new ReportsApi();
String reportId = "reportId_example"; // String | The identifier for the report. This identifier is unique only in combination with a seller ID.
try {
    CancelReportResponse result = apiInstance.cancelReport(reportId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ReportsApi#cancelReport");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **reportId** | **String**| The identifier for the report. This identifier is unique only in combination with a seller ID. |

### Return type

[**CancelReportResponse**](CancelReportResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="cancelReportSchedule"></a>
# **cancelReportSchedule**
> CancelReportScheduleResponse cancelReportSchedule(reportScheduleId)



Cancels the report schedule that you specify.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 0.0222 | 10 |  For more information, see \&quot;Usage Plans and Rate Limits\&quot; in the Selling Partner API documentation.

### Example
```java
// Import classes:
//import com.amazon.spapi.reports.ApiException;
//import com.amazon.spapi.reports.api.ReportsApi;


ReportsApi apiInstance = new ReportsApi();
String reportScheduleId = "reportScheduleId_example"; // String | The identifier for the report schedule. This identifier is unique only in combination with a seller ID.
try {
    CancelReportScheduleResponse result = apiInstance.cancelReportSchedule(reportScheduleId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ReportsApi#cancelReportSchedule");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **reportScheduleId** | **String**| The identifier for the report schedule. This identifier is unique only in combination with a seller ID. |

### Return type

[**CancelReportScheduleResponse**](CancelReportScheduleResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="createReport"></a>
# **createReport**
> CreateReportResponse createReport(body)



Creates a report.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 0.0167 | 15 |  For more information, see \&quot;Usage Plans and Rate Limits\&quot; in the Selling Partner API documentation.

### Example
```java
// Import classes:
//import com.amazon.spapi.reports.ApiException;
//import com.amazon.spapi.reports.api.ReportsApi;


ReportsApi apiInstance = new ReportsApi();
CreateReportSpecification body = new CreateReportSpecification(); // CreateReportSpecification | 
try {
    CreateReportResponse result = apiInstance.createReport(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ReportsApi#createReport");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**CreateReportSpecification**](CreateReportSpecification.md)|  |

### Return type

[**CreateReportResponse**](CreateReportResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="createReportSchedule"></a>
# **createReportSchedule**
> CreateReportScheduleResponse createReportSchedule(body)



Creates a report schedule. If a report schedule with the same report type and marketplace IDs already exists, it will be cancelled and replaced with this one.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 0.0222 | 10 |  For more information, see \&quot;Usage Plans and Rate Limits\&quot; in the Selling Partner API documentation.

### Example
```java
// Import classes:
//import com.amazon.spapi.reports.ApiException;
//import com.amazon.spapi.reports.api.ReportsApi;


ReportsApi apiInstance = new ReportsApi();
CreateReportScheduleSpecification body = new CreateReportScheduleSpecification(); // CreateReportScheduleSpecification | 
try {
    CreateReportScheduleResponse result = apiInstance.createReportSchedule(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ReportsApi#createReportSchedule");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**CreateReportScheduleSpecification**](CreateReportScheduleSpecification.md)|  |

### Return type

[**CreateReportScheduleResponse**](CreateReportScheduleResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getReport"></a>
# **getReport**
> GetReportResponse getReport(reportId)



Returns report details (including the reportDocumentId, if available) for the report that you specify.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 2.0 | 15 |  For more information, see \&quot;Usage Plans and Rate Limits\&quot; in the Selling Partner API documentation.

### Example
```java
// Import classes:
//import com.amazon.spapi.reports.ApiException;
//import com.amazon.spapi.reports.api.ReportsApi;


ReportsApi apiInstance = new ReportsApi();
String reportId = "reportId_example"; // String | The identifier for the report. This identifier is unique only in combination with a seller ID.
try {
    GetReportResponse result = apiInstance.getReport(reportId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ReportsApi#getReport");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **reportId** | **String**| The identifier for the report. This identifier is unique only in combination with a seller ID. |

### Return type

[**GetReportResponse**](GetReportResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getReportDocument"></a>
# **getReportDocument**
> GetReportDocumentResponse getReportDocument(reportDocumentId)



Returns the information required for retrieving a report document&#39;s contents. This includes a presigned URL for the report document as well as the information required to decrypt the document&#39;s contents.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 0.0167 | 15 |  For more information, see \&quot;Usage Plans and Rate Limits\&quot; in the Selling Partner API documentation.

### Example
```java
// Import classes:
//import com.amazon.spapi.reports.ApiException;
//import com.amazon.spapi.reports.api.ReportsApi;


ReportsApi apiInstance = new ReportsApi();
String reportDocumentId = "reportDocumentId_example"; // String | The identifier for the report document.
try {
    GetReportDocumentResponse result = apiInstance.getReportDocument(reportDocumentId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ReportsApi#getReportDocument");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **reportDocumentId** | **String**| The identifier for the report document. |

### Return type

[**GetReportDocumentResponse**](GetReportDocumentResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getReportSchedule"></a>
# **getReportSchedule**
> GetReportScheduleResponse getReportSchedule(reportScheduleId)



Returns report schedule details for the report schedule that you specify.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 0.0222 | 10 |  For more information, see \&quot;Usage Plans and Rate Limits\&quot; in the Selling Partner API documentation.

### Example
```java
// Import classes:
//import com.amazon.spapi.reports.ApiException;
//import com.amazon.spapi.reports.api.ReportsApi;


ReportsApi apiInstance = new ReportsApi();
String reportScheduleId = "reportScheduleId_example"; // String | The identifier for the report schedule. This identifier is unique only in combination with a seller ID.
try {
    GetReportScheduleResponse result = apiInstance.getReportSchedule(reportScheduleId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ReportsApi#getReportSchedule");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **reportScheduleId** | **String**| The identifier for the report schedule. This identifier is unique only in combination with a seller ID. |

### Return type

[**GetReportScheduleResponse**](GetReportScheduleResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getReportSchedules"></a>
# **getReportSchedules**
> GetReportSchedulesResponse getReportSchedules(reportTypes)



Returns report schedule details that match the filters that you specify.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 0.0222 | 10 |  For more information, see \&quot;Usage Plans and Rate Limits\&quot; in the Selling Partner API documentation.

### Example
```java
// Import classes:
//import com.amazon.spapi.reports.ApiException;
//import com.amazon.spapi.reports.api.ReportsApi;


ReportsApi apiInstance = new ReportsApi();
List<String> reportTypes = Arrays.asList("reportTypes_example"); // List<String> | A list of report types used to filter report schedules.
try {
    GetReportSchedulesResponse result = apiInstance.getReportSchedules(reportTypes);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ReportsApi#getReportSchedules");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **reportTypes** | [**List&lt;String&gt;**](String.md)| A list of report types used to filter report schedules. |

### Return type

[**GetReportSchedulesResponse**](GetReportSchedulesResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getReports"></a>
# **getReports**
> GetReportsResponse getReports(reportTypes, processingStatuses, marketplaceIds, pageSize, createdSince, createdUntil, nextToken)



Returns report details for the reports that match the filters that you specify.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 0.0222 | 10 |  For more information, see \&quot;Usage Plans and Rate Limits\&quot; in the Selling Partner API documentation.

### Example
```java
// Import classes:
//import com.amazon.spapi.reports.ApiException;
//import com.amazon.spapi.reports.api.ReportsApi;


ReportsApi apiInstance = new ReportsApi();
List<String> reportTypes = Arrays.asList("reportTypes_example"); // List<String> | A list of report types used to filter reports. When reportTypes is provided, the other filter parameters (processingStatuses, marketplaceIds, createdSince, createdUntil) and pageSize may also be provided. Either reportTypes or nextToken is required.
List<String> processingStatuses = Arrays.asList("processingStatuses_example"); // List<String> | A list of processing statuses used to filter reports.
List<String> marketplaceIds = Arrays.asList("marketplaceIds_example"); // List<String> | A list of marketplace identifiers used to filter reports. The reports returned will match at least one of the marketplaces that you specify.
Integer pageSize = 10; // Integer | The maximum number of reports to return in a single call.
OffsetDateTime createdSince = OffsetDateTime.now(); // OffsetDateTime | The earliest report creation date and time for reports to include in the response, in ISO 8601 date time format. The default is 90 days ago. Reports are retained for a maximum of 90 days.
OffsetDateTime createdUntil = OffsetDateTime.now(); // OffsetDateTime | The latest report creation date and time for reports to include in the response, in ISO 8601 date time format. The default is now.
String nextToken = "nextToken_example"; // String | A string token returned in the response to your previous request. nextToken is returned when the number of results exceeds the specified pageSize value. To get the next page of results, call the getReports operation and include this token as the only parameter. Specifying nextToken with any other parameters will cause the request to fail.
try {
    GetReportsResponse result = apiInstance.getReports(reportTypes, processingStatuses, marketplaceIds, pageSize, createdSince, createdUntil, nextToken);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ReportsApi#getReports");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **reportTypes** | [**List&lt;String&gt;**](String.md)| A list of report types used to filter reports. When reportTypes is provided, the other filter parameters (processingStatuses, marketplaceIds, createdSince, createdUntil) and pageSize may also be provided. Either reportTypes or nextToken is required. | [optional]
 **processingStatuses** | [**List&lt;String&gt;**](String.md)| A list of processing statuses used to filter reports. | [optional] [enum: CANCELLED, DONE, FATAL, IN_PROGRESS, IN_QUEUE]
 **marketplaceIds** | [**List&lt;String&gt;**](String.md)| A list of marketplace identifiers used to filter reports. The reports returned will match at least one of the marketplaces that you specify. | [optional]
 **pageSize** | **Integer**| The maximum number of reports to return in a single call. | [optional] [default to 10]
 **createdSince** | **OffsetDateTime**| The earliest report creation date and time for reports to include in the response, in ISO 8601 date time format. The default is 90 days ago. Reports are retained for a maximum of 90 days. | [optional]
 **createdUntil** | **OffsetDateTime**| The latest report creation date and time for reports to include in the response, in ISO 8601 date time format. The default is now. | [optional]
 **nextToken** | **String**| A string token returned in the response to your previous request. nextToken is returned when the number of results exceeds the specified pageSize value. To get the next page of results, call the getReports operation and include this token as the only parameter. Specifying nextToken with any other parameters will cause the request to fail. | [optional]

### Return type

[**GetReportsResponse**](GetReportsResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

