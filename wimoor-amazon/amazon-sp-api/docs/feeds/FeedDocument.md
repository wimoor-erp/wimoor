
# FeedDocument

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**feedDocumentId** | **String** | The identifier for the feed document. This identifier is unique only in combination with a seller ID. | 
**url** | **String** | A presigned URL for the feed document. This URL expires after 5 minutes. | 
**encryptionDetails** | [**FeedDocumentEncryptionDetails**](FeedDocumentEncryptionDetails.md) |  | 
**compressionAlgorithm** | [**CompressionAlgorithmEnum**](#CompressionAlgorithmEnum) | If present, the feed document contents are compressed using the indicated algorithm. |  [optional]


<a name="CompressionAlgorithmEnum"></a>
## Enum: CompressionAlgorithmEnum
Name | Value
---- | -----
GZIP | &quot;GZIP&quot;



