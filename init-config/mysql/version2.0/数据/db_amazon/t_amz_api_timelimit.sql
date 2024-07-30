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

-- 正在导出表  db_amazon.t_amz_api_timelimit 的数据：~7 rows (大约)
DELETE FROM `t_amz_api_timelimit`;
INSERT INTO `t_amz_api_timelimit` (`id`, `apiname`, `maximum`, `quarter`, `half`, `hour`, `restore_rate`, `restore_unit`) VALUES
	(0, 'GetMatchingProductForId', 20, NULL, NULL, 720, 5, 1),
	(1, 'SubmitFeed', 15, NULL, NULL, 30, 120, 1),
	(2, 'ListOrders', 6, NULL, NULL, 60, 60, 1),
	(3, 'GetProductCategoriesForSKU', 20, NULL, NULL, 720, 5, 1),
	(4, 'GetMyPriceForSKU', 20, NULL, NULL, 36000, 1, 10),
	(5, 'GetLowestPricedOffersForSKU', 10, NULL, NULL, 200, 1, 5),
	(6, 'GetFeedSubmissionList', 10, NULL, NULL, 80, 45, 1);

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
