
# DebtRecoveryEvent

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**debtRecoveryType** | **String** | The debt recovery type.  Possible values:  * DebtPayment  * DebtPaymentFailure  *DebtAdjustment |  [optional]
**recoveryAmount** | [**Currency**](Currency.md) | The amount applied for recovery. |  [optional]
**overPaymentCredit** | [**Currency**](Currency.md) | The amount returned for overpayment. |  [optional]
**debtRecoveryItemList** | [**DebtRecoveryItemList**](DebtRecoveryItemList.md) |  |  [optional]
**chargeInstrumentList** | [**ChargeInstrumentList**](ChargeInstrumentList.md) |  |  [optional]



