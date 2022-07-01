
# OrderMetricsInterval

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**interval** | **String** | The interval of time based on requested granularity (ex. Hour, Day, etc.) If this is the first or the last interval from the list, it might contain incomplete data if the requested interval doesn&#39;t align with the requested granularity (ex. request interval 2018-09-01T02:00:00Z--2018-09-04T19:00:00Z and granularity day will result in Sept 1st UTC day and Sept 4th UTC days having partial data). | 
**unitCount** | **Integer** | The number of units in orders based on the specified filters. | 
**orderItemCount** | **Integer** | The number of order items based on the specified filters. | 
**orderCount** | **Integer** | The number of orders based on the specified filters. | 
**averageUnitPrice** | [**Money**](Money.md) | The average price for an item based on the specified filters. Formula is totalSales/unitCount. | 
**totalSales** | [**Money**](Money.md) | The total ordered product sales for all orders based on the specified filters. | 



