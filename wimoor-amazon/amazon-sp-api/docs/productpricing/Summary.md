
# Summary

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**totalOfferCount** | **Integer** | The number of unique offers contained in NumberOfOffers. | 
**numberOfOffers** | [**NumberOfOffers**](NumberOfOffers.md) | A list that contains the total number of offers for the item for the given conditions and fulfillment channels. |  [optional]
**lowestPrices** | [**LowestPrices**](LowestPrices.md) | A list of the lowest prices for the item. |  [optional]
**buyBoxPrices** | [**BuyBoxPrices**](BuyBoxPrices.md) | A list of item prices. |  [optional]
**listPrice** | [**MoneyType**](MoneyType.md) | The list price of the item as suggested by the manufacturer. |  [optional]
**suggestedLowerPricePlusShipping** | [**MoneyType**](MoneyType.md) | The suggested lower price of the item, including shipping and Amazon Points. The suggested lower price is based on a range of factors, including historical selling prices, recent Buy Box-eligible prices, and input from customers for your products. |  [optional]
**buyBoxEligibleOffers** | [**BuyBoxEligibleOffers**](BuyBoxEligibleOffers.md) | A list that contains the total number of offers that are eligible for the Buy Box for the given conditions and fulfillment channels. |  [optional]
**offersAvailableTime** | [**OffsetDateTime**](OffsetDateTime.md) | When the status is ActiveButTooSoonForProcessing, this is the time when the offers will be available for processing. |  [optional]



