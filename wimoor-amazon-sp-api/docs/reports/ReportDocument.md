
# ReportDocument

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**reportDocumentId** | **String** | The identifier for the report document. This identifier is unique only in combination with a seller ID. | 
**url** | **String** | A presigned URL for the report document. This URL expires after 5 minutes. | 
**encryptionDetails** | [**ReportDocumentEncryptionDetails**](ReportDocumentEncryptionDetails.md) |  | 
**compressionAlgorithm** | [**CompressionAlgorithmEnum**](#CompressionAlgorithmEnum) | If present, the report document contents have been compressed with the provided algorithm. |  [optional]


<a name="CompressionAlgorithmEnum"></a>
## Enum: CompressionAlgorithmEnum
Name | Value
---- | -----
GZIP | &quot;GZIP&quot;



