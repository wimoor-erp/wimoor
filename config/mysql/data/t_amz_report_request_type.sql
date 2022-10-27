-- --------------------------------------------------------
-- 主机:                           192.168.0.252
-- 服务器版本:                        8.0.20 - MySQL Community Server - GPL
-- 服务器操作系统:                      Win64
-- HeidiSQL 版本:                  11.3.0.6295
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

-- 正在导出表  db_plum.t_amz_report_request_type 的数据：~21 rows (大约)
/*!40000 ALTER TABLE `t_amz_report_request_type` DISABLE KEYS */;
INSERT INTO `t_amz_report_request_type` (`id`, `cname`, `ename`, `code`, `description`, `bean`, `day`, `disabled`, `reportOptions`) VALUES
	(1, '所有产品报表', 'ProductListings', 'GET_MERCHANT_LISTINGS_ALL_DATA', '用于获取所有产品里列表', 'reportAmzProductAllListService', 10, b'0', ''),
	(2, '可售品报表', 'ProductOpenListings', 'GET_FLAT_FILE_OPEN_LISTINGS_DATA', '用于排除掉不可售产品', 'reportAmzProductOpenListService', 0, b'0', ''),
	(3, '在售品报表', 'ProductActiveListings', 'GET_MERCHANT_LISTINGS_DATA', '用于计算在售产品的广告比例', 'reportAmzProductActiveListService', 0, b'0', ''),
	(4, '产品推荐报表', 'ProductRecommendedListings', 'MBOP_RECOMMENDED_PRODUCTS_REPORT', '推荐给卖家并给出利润分析', 'reportAmzProductRecommendedListService', 0, b'0', ''),
	(5, '订单购买报表', 'OrdersByOrderDateReport', 'GET_FLAT_FILE_ALL_ORDERS_DATA_BY_ORDER_DATE_GENERAL', '销售订单统计', 'reportAmzOrderByOrderDateService', 2, b'0', ''),
	(6, '订单更新报表', 'OrdersByLastUpdateReport', 'GET_FLAT_FILE_ALL_ORDERS_DATA_BY_LAST_UPDATE_GENERAL', '销售订单统计', 'reportAmzOrderByLastUpdateService', 1, b'0', ''),
	(7, '库存报表', 'InventoryReport', 'GET_FBA_MYI_UNSUPPRESSED_INVENTORY_DATA', 'FBA库存', 'reportAmzInventoryService', 0, b'0', ''),
	(8, '预留库存报表', 'ReservedInventoryReport', 'GET_RESERVED_INVENTORY_DATA', 'FBA库存预留情况', 'reportAmzReservedInventoryService', 0, b'0', ''),
	(9, '轻小产品库存报表', 'FBAUNOInventoryReport', 'GET_FBA_UNO_INVENTORY_DATA', '轻小产品标记', 'reportFBAUNOInventoryService', 0, b'0', ''),
	(10, '预估FBA费用报表', 'FbaFeeReport', 'GET_FBA_ESTIMATED_FBA_FEES_TXT_DATA', 'FBA费用与本地计算比对', 'reportAmzFBAFeeService', 7, b'0', ''),
	(11, '仓储费报表', 'FbaStorageFeeReport', 'GET_FBA_STORAGE_FEE_CHARGES_DATA', '仓储费用报表', 'reportAmzFBAStorageFeeService', 0, b'0', ''),
	(12, '长期仓储费报表', 'FbaLongTermStorageFeeReport', 'GET_FBA_FULFILLMENT_LONGTERM_STORAGE_FEE_CHARGES_DATA', '长期仓储费用报表', 'reportAmzfBALongTermStorageFeeService', 0, b'0', ''),
	(14, '账期报表', 'SettlementReport', 'GET_V2_SETTLEMENT_REPORT_DATA_FLAT_FILE_V2', '账期报告', 'reportAmzSettlementListService', 30, b'0', ''),
	(15, '库存滞销报表', 'FBAInventoryHealthReport', 'GET_FBA_FULFILLMENT_INVENTORY_HEALTH_DATA', '库存滞销情况', 'reportInventoryHealthListService', 5, b'0', ''),
	(16, '退货单报表', 'FBAReturnsReport', 'GET_FBA_FULFILLMENT_CUSTOMER_RETURNS_DATA', '退货单用于订单管理', 'reportAmzReturnsService', 10, b'0', ''),
	(17, '产品分类报表', 'BrowseTreeReport', 'GET_XML_BROWSE_TREE_DATA', '产品分类下载用于广告模块', 'reportBrowseTreeService', 0, b'0', ''),
	(18, '店铺绩效报表', 'PerformanceReport', 'GET_V2_SELLER_PERFORMANCE_REPORT', '店铺绩效情况', 'reportAmzPerformanceService', 1, b'0', ''),
	(19, '发货报表', 'FulfilledShipmentsReport', 'GET_AMAZON_FULFILLED_SHIPMENTS_DATA_GENERAL', '发货报表获取发货地址与对于情况', 'reportAmzFulFilledShipmentsService', 2, b'0', ''),
	(20, '货物移除报表', 'FbaShipmentDetailRemoveReport', 'GET_FBA_FULFILLMENT_REMOVAL_ORDER_DETAIL_DATA', '获取移除库存的订单报表', 'reportAmzFBAShipmentRemoveService', 5, b'0', ''),
	(21, '流量报表', 'SalesAndTrafficBusinessReport', 'GET_SALES_AND_TRAFFIC_REPORT', '流量报表', 'reportAmzSalesAndTrafficBusinessService', 2, b'0', '{"dateGranularity":"DAY", "asinGranularity":"CHILD'),
	(22, '订单发票报表', 'OrdersInvoicingReport', 'GET_FLAT_FILE_ORDER_REPORT_DATA_INVOICING', '订单发票报表', 'reportAmzOrderInvoiceService', 60, b'0', ''),
	(23, '欧洲库存报表', 'InventoryByCountry', 'GET_AFN_INVENTORY_DATA_BY_COUNTRY', '欧洲库存报表', 'reportAmzInventoryCountryService', 3, b'0', ''),
	(24, '库龄报表', 'InventoryAge', 'GET_FBA_INVENTORY_AGED_DATA', '库龄报表', 'reportAmzInventoryAgeService', 0, b'0', '');
/*!40000 ALTER TABLE `t_amz_report_request_type` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
