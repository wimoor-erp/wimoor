# ServiceApi

All URIs are relative to *https://sellingpartnerapi-na.amazon.com*

Method | HTTP request | Description
------------- | ------------- | -------------
[**addAppointmentForServiceJobByServiceJobId**](ServiceApi.md#addAppointmentForServiceJobByServiceJobId) | **POST** /service/v1/serviceJobs/{serviceJobId}/appointments | 
[**cancelServiceJobByServiceJobId**](ServiceApi.md#cancelServiceJobByServiceJobId) | **PUT** /service/v1/serviceJobs/{serviceJobId}/cancellations | 
[**completeServiceJobByServiceJobId**](ServiceApi.md#completeServiceJobByServiceJobId) | **PUT** /service/v1/serviceJobs/{serviceJobId}/completions | 
[**getServiceJobByServiceJobId**](ServiceApi.md#getServiceJobByServiceJobId) | **GET** /service/v1/serviceJobs/{serviceJobId} | 
[**getServiceJobs**](ServiceApi.md#getServiceJobs) | **GET** /service/v1/serviceJobs | 
[**rescheduleAppointmentForServiceJobByServiceJobId**](ServiceApi.md#rescheduleAppointmentForServiceJobByServiceJobId) | **POST** /service/v1/serviceJobs/{serviceJobId}/appointments/{appointmentId} | 


<a name="addAppointmentForServiceJobByServiceJobId"></a>
# **addAppointmentForServiceJobByServiceJobId**
> SetAppointmentResponse addAppointmentForServiceJobByServiceJobId(serviceJobId, body)



Adds an appointment to the service job indicated by the service job identifier you specify.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 5 | 20 |  For more information, see \&quot;Usage Plans and Rate Limits\&quot; in the Selling Partner API documentation.

### Example
```java
// Import classes:
//import com.amazon.spapi.services.ApiException;
//import com.amazon.spapi.services.api.ServiceApi;


ServiceApi apiInstance = new ServiceApi();
String serviceJobId = "serviceJobId_example"; // String | An Amazon defined service job identifier.
AddAppointmentRequest body = new AddAppointmentRequest(); // AddAppointmentRequest | Add appointment operation input details.
try {
    SetAppointmentResponse result = apiInstance.addAppointmentForServiceJobByServiceJobId(serviceJobId, body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ServiceApi#addAppointmentForServiceJobByServiceJobId");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **serviceJobId** | **String**| An Amazon defined service job identifier. |
 **body** | [**AddAppointmentRequest**](AddAppointmentRequest.md)| Add appointment operation input details. |

### Return type

[**SetAppointmentResponse**](SetAppointmentResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="cancelServiceJobByServiceJobId"></a>
# **cancelServiceJobByServiceJobId**
> CancelServiceJobByServiceJobIdResponse cancelServiceJobByServiceJobId(serviceJobId, cancellationReasonCode)



Cancels the service job indicated by the service job identifier you specify.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 5 | 20 |  For more information, see \&quot;Usage Plans and Rate Limits\&quot; in the Selling Partner API documentation.

### Example
```java
// Import classes:
//import com.amazon.spapi.services.ApiException;
//import com.amazon.spapi.services.api.ServiceApi;


ServiceApi apiInstance = new ServiceApi();
String serviceJobId = "serviceJobId_example"; // String | An Amazon defined service job identifier.
String cancellationReasonCode = "cancellationReasonCode_example"; // String | A cancel reason code that specifies the reason for cancelling a service job.
try {
    CancelServiceJobByServiceJobIdResponse result = apiInstance.cancelServiceJobByServiceJobId(serviceJobId, cancellationReasonCode);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ServiceApi#cancelServiceJobByServiceJobId");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **serviceJobId** | **String**| An Amazon defined service job identifier. |
 **cancellationReasonCode** | **String**| A cancel reason code that specifies the reason for cancelling a service job. |

### Return type

[**CancelServiceJobByServiceJobIdResponse**](CancelServiceJobByServiceJobIdResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="completeServiceJobByServiceJobId"></a>
# **completeServiceJobByServiceJobId**
> CompleteServiceJobByServiceJobIdResponse completeServiceJobByServiceJobId(serviceJobId)



Completes the service job indicated by the service job identifier you specify.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 5 | 20 |  For more information, see \&quot;Usage Plans and Rate Limits\&quot; in the Selling Partner API documentation.

### Example
```java
// Import classes:
//import com.amazon.spapi.services.ApiException;
//import com.amazon.spapi.services.api.ServiceApi;


ServiceApi apiInstance = new ServiceApi();
String serviceJobId = "serviceJobId_example"; // String | An Amazon defined service job identifier.
try {
    CompleteServiceJobByServiceJobIdResponse result = apiInstance.completeServiceJobByServiceJobId(serviceJobId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ServiceApi#completeServiceJobByServiceJobId");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **serviceJobId** | **String**| An Amazon defined service job identifier. |

### Return type

[**CompleteServiceJobByServiceJobIdResponse**](CompleteServiceJobByServiceJobIdResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getServiceJobByServiceJobId"></a>
# **getServiceJobByServiceJobId**
> GetServiceJobByServiceJobIdResponse getServiceJobByServiceJobId(serviceJobId)



Gets service job details for the service job indicated by the service job identifier you specify.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 20 | 40 |  For more information, see \&quot;Usage Plans and Rate Limits\&quot; in the Selling Partner API documentation.

### Example
```java
// Import classes:
//import com.amazon.spapi.services.ApiException;
//import com.amazon.spapi.services.api.ServiceApi;


ServiceApi apiInstance = new ServiceApi();
String serviceJobId = "serviceJobId_example"; // String | A service job identifier.
try {
    GetServiceJobByServiceJobIdResponse result = apiInstance.getServiceJobByServiceJobId(serviceJobId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ServiceApi#getServiceJobByServiceJobId");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **serviceJobId** | **String**| A service job identifier. |

### Return type

[**GetServiceJobByServiceJobIdResponse**](GetServiceJobByServiceJobIdResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getServiceJobs"></a>
# **getServiceJobs**
> GetServiceJobsResponse getServiceJobs(marketplaceIds, serviceOrderIds, serviceJobStatus, pageToken, pageSize, sortField, sortOrder, createdAfter, createdBefore, lastUpdatedAfter, lastUpdatedBefore, scheduleStartDate, scheduleEndDate)



Gets service job details for the specified filter query.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 10 | 40 |  For more information, see \&quot;Usage Plans and Rate Limits\&quot; in the Selling Partner API documentation.

### Example
```java
// Import classes:
//import com.amazon.spapi.services.ApiException;
//import com.amazon.spapi.services.api.ServiceApi;


ServiceApi apiInstance = new ServiceApi();
List<String> marketplaceIds = Arrays.asList("marketplaceIds_example"); // List<String> | Used to select jobs that were placed in the specified marketplaces. 
List<String> serviceOrderIds = Arrays.asList("serviceOrderIds_example"); // List<String> | List of service order ids for the query you want to perform.Max values supported 20. 
List<String> serviceJobStatus = Arrays.asList("serviceJobStatus_example"); // List<String> | A list of one or more job status by which to filter the list of jobs.
String pageToken = "pageToken_example"; // String | String returned in the response of your previous request.
Integer pageSize = 20; // Integer | A non-negative integer that indicates the maximum number of jobs to return in the list, Value must be 1 - 20. Default 20. 
String sortField = "sortField_example"; // String | Sort fields on which you want to sort the output.
String sortOrder = "sortOrder_example"; // String | Sort order for the query you want to perform.
String createdAfter = "createdAfter_example"; // String | A date used for selecting jobs created after (or at) a specified time must be in ISO 8601 format. Required if LastUpdatedAfter is not specified.Specifying both CreatedAfter and LastUpdatedAfter returns an error. 
String createdBefore = "createdBefore_example"; // String | A date used for selecting jobs created before (or at) a specified time must be in ISO 8601 format. 
String lastUpdatedAfter = "lastUpdatedAfter_example"; // String | A date used for selecting jobs updated after (or at) a specified time must be in ISO 8601 format. Required if createdAfter is not specified.Specifying both CreatedAfter and LastUpdatedAfter returns an error. 
String lastUpdatedBefore = "lastUpdatedBefore_example"; // String | A date used for selecting jobs updated before (or at) a specified time must be in ISO 8601 format. 
String scheduleStartDate = "scheduleStartDate_example"; // String | A date used for filtering jobs schedule after (or at) a specified time must be in ISO 8601 format. schedule end date should not be earlier than schedule start date. 
String scheduleEndDate = "scheduleEndDate_example"; // String | A date used for filtering jobs schedule before (or at) a specified time must be in ISO 8601 format. schedule end date should not be earlier than schedule start date. 
try {
    GetServiceJobsResponse result = apiInstance.getServiceJobs(marketplaceIds, serviceOrderIds, serviceJobStatus, pageToken, pageSize, sortField, sortOrder, createdAfter, createdBefore, lastUpdatedAfter, lastUpdatedBefore, scheduleStartDate, scheduleEndDate);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ServiceApi#getServiceJobs");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **marketplaceIds** | [**List&lt;String&gt;**](String.md)| Used to select jobs that were placed in the specified marketplaces.  |
 **serviceOrderIds** | [**List&lt;String&gt;**](String.md)| List of service order ids for the query you want to perform.Max values supported 20.  | [optional]
 **serviceJobStatus** | [**List&lt;String&gt;**](String.md)| A list of one or more job status by which to filter the list of jobs. | [optional] [enum: NOT_SERVICED, CANCELLED, COMPLETED, PENDING_SCHEDULE, NOT_FULFILLABLE, HOLD, PAYMENT_DECLINED]
 **pageToken** | **String**| String returned in the response of your previous request. | [optional]
 **pageSize** | **Integer**| A non-negative integer that indicates the maximum number of jobs to return in the list, Value must be 1 - 20. Default 20.  | [optional] [default to 20]
 **sortField** | **String**| Sort fields on which you want to sort the output. | [optional] [enum: JOB_DATE, JOB_STATUS]
 **sortOrder** | **String**| Sort order for the query you want to perform. | [optional] [enum: ASC, DESC]
 **createdAfter** | **String**| A date used for selecting jobs created after (or at) a specified time must be in ISO 8601 format. Required if LastUpdatedAfter is not specified.Specifying both CreatedAfter and LastUpdatedAfter returns an error.  | [optional]
 **createdBefore** | **String**| A date used for selecting jobs created before (or at) a specified time must be in ISO 8601 format.  | [optional]
 **lastUpdatedAfter** | **String**| A date used for selecting jobs updated after (or at) a specified time must be in ISO 8601 format. Required if createdAfter is not specified.Specifying both CreatedAfter and LastUpdatedAfter returns an error.  | [optional]
 **lastUpdatedBefore** | **String**| A date used for selecting jobs updated before (or at) a specified time must be in ISO 8601 format.  | [optional]
 **scheduleStartDate** | **String**| A date used for filtering jobs schedule after (or at) a specified time must be in ISO 8601 format. schedule end date should not be earlier than schedule start date.  | [optional]
 **scheduleEndDate** | **String**| A date used for filtering jobs schedule before (or at) a specified time must be in ISO 8601 format. schedule end date should not be earlier than schedule start date.  | [optional]

### Return type

[**GetServiceJobsResponse**](GetServiceJobsResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="rescheduleAppointmentForServiceJobByServiceJobId"></a>
# **rescheduleAppointmentForServiceJobByServiceJobId**
> SetAppointmentResponse rescheduleAppointmentForServiceJobByServiceJobId(serviceJobId, appointmentId, body)



Reschedules an appointment for the service job indicated by the service job identifier you specify.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 5 | 20 |  For more information, see \&quot;Usage Plans and Rate Limits\&quot; in the Selling Partner API documentation.

### Example
```java
// Import classes:
//import com.amazon.spapi.services.ApiException;
//import com.amazon.spapi.services.api.ServiceApi;


ServiceApi apiInstance = new ServiceApi();
String serviceJobId = "serviceJobId_example"; // String | An Amazon defined service job identifier.
String appointmentId = "appointmentId_example"; // String | An existing appointment identifier for the Service Job.
RescheduleAppointmentRequest body = new RescheduleAppointmentRequest(); // RescheduleAppointmentRequest | Reschedule appointment operation input details.
try {
    SetAppointmentResponse result = apiInstance.rescheduleAppointmentForServiceJobByServiceJobId(serviceJobId, appointmentId, body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ServiceApi#rescheduleAppointmentForServiceJobByServiceJobId");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **serviceJobId** | **String**| An Amazon defined service job identifier. |
 **appointmentId** | **String**| An existing appointment identifier for the Service Job. |
 **body** | [**RescheduleAppointmentRequest**](RescheduleAppointmentRequest.md)| Reschedule appointment operation input details. |

### Return type

[**SetAppointmentResponse**](SetAppointmentResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

