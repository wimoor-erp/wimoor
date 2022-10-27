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

-- 正在导出表  db_plum.t_sys_quartz_task 的数据：~36 rows (大约)
/*!40000 ALTER TABLE `t_sys_quartz_task` DISABLE KEYS */;
INSERT INTO `t_sys_quartz_task` (`id`, `name`, `fgroup`, `description`, `cron`, `server`, `bean`, `method`, `parameter`, `path`, `isdelete`, `createdate`) VALUES
	(1, '阿里云巴巴订单跟踪', '阿里巴巴', '物流信息跟踪', '0 0 1 * * ?', 'wimoor-erp', NULL, NULL, '', 'http://wimoor-erp/erp/api/v1/purchase_form/refreshAlibabaOrder', b'1', '2022-05-09 11:23:14'),
	(2, '亚马逊订单跟踪', '亚马逊订单', '亚马逊订单', '0 0/6 * * * ?', 'wimoor-amazon', 'ordersController', 'refreshOrderAction', '', 'http://wimoor-amazon/amazon/api/v0/orders/refreshOrder', b'0', '2022-06-09 14:10:28'),
	(3, '亚马逊订单详情跟踪', '亚马逊订单', '亚马逊订单详情', '0 0/3 * * * ?', 'wimoor-amazon', 'ordersController', 'refreshOrdersItemAction', '', 'http://wimoor-amazon/amazon/api/v0/orders/refreshOrdersItem', b'0', '2022-06-09 14:10:29'),
	(4, '订单购买', '亚马逊报表', '亚马逊订单报表', '10 40 6 * * ?', 'wimoor-amazon', 'reportController', 'requestReportAction', 'GET_FLAT_FILE_ALL_ORDERS_DATA_BY_ORDER_DATE_GENERAL', 'http://wimoor-amazon/amazon/api/v1/report/requestReport/GET_FLAT_FILE_ALL_ORDERS_DATA_BY_ORDER_DATE_GENERAL', b'0', '2022-06-09 14:13:43'),
	(5, '订单更新', '亚马逊报表', '订单更新报表', '27 20 4 * * ?', 'wimoor-amazon', 'reportController', 'requestReportAction', 'GET_FLAT_FILE_ALL_ORDERS_DATA_BY_LAST_UPDATE_GENERAL', 'http://wimoor-amazon/amazon/api/v1/report/requestReport/GET_FLAT_FILE_ALL_ORDERS_DATA_BY_LAST_UPDATE_GENERAL', b'0', '2022-06-09 14:15:48'),
	(6, '退货', '亚马逊报表', '退货报表', '38 18 18 * * ?', 'wimoor-amazon', 'reportController', 'requestReportAction', 'GET_FBA_FULFILLMENT_CUSTOMER_RETURNS_DATA', 'http://wimoor-amazon/amazon/api/v1/report/requestReport/GET_FBA_FULFILLMENT_CUSTOMER_RETURNS_DATA', b'0', '2022-06-09 14:15:48'),
	(7, '订单发货', '亚马逊报表', '订单发货报表', '43 30 18 * * ?', 'wimoor-amazon', 'reportController', 'requestReportAction', 'GET_AMAZON_FULFILLED_SHIPMENTS_DATA_GENERAL', 'http://wimoor-amazon/amazon/api/v1/report/requestReport/GET_AMAZON_FULFILLED_SHIPMENTS_DATA_GENERAL', b'0', '2022-06-09 14:16:43'),
	(8, '移除', '亚马逊报表', '订单移除报表', '55 10 19 * * ?', 'wimoor-amazon', 'reportController', 'requestReportAction', 'GET_FBA_FULFILLMENT_REMOVAL_ORDER_DETAIL_DATA', 'http://wimoor-amazon/amazon/api/v1/report/requestReport/GET_FBA_FULFILLMENT_REMOVAL_ORDER_DETAIL_DATA', b'0', '2022-06-09 14:35:44'),
	(9, '订单', '亚马逊汇总', '订单汇总，根据下载的订单判断是否需要汇总', '0 5/30 * * * ?', 'wimoor-amazon', 'reportController', 'summaryOrderReportAction', '', 'http://wimoor-amazon/amazon/api/v1/report/summaryOrderReport', b'0', '2022-06-09 14:35:45'),
	(10, '库存', '亚马逊报表', '亚马逊库存报表', '25 20 5 * * ?', 'wimoor-amazon', 'reportController', 'requestReportAction', 'GET_FBA_MYI_UNSUPPRESSED_INVENTORY_DATA', 'http://wimoor-amazon/amazon/api/v1/report/requestReport/GET_FBA_MYI_UNSUPPRESSED_INVENTORY_DATA', b'0', '2022-06-09 14:35:46'),
	(11, '库龄', '亚马逊报表', '亚马逊库龄报表', '35 12 1 * * ?', 'wimoor-amazon', 'reportController', 'requestReportAction', 'GET_FBA_FULFILLMENT_INVENTORY_HEALTH_DATA', 'http://wimoor-amazon/amazon/api/v1/report/requestReport/GET_FBA_FULFILLMENT_INVENTORY_HEALTH_DATA', b'0', '2022-06-09 14:35:47'),
	(12, '更新处理', '亚马逊报表', '亚马逊报表更新处理', '0 2/5 * * * ?', 'wimoor-amazon', 'reportController', 'refreshReportAction', '', 'http://wimoor-amazon/amazon/api/v1/report/refreshReport', b'0', '2022-06-09 14:35:48'),
	(13, '账期', '亚马逊报表', '亚马逊账期报表', '55 30 19 * * ?', 'wimoor-amazon', 'reportController', 'requestReportAction', 'GET_V2_SETTLEMENT_REPORT_DATA_FLAT_FILE_V2', 'http://wimoor-amazon/amazon/api/v1/report/requestReport/GET_V2_SETTLEMENT_REPORT_DATA_FLAT_FILE_V2', b'0', '2022-06-09 14:35:49'),
	(14, '商品分类', '亚马逊报表', '亚马逊商品分类报表', '0 41 13 ? * 1L', NULL, NULL, NULL, '', NULL, b'1', '2022-06-09 14:35:50'),
	(15, 'FBA费用', '亚马逊报表', '亚马逊FBA费用报表', '10 41 2 * * ?', 'wimoor-amazon', 'reportController', 'requestReportAction', 'GET_FBA_ESTIMATED_FBA_FEES_TXT_DATA', 'http://wimoor-amazon/amazon/api/v1/report/requestReport/GET_FBA_ESTIMATED_FBA_FEES_TXT_DATA', b'0', '2022-06-09 14:35:51'),
	(16, '仓储费', '亚马逊报表', '亚马逊仓储费报表', '20 40 22 * * ?', 'wimoor-amazon', 'reportController', 'requestReportAction', 'GET_FBA_STORAGE_FEE_CHARGES_DATA', 'http://wimoor-amazon/amazon/api/v1/report/requestReport/GET_FBA_STORAGE_FEE_CHARGES_DATA', b'0', '2022-06-09 14:35:51'),
	(17, '长期仓储费', '亚马逊报表', '亚马逊长期仓储费报表', '30 0 22 * * ?', 'wimoor-amazon', 'reportController', 'requestReportAction', 'GET_FBA_FULFILLMENT_LONGTERM_STORAGE_FEE_CHARGES_DATA', 'http://wimoor-amazon/amazon/api/v1/report/requestReport/GET_FBA_FULFILLMENT_LONGTERM_STORAGE_FEE_CHARGES_DATA', b'0', '2022-06-09 14:35:52'),
	(18, '上架商品', '亚马逊报表', '亚马逊上架商品报表', '40 40 21 * * ?', 'wimoor-amazon', 'reportController', 'requestReportAction', 'GET_FLAT_FILE_OPEN_LISTINGS_DATA', 'http://wimoor-amazon/amazon/api/v1/report/requestReport/GET_FLAT_FILE_OPEN_LISTINGS_DATA', b'0', '2022-06-09 14:35:53'),
	(19, '全部商品', '亚马逊报表', '亚马逊全部商品报表', '5 30 23 * * ?', 'wimoor-amazon', 'reportController', 'requestReportAction', 'GET_MERCHANT_LISTINGS_ALL_DATA', 'http://wimoor-amazon/amazon/api/v1/report/requestReport/GET_MERCHANT_LISTINGS_ALL_DATA', b'0', '2022-06-09 14:35:54'),
	(20, '推荐商品', '亚马逊报表', '亚马逊推荐商品报表', '0 0 9 ? * SUN', 'wimoor-amazon', 'reportController', 'requestReportAction', 'MBOP_RECOMMENDED_PRODUCTS_REPORT', 'http://wimoor-amazon/amazon/api/v1/report/requestReport/MBOP_RECOMMENDED_PRODUCTS_REPORT', b'0', '2022-06-09 14:35:55'),
	(21, '提交处理', '亚马逊Feed', '亚马逊Feed数据提交', '0 0/5 * * * ?', 'wimoor-amazon', 'feedController', 'processAction', '', 'http://wimoor-amazon/amazon/api/v0/feed/process', b'0', '2022-06-09 14:37:35'),
	(22, '流量汇总', '亚马逊汇总', '汇总客户提交的流量报表', '0 10 21 * * ?', 'wimoor-amazon', 'amzProductPageviewsController', 'refreshSummaryAction', '', 'http://wimoor-amazon/amazon/api/v1/report/product/amzProductPageviews/refreshSummary', b'0', '2022-06-28 15:23:35'),
	(23, '流量上传迁移', '亚马逊汇总', '客户提交的流量报表迁移模拟队列', '0 1/10 * * * ?', 'wimoor-amazon', 'amzProductPageviewsController', 'refreshDownloadAction', '', 'http://wimoor-amazon/amazon/api/v1/report/product/amzProductPageviews/refreshDownload', b'0', '2022-06-28 15:27:58'),
	(24, '预留库存', '亚马逊报表', '亚马逊预留库存报表', '0 5 3 * * ?', 'wimoor-amazon', 'reportController', 'requestReportAction', 'GET_RESERVED_INVENTORY_DATA', 'http://wimoor-amazon/amazon/api/v1/report/requestReport/GET_RESERVED_INVENTORY_DATA', b'0', '2022-06-28 18:54:18'),
	(25, '报表申请', '亚马逊广告', '亚马逊广告报表申请', '55 0/10 * * * ?', 'wimoor-amazon-adv', 'schedulingConfigController', 'requestReport', '', 'http://wimoor-amazon-adv/amazonadv/api/v1/advReport/requestReport', b'0', '2022-07-04 16:40:51'),
	(26, '报表读取', '亚马逊广告', '亚马逊广告报表读取', '50 0/5 * * * ?', 'wimoor-amazon-adv', 'schedulingConfigController', 'readReport', '', 'http://wimoor-amazon-adv/amazonadv/api/v1/advReport/readReport', b'0', '2022-07-04 16:40:52'),
	(27, '快照申请', '亚马逊广告', '亚马逊广告快照申请', '45 5/50 0-8 * * ?', 'wimoor-amazon-adv', 'schedulingConfigController', 'requestSnapshot', '', 'http://wimoor-amazon-adv/amazonadv/api/v1/advReport/requestSnapshot', b'0', '2022-07-04 16:40:52'),
	(28, '快照读取', '亚马逊广告', '亚马逊广告快照读取', '30 0/2 * * * ?', 'wimoor-amazon-adv', 'schedulingConfigController', 'readSnapshot', '', 'http://wimoor-amazon-adv/amazonadv/api/v1/advReport/readSnapshot', b'0', '2022-07-04 16:47:12'),
	(29, '账期未出账', '亚马逊财务', '亚马逊财务账期未出账', '0 37 0 * * ?', 'wimoor-amazon', 'amzFinAccountController', 'refreshAmzFin', '', 'http://wimoor-amazon/amazon/api/v1/amzFinAccount/refreshAmzFin', b'0', '2022-05-09 11:23:14'),
	(30, '更新Portfolios', '亚马逊广告', '亚马逊广告更新Portfolios', '40 30 5 * * ?', 'wimoor-amazon-adv', 'schedulingConfigController', 'refreshListPortfolios', '', 'http://wimoor-amazon-adv/amazonadv/api/v1/AdvertPortfolios/refreshListPortfolios', b'0', '2022-07-05 09:00:58'),
	(31, 'Remind', '亚马逊广告', '亚马逊广告提醒', '38 20 8 * * ?', 'wimoor-amazon-adv', 'schedulingConfigController', 'refreshRemaindAction', '', 'http://wimoor-amazon-adv/amazonadv/api/v1/advManager/refreshRemaind', b'0', '2022-07-05 09:12:20'),
	(32, '更新商品对象', '亚马逊商品', '亚马逊产品更新商品对象', '0 1/8 19-23,0-9 * * ?', 'wimoor-amazon', 'amzProductRefreshController', 'refreshAction', '', 'http://wimoor-amazon/amazon/api/v1/report/product/amzProductRefresh/refresh', b'0', '2022-07-14 10:46:11'),
	(33, '更新关系', '亚马逊商品', '亚马逊产品更新关系', '0 1/6 18-23,0-8 * * ?', 'wimoor-amazon', 'amzProductRefreshController', 'refreshCatalogAction', '', 'http://wimoor-amazon/amazon/api/v1/report/product/amzProductRefresh/refreshCatalog', b'0', '2022-07-14 10:46:12'),
	(34, '价格', '亚马逊商品', '亚马逊商品价格', '0 0/7 18-23,0-8 * * ?', 'wimoor-amazon', 'amzProductRefreshController', 'refreshPriceAction', '', 'http://wimoor-amazon/amazon/api/v1/report/product/amzProductRefresh/refreshPrice', b'0', '2022-07-14 10:47:42'),
	(35, '授权迁移', '亚马逊权限', '亚马逊权限授权迁移', '0 37 0/2 * * ?', 'wimoor-amazon', 'amazonAuthorityController', 'getRefreshTokenAction', '', 'http://wimoor-amazon/amazon/api/v1/amzauthority/getRefreshToken', b'0', '2022-07-15 09:39:57'),
	(36, '处理报表', '亚马逊报表', '亚马逊报表处理', '0 1/10 * * * ?', 'wimoor-amazon', 'reportController', 'processReportAction', '', 'http://wimoor-amazon/amazon/api/v1/report/processReport', b'0', '2022-07-18 13:07:58');
/*!40000 ALTER TABLE `t_sys_quartz_task` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
