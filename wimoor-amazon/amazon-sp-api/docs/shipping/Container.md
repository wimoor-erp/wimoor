
# Container

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**containerType** | [**ContainerTypeEnum**](#ContainerTypeEnum) | The type of physical container being used. (always &#39;PACKAGE&#39;) |  [optional]
**containerReferenceId** | **String** |  | 
**value** | [**Currency**](Currency.md) | The total value of all items in the container. | 
**dimensions** | [**Dimensions**](Dimensions.md) | The length, width, height, and weight of the container. | 
**items** | [**List&lt;ContainerItem&gt;**](ContainerItem.md) | A list of the items in the container. | 
**weight** | [**Weight**](Weight.md) | The weight of the container. | 


<a name="ContainerTypeEnum"></a>
## Enum: ContainerTypeEnum
Name | Value
---- | -----
PACKAGE | &quot;PACKAGE&quot;



