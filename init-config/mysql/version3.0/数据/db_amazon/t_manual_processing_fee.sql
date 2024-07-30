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

-- 正在导出表  db_amazon.t_inventorystoragefee 的数据：~29 rows (大约)
DELETE FROM `t_inventorystoragefee`;
INSERT INTO `t_inventorystoragefee` (`id`, `month`, `price`, `country`, `isStandard`) VALUES
	(1, 'Jan,Feb,Mar,Apr,May,June,July,Aug,Sept', 0.470, 'UK', NULL),
	(2, 'Oct,Nov,Dec', 0.600, 'UK', NULL),
	(3, 'Jan,Feb,Mar,Apr,May,June,July,Aug,Sept', 0.870, 'US', b'1'),
	(4, 'Jan,Feb,Mar,Apr,May,June,July,Aug,Sept', 0.560, 'US', b'0'),
	(5, 'Oct,Nov,Dec', 2.400, 'US', b'1'),
	(6, 'Oct,Nov,Dec', 1.400, 'US', b'0'),
	(7, 'Jan,Feb,Mar,Apr,May,June,July,Aug,Sept', 5.160, 'JP', b'1'),
	(8, 'Jan,Feb,Mar,Apr,May,June,July,Aug,Sept', 30.600, 'DE', NULL),
	(9, 'Oct,Nov,Dec', 42.370, 'DE', NULL),
	(10, 'Jan,Feb,Mar,Apr,May,June,July,Aug,Sept', 20.000, 'CA', NULL),
	(11, 'Oct,Nov,Dec', 28.000, 'CA', NULL),
	(12, 'Jan,Feb,Mar,Apr,May,June,July,Aug,Sept', 19.400, 'AU', NULL),
	(13, 'Oct,Nov,Dec', 26.500, 'AU', NULL),
	(14, 'ALL', 20.000, 'IN', NULL),
	(15, 'Jan,Feb,Mar,Apr,May,June,July,Aug,Sept', 0.310, 'MX', NULL),
	(16, 'Oct,Nov,Dec', 0.440, 'MX', NULL),
	(17, 'Oct,Nov,Dec', 9.170, 'JP', b'1'),
	(18, 'Oct,Nov,Dec', 7.760, 'JP', b'0'),
	(19, 'Jan,Feb,Mar,Apr,May,June,July,Aug,Sept', 4.370, 'JP', b'0'),
	(20, 'Jan,Feb,Mar,Apr,May,June,July,Aug,Sept,Oct,Nov,Dec', 3.000, 'SA', b'1'),
	(21, 'Jan,Feb,Mar,Apr,May,June,July,Aug,Sept,Oct,Nov,Dec', 30.000, 'SA', b'0'),
	(22, 'Jan,Feb,Mar,Apr,May,June,July,Aug,Sept,Oct,Nov,Dec', 2.000, 'AE', b'1'),
	(23, 'Jan,Feb,Mar,Apr,May,June,July,Aug,Sept,Oct,Nov,Dec', 20.000, 'AE', b'0'),
	(24, 'Jan,Feb,Mar,Apr,May,June,July,Aug,Sept', 273.000, 'SE', b'1'),
	(25, 'Oct,Nov,Dec', 378.000, 'SE', b'1'),
	(26, 'Jan,Feb,Mar,Apr,May,June,July,Aug,Sept', 189.000, 'SE', b'0'),
	(27, 'Oct,Nov,Dec', 263.000, 'SE', b'0'),
	(28, 'Jan,Feb,Mar,Apr,May,June,July,Aug,Sept', 0.780, 'UK', b'1'),
	(29, 'Oct,Nov,Dec', 1.100, 'UK', b'1');

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
