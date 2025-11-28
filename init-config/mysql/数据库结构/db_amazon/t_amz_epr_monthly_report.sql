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

-- 导出  表 db_amazon.t_amz_epr_monthly_report 结构
CREATE TABLE IF NOT EXISTS `t_amz_epr_monthly_report` (
  `sellerid` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `report_period_start` date NOT NULL,
  `report_period_end` date NOT NULL,
  `asin` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `amazon_marketplace` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `ship_to_country` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `ship_to_country_code` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `registration_number` varchar(20) COLLATE utf8mb4_bin DEFAULT NULL,
  `epr_category` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `epr_subcategory1` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `epr_subcategory2` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `epr_subcategory3` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `epr_subcategory4` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `gl_product_group_description` varchar(20) COLLATE utf8mb4_bin DEFAULT NULL,
  `product_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `total_units_sold` int DEFAULT NULL,
  `units_per_asin` int DEFAULT NULL,
  `battery_embedded` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `item_weight_without_package_kg` decimal(10,2) DEFAULT NULL,
  `item_weight_with_package_kg` decimal(10,2) DEFAULT NULL,
  `total_reported_weight_kg` decimal(10,2) DEFAULT NULL,
  `item_width_cm` decimal(10,2) DEFAULT NULL,
  `package_width_cm` decimal(10,2) DEFAULT NULL,
  `item_height_cm` decimal(10,2) DEFAULT NULL,
  `package_height_cm` decimal(10,2) DEFAULT NULL,
  `paper_kg` decimal(10,2) DEFAULT NULL,
  `glass_kg` decimal(10,2) DEFAULT NULL,
  `aluminum_kg` decimal(10,2) DEFAULT NULL,
  `steel_kg` decimal(10,2) DEFAULT NULL,
  `plastic_kg` decimal(10,2) DEFAULT NULL,
  `wood_kg` decimal(10,2) DEFAULT NULL,
  `other_kg` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`sellerid`,`report_period_start`,`report_period_end`,`asin`,`ship_to_country_code`,`epr_category`),
  KEY `sellerid_report_period_start_report_period_end_epr_category` (`sellerid`,`report_period_start`,`report_period_end`,`epr_category`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- 数据导出被取消选择。

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
