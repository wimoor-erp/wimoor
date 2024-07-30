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

-- 正在导出表  db_amazon.t_product_type 的数据：~36 rows (大约)
DELETE FROM `t_product_type`;
INSERT INTO `t_product_type` (`id`, `name`, `country`) VALUES
	(111, 'Amazon Device Accessories', 'US'),
	(112, 'Amazon Kindle', 'US'),
	(113, 'Automotive & Powersports', 'US'),
	(114, 'Baby Products (excluding apparel)', 'US'),
	(115, 'Beauty', 'US'),
	(116, 'Books', 'US'),
	(117, 'Camera & Photo', 'US'),
	(118, 'Cell Phones & Accessories', 'US'),
	(119, 'Clothing & Accessories', 'US'),
	(120, 'Consumer Electronics', 'US'),
	(121, 'Fine Art', 'US'),
	(122, 'Grocery & Gourmet Food', 'US'),
	(123, 'Health & Personal Care', 'US'),
	(124, 'Home & Garden', 'US'),
	(125, 'Independent Design', 'US'),
	(126, 'Industrial & Scientific', 'US'),
	(127, 'Jewelry', 'US'),
	(128, 'Kindle Accessories and Amazon Fire TV Accessories', 'US'),
	(129, 'Luggage & Travel Accessories', 'US'),
	(130, 'Major Appliances', 'US'),
	(131, 'Music', 'US'),
	(132, 'Musical Instruments', 'US'),
	(133, 'Office Products', 'US'),
	(134, 'Outdoors', 'US'),
	(135, 'Personal Computers', 'US'),
	(136, 'Pet Supplies', 'US'),
	(137, 'Sexual Wellness', 'US'),
	(138, 'Shoes, Handbags & Sunglasses', 'US'),
	(139, 'Software', 'US'),
	(140, 'Sports', 'US'),
	(141, 'Tools & Home Improvement', 'US'),
	(142, 'Toys & Games', 'US'),
	(143, 'Video, DVD & Blu-ray', 'US'),
	(144, 'Video Games', 'US'),
	(145, 'Watches', 'US'),
	(146, 'Wine', 'US');

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
