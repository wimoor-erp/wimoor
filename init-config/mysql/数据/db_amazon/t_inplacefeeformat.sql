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

-- 正在导出表  db_amazon.t_inplacefeeformat 的数据：~61 rows (大约)
DELETE FROM `t_inplacefeeformat`;
INSERT INTO `t_inplacefeeformat` (`id`, `inplacefeeid`, `producttierId`, `standard`, `format`, `description`, `country`) VALUES
	(4, 'uncalculate', NULL, NULL, '0', '不参与计算', ''),
	(8, 'calculate', 'small', b'0', '5', NULL, 'JP'),
	(9, 'calculate', 'standard', b'0', '8', NULL, 'JP'),
	(10, 'calculate', 'oversize1', b'0', '9', NULL, 'JP'),
	(11, 'calculate', 'oversize2', b'0', '9', NULL, 'JP'),
	(12, 'calculate', 'oversize3', b'0', '9', NULL, 'JP'),
	(13, 'calculate', 'special_large', b'0', '9', NULL, 'JP'),
	(14, 'one-mid', 'large_standard_us', b'1', 'weight<=0.75? "0.29" :weight<=1.5?"0.34":weight<=3?"0.41":weight<=20?"0.55":"0"', NULL, 'US'),
	(15, 'more-mid', 'sml_standard_us', b'1', '0', NULL, 'US'),
	(16, 'two-mid', 'sml_standard_us', b'1', '0.17', NULL, 'US'),
	(17, 'one-mid', 'sml_standard_us', b'1', '0.26', NULL, 'US'),
	(18, 'two-mid', 'large_standard_us', b'1', 'weight<=0.75?"0.19" :weight<=1.5?"0.22":weight<=3?"0.26":weight<=20?"0.36":"0"', NULL, 'US'),
	(19, 'more-mid', 'large_standard_us', b'1', '0', NULL, 'US'),
	(21, 'one-east', 'large_standard_us', b'1', 'weight<=0.75? "0.23" :weight<=1.5?"0.27":weight<=3?"0.32":weight<=20?"0.42":"0"', NULL, 'US'),
	(22, 'more-east', 'large_standard_us', b'1', '0', NULL, 'US'),
	(23, 'two-east', 'sml_standard_us', b'1', '0.12', NULL, 'US'),
	(24, 'one-east', 'sml_standard_us', b'1', '0.21', NULL, 'US'),
	(25, 'two-east', 'large_standard_us', b'1', 'weight<=0.75?"0.13" :weight<=1.5?"0.15":weight<=3?"0.17":weight<=20?"0.23":"0"', NULL, 'US'),
	(26, 'more-east', 'sml_oversize_us', b'0', '0', NULL, 'US'),
	(31, 'one-west', 'sml_standard_us', b'1', '0.30', NULL, 'US'),
	(32, 'more-west', 'sml_standard_us', b'1', '0', NULL, 'US'),
	(33, 'two-west', 'sml_standard_us', b'1', '0.21', NULL, 'US'),
	(34, 'one-west', 'large_standard_us', b'1', 'weight<=0.75?"0.34" :weight<=1.5?"0.41":weight<=3?"0.49":weight<=20?"0.68":"0"', NULL, 'US'),
	(35, 'two-west', 'large_standard_us', b'1', 'weight<=0.75?"0.24" :weight<=1.5?"0.28":weight<=3?"0.34":weight<=20?"0.48":"0"', NULL, 'US'),
	(36, 'more-west', 'large_standard_us', b'1', '0', NULL, 'US'),
	(37, 'one-west', 'sml_oversize_us', b'0', 'weight<=5?"2.67" :weight<=12?"3.15":weight<=28?"3.95":weight<=42?"5.11":"6.00"', NULL, 'US'),
	(38, 'one-mid', 'sml_oversize_us', b'0', 'weight<=5?"2.42" :weight<=12?"2.85":weight<=28?"3.57":weight<=42?"4.62":"5.430"', NULL, 'US'),
	(39, 'more-east', 'sml_standard_us', b'1', '0', NULL, 'US'),
	(40, 'more-west', 'sml_oversize_us', b'0', '0', NULL, 'US'),
	(41, 'more-mid', 'sml_oversize_us', b'0', '0', NULL, 'US'),
	(42, 'one-east', 'sml_oversize_us', b'0', 'weight<=5?"2.16" :weight<=12?"2.55":weight<=28?"3.19":weight<=42?"4.13":"4.85"', NULL, 'US'),
	(43, 'two-east', 'sml_oversize_us', b'0', 'weight<=5?"0.55" :weight<=12?"0.65":weight<=28?"0.84":weight<=42?"1.05":"1.23"', NULL, 'US'),
	(44, 'two-mid', 'sml_oversize_us', b'0', 'weight<=5?"1.02" :weight<=12?"1.20":weight<=28?"1.50":weight<=42?"1.94":"2.28"', NULL, 'US'),
	(45, 'two-west', 'sml_oversize_us', b'0', 'weight<=5?"1.48" :weight<=12?"1.75":weight<=28?"2.19":weight<=42?"2.83":"3.32"', NULL, 'US'),
	(46, 'one-mid', 'light_large_standard_us', b'1', 'weight<=0.75? "0.29" :weight<=1.5?"0.34":weight<=3?"0.41":weight<=20?"0.55":"0"', NULL, 'US'),
	(47, 'more-mid', 'light_sml_standard_us', b'1', '0', NULL, 'US'),
	(48, 'two-mid', 'light_sml_standard_us', b'1', '0.17', NULL, 'US'),
	(49, 'one-mid', 'light_sml_standard_us', b'1', '0.26', NULL, 'US'),
	(50, 'two-mid', 'light_large_standard_us', b'1', 'weight<=0.75?"0.19" :weight<=1.5?"0.22":weight<=3?"0.26":weight<=20?"0.36":"0"', NULL, 'US'),
	(51, 'more-mid', 'light_large_standard_us', b'1', '0', NULL, 'US'),
	(52, 'one-east', 'light_large_standard_us', b'1', 'weight<=0.75? "0.23" :weight<=1.5?"0.27":weight<=3?"0.32":weight<=20?"0.42":"0"', NULL, 'US'),
	(53, 'more-east', 'light_large_standard_us', b'1', '0', NULL, 'US'),
	(54, 'two-east', 'light_sml_standard_us', b'1', '0.12', NULL, 'US'),
	(55, 'one-east', 'light_sml_standard_us', b'1', '0.21', NULL, 'US'),
	(56, 'two-east', 'light_large_standard_us', b'1', 'weight<=0.75?"0.13" :weight<=1.5?"0.15":weight<=3?"0.17":weight<=20?"0.23":"0"', NULL, 'US'),
	(57, 'more-east', 'light_sml_oversize_us', b'0', '0', NULL, 'US'),
	(58, 'one-west', 'light_sml_standard_us', b'1', '0.30', NULL, 'US'),
	(59, 'more-west', 'light_sml_standard_us', b'1', '0', NULL, 'US'),
	(60, 'two-west', 'light_sml_standard_us', b'1', '0.21', NULL, 'US'),
	(61, 'one-west', 'light_large_standard_us', b'1', 'weight<=0.75?"0.34" :weight<=1.5?"0.41":weight<=3?"0.49":weight<=20?"0.68":"0"', NULL, 'US'),
	(62, 'two-west', 'light_large_standard_us', b'1', 'weight<=0.75?"0.24" :weight<=1.5?"0.28":weight<=3?"0.34":weight<=20?"0.48":"0"', NULL, 'US'),
	(63, 'more-west', 'light_large_standard_us', b'1', '0', NULL, 'US'),
	(64, 'one-west', 'light_sml_oversize_us', b'0', 'weight<=5?"2.67" :weight<=12?"3.15":weight<=28?"3.95":weight<=42?"5.11":"6.00"', NULL, 'US'),
	(65, 'one-mid', 'light_sml_oversize_us', b'0', 'weight<=5?"2.42" :weight<=12?"2.85":weight<=28?"3.57":weight<=42?"4.62":"5.430"', NULL, 'US'),
	(66, 'more-east', 'light_sml_standard_us', b'1', '0', NULL, 'US'),
	(67, 'more-west', 'light_sml_oversize_us', b'0', '0', NULL, 'US'),
	(68, 'more-mid', 'light_sml_oversize_us', b'0', '0', NULL, 'US'),
	(69, 'one-east', 'light_sml_oversize_us', b'0', 'weight<=5?"2.16" :weight<=12?"2.55":weight<=28?"3.19":weight<=42?"4.13":"4.85"', NULL, 'US'),
	(70, 'two-east', 'light_sml_oversize_us', b'0', 'weight<=5?"0.55" :weight<=12?"0.65":weight<=28?"0.84":weight<=42?"1.05":"1.23"', NULL, 'US'),
	(71, 'two-mid', 'light_sml_oversize_us', b'0', 'weight<=5?"1.02" :weight<=12?"1.20":weight<=28?"1.50":weight<=42?"1.94":"2.28"', NULL, 'US'),
	(72, 'two-west', 'light_sml_oversize_us', b'0', 'weight<=5?"1.48" :weight<=12?"1.75":weight<=28?"2.19":weight<=42?"2.83":"3.32"', NULL, 'US');

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
