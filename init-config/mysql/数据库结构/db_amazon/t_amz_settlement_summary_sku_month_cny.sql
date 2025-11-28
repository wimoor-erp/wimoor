-- --------------------------------------------------------
-- 主机:                           wimoor.rwlb.rds.aliyuncs.com
-- 服务器版本:                        8.0.36 - Source distribution
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

-- 导出  表 db_amazon.t_amz_settlement_summary_sku_month_cny 结构
CREATE TABLE IF NOT EXISTS `t_amz_settlement_summary_sku_month_cny` (
  `id` bigint unsigned NOT NULL,
  `amazonAuthId` bigint unsigned NOT NULL,
  `sku` char(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `msku` char(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `groupid` bigint unsigned NOT NULL,
  `asin` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `parentasin` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `categoryid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `marketplace_name` char(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `posted_date` date NOT NULL,
  `owner` bigint unsigned DEFAULT NULL,
  `pid` bigint unsigned DEFAULT NULL,
  `mid` bigint unsigned DEFAULT NULL,
  `currency` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `order_amount` int DEFAULT NULL COMMENT '订单量',
  `sales` int DEFAULT NULL COMMENT '销量',
  `refundsales` int DEFAULT NULL,
  `refundorder` int DEFAULT NULL,
  `principal` decimal(20,8) DEFAULT NULL COMMENT '销售额',
  `commission` decimal(20,8) DEFAULT NULL COMMENT '销售佣金',
  `fbafee` decimal(20,8) DEFAULT NULL COMMENT 'FBA费用',
  `refund` decimal(20,8) DEFAULT NULL COMMENT '退款金额',
  `shipping` decimal(20,8) DEFAULT NULL,
  `promotion` decimal(20,8) DEFAULT NULL,
  `tax` decimal(20,8) DEFAULT NULL,
  `otherfee` decimal(20,8) DEFAULT NULL COMMENT '其它',
  `share_storage_fee` decimal(20,8) DEFAULT NULL,
  `share_long_storage_fee` decimal(20,8) DEFAULT NULL,
  `share_adv_spend_fee` decimal(20,8) DEFAULT NULL,
  `share_coupon_redemption_fee` decimal(20,8) DEFAULT NULL,
  `share_reserve_fee` decimal(20,8) DEFAULT NULL,
  `share_reimbursement_fee` decimal(20,8) DEFAULT NULL,
  `share_disposal_fee` decimal(20,8) DEFAULT NULL,
  `share_shop_other_fee` decimal(20,8) DEFAULT NULL,
  `local_price` decimal(20,8) DEFAULT NULL,
  `local_unit_price` decimal(20,8) DEFAULT NULL,
  `local_other_cost` decimal(20,8) DEFAULT NULL,
  `local_return_tax` decimal(20,8) DEFAULT NULL,
  `local_shipment_item_fee` decimal(20,8) DEFAULT NULL COMMENT '来自本地货件',
  `local_fifo_shipment_fee` decimal(20,8) DEFAULT NULL,
  `local_fifo_cost` decimal(20,8) DEFAULT NULL,
  `profit_local_shipmentfee` decimal(20,8) DEFAULT NULL,
  `profit_vat` decimal(20,8) DEFAULT NULL,
  `profit_companytax` decimal(20,8) DEFAULT NULL,
  `profit_customstax` decimal(20,8) DEFAULT NULL,
  `profit_exchangelost` decimal(20,8) DEFAULT NULL,
  `profit_lostrate` decimal(20,8) DEFAULT NULL,
  `profit_marketfee` decimal(20,8) DEFAULT NULL,
  `profit_otherfee` decimal(20,6) DEFAULT NULL,
  `rpt_storage_fee` decimal(20,8) DEFAULT NULL COMMENT '来自仓储费报表',
  `rpt_long_storage_fee` decimal(20,8) DEFAULT NULL,
  `rpt_adv_spend_fee` decimal(20,8) DEFAULT NULL,
  `rpt_adv_sales` decimal(20,8) DEFAULT NULL,
  `rpt_adv_units` int DEFAULT NULL,
  `rpt_disposal_fee` decimal(20,8) DEFAULT NULL,
  `rpt_disposal_units` int DEFAULT NULL,
  `rpt_reimbursements_fee` decimal(20,8) DEFAULT NULL,
  `fin_sum_fee` decimal(20,8) DEFAULT NULL,
  `profit` decimal(20,8) DEFAULT NULL,
  PRIMARY KEY (`posted_date`,`id`),
  KEY `owner` (`owner`),
  KEY `parentasin` (`parentasin`),
  KEY `msku` (`msku`),
  KEY `amazonAuthId` (`amazonAuthId`),
  KEY `groupid` (`groupid`),
  KEY `mid` (`mid`),
  KEY `asin` (`asin`),
  KEY `pid` (`pid`),
  KEY `sku` (`sku`),
  KEY `categoryid` (`categoryid`),
  KEY `marketplace_name` (`marketplace_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
