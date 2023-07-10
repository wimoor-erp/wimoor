
# ReportDocumentEncryptionDetails

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**standard** | [**StandardEnum**](#StandardEnum) | The encryption standard required to decrypt the document contents. | 
**initializationVector** | **String** | The vector to decrypt the document contents using Cipher Block Chaining (CBC). | 
**key** | **String** | The encryption key used to decrypt the document contents. | 


<a name="StandardEnum"></a>
## Enum: StandardEnum
Name | Value
---- | -----
AES | &quot;AES&quot;



