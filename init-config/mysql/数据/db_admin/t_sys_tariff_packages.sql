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

-- 正在导出表  db_admin.t_sys_tariff_packages 的数据：~5 rows (大约)
DELETE FROM `t_sys_tariff_packages`;
INSERT INTO `t_sys_tariff_packages` (`id`, `name`, `roleId`, `isdefault`, `maxShopCount`, `maxProductCount`, `maxOrderCount`, `maxMember`, `maxProfitPlanCount`, `maxMarketCount`, `orderMemoryCount`, `dayOpenAdvCount`, `controlProductCount`, `anysisProductCount`, `yearprice`, `monthprice`, `lastUpdateTime`, `lastUpdateUser`) VALUES
	(0, '免费版', 26138972975989607, b'1', 100000000, 100000000, 1800, 3, 1, 100000000, '12', 50, 100000000, 100000000, 0.00, 0.00, '2019-03-11', 'aeaec5ff-ab13-11e6-bab5-00e04c023f0e'),
	(1, '入门版', 26138972975989587, b'0', 100000000, 100000000, 9000, 10, 2, 100000000, '36', 300, 100000000, 100000000, 380.00, 38.00, '2019-03-11', 'aeaec5ff-ab13-11e6-bab5-00e04c023f0e'),
	(2, '标准版', 26138972975989571, b'0', 100000000, 100000000, 30000, 50, 5, 100000000, '36', 3000, 100000000, 100000000, 899.00, 89.00, '2019-03-11', 'aeaec5ff-ab13-11e6-bab5-00e04c023f0e'),
	(3, '专业版', 26138972975989575, b'0', 100000000, 100000000, 60000, 100, 10, 100000000, '36', 8000, 100000000, 100000000, 1600.00, 140.00, '2019-03-11', 'aeaec5ff-ab13-11e6-bab5-00e04c023f0e'),
	(4, '旗舰版', 26138972975989596, b'0', 100000000, 100000000, 100000000, 100000000, 100000000, 100000000, '72', 100000000, 100000000, 100000000, 8000.00, 1000.00, '2019-03-11', 'aeaec5ff-ab13-11e6-bab5-00e04c023f0e');

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
