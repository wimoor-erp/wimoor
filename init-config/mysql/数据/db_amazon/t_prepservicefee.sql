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

-- 正在导出表  db_amazon.t_prepservicefee 的数据：~37 rows (大约)
DELETE FROM `t_prepservicefee`;
INSERT INTO `t_prepservicefee` (`id`, `category`, `isStandard`, `prepServiceFee`, `country`) VALUES
	(1, 'Fragile/Glass', b'1', 0.70, 'DE'),
	(2, 'Liquids', b'1', 0.45, 'DE'),
	(3, 'Fragile/Glass', b'0', 1.40, 'DE'),
	(4, 'Liquids', b'0', 0.90, 'DE'),
	(5, 'Apparel, Fabric, Plush, and Textiles', b'1', 0.45, 'DE'),
	(6, 'Apparel, Fabric, Plush, and Textiles', b'0', 0.90, 'DE'),
	(7, 'Baby Products', b'1', 0.45, 'DE'),
	(8, 'Baby Products', b'0', 0.90, 'DE'),
	(9, 'Small', b'1', 0.45, 'DE'),
	(10, 'Adult', b'1', 0.90, 'DE'),
	(11, 'Adult', b'0', 1.80, 'DE'),
	(12, 'Fragile/Glass', b'1', 0.80, 'US'),
	(13, 'Fragile/Glass', b'0', 1.60, 'US'),
	(14, 'Liquids', b'1', 0.50, 'US'),
	(15, 'Liquids', b'0', 1.00, 'US'),
	(16, 'Apparel, Fabric, Plush, and Textiles', b'1', 0.50, 'US'),
	(17, 'Apparel, Fabric, Plush, and Textiles', b'0', 1.00, 'US'),
	(18, 'Baby Products', b'1', 0.50, 'US'),
	(19, 'Baby Products', b'0', 1.00, 'US'),
	(20, 'Small', b'1', 0.50, 'US'),
	(21, 'Adult', b'1', 1.00, 'US'),
	(22, 'Adult', b'0', 2.00, 'US'),
	(23, 'Sharp', b'1', 0.80, 'US'),
	(24, 'Sharp', b'0', 1.60, 'US'),
	(25, 'Fragile/Glass', b'1', 0.40, 'UK'),
	(26, 'Fragile/Glass', b'0', 0.80, 'UK'),
	(27, 'Liquids', b'1', 0.25, 'UK'),
	(28, 'Liquids', b'0', 0.50, 'UK'),
	(29, 'Apparel, Fabric, Plush, and Textiles', b'1', 0.25, 'UK'),
	(30, 'Apparel, Fabric, Plush, and Textiles', b'0', 0.50, 'UK'),
	(31, 'Baby Products', b'1', 0.25, 'UK'),
	(32, 'Baby Products', b'0', 0.50, 'UK'),
	(33, 'Small', b'1', 0.25, 'UK'),
	(34, 'Others', b'1', 0.45, 'DE'),
	(35, 'Others', b'0', 0.90, 'DE'),
	(36, 'Others', b'1', 0.25, 'UK'),
	(37, 'Others', b'0', 0.50, 'UK');

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
