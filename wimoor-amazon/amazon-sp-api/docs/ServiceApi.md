# ServiceApi

All URIs are relative to *https://sellingpartnerapi-na.amazon.com*

Method | HTTP request | Description
------------- | ------------- | -------------
[**addAppointmentForServiceJobByServiceJobId**](ServiceApi.md#addAppointmentForServiceJobByServiceJobId) | **POST** /service/v1/serviceJobs/{serviceJobId}/appointments | 
[**assignAppointmentResources**](ServiceApi.md#assignAppointmentResources) | **PUT** /service/v1/serviceJobs/{serviceJobId}/appointments/{appointmentId}/resources | 
[**cancelReservation**](ServiceApi.md#cancelReservation) | **DELETE** /service/v1/reservation/{reservationId} | 
[**cancelServiceJobByServiceJobId**](ServiceApi.md#cancelServiceJobByServiceJobId) | **PUT** /service/v1/serviceJobs/{serviceJobId}/cancellations | 
[**completeServiceJobByServiceJobId**](ServiceApi.md#completeServiceJobByServiceJobId) | **PUT** /service/v1/serviceJobs/{serviceJobId}/completions | 
[**createReservation**](ServiceApi.md#createReservation) | **POST** /service/v1/reservation | 
[**createServiceDocumentUploadDestination**](ServiceApi.md#createServiceDocumentUploadDestination) | **POST** /service/v1/documents | 
[**getAppointmentSlots**](ServiceApi.md#getAppointmentSlots) | **GET** /service/v1/appointmentSlots | 
[**getAppointmmentSlotsByJobId**](ServiceApi.md#getAppointmmentSlotsByJobId) | **GET** /service/v1/serviceJobs/{serviceJobId}/appointmentSlots | 
[**getFixedSlotCapacity**](ServiceApi.md#getFixedSlotCapacity) | **POST** /service/v1/serviceResources/{resourceId}/capacity/fixed | 
[**getRangeSlotCapacity**](ServiceApi.md#getRangeSlotCapacity) | **POST** /service/v1/serviceResources/{resourceId}/capacity/range | 
[**getServiceJobByServiceJobId**](ServiceApi.md#getServiceJobByServiceJobId) | **GET** /service/v1/serviceJobs/{serviceJobId} | 
[**getServiceJobs**](ServiceApi.md#getServiceJobs) | **GET** /service/v1/serviceJobs | 
[**rescheduleAppointmentForServiceJobByServiceJobId**](ServiceApi.md#rescheduleAppointmentForServiceJobByServiceJobId) | **POST** /service/v1/serviceJobs/{serviceJobId}/appointments/{appointmentId} | 
[**setAppointmentFulfillmentData**](ServiceApi.md#setAppointmentFulfillmentData) | **PUT** /service/v1/serviceJobs/{serviceJobId}/appointments/{appointmentId}/fulfillment | 
[**updateReservation**](ServiceApi.md#updateReservation) | **PUT** /service/v1/reservation/{reservationId} | 
[**updateSchedule**](ServiceApi.md#updateSchedule) | **PUT** /service/v1/serviceResources/{resourceId}/schedules | 


<a name="addAppointmentForServiceJobByServiceJobId"></a>
# **addAppointmentForServiceJobByServiceJobId**
> SetAppointmentResponse addAppointmentForServiceJobByServiceJobId(serviceJobId, body)



Adds an appointment to the service job indicated by the service job identifier specified.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 5 | 20 |  The &#x60;x-amzn-RateLimit-Limit&#x60; response header returns the usage plan rate limits that were applied to the requested operation, when available. The table above indicates the default rate and burst values for this operation. Selling partners whose business demands require higher throughput may see higher rate and burst values than those shown here. For more information, see [Usage Plans and Rate Limits in the Selling Partner API](doc:usage-plans-and-rate-limits-in-the-sp-api).

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.ServiceApi;


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

<a name="assignAppointmentResources"></a>
# **assignAppointmentResources**
> AssignAppointmentResourcesResponse assignAppointmentResources(serviceJobId, appointmentId, body)



Assigns new resource(s) or overwrite/update the existing one(s) to a service job appointment.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 1 | 2 |  The &#x60;x-amzn-RateLimit-Limit&#x60; response header returns the usage plan rate limits that were applied to the requested operation, when available. The table above indicates the default rate and burst values for this operation. Selling partners whose business demands require higher throughput may see higher rate and burst values than those shown here. For more information, see [Usage Plans and Rate Limits in the Selling Partner API](doc:usage-plans-and-rate-limits-in-the-sp-api).

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.ServiceApi;


ServiceApi apiInstance = new ServiceApi();
String serviceJobId = "serviceJobId_example"; // String | An Amazon-defined service job identifier. Get this value by calling the `getServiceJobs` operation of the Services API.
String appointmentId = "appointmentId_example"; // String | An Amazon-defined identifier of active service job appointment.
AssignAppointmentResourcesRequest body = new AssignAppointmentResourcesRequest(); // AssignAppointmentResourcesRequest | 
try {
    AssignAppointmentResourcesResponse result = apiInstance.assignAppointmentResources(serviceJobId, appointmentId, body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ServiceApi#assignAppointmentResources");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **serviceJobId** | **String**| An Amazon-defined service job identifier. Get this value by calling the &#x60;getServiceJobs&#x60; operation of the Services API. |
 **appointmentId** | **String**| An Amazon-defined identifier of active service job appointment. |
 **body** | [**AssignAppointmentResourcesRequest**](AssignAppointmentResourcesRequest.md)|  |

### Return type

[**AssignAppointmentResourcesResponse**](AssignAppointmentResourcesResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="cancelReservation"></a>
# **cancelReservation**
> CancelReservationResponse cancelReservation(reservationId, marketplaceIds)



Cancel a reservation.   **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 5 | 20 |  The &#x60;x-amzn-RateLimit-Limit&#x60; response header returns the usage plan rate limits that were applied to the requested operation, when available. The table above indicates the default rate and burst values for this operation. Selling partners whose business demands require higher throughput may see higher rate and burst values than those shown here. For more information, see [Usage Plans and Rate Limits in the Selling Partner API](doc:usage-plans-and-rate-limits-in-the-sp-api).

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.ServiceApi;


ServiceApi apiInstance = new ServiceApi();
String reservationId = "reservationId_example"; // String | Reservation Identifier
List<String> marketplaceIds = Arrays.asList("marketplaceIds_example"); // List<String> | An identifier for the marketplace in which the resource operates.
try {
    CancelReservationResponse result = apiInstance.cancelReservation(reservationId, marketplaceIds);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ServiceApi#cancelReservation");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **reservationId** | **String**| Reservation Identifier |
 **marketplaceIds** | [**List&lt;String&gt;**](String.md)| An identifier for the marketplace in which the resource operates. |

### Return type

[**CancelReservationResponse**](CancelReservationResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="cancelServiceJobByServiceJobId"></a>
# **cancelServiceJobByServiceJobId**
> CancelServiceJobByServiceJobIdResponse cancelServiceJobByServiceJobId(serviceJobId, cancellationReasonCode)



Cancels the service job indicated by the service job identifier specified.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 5 | 20 |  The &#x60;x-amzn-RateLimit-Limit&#x60; response header returns the usage plan rate limits that were applied to the requested operation, when available. The table above indicates the default rate and burst values for this operation. Selling partners whose business demands require higher throughput may see higher rate and burst values than those shown here. For more information, see [Usage Plans and Rate Limits in the Selling Partner API](doc:usage-plans-and-rate-limits-in-the-sp-api).

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.ServiceApi;


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



Completes the service job indicated by the service job identifier specified.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 5 | 20 |  The &#x60;x-amzn-RateLimit-Limit&#x60; response header returns the usage plan rate limits that were applied to the requested operation, when available. The table above indicates the default rate and burst values for this operation. Selling partners whose business demands require higher throughput may see higher rate and burst values than those shown here. For more information, see [Usage Plans and Rate Limits in the Selling Partner API](doc:usage-plans-and-rate-limits-in-the-sp-api).

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.ServiceApi;


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

<a name="createReservation"></a>
# **createReservation**
> CreateReservationResponse createReservation(body, marketplaceIds)



Create a reservation.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 5 | 20 |  The &#x60;x-amzn-RateLimit-Limit&#x60; response header returns the usage plan rate limits that were applied to the requested operation, when available. The table above indicates the default rate and burst values for this operation. Selling partners whose business demands require higher throughput may see higher rate and burst values than those shown here. For more information, see [Usage Plans and Rate Limits in the Selling Partner API](doc:usage-plans-and-rate-limits-in-the-sp-api).

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.ServiceApi;


ServiceApi apiInstance = new ServiceApi();
CreateReservationRequest body = new CreateReservationRequest(); // CreateReservationRequest | Reservation details
List<String> marketplaceIds = Arrays.asList("marketplaceIds_example"); // List<String> | An identifier for the marketplace in which the resource operates.
try {
    CreateReservationResponse result = apiInstance.createReservation(body, marketplaceIds);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ServiceApi#createReservation");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**CreateReservationRequest**](CreateReservationRequest.md)| Reservation details |
 **marketplaceIds** | [**List&lt;String&gt;**](String.md)| An identifier for the marketplace in which the resource operates. |

### Return type

[**CreateReservationResponse**](CreateReservationResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="createServiceDocumentUploadDestination"></a>
# **createServiceDocumentUploadDestination**
> CreateServiceDocumentUploadDestination createServiceDocumentUploadDestination(body)



Creates an upload destination.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 5 | 20 |  The &#x60;x-amzn-RateLimit-Limit&#x60; response header returns the usage plan rate limits that were applied to the requested operation, when available. The table above indicates the default rate and burst values for this operation. Selling partners whose business demands require higher throughput may see higher rate and burst values than those shown here. For more information, see [Usage Plans and Rate Limits in the Selling Partner API](doc:usage-plans-and-rate-limits-in-the-sp-api).

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.ServiceApi;


ServiceApi apiInstance = new ServiceApi();
ServiceUploadDocument body = new ServiceUploadDocument(); // ServiceUploadDocument | Upload document operation input details.
try {
    CreateServiceDocumentUploadDestination result = apiInstance.createServiceDocumentUploadDestination(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ServiceApi#createServiceDocumentUploadDestination");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**ServiceUploadDocument**](ServiceUploadDocument.md)| Upload document operation input details. |

### Return type

[**CreateServiceDocumentUploadDestination**](CreateServiceDocumentUploadDestination.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getAppointmentSlots"></a>
# **getAppointmentSlots**
> GetAppointmentSlotsResponse getAppointmentSlots(asin, storeId, marketplaceIds, startTime, endTime)



Gets appointment slots as per the service context specified.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 20 | 40 |  The &#x60;x-amzn-RateLimit-Limit&#x60; response header returns the usage plan rate limits that were applied to the requested operation, when available. The table above indicates the default rate and burst values for this operation. Selling partners whose business demands require higher throughput may see higher rate and burst values than those shown here. For more information, see [Usage Plans and Rate Limits in the Selling Partner API](doc:usage-plans-and-rate-limits-in-the-sp-api).

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.ServiceApi;


ServiceApi apiInstance = new ServiceApi();
String asin = "asin_example"; // String | ASIN associated with the service.
String storeId = "storeId_example"; // String | Store identifier defining the region scope to retrive appointment slots.
List<String> marketplaceIds = Arrays.asList("marketplaceIds_example"); // List<String> | An identifier for the marketplace for which appointment slots are queried
String startTime = "startTime_example"; // String | A time from which the appointment slots will be retrieved. The specified time must be in ISO 8601 format. If `startTime` is provided, `endTime` should also be provided. Default value is as per business configuration.
String endTime = "endTime_example"; // String | A time up to which the appointment slots will be retrieved. The specified time must be in ISO 8601 format. If `endTime` is provided, `startTime` should also be provided. Default value is as per business configuration. Maximum range of appointment slots can be 90 days.
try {
    GetAppointmentSlotsResponse result = apiInstance.getAppointmentSlots(asin, storeId, marketplaceIds, startTime, endTime);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ServiceApi#getAppointmentSlots");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **asin** | **String**| ASIN associated with the service. |
 **storeId** | **String**| Store identifier defining the region scope to retrive appointment slots. |
 **marketplaceIds** | [**List&lt;String&gt;**](String.md)| An identifier for the marketplace for which appointment slots are queried |
 **startTime** | **String**| A time from which the appointment slots will be retrieved. The specified time must be in ISO 8601 format. If &#x60;startTime&#x60; is provided, &#x60;endTime&#x60; should also be provided. Default value is as per business configuration. | [optional]
 **endTime** | **String**| A time up to which the appointment slots will be retrieved. The specified time must be in ISO 8601 format. If &#x60;endTime&#x60; is provided, &#x60;startTime&#x60; should also be provided. Default value is as per business configuration. Maximum range of appointment slots can be 90 days. | [optional]

### Return type

[**GetAppointmentSlotsResponse**](GetAppointmentSlotsResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getAppointmmentSlotsByJobId"></a>
# **getAppointmmentSlotsByJobId**
> GetAppointmentSlotsResponse getAppointmmentSlotsByJobId(serviceJobId, marketplaceIds, startTime, endTime)



Gets appointment slots for the service associated with the service job id specified.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 5 | 20 |  The &#x60;x-amzn-RateLimit-Limit&#x60; response header returns the usage plan rate limits that were applied to the requested operation, when available. The table above indicates the default rate and burst values for this operation. Selling partners whose business demands require higher throughput may see higher rate and burst values than those shown here. For more information, see [Usage Plans and Rate Limits in the Selling Partner API](doc:usage-plans-and-rate-limits-in-the-sp-api).

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.ServiceApi;


ServiceApi apiInstance = new ServiceApi();
String serviceJobId = "serviceJobId_example"; // String | A service job identifier to retrive appointment slots for associated service.
List<String> marketplaceIds = Arrays.asList("marketplaceIds_example"); // List<String> | An identifier for the marketplace in which the resource operates.
String startTime = "startTime_example"; // String | A time from which the appointment slots will be retrieved. The specified time must be in ISO 8601 format. If `startTime` is provided, `endTime` should also be provided. Default value is as per business configuration.
String endTime = "endTime_example"; // String | A time up to which the appointment slots will be retrieved. The specified time must be in ISO 8601 format. If `endTime` is provided, `startTime` should also be provided. Default value is as per business configuration. Maximum range of appointment slots can be 90 days.
try {
    GetAppointmentSlotsResponse result = apiInstance.getAppointmmentSlotsByJobId(serviceJobId, marketplaceIds, startTime, endTime);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ServiceApi#getAppointmmentSlotsByJobId");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **serviceJobId** | **String**| A service job identifier to retrive appointment slots for associated service. |
 **marketplaceIds** | [**List&lt;String&gt;**](String.md)| An identifier for the marketplace in which the resource operates. |
 **startTime** | **String**| A time from which the appointment slots will be retrieved. The specified time must be in ISO 8601 format. If &#x60;startTime&#x60; is provided, &#x60;endTime&#x60; should also be provided. Default value is as per business configuration. | [optional]
 **endTime** | **String**| A time up to which the appointment slots will be retrieved. The specified time must be in ISO 8601 format. If &#x60;endTime&#x60; is provided, &#x60;startTime&#x60; should also be provided. Default value is as per business configuration. Maximum range of appointment slots can be 90 days. | [optional]

### Return type

[**GetAppointmentSlotsResponse**](GetAppointmentSlotsResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getFixedSlotCapacity"></a>
# **getFixedSlotCapacity**
> FixedSlotCapacity getFixedSlotCapacity(resourceId, body, marketplaceIds, nextPageToken)



Provides capacity in fixed-size slots.   **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 5 | 20 |  The &#x60;x-amzn-RateLimit-Limit&#x60; response header returns the usage plan rate limits that were applied to the requested operation, when available. The table above indicates the default rate and burst values for this operation. Selling partners whose business demands require higher throughput may see higher rate and burst values than those shown here. For more information, see [Usage Plans and Rate Limits in the Selling Partner API](doc:usage-plans-and-rate-limits-in-the-sp-api).

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.ServiceApi;


ServiceApi apiInstance = new ServiceApi();
String resourceId = "resourceId_example"; // String | Resource Identifier.
FixedSlotCapacityQuery body = new FixedSlotCapacityQuery(); // FixedSlotCapacityQuery | Request body.
List<String> marketplaceIds = Arrays.asList("marketplaceIds_example"); // List<String> | An identifier for the marketplace in which the resource operates.
String nextPageToken = "nextPageToken_example"; // String | Next page token returned in the response of your previous request.
try {
    FixedSlotCapacity result = apiInstance.getFixedSlotCapacity(resourceId, body, marketplaceIds, nextPageToken);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ServiceApi#getFixedSlotCapacity");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **resourceId** | **String**| Resource Identifier. |
 **body** | [**FixedSlotCapacityQuery**](FixedSlotCapacityQuery.md)| Request body. |
 **marketplaceIds** | [**List&lt;String&gt;**](String.md)| An identifier for the marketplace in which the resource operates. |
 **nextPageToken** | **String**| Next page token returned in the response of your previous request. | [optional]

### Return type

[**FixedSlotCapacity**](FixedSlotCapacity.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getRangeSlotCapacity"></a>
# **getRangeSlotCapacity**
> RangeSlotCapacity getRangeSlotCapacity(resourceId, body, marketplaceIds, nextPageToken)



Provides capacity slots in a format similar to availability records.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 5 | 20 |  The &#x60;x-amzn-RateLimit-Limit&#x60; response header returns the usage plan rate limits that were applied to the requested operation, when available. The table above indicates the default rate and burst values for this operation. Selling partners whose business demands require higher throughput may see higher rate and burst values than those shown here. For more information, see [Usage Plans and Rate Limits in the Selling Partner API](doc:usage-plans-and-rate-limits-in-the-sp-api).

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.ServiceApi;


ServiceApi apiInstance = new ServiceApi();
String resourceId = "resourceId_example"; // String | Resource Identifier.
RangeSlotCapacityQuery body = new RangeSlotCapacityQuery(); // RangeSlotCapacityQuery | Request body.
List<String> marketplaceIds = Arrays.asList("marketplaceIds_example"); // List<String> | An identifier for the marketplace in which the resource operates.
String nextPageToken = "nextPageToken_example"; // String | Next page token returned in the response of your previous request.
try {
    RangeSlotCapacity result = apiInstance.getRangeSlotCapacity(resourceId, body, marketplaceIds, nextPageToken);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ServiceApi#getRangeSlotCapacity");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **resourceId** | **String**| Resource Identifier. |
 **body** | [**RangeSlotCapacityQuery**](RangeSlotCapacityQuery.md)| Request body. |
 **marketplaceIds** | [**List&lt;String&gt;**](String.md)| An identifier for the marketplace in which the resource operates. |
 **nextPageToken** | **String**| Next page token returned in the response of your previous request. | [optional]

### Return type

[**RangeSlotCapacity**](RangeSlotCapacity.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getServiceJobByServiceJobId"></a>
# **getServiceJobByServiceJobId**
> GetServiceJobByServiceJobIdResponse getServiceJobByServiceJobId(serviceJobId)



Gets details of service job indicated by the provided &#x60;serviceJobID&#x60;.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 20 | 40 |  The &#x60;x-amzn-RateLimit-Limit&#x60; response header returns the usage plan rate limits that were applied to the requested operation, when available. The table above indicates the default rate and burst values for this operation. Selling partners whose business demands require higher throughput may see higher rate and burst values than those shown here. For more information, see [Usage Plans and Rate Limits in the Selling Partner API](doc:usage-plans-and-rate-limits-in-the-sp-api).

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.ServiceApi;


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
> GetServiceJobsResponse getServiceJobs(marketplaceIds, serviceOrderIds, serviceJobStatus, pageToken, pageSize, sortField, sortOrder, createdAfter, createdBefore, lastUpdatedAfter, lastUpdatedBefore, scheduleStartDate, scheduleEndDate, asins, requiredSkills, storeIds)



Gets service job details for the specified filter query.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 10 | 40 |  The &#x60;x-amzn-RateLimit-Limit&#x60; response header returns the usage plan rate limits that were applied to the requested operation, when available. The table above indicates the default rate and burst values for this operation. Selling partners whose business demands require higher throughput may see higher rate and burst values than those shown here. For more information, see [Usage Plans and Rate Limits in the Selling Partner API](doc:usage-plans-and-rate-limits-in-the-sp-api).

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.ServiceApi;


ServiceApi apiInstance = new ServiceApi();
List<String> marketplaceIds = Arrays.asList("marketplaceIds_example"); // List<String> | Used to select jobs that were placed in the specified marketplaces.
List<String> serviceOrderIds = Arrays.asList("serviceOrderIds_example"); // List<String> | List of service order ids for the query you want to perform.Max values supported 20.
List<String> serviceJobStatus = Arrays.asList("serviceJobStatus_example"); // List<String> | A list of one or more job status by which to filter the list of jobs.
String pageToken = "pageToken_example"; // String | String returned in the response of your previous request.
Integer pageSize = 20; // Integer | A non-negative integer that indicates the maximum number of jobs to return in the list, Value must be 1 - 20. Default 20.
String sortField = "sortField_example"; // String | Sort fields on which you want to sort the output.
String sortOrder = "sortOrder_example"; // String | Sort order for the query you want to perform.
String createdAfter = "createdAfter_example"; // String | A date used for selecting jobs created at or after a specified time. Must be in ISO 8601 format. Required if `LastUpdatedAfter` is not specified. Specifying both `CreatedAfter` and `LastUpdatedAfter` returns an error.
String createdBefore = "createdBefore_example"; // String | A date used for selecting jobs created at or before a specified time. Must be in ISO 8601 format.
String lastUpdatedAfter = "lastUpdatedAfter_example"; // String | A date used for selecting jobs updated at or after a specified time. Must be in ISO 8601 format. Required if `createdAfter` is not specified. Specifying both `CreatedAfter` and `LastUpdatedAfter` returns an error.
String lastUpdatedBefore = "lastUpdatedBefore_example"; // String | A date used for selecting jobs updated at or before a specified time. Must be in ISO 8601 format.
String scheduleStartDate = "scheduleStartDate_example"; // String | A date used for filtering jobs schedules at or after a specified time. Must be in ISO 8601 format. Schedule end date should not be earlier than schedule start date.
String scheduleEndDate = "scheduleEndDate_example"; // String | A date used for filtering jobs schedules at or before a specified time. Must be in ISO 8601 format. Schedule end date should not be earlier than schedule start date.
List<String> asins = Arrays.asList("asins_example"); // List<String> | List of Amazon Standard Identification Numbers (ASIN) of the items. Max values supported is 20.
List<String> requiredSkills = Arrays.asList("requiredSkills_example"); // List<String> | A defined set of related knowledge, skills, experience, tools, materials, and work processes common to service delivery for a set of products and/or service scenarios. Max values supported is 20.
List<String> storeIds = Arrays.asList("storeIds_example"); // List<String> | List of Amazon-defined identifiers for the region scope. Max values supported is 50.
try {
    GetServiceJobsResponse result = apiInstance.getServiceJobs(marketplaceIds, serviceOrderIds, serviceJobStatus, pageToken, pageSize, sortField, sortOrder, createdAfter, createdBefore, lastUpdatedAfter, lastUpdatedBefore, scheduleStartDate, scheduleEndDate, asins, requiredSkills, storeIds);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ServiceApi#getServiceJobs");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **marketplaceIds** | [**List&lt;String&gt;**](String.md)| Used to select jobs that were placed in the specified marketplaces. |
 **serviceOrderIds** | [**List&lt;String&gt;**](String.md)| List of service order ids for the query you want to perform.Max values supported 20. | [optional]
 **serviceJobStatus** | [**List&lt;String&gt;**](String.md)| A list of one or more job status by which to filter the list of jobs. | [optional] [enum: NOT_SERVICED, CANCELLED, COMPLETED, PENDING_SCHEDULE, NOT_FULFILLABLE, HOLD, PAYMENT_DECLINED]
 **pageToken** | **String**| String returned in the response of your previous request. | [optional]
 **pageSize** | **Integer**| A non-negative integer that indicates the maximum number of jobs to return in the list, Value must be 1 - 20. Default 20. | [optional] [default to 20]
 **sortField** | **String**| Sort fields on which you want to sort the output. | [optional] [enum: JOB_DATE, JOB_STATUS]
 **sortOrder** | **String**| Sort order for the query you want to perform. | [optional] [enum: ASC, DESC]
 **createdAfter** | **String**| A date used for selecting jobs created at or after a specified time. Must be in ISO 8601 format. Required if &#x60;LastUpdatedAfter&#x60; is not specified. Specifying both &#x60;CreatedAfter&#x60; and &#x60;LastUpdatedAfter&#x60; returns an error. | [optional]
 **createdBefore** | **String**| A date used for selecting jobs created at or before a specified time. Must be in ISO 8601 format. | [optional]
 **lastUpdatedAfter** | **String**| A date used for selecting jobs updated at or after a specified time. Must be in ISO 8601 format. Required if &#x60;createdAfter&#x60; is not specified. Specifying both &#x60;CreatedAfter&#x60; and &#x60;LastUpdatedAfter&#x60; returns an error. | [optional]
 **lastUpdatedBefore** | **String**| A date used for selecting jobs updated at or before a specified time. Must be in ISO 8601 format. | [optional]
 **scheduleStartDate** | **String**| A date used for filtering jobs schedules at or after a specified time. Must be in ISO 8601 format. Schedule end date should not be earlier than schedule start date. | [optional]
 **scheduleEndDate** | **String**| A date used for filtering jobs schedules at or before a specified time. Must be in ISO 8601 format. Schedule end date should not be earlier than schedule start date. | [optional]
 **asins** | [**List&lt;String&gt;**](String.md)| List of Amazon Standard Identification Numbers (ASIN) of the items. Max values supported is 20. | [optional]
 **requiredSkills** | [**List&lt;String&gt;**](String.md)| A defined set of related knowledge, skills, experience, tools, materials, and work processes common to service delivery for a set of products and/or service scenarios. Max values supported is 20. | [optional]
 **storeIds** | [**List&lt;String&gt;**](String.md)| List of Amazon-defined identifiers for the region scope. Max values supported is 50. | [optional]

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



Reschedules an appointment for the service job indicated by the service job identifier specified.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 5 | 20 |  The &#x60;x-amzn-RateLimit-Limit&#x60; response header returns the usage plan rate limits that were applied to the requested operation, when available. The table above indicates the default rate and burst values for this operation. Selling partners whose business demands require higher throughput may see higher rate and burst values than those shown here. For more information, see [Usage Plans and Rate Limits in the Selling Partner API](doc:usage-plans-and-rate-limits-in-the-sp-api).

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.ServiceApi;


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

<a name="setAppointmentFulfillmentData"></a>
# **setAppointmentFulfillmentData**
> String setAppointmentFulfillmentData(serviceJobId, appointmentId, body)



Updates the appointment fulfillment data related to a given &#x60;jobID&#x60; and &#x60;appointmentID&#x60;.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 5 | 20 |  The &#x60;x-amzn-RateLimit-Limit&#x60; response header returns the usage plan rate limits that were applied to the requested operation, when available. The table above indicates the default rate and burst values for this operation. Selling partners whose business demands require higher throughput may see higher rate and burst values than those shown here. For more information, see [Usage Plans and Rate Limits in the Selling Partner API](doc:usage-plans-and-rate-limits-in-the-sp-api).

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.ServiceApi;


ServiceApi apiInstance = new ServiceApi();
String serviceJobId = "serviceJobId_example"; // String | An Amazon-defined service job identifier. Get this value by calling the `getServiceJobs` operation of the Services API.
String appointmentId = "appointmentId_example"; // String | An Amazon-defined identifier of active service job appointment.
SetAppointmentFulfillmentDataRequest body = new SetAppointmentFulfillmentDataRequest(); // SetAppointmentFulfillmentDataRequest | Appointment fulfillment data collection details.
try {
    String result = apiInstance.setAppointmentFulfillmentData(serviceJobId, appointmentId, body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ServiceApi#setAppointmentFulfillmentData");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **serviceJobId** | **String**| An Amazon-defined service job identifier. Get this value by calling the &#x60;getServiceJobs&#x60; operation of the Services API. |
 **appointmentId** | **String**| An Amazon-defined identifier of active service job appointment. |
 **body** | [**SetAppointmentFulfillmentDataRequest**](SetAppointmentFulfillmentDataRequest.md)| Appointment fulfillment data collection details. |

### Return type

**String**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="updateReservation"></a>
# **updateReservation**
> UpdateReservationResponse updateReservation(reservationId, body, marketplaceIds)



Update a reservation.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 5 | 20 |  The &#x60;x-amzn-RateLimit-Limit&#x60; response header returns the usage plan rate limits that were applied to the requested operation, when available. The table above indicates the default rate and burst values for this operation. Selling partners whose business demands require higher throughput may see higher rate and burst values than those shown here. For more information, see [Usage Plans and Rate Limits in the Selling Partner API](doc:usage-plans-and-rate-limits-in-the-sp-api).

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.ServiceApi;


ServiceApi apiInstance = new ServiceApi();
String reservationId = "reservationId_example"; // String | Reservation Identifier
UpdateReservationRequest body = new UpdateReservationRequest(); // UpdateReservationRequest | Reservation details
List<String> marketplaceIds = Arrays.asList("marketplaceIds_example"); // List<String> | An identifier for the marketplace in which the resource operates.
try {
    UpdateReservationResponse result = apiInstance.updateReservation(reservationId, body, marketplaceIds);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ServiceApi#updateReservation");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **reservationId** | **String**| Reservation Identifier |
 **body** | [**UpdateReservationRequest**](UpdateReservationRequest.md)| Reservation details |
 **marketplaceIds** | [**List&lt;String&gt;**](String.md)| An identifier for the marketplace in which the resource operates. |

### Return type

[**UpdateReservationResponse**](UpdateReservationResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="updateSchedule"></a>
# **updateSchedule**
> UpdateScheduleResponse updateSchedule(resourceId, body, marketplaceIds)



Update the schedule of the given resource.  **Usage Plan:**  | Rate (requests per second) | Burst | | ---- | ---- | | 5 | 20 |  The &#x60;x-amzn-RateLimit-Limit&#x60; response header returns the usage plan rate limits that were applied to the requested operation, when available. The table above indicates the default rate and burst values for this operation. Selling partners whose business demands require higher throughput may see higher rate and burst values than those shown here. For more information, see [Usage Plans and Rate Limits in the Selling Partner API](doc:usage-plans-and-rate-limits-in-the-sp-api).

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.ServiceApi;


ServiceApi apiInstance = new ServiceApi();
String resourceId = "resourceId_example"; // String | Resource (store) Identifier
UpdateScheduleRequest body = new UpdateScheduleRequest(); // UpdateScheduleRequest | Schedule details
List<String> marketplaceIds = Arrays.asList("marketplaceIds_example"); // List<String> | An identifier for the marketplace in which the resource operates.
try {
    UpdateScheduleResponse result = apiInstance.updateSchedule(resourceId, body, marketplaceIds);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ServiceApi#updateSchedule");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **resourceId** | **String**| Resource (store) Identifier |
 **body** | [**UpdateScheduleRequest**](UpdateScheduleRequest.md)| Schedule details |
 **marketplaceIds** | [**List&lt;String&gt;**](String.md)| An identifier for the marketplace in which the resource operates. |

### Return type

[**UpdateScheduleResponse**](UpdateScheduleResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

