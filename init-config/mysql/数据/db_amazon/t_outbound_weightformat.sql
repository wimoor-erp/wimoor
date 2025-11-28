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

-- 正在导出表  db_amazon.t_outbound_weightformat 的数据：~15 rows (大约)
DELETE FROM `t_outbound_weightformat`;
INSERT INTO `t_outbound_weightformat` (`id`, `producttierId`, `isMedia`, `format`) VALUES
	(1, 'envelope_mx', NULL, 'dim.weight.value+0.075'),
	(2, 'std_mx', NULL, 'dim.weight.value > 0.5 ? (dim.weight.value > dim.dimensionalWeight.value ? dim.weight.value+0.125 : dim.dimensionalWeight.value+0.125):dim.weight.value+0.075'),
	(3, 'oversize_mx', NULL, 'dim.weight.value > dim.dimensionalWeight.value ? dim.weight.value+0.5 : dim.dimensionalWeight.value+0.5'),
	(4, 'sml_standard_us', NULL, 'dim.weight.value'),
	(5, 'large_standard_us', NULL, 'dim.weight.value > dim.dimensionalWeight.value ? dim.weight.value : dim.dimensionalWeight.value'),
	(6, 'sml_oversize_us', NULL, 'dim.weight.value > dim.dimensionalWeight.value ? dim.weight.value : dim.dimensionalWeight.value'),
	(7, 'special_oversize_us', NULL, 'dim.weight.value > dim.dimensionalWeight.value ? dim.weight.value : dim.dimensionalWeight.value'),
	(8, 'medium_oversize_us', NULL, 'dim.weight.value > dim.dimensionalWeight.value ? dim.weight.value : dim.dimensionalWeight.value'),
	(9, 'large_oversize_us', NULL, 'dim.weight.value > dim.dimensionalWeight.value ? dim.weight.value : dim.dimensionalWeight.value'),
	(10, 'envelope_ca', NULL, 'dim.weight.value > dim.dimensionalWeight.value ? dim.weight.value : dim.dimensionalWeight.value'),
	(11, 'std_ca', NULL, 'dim.weight.value > dim.dimensionalWeight.value ? dim.weight.value : dim.dimensionalWeight.value'),
	(12, 'oversize_sm_ca', NULL, 'dim.weight.value > dim.dimensionalWeight.value ? dim.weight.value : dim.dimensionalWeight.value'),
	(13, 'oversize_mid_ca', NULL, 'dim.weight.value > dim.dimensionalWeight.value ? dim.weight.value : dim.dimensionalWeight.value'),
	(14, 'oversize_lg_ca', NULL, 'dim.weight.value > dim.dimensionalWeight.value ? dim.weight.value : dim.dimensionalWeight.value'),
	(15, 'oversize_special_ca', NULL, 'dim.weight.value');

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
