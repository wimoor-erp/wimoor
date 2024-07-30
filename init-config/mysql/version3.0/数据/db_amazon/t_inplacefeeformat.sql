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

-- 正在导出表  db_amazon.t_inplacefeeformat 的数据：~13 rows (大约)
DELETE FROM `t_inplacefeeformat`;
INSERT INTO `t_inplacefeeformat` (`id`, `inplacefeeid`, `producttierId`, `standard`, `format`, `description`, `country`) VALUES
	(1, 'one', NULL, b'1', 'weight>2? "0.30+0.10*({0}-2)" :"0.30"', '是否是标准尺寸，标准尺寸：0.30 美元 + 0.10 美元/磅（超出首重 2 磅的部分）;超大尺寸：1.65 美元 + 0.20 美元/磅（超出首重 5 磅的部分）', 'US'),
	(2, 'three', NULL, b'1', '0', '免费', 'US'),
	(3, 'two', NULL, b'1', 'weight>2? "0.10+0.10*({0}-2)" :"0.10"', '是否是标准尺寸，标准尺寸：0.10 美元 + 0.10 美元/磅（超出首重 2 磅的部分）；超大尺寸：0.50 美元 + 0.20 美元/磅（超出首重 5 磅的部分）', 'US'),
	(4, 'uncalculate', NULL, NULL, '0', '不参与计算', ''),
	(5, 'one', NULL, b'0', 'weight>2? "1.65+0.20*({0}-5)" :"1.65"', NULL, 'US'),
	(6, 'two', NULL, b'0', 'weight>2? "0.50+0.20*({0}-5)" :"0.50"', NULL, 'US'),
	(7, 'three', NULL, b'0', '0', NULL, 'US'),
	(8, 'calculate', 'small', b'0', '5', NULL, 'JP'),
	(9, 'calculate', 'standard', b'0', '8', NULL, 'JP'),
	(10, 'calculate', 'oversize1', b'0', '9', NULL, 'JP'),
	(11, 'calculate', 'oversize2', b'0', '9', NULL, 'JP'),
	(12, 'calculate', 'oversize3', b'0', '9', NULL, 'JP'),
	(13, 'calculate', 'special_large', b'0', '9', NULL, 'JP');

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
