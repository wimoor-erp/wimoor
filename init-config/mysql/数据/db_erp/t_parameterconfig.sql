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

-- 导出  表 db_erp.t_parameterconfig 结构
CREATE TABLE IF NOT EXISTS `t_parameterconfig` (
  `id` int NOT NULL AUTO_INCREMENT,
  `ptype` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `pkey` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `sortindex` int DEFAULT NULL,
  `description` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `pkey` (`pkey`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 正在导出表  db_erp.t_parameterconfig 的数据：~29 rows (大约)
DELETE FROM `t_parameterconfig`;
INSERT INTO `t_parameterconfig` (`id`, `ptype`, `pkey`, `value`, `sortindex`, `description`) VALUES
	(1, 'salesChannel', 'amz_fba', '亚马逊订单、亚马逊配送', 1, '销售渠道及发货方式'),
	(2, 'salesChannel', 'amz_byself', '亚马逊订单、卖家自配送', 2, NULL),
	(3, 'salesChannel', 'unamz_fba', '非亚马逊订单、亚马逊FBA配送', 3, NULL),
	(4, 'sellerplan', 'profession', '专业卖家（全球开店）', 1, '卖家销售计划'),
	(5, 'sellerplan', 'person', '个人卖家', 2, NULL),
	(6, 'shipmentstyle', 'manually', '手动输入', 1, '运费计算'),
	(7, 'shipmentstyle', 'weight', '根据产品重量计算', 2, NULL),
	(8, 'shipmentstyle', 'dim_weight', '根据产品重量与材积重量大者计算', 3, NULL),
	(9, 'logistics_us', 'us_std', 'Domestic Standard', 1, '物流方式'),
	(10, 'logistics_us', 'us_exp', 'Domestic Expedited', 2, NULL),
	(11, 'logistics_us', 'us_int', 'International', 3, NULL),
	(12, 'logistics_uk', 'uk_std', 'Standard Delivery UK', 1, NULL),
	(13, 'logistics_uk', 'uk_exp', 'Express Delivery UK', 2, NULL),
	(14, 'logistics_uk', 'uk_eur', 'European Amazon.co.uk Marketplace countries', 3, NULL),
	(15, 'logistics_uk', 'uk_us', 'US (including US protectorates)', 4, NULL),
	(16, 'logistics_uk', 'uk_rest', 'Rest of World Amazon.co.uk Marketplace countries', 5, NULL),
	(17, 'isfba', 'AMAZON', '亚马逊配送', 1, '配送方式,用于产品本身发货方式上的设定（如，使用自己的仓库则是自配送）'),
	(18, 'isfba', 'DEFAULT', '卖家自配送', 2, NULL),
	(19, 'order_status', 'Pending', 'Pending', 1, NULL),
	(20, 'order_status', 'Unshipped', 'Unshipped', 2, NULL),
	(21, 'order_status', 'Shipped', 'Shipped', 3, NULL),
	(22, 'order_status', 'Cancelled', 'Cancelled', 4, NULL),
	(23, 'order_channel', 'Amazon', '亚马逊配送', 1, '订单的配送方式，'),
	(24, 'order_channel', 'Merchant', '卖家自配送', 2, NULL),
	(25, 'is_business_order', 'true', '企业买家', 1, NULL),
	(26, 'is_business_order', 'false', '个人买家', 2, NULL),
	(27, 'customer', 'supplier', '供应商', 1, NULL),
	(28, 'customer', 'purchaser', '采购商', 2, NULL),
	(29, 'shipmentstyle', 'volume', '根据产品体积计算', 4, NULL);

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
