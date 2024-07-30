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

-- 正在导出表  db_amazon.t_variable_closing_fee 的数据：~64 rows (大约)
DELETE FROM `t_variable_closing_fee`;
INSERT INTO `t_variable_closing_fee` (`id`, `country`, `typeid`, `isMedia`, `logisticsId`, `name`, `format`) VALUES
	(39, 'JP', 8, '1', '0', NULL, '80'),
	(40, 'JP', 28, '1', '0', NULL, '140'),
	(41, 'JP', 103, '1', '0', NULL, '140'),
	(42, 'JP', 102, '1', '0', NULL, '140'),
	(43, 'JP', 113, '1', '0', NULL, '140'),
	(44, 'UK', 8, '1', '0', NULL, '0.50'),
	(45, 'UK', 101, '1', '0', NULL, '0.50'),
	(46, 'UK', 103, '1', '0', NULL, '0.50'),
	(47, 'UK', 102, '1', '0', NULL, '0.50'),
	(48, 'UK', 104, '1', '0', NULL, '0.50'),
	(49, 'UK', 31, '1', '0', NULL, '0.50'),
	(50, 'ES', 8, '1', '0', NULL, '1.01'),
	(51, 'ES', 101, '1', '0', NULL, '0.81'),
	(52, 'ES', 103, '1', '0', NULL, '0.81'),
	(53, 'ES', 102, '1', '0', NULL, '0.81'),
	(54, 'ES', 104, '1', '0', NULL, '0.81'),
	(55, 'ES', 31, '1', '0', NULL, '0.81'),
	(56, 'IT', 8, '1', '0', NULL, '1.01'),
	(57, 'IT', 101, '1', '0', NULL, '0.81'),
	(58, 'IT', 103, '1', '0', NULL, '0.81'),
	(59, 'IT', 102, '1', '0', NULL, '0.81'),
	(60, 'IT', 104, '1', '0', NULL, '0.81'),
	(61, 'IT', 31, '1', '0', NULL, '0.81'),
	(64, 'FR', 103, '1', '0', NULL, '0.61'),
	(65, 'FR', 102, '1', '0', NULL, '0.61'),
	(66, 'FR', 104, '1', '0', NULL, '0.61'),
	(67, 'FR', 31, '1', '0', NULL, '0.61'),
	(68, 'FR', 8, '1', '0', NULL, '0.61'),
	(69, 'FR', 101, '1', '0', NULL, '0.61'),
	(74, 'DE', 8, '1', '0', NULL, '1.01'),
	(75, 'DE', 101, '1', '0', NULL, '0.81'),
	(76, 'DE', 103, '1', '0', NULL, '0.81'),
	(77, 'DE', 102, '1', '0', NULL, '0.81'),
	(78, 'DE', 104, '1', '0', NULL, '0.81'),
	(79, 'DE', 31, '1', '0', NULL, '0.81'),
	(80, 'US', 8, '1', '0', NULL, '1.8'),
	(81, 'US', 101, '1', '0', NULL, '1.8'),
	(82, 'US', 103, '1', '0', NULL, '1.8'),
	(83, 'US', 102, '1', '0', NULL, '1.8'),
	(84, 'US', 104, '1', '0', NULL, '1.8'),
	(85, 'US', 31, '1', '0', NULL, '1.8'),
	(86, 'US', 39, '1', '0', NULL, '1.8'),
	(87, 'CA', 8, '1', '0', NULL, '1'),
	(88, 'CA', 101, '1', '0', NULL, '1'),
	(89, 'CA', 102, '1', '0', NULL, '1'),
	(90, 'CA', 103, '1', '0', NULL, '1'),
	(91, 'CA', 104, '1', '0', NULL, '1'),
	(92, 'CA', 31, '1', '0', NULL, '1'),
	(93, 'CA', 39, '1', '0', NULL, '1'),
	(94, 'AU', 8, '1', '0', NULL, '1'),
	(95, 'AU', 103, '1', '0', NULL, '1'),
	(96, 'AU', 102, '1', '0', NULL, '1'),
	(97, 'AU', 104, '1', '0', NULL, '1'),
	(98, 'AU', 31, '1', '0', NULL, '1'),
	(99, 'AU', 39, '1', '0', NULL, '1'),
	(100, 'AU', 101, '1', '0', NULL, '1'),
	(101, 'PL', 526, '1', '0', NULL, '4'),
	(102, 'PL', 526, '1', '0', NULL, '3'),
	(103, 'PL', 526, '1', '0', NULL, '3'),
	(104, 'PL', 544, '1', '0', NULL, '3'),
	(105, 'SE', 564, '1', '0', NULL, '11'),
	(106, 'SE', 564, '1', '0', NULL, '9'),
	(107, 'SE', 564, '1', '0', NULL, '9'),
	(108, 'SE', 584, '1', '0', NULL, '9');

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
