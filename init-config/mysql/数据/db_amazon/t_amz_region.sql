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

-- 正在导出表  db_amazon.t_amz_region 的数据：~3 rows (大约)
DELETE FROM `t_amz_region`;
INSERT INTO `t_amz_region` (`code`, `name`, `advname`, `advpoint`, `client_id`, `client_secret`) VALUES
	('EU', '欧洲', 'Europe (EU). Covers UK, FR, IT, ES, DE, and AE marketplaces', 'advertising-api-eu.amazon.com', 'amzn1.application-oa2-client.d5aff6c66c8848418a4ac4209ba694c7', '88ef87ca4896e311a7008ec42e75d4ce246bfe157c621810ef790da36bbc929a'),
	('FE', '远东', 'Far East (FE). Covers JP and AU marketplaces.', 'advertising-api-fe.amazon.com', 'amzn1.application-oa2-client.d5aff6c66c8848418a4ac4209ba694c7', '88ef87ca4896e311a7008ec42e75d4ce246bfe157c621810ef790da36bbc929a'),
	('NA', '北美', 'North America (NA). Covers US and CA marketplaces', 'advertising-api.amazon.com', 'amzn1.application-oa2-client.d5aff6c66c8848418a4ac4209ba694c7', '88ef87ca4896e311a7008ec42e75d4ce246bfe157c621810ef790da36bbc929a');

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
