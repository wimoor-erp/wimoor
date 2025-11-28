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

-- 导出  表 db_amazon.t_amz_vat_transaction 结构
CREATE TABLE IF NOT EXISTS `t_amz_vat_transaction` (
  `id` bigint unsigned NOT NULL,
  `amazonAuthid` bigint unsigned NOT NULL,
  `unique_account_identifier` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `activity_period` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `activity_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `sales_channel` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `marketplace` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `program_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `transaction_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `transaction_event_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `activity_transaction_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `tax_calculation_date` date DEFAULT NULL,
  `transaction_depart_date` date DEFAULT NULL,
  `transaction_arrival_date` date DEFAULT NULL,
  `transaction_complete_date` date NOT NULL,
  `seller_sku` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `asin` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `item_manufacture_country` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `qty` int DEFAULT NULL,
  `item_weight` decimal(10,2) DEFAULT NULL,
  `total_activity_weight` decimal(10,2) DEFAULT NULL,
  `cost_price_of_items` decimal(10,2) DEFAULT NULL,
  `price_of_items_amt_vat_excl` decimal(10,2) DEFAULT NULL,
  `promo_price_of_items_amt_vat_excl` decimal(10,2) DEFAULT NULL,
  `total_price_of_items_amt_vat_excl` decimal(10,2) DEFAULT NULL,
  `ship_charge_amt_vat_excl` decimal(10,2) DEFAULT NULL,
  `promo_ship_charge_amt_vat_excl` decimal(10,2) DEFAULT NULL,
  `total_ship_charge_amt_vat_excl` decimal(10,2) DEFAULT NULL,
  `gift_wrap_amt_vat_excl` decimal(10,2) DEFAULT NULL,
  `promo_gift_wrap_amt_vat_excl` decimal(10,2) DEFAULT NULL,
  `total_gift_wrap_amt_vat_excl` decimal(10,2) DEFAULT NULL,
  `total_activity_value_amt_vat_excl` decimal(10,2) DEFAULT NULL,
  `price_of_items_vat_rate_percent` decimal(10,2) DEFAULT NULL,
  `price_of_items_vat_amt` decimal(10,2) DEFAULT NULL,
  `promo_price_of_items_vat_amt` decimal(10,2) DEFAULT NULL,
  `total_price_of_items_vat_amt` decimal(10,2) DEFAULT NULL,
  `ship_charge_vat_rate_percent` decimal(10,2) DEFAULT NULL,
  `ship_charge_vat_amt` decimal(10,2) DEFAULT NULL,
  `promo_ship_charge_vat_amt` decimal(10,2) DEFAULT NULL,
  `total_ship_charge_vat_amt` decimal(10,2) DEFAULT NULL,
  `gift_wrap_vat_rate_percent` decimal(10,2) DEFAULT NULL,
  `gift_wrap_vat_amt` decimal(10,2) DEFAULT NULL,
  `promo_gift_wrap_vat_amt` decimal(10,2) DEFAULT NULL,
  `total_gift_wrap_vat_amt` decimal(10,2) DEFAULT NULL,
  `total_activity_value_vat_amt` decimal(10,2) DEFAULT NULL,
  `price_of_items_amt_vat_incl` decimal(10,2) DEFAULT NULL,
  `promo_price_of_items_amt_vat_incl` decimal(10,2) DEFAULT NULL,
  `total_price_of_items_amt_vat_incl` decimal(10,2) DEFAULT NULL,
  `ship_charge_amt_vat_incl` decimal(10,2) DEFAULT NULL,
  `promo_ship_charge_amt_vat_incl` decimal(10,2) DEFAULT NULL,
  `total_ship_charge_amt_vat_incl` decimal(10,2) DEFAULT NULL,
  `gift_wrap_amt_vat_incl` decimal(10,2) DEFAULT NULL,
  `promo_gift_wrap_amt_vat_incl` decimal(10,2) DEFAULT NULL,
  `total_gift_wrap_amt_vat_incl` decimal(10,2) DEFAULT NULL,
  `total_activity_value_amt_vat_incl` decimal(10,2) DEFAULT NULL,
  `transaction_currency_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `commodity_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `statistical_code_depart` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `statistical_code_arrival` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `commodity_code_supplementary_unit` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `item_qty_supplementary_unit` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL,
  `total_activity_supplementary_unit` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL,
  `product_tax_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `depature_city` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `departure_country` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `departure_post_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `arrival_city` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `arrival_country` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `arrival_post_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `sale_depart_country` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `sale_arrival_country` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `transportation_mode` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `delivery_conditions` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `seller_depart_vat_number_country` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `seller_depart_country_vat_number` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `seller_arrival_vat_number_country` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `seller_arrival_country_vat_number` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `transaction_seller_vat_number_country` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `transaction_seller_vat_number` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `buyer_vat_number_country` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `buyer_vat_number` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `vat_calculation_imputation_country` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `taxable_jurisdiction` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `taxable_jurisdiction_level` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `vat_inv_number` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `vat_inv_converted_amt` decimal(10,2) DEFAULT NULL,
  `vat_inv_currency_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `vat_inv_exchange_rate` decimal(10,2) DEFAULT NULL,
  `vat_inv_exchange_rate_date` date DEFAULT NULL,
  `export_outside_eu` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `invoice_url` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `buyer_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `arrival_address` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `supplier_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `supplier_vat_number` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `tax_reporting_scheme` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `tax_collection_responsibility` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`transaction_complete_date`,`id`) USING BTREE,
  UNIQUE KEY `transaction_event_id_activity_transaction_id` (`transaction_event_id`,`activity_transaction_id`,`seller_sku`),
  KEY `amazonAuthid` (`amazonAuthid`,`transaction_complete_date`,`transaction_type`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- 数据导出被取消选择。

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
