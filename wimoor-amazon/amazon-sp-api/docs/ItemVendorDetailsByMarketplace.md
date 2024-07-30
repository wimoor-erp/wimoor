
# ItemVendorDetailsByMarketplace

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**marketplaceId** | **String** | Amazon marketplace identifier. | 
**brandCode** | **String** | Brand code associated with an Amazon catalog item. |  [optional]
**manufacturerCode** | **String** | Manufacturer code associated with an Amazon catalog item. |  [optional]
**manufacturerCodeParent** | **String** | Parent vendor code of the manufacturer code. |  [optional]
**productCategory** | [**ItemVendorDetailsCategory**](ItemVendorDetailsCategory.md) | Product category associated with an Amazon catalog item. |  [optional]
**productGroup** | **String** | Product group associated with an Amazon catalog item. |  [optional]
**productSubcategory** | [**ItemVendorDetailsCategory**](ItemVendorDetailsCategory.md) | Product subcategory associated with an Amazon catalog item. |  [optional]
**replenishmentCategory** | [**ReplenishmentCategoryEnum**](#ReplenishmentCategoryEnum) | Replenishment category associated with an Amazon catalog item. |  [optional]


<a name="ReplenishmentCategoryEnum"></a>
## Enum: ReplenishmentCategoryEnum
Name | Value
---- | -----
ALLOCATED | &quot;ALLOCATED&quot;
BASIC_REPLENISHMENT | &quot;BASIC_REPLENISHMENT&quot;
IN_SEASON | &quot;IN_SEASON&quot;
LIMITED_REPLENISHMENT | &quot;LIMITED_REPLENISHMENT&quot;
MANUFACTURER_OUT_OF_STOCK | &quot;MANUFACTURER_OUT_OF_STOCK&quot;
NEW_PRODUCT | &quot;NEW_PRODUCT&quot;
NON_REPLENISHABLE | &quot;NON_REPLENISHABLE&quot;
NON_STOCKUPABLE | &quot;NON_STOCKUPABLE&quot;
OBSOLETE | &quot;OBSOLETE&quot;
PLANNED_REPLENISHMENT | &quot;PLANNED_REPLENISHMENT&quot;



