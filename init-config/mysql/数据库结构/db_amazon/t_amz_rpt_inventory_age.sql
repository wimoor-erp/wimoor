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

-- 导出  表 db_amazon.t_amz_rpt_inventory_age 结构
CREATE TABLE IF NOT EXISTS `t_amz_rpt_inventory_age` (
  `id` bigint unsigned NOT NULL,
  `authid` bigint unsigned DEFAULT NULL,
  `snapshot` datetime DEFAULT NULL,
  `marketplace` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `sku` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `fnsku` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `asin` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `fcondition` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `qty_with_removals_in_progress` int DEFAULT NULL,
  `inv_age_0_to_90_days` int DEFAULT NULL,
  `inv_age_91_to_180_days` int DEFAULT NULL,
  `inv_age_181_to_270_days` int DEFAULT NULL,
  `inv_age_271_to_365_days` int DEFAULT NULL,
  `inv_age_365_plus_days` int DEFAULT NULL,
  `currency` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `qty_to_be_charged_ltsf_6_mo` int DEFAULT NULL,
  `projected_ltsf_6_mo` decimal(20,6) DEFAULT NULL,
  `qty_to_be_charged_ltsf_12_mo` int DEFAULT NULL,
  `projected_ltsf_12_mo` decimal(20,6) DEFAULT NULL,
  `units_shipped_last_7_days` int DEFAULT NULL,
  `units_shipped_last_30_days` int DEFAULT NULL,
  `units_shipped_last_60_days` int DEFAULT NULL,
  `units_shipped_last_90_days` int DEFAULT NULL,
  `your_price` decimal(20,6) DEFAULT NULL,
  `sales_price` decimal(20,6) DEFAULT NULL,
  `lowest_price_new` decimal(20,6) DEFAULT NULL,
  `lowest_price_used` decimal(20,6) DEFAULT NULL,
  `recommended_action` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `days` int DEFAULT NULL,
  `removal_quantity` decimal(20,6) DEFAULT NULL,
  `estimated_cost_savings_of_recommended_actions` decimal(20,6) DEFAULT NULL,
  `sell_through` decimal(20,6) DEFAULT NULL,
  `item_volume` decimal(20,6) DEFAULT NULL,
  `volume_units` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `storage_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `authid_marketplace_sku` (`authid`,`marketplace`,`sku`,`fcondition`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='GET_FBA_INVENTORY_AGED_DATA 已经废弃';

-- 数据导出被取消选择。

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
