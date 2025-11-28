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

-- 正在导出表  db_amazon.t_fba_labeling_service_fee 的数据：~7 rows (大约)
DELETE FROM `t_fba_labeling_service_fee`;
INSERT INTO `t_fba_labeling_service_fee` (`id`, `isStandard`, `price`, `country`) VALUES
	('048a32b4-f4e3-11e6-bd71-00e04c023f0e', b'0', 43.00, 'JP'),
	('5e16c93a-f7ed-11e6-bd71-00e04c023f0e', NULL, 0.15, 'UK'),
	('602c6dad-f7ed-11e6-bd71-00e04c023f0e', NULL, 0.15, 'DE'),
	('64051190-2d29-11ea-9e0c-506b4b231e50', b'1', 0.10, 'US'),
	('6e12f54b-5f29-11e8-a076-00e04c023f0e', NULL, 0.20, 'CA'),
	('904fd19d-f4e2-11e6-bd71-00e04c023f0e', b'1', 19.00, 'JP'),
	('965215f9-f7ea-11e6-bd71-00e04c023f0e', b'0', 0.30, 'US');

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
