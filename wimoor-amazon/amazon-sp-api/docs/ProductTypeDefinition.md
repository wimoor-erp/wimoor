
# ProductTypeDefinition

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**metaSchema** | [**SchemaLink**](SchemaLink.md) | Link to meta-schema describing the vocabulary used by the product type schema. |  [optional]
**schema** | [**SchemaLink**](SchemaLink.md) | Link to schema describing the attributes and requirements for the product type. | 
**requirements** | [**RequirementsEnum**](#RequirementsEnum) | Name of the requirements set represented in this product type definition. | 
**requirementsEnforced** | [**RequirementsEnforcedEnum**](#RequirementsEnforcedEnum) | Identifies if the required attributes for a requirements set are enforced by the product type definition schema. Non-enforced requirements enable structural validation of individual attributes without all of the required attributes being present (such as for partial updates). | 
**propertyGroups** | [**Map&lt;String, PropertyGroup&gt;**](PropertyGroup.md) | Mapping of property group names to property groups. Property groups represent logical groupings of schema properties that can be used for display or informational purposes. | 
**locale** | **String** | Locale of the display elements contained in the product type definition. | 
**marketplaceIds** | **List&lt;String&gt;** | Amazon marketplace identifiers for which the product type definition is applicable. | 
**productType** | **String** | The name of the Amazon product type that this product type definition applies to. | 
**displayName** | **String** | Human-readable and localized description of the Amazon product type. | 
**productTypeVersion** | [**ProductTypeVersion**](ProductTypeVersion.md) | The version details for the Amazon product type. | 


<a name="RequirementsEnum"></a>
## Enum: RequirementsEnum
Name | Value
---- | -----
LISTING | &quot;LISTING&quot;
LISTING_PRODUCT_ONLY | &quot;LISTING_PRODUCT_ONLY&quot;
LISTING_OFFER_ONLY | &quot;LISTING_OFFER_ONLY&quot;


<a name="RequirementsEnforcedEnum"></a>
## Enum: RequirementsEnforcedEnum
Name | Value
---- | -----
ENFORCED | &quot;ENFORCED&quot;
NOT_ENFORCED | &quot;NOT_ENFORCED&quot;



