-- --------------------------------------------------------
-- 主机:                           rm-wz903sa454i2h35ik6o.mysql.rds.aliyuncs.com
-- 服务器版本:                        5.7.28-log - Source distribution
-- 服务器操作系统:                      Linux
-- HeidiSQL 版本:                  12.6.0.6765
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

-- 正在导出表  db_amazon.t_amz_settlement_amount_type_nonsku 的数据：~68 rows (大约)
DELETE FROM `t_amz_settlement_amount_type_nonsku`;
INSERT INTO `t_amz_settlement_amount_type_nonsku` (`transaction_type`, `amount_type`, `amount_description`) VALUES
	('CouponRedemptionFee', 'CouponRedemptionFee', '*'),
	('Fee Adjustment', 'Item Fee Adjustment', 'FBA Pick & Pack Fee'),
	('Grade and Resell Fees', 'Grade and Resell Charge', '*'),
	('Income Tax Charged', 'other-transaction', 'MiscAdjustment'),
	('Lightning Deal Fee', 'LightningDealFee', 'LightningDealFee'),
	('Lightning Deal Fee', 'LightningDealFee', 'LightningDealFee Special'),
	('Order', 'DirectPayment', 'CollectOnDeliveryRevenue'),
	('Order', 'ShipmentFees', 'FBA transportation fee'),
	('Order_Retrocharge', 'ItemPrice', 'ShippingTax'),
	('Order_Retrocharge', 'ItemPrice', 'Tax'),
	('Refund_Retrocharge', 'ItemPrice', 'ShippingTax'),
	('Refund_Retrocharge', 'ItemPrice', 'Tax'),
	('SellerReviewEnrollmentPayment', 'Review Enrollment Fee', 'Early Reviewer Program fee'),
	('ServiceFee', 'Cost of Advertising', 'TransactionTotalAmount'),
	('TBYB Customer Return Fee', 'ItemFees', 'FBACustomerReturnPerUnitFee'),
	('TBYB Trial Shipment', 'Amazon Fees', 'TrialShipment'),
	('Tax', 'Tax Withheld', '*'),
	('Transfers', 'Micro Deposit', 'Micro Deposit'),
	('Vine Enrollment Fee', 'Vine Enrollment Fee', 'Vine Enrollment Fee'),
	('other-transaction', 'Amazon Warehousing & Distribution (AWD)', 'AWD Processing Fee'),
	('other-transaction', 'Amazon Warehousing & Distribution (AWD)', 'AWD Storage Fee'),
	('other-transaction', 'Amazon Warehousing & Distribution (AWD)', 'AWD Transportation Fee'),
	('other-transaction', 'FBA Inventory Reimbursement', 'CS_ERROR_NON_ITEMIZED'),
	('other-transaction', 'FBA Inventory Reimbursement', 'INCORRECT_FEES_NON_ITEMIZED'),
	('other-transaction', 'ItemFees', 'FBACustomerReturnPerOrderFee'),
	('other-transaction', 'ItemFees', 'FBACustomerReturnPerUnitFee'),
	('other-transaction', 'ItemFees', 'FBACustomerReturnWeightBasedFee'),
	('other-transaction', 'Manual Processing Fee Reimbursement', 'Manual Processing Fee Reimbursement'),
	('other-transaction', 'other-transaction', 'A2ZGuaranteeRecovery'),
	('other-transaction', 'other-transaction', 'Adjustment'),
	('other-transaction', 'other-transaction', 'BuyerRecharge'),
	('other-transaction', 'other-transaction', 'ChargeBackRecovery'),
	('other-transaction', 'other-transaction', 'CommissionCorrection'),
	('other-transaction', 'other-transaction', 'Current Reserve Amount'),
	('other-transaction', 'other-transaction', 'DisposalComplete'),
	('other-transaction', 'other-transaction', 'FBA International Freight Duties and Taxes Charge'),
	('other-transaction', 'other-transaction', 'FBA International Freight Shipping Charge'),
	('other-transaction', 'other-transaction', 'FBAInboundTransportationFee'),
	('other-transaction', 'other-transaction', 'FBAInboundTransportationProgramFee'),
	('other-transaction', 'other-transaction', 'HighVolumeListing'),
	('other-transaction', 'other-transaction', 'Inventory Placement Service Fee'),
	('other-transaction', 'other-transaction', 'Inventory Storage Overage Fee'),
	('other-transaction', 'other-transaction', 'Manual Processing Fee'),
	('other-transaction', 'other-transaction', 'MiscAdjustment'),
	('other-transaction', 'other-transaction', 'NonSubscriptionFeeAdj'),
	('other-transaction', 'other-transaction', 'Paid Services Fee'),
	('other-transaction', 'other-transaction', 'Payable to Amazon'),
	('other-transaction', 'other-transaction', 'Previous Reserve Amount Balance'),
	('other-transaction', 'other-transaction', 'RemovalComplete'),
	('other-transaction', 'other-transaction', 'RemovalCompleteCGST'),
	('other-transaction', 'other-transaction', 'RemovalCompleteSGST'),
	('other-transaction', 'other-transaction', 'Shipping label purchase'),
	('other-transaction', 'other-transaction', 'Shipping label purchase for return'),
	('other-transaction', 'other-transaction', 'Storage Fee'),
	('other-transaction', 'other-transaction', 'StorageBillingCGST'),
	('other-transaction', 'other-transaction', 'StorageBillingSGST'),
	('other-transaction', 'other-transaction', 'StorageRenewalBilling'),
	('other-transaction', 'other-transaction', 'StorageRenewalBillingCGST'),
	('other-transaction', 'other-transaction', 'StorageRenewalBillingSGST'),
	('other-transaction', 'other-transaction', 'Subscription Fee'),
	('other-transaction', 'other-transaction', 'SubscriptionFeeCorrection'),
	('other-transaction', 'other-transaction', 'Successful charge'),
	('other-transaction', 'other-transaction', 'Transfer of funds unsuccessful: A transfer could not be initiated because the transfer amount does n'),
	('other-transaction', 'other-transaction', 'Transfer of funds unsuccessful: Amazon has cancelled your transfer of funds.'),
	('other-transaction', 'other-transaction', 'Transfer of funds unsuccessful: An unexpected issue occurred. Please contact Seller Support for more'),
	('other-transaction', 'other-transaction', 'Transfer of funds unsuccessful: We could not transfer funds to your bank account because the account'),
	('other-transaction', 'other-transaction', 'Unplanned Service Fee - Barcode label missing'),
	('other-transaction', 'other-transaction', 'WarehousePrep');

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
