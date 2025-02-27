
# FeePreview

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**asin** | **String** | The Amazon Standard Identification Number (ASIN) value used to identify the item. |  [optional]
**price** | [**MoneyType**](MoneyType.md) | The price that the seller plans to charge for the item. |  [optional]
**feeBreakdown** | [**List&lt;FeeLineItem&gt;**](FeeLineItem.md) | A list of the Small and Light fees for the item. |  [optional]
**totalFees** | [**MoneyType**](MoneyType.md) | The total fees charged if the item participated in the Small and Light program. |  [optional]
**errors** | [**ErrorList**](../ErrorList.md) | One or more unexpected errors occurred during the getSmallAndLightFeePreview operation. |  [optional]



