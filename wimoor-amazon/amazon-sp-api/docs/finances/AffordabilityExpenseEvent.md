
# AffordabilityExpenseEvent

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**amazonOrderId** | **String** | An Amazon-defined identifier for an order. |  [optional]
**postedDate** | **String** | The date and time when the financial event was created. |  [optional]
**marketplaceId** | **String** | An encrypted, Amazon-defined marketplace identifier. |  [optional]
**transactionType** | **String** | Indicates the type of transaction.   Possible values:  * Charge - For an affordability promotion expense.  * Refund - For an affordability promotion expense reversal. |  [optional]
**baseExpense** | [**Currency**](Currency.md) | The amount charged for clicks incurred under the Sponsored Products program. |  [optional]
**taxTypeCGST** | [**Currency**](Currency.md) | Central Goods and Service Tax, charged and collected by the central government. | 
**taxTypeSGST** | [**Currency**](Currency.md) | State Goods and Service Tax, charged and collected by the state government. | 
**taxTypeIGST** | [**Currency**](Currency.md) | Integrated Goods and Service Tax, charged and collected by the central government. | 
**totalExpense** | [**Currency**](Currency.md) | The total amount charged to the seller. TotalExpense &#x3D; BaseExpense + TaxTypeIGST + TaxTypeCGST + TaxTypeSGST. |  [optional]



