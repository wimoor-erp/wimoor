
# PurchaseShipmentRequest

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**requestToken** | **String** |  | 
**rateId** | **String** |  | 
**requestedDocumentSpecification** | [**RequestedDocumentSpecification**](RequestedDocumentSpecification.md) |  | 
**requestedValueAddedServices** | [**RequestedValueAddedServiceList**](RequestedValueAddedServiceList.md) |  |  [optional]
**additionalInputs** | **Map&lt;String, Object&gt;** | The additional inputs required to purchase a shipping offering, in JSON format. The JSON provided here must adhere to the JSON schema that is returned in the response to the getAdditionalInputs operation.  Additional inputs are only required when indicated by the requiresAdditionalInputs property in the response to the getRates operation. |  [optional]



