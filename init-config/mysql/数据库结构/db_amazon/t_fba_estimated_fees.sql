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

-- 导出  表 db_amazon.t_fba_estimated_fees 结构
CREATE TABLE IF NOT EXISTS `t_fba_estimated_fees` (
  `sku` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `fnsku` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `asin` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `product_name` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `product_group` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `brand` char(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `fulfilled_by` char(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `has_local_inventory` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `your_price` decimal(10,2) DEFAULT NULL,
  `sales_price` decimal(10,2) DEFAULT NULL,
  `longest_side` decimal(10,2) DEFAULT NULL,
  `median_side` decimal(10,2) DEFAULT NULL,
  `shortest_side` decimal(10,2) DEFAULT NULL,
  `length_and_girth` decimal(10,2) DEFAULT NULL,
  `unit_of_dimension` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `item_package_weight` decimal(10,2) DEFAULT NULL,
  `unit_of_weight` char(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `product_size_tier` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `currency` char(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `estimated_fee_total` decimal(10,2) DEFAULT NULL,
  `estimated_referral_fee_per_unit` decimal(10,2) DEFAULT NULL,
  `estimated_variable_closing_fee` decimal(10,2) DEFAULT NULL COMMENT '自配送方式才有的费用',
  `estimated_fixed_closing_fee` decimal(10,2) DEFAULT NULL COMMENT '印度独有的费用',
  `estimated_order_handling_fee_per_order` decimal(10,2) DEFAULT NULL,
  `estimated_pick_pack_fee_per_unit` decimal(10,2) DEFAULT NULL,
  `estimated_weight_handling_fee_per_unit` decimal(10,2) DEFAULT NULL,
  `expected_fulfillment_fee_per_unit` decimal(10,2) DEFAULT NULL,
  `estimated_future_fee` decimal(10,2) DEFAULT NULL,
  `estimated_future_order_handling_fee_per_order` decimal(10,2) DEFAULT NULL,
  `estimated_future_pick_pack_fee_per_unit` decimal(10,2) DEFAULT NULL,
  `estimated_future_weight_handling_fee_per_unit` decimal(10,2) DEFAULT NULL,
  `expected_future_fulfillment_fee_per_unit` decimal(10,2) DEFAULT NULL,
  `expected_domestic_fulfilment_fee_per_unit` decimal(10,2) DEFAULT NULL,
  `expected_efn_fulfilment_fee_per_unit_uk` decimal(10,2) DEFAULT NULL,
  `expected_efn_fulfilment_fee_per_unit_de` decimal(10,2) DEFAULT NULL,
  `expected_efn_fulfilment_fee_per_unit_it` decimal(10,2) DEFAULT NULL,
  `expected_efn_fulfilment_fee_per_unit_fr` decimal(10,2) DEFAULT NULL,
  `expected_efn_fulfilment_fee_per_unit_es` decimal(10,2) DEFAULT NULL,
  `expected_efn_fulfilment_fee_per_unit_se` decimal(10,2) DEFAULT NULL,
  `amazonAuthId` bigint unsigned NOT NULL,
  `marketplaceid` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `lastupdate` datetime DEFAULT NULL,
  PRIMARY KEY (`sku`,`asin`,`amazonAuthId`,`marketplaceid`),
  KEY `index_auth` (`amazonAuthId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
