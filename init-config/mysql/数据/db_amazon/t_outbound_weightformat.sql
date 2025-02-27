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

-- 正在导出表  db_amazon.t_outbound_weightformat 的数据：~17 rows (大约)
DELETE FROM `t_outbound_weightformat`;
INSERT INTO `t_outbound_weightformat` (`id`, `producttierId`, `isMedia`, `format`) VALUES
	('06b43d85-7257-11ea-b1ed-00e04c023f0e', 'envelope_mx', NULL, 'dim.weight.value+0.075'),
	('06be22bc-7257-11ea-b1ed-00e04c023f0e', 'std_mx', NULL, 'dim.weight.value > 0.5 ? (dim.weight.value > dim.dimensionalWeight.value ? dim.weight.value+0.125 : dim.dimensionalWeight.value+0.125):dim.weight.value+0.075'),
	('06c3d349-7257-11ea-b1ed-00e04c023f0e', 'oversize_mx', NULL, 'dim.weight.value > dim.dimensionalWeight.value ? dim.weight.value+0.5 : dim.dimensionalWeight.value+0.5'),
	('2381f81f-cbd7-11e6-bab5-00e04c023f0e', 'sml_standard_us', b'0', 'dim.weight.value'),
	('2cee3506-cbd8-11e6-bab5-00e04c023f0e', 'large_standard_us', b'0', 'dim.weight.value > dim.dimensionalWeight.value ? dim.weight.value : dim.dimensionalWeight.value'),
	('39800810-cbde-11e6-bab5-00e04c023f0e', 'sml_oversize_us', NULL, 'dim.weight.value > dim.dimensionalWeight.value ? dim.weight.value : dim.dimensionalWeight.value'),
	('4acce981-cbd7-11e6-bab5-00e04c023f0e', 'large_standard_us', b'1', 'dim.weight.value > dim.dimensionalWeight.value ? dim.weight.value : dim.dimensionalWeight.value'),
	('905a6bf4-cbdb-11e6-bab5-00e04c023f0e', 'special_oversize_us', NULL, 'dim.weight.value > dim.dimensionalWeight.value ? dim.weight.value : dim.dimensionalWeight.value'),
	('a8afe768-cbd6-11e6-bab5-00e04c023f0e', 'sml_standard_us', b'1', 'dim.weight.value'),
	('ab19ff7e-cbde-11e6-bab5-00e04c023f0e', 'medium_oversize_us', NULL, 'dim.weight.value > dim.dimensionalWeight.value ? dim.weight.value : dim.dimensionalWeight.value'),
	('b25eadb6-cbde-11e6-bab5-00e04c023f0e', 'large_oversize_us', NULL, 'dim.weight.value > dim.dimensionalWeight.value ? dim.weight.value : dim.dimensionalWeight.value'),
	('ca-1', 'envelope_ca', NULL, 'dim.weight.value > dim.dimensionalWeight.value ? dim.weight.value : dim.dimensionalWeight.value'),
	('ca-2', 'std_ca', NULL, 'dim.weight.value > dim.dimensionalWeight.value ? dim.weight.value : dim.dimensionalWeight.value'),
	('ca-3', 'oversize_sm_ca', NULL, 'dim.weight.value > dim.dimensionalWeight.value ? dim.weight.value : dim.dimensionalWeight.value'),
	('ca-4', 'oversize_mid_ca', NULL, 'dim.weight.value > dim.dimensionalWeight.value ? dim.weight.value : dim.dimensionalWeight.value'),
	('ca-5', 'oversize_lg_ca', NULL, 'dim.weight.value > dim.dimensionalWeight.value ? dim.weight.value : dim.dimensionalWeight.value'),
	('ca-6', 'oversize_special_ca', NULL, 'dim.weight.value');

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
