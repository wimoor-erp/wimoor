
# ServiceUploadDocument

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**contentType** | [**ContentTypeEnum**](#ContentTypeEnum) | The content type of the to-be-uploaded file | 
**contentLength** | [**BigDecimal**](BigDecimal.md) | The content length of the to-be-uploaded file | 
**contentMD5** | **String** | An MD5 hash of the content to be submitted to the upload destination. This value is used to determine if the data has been corrupted or tampered with during transit. |  [optional]


<a name="ContentTypeEnum"></a>
## Enum: ContentTypeEnum
Name | Value
---- | -----
TIFF | &quot;TIFF&quot;
JPG | &quot;JPG&quot;
PNG | &quot;PNG&quot;
JPEG | &quot;JPEG&quot;
GIF | &quot;GIF&quot;
PDF | &quot;PDF&quot;



