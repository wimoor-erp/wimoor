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

-- 正在导出表  db_amazon.t_amz_notifications 的数据：~16 rows (大约)
DELETE FROM `t_amz_notifications`;
INSERT INTO `t_amz_notifications` (`id`, `notifications`, `description`, `isrun`) VALUES
	(101, 'ACCOUNT_STATUS_CHANGED', '每当开发人员订阅的商家/市场对的帐户状态发生变化时发送。每当商家的帐户状态在 NORMAL、AT_RISK 和 DEACTIVATED 之间变化时，都会发布通知。', b'0'),
	(102, 'ANY_OFFER_CHANGED', '每当前 20 个报价中的任何一个发生变化时发送，按条件（新的或旧的），或者如果卖家列出的商品的外部价格（来自其他零售商的价格）发生变化。', b'1'),
	(103, 'B2B_ANY_OFFER_CHANGED', '每当前 20 名 B2B 报价发生变化时发送，以卖方列出的商品的任何价格变化（单件或数量折扣等级价格）的形式发送。', b'0'),
	(104, 'BRANDED_ITEM_CONTENT_CHANGE', '每当与销售合作伙伴有品牌关系的任何 ASIN 的标题、描述、项目符号或图像发生更改时发送。', b'0'),
	(105, 'FBA_OUTBOUND_SHIPMENT_STATUS', '每当我们为卖家创建或取消亚马逊物流货件时发送。', b'0'),
	(106, 'FEE_PROMOTION', '当促销活动开始时发送', b'0'),
	(107, 'FEED_PROCESSING_FINISHED', '每当使用 Selling Partner API for Feeds 提交的任何 Feed 达到 DONE、CANCELLED 或 FATAL 的 Feed 处理状态时发送', b'1'),
	(108, 'FULFILLMENT_ORDER_STATUS', '多渠道配送订单状态发生变化时发送', b'0'),
	(109, 'ITEM_PRODUCT_TYPE_CHANGE', '每当与销售合作伙伴有品牌关系的任何 ASIN 的产品类型名称发生更改时发送。', b'0'),
	(110, 'PRODUCT_TYPE_DEFINITIONS_CHANGE', '每当与销售合作伙伴有品牌关系的任何 ASIN 的产品类型名称发生更改时发送。', b'0'),
	(111, 'LISTINGS_ITEM_STATUS_CHANGE', '每当销售伙伴拥有的商品状态发生变化时发送。', b'0'),
	(112, 'LISTINGS_ITEM_ISSUES_CHANGE', '每当与销售伙伴拥有的商品相关的问题发生变化时发送。', b'0'),
	(113, 'MFN_ORDER_STATUS_CHANGE', 'MFN 订单可用性状态发生变化时发送', b'0'),
	(114, 'ORDER_STATUS_CHANGE', '每当订单可用性状态发生变化时发送。', b'1'),
	(115, 'PRICING_HEALTH', '每当卖家报价因为没有竞争力的价格而没有资格成为特色报价（Buy Box 报价）时发送', b'0'),
	(116, 'REPORT_PROCESSING_FINISHED', '每当您使用 Selling Partner API for Reports 请求的任何报告达到 DONE、CANCELLED 或 FATAL 的报告处理状态时发送。', b'1');

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
