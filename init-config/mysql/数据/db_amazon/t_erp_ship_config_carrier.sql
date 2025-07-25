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

-- 正在导出表  db_amazon.t_erp_ship_config_carrier 的数据：~65 rows (大约)
DELETE FROM `t_erp_ship_config_carrier`;
INSERT INTO `t_erp_ship_config_carrier` (`country`, `name`, `transtyle`) VALUES
	('CA', 'OTHER', 'LTL'),
	('CA', 'OTHER', 'SP'),
	('DE', 'BUSINESS_POST', 'SP'),
	('DE', 'DHL_AIRWAYS_INC', 'SP'),
	('DE', 'DPD', 'SP'),
	('DE', 'OTHER', 'SP'),
	('DE', 'PARCELFORCE', 'SP'),
	('DE', 'TNT', 'SP'),
	('DE', 'TNT_LOGISTICS_CORPORATION', 'SP'),
	('DE', 'UNITED_PARCEL_SERVICE_INC', 'SP'),
	('ES', 'BUSINESS_POST', 'SP'),
	('ES', 'DHL_AIRWAYS_INC', 'SP'),
	('ES', 'DPD', 'SP'),
	('ES', 'OTHER', 'SP'),
	('ES', 'PARCELFORCE', 'SP'),
	('ES', 'TNT', 'SP'),
	('ES', 'TNT_LOGISTICS_CORPORATION', 'SP'),
	('ES', 'UNITED_PARCEL_SERVICE_INC', 'SP'),
	('FR', 'BUSINESS_POST', 'SP'),
	('FR', 'DHL_AIRWAYS_INC', 'SP'),
	('FR', 'DPD', 'SP'),
	('FR', 'OTHER', 'SP'),
	('FR', 'PARCELFORCE', 'SP'),
	('FR', 'TNT', 'SP'),
	('FR', 'TNT_LOGISTICS_CORPORATION', 'SP'),
	('FR', 'UNITED_PARCEL_SERVICE_INC', 'SP'),
	('IT', 'BUSINESS_POST', 'SP'),
	('IT', 'DHL_AIRWAYS_INC', 'SP'),
	('IT', 'DPD', 'SP'),
	('IT', 'OTHER', 'SP'),
	('IT', 'PARCELFORCE', 'SP'),
	('IT', 'TNT', 'SP'),
	('IT', 'TNT_LOGISTICS_CORPORATION', 'SP'),
	('IT', 'UNITED_PARCEL_SERVICE_INC', 'SP'),
	('JP', 'OTHER', 'SP'),
	('UK', 'BUSINESS_POST', 'LTL'),
	('UK', 'BUSINESS_POST', 'SP'),
	('UK', 'DHL_AIRWAYS_INC', 'LTL'),
	('UK', 'DHL_AIRWAYS_INC', 'SP'),
	('UK', 'DHL_UK', 'LTL'),
	('UK', 'DHL_UK', 'SP'),
	('UK', 'DPD', 'LTL'),
	('UK', 'DPD', 'SP'),
	('UK', 'OTHER', 'LTL'),
	('UK', 'OTHER', 'SP'),
	('UK', 'PARCELFORCE', 'LTL'),
	('UK', 'PARCELFORCE', 'SP'),
	('UK', 'TNT', 'LTL'),
	('UK', 'TNT', 'SP'),
	('UK', 'TNT_LOGISTICS_CORPORATION', 'LTL'),
	('UK', 'TNT_LOGISTICS_CORPORATION', 'SP'),
	('UK', 'UNITED_PARCEL_SERVICE_INC', 'LTL'),
	('UK', 'UNITED_PARCEL_SERVICE_INC', 'SP'),
	('UK', 'YODEL', 'LTL'),
	('UK', 'YODEL', 'SP'),
	('US', 'DHL_EXPRESS_USA_INC', 'LTL'),
	('US', 'DHL_EXPRESS_USA_INC', 'SP'),
	('US', 'FEDERAL_EXPRESS_CORP', 'LTL'),
	('US', 'FEDERAL_EXPRESS_CORP', 'SP'),
	('US', 'OTHER', 'LTL'),
	('US', 'OTHER', 'SP'),
	('US', 'UNITED_PARCEL_SERVICE_INC', 'LTL'),
	('US', 'UNITED_PARCEL_SERVICE_INC', 'SP'),
	('US', 'UNITED_STATES_POSTAL_SERVICE', 'LTL'),
	('US', 'UNITED_STATES_POSTAL_SERVICE', 'SP');

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
