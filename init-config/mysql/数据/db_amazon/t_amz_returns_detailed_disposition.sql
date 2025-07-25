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

-- 正在导出表  db_amazon.t_amz_returns_detailed_disposition 的数据：~6 rows (大约)
DELETE FROM `t_amz_returns_detailed_disposition`;
INSERT INTO `t_amz_returns_detailed_disposition` (`code`, `name`, `description`) VALUES
	('CARRIER_DAMAGED', '承运人损坏', '商品以“不可售”状态退回库存。 这并不一定意味着商品自身损坏（例如，商品的外包装有可能被打开），但是'),
	('CUSTOMER_DAMAGED', '客户损坏', '商品以“不可售”状态退回库存。 这并不一定意味着商品自身损坏（例如，商品的外包装有可能被打开），但是'),
	('DAMAGED', '损坏', '亚马逊对残损负责。所有权转移给亚马逊。亚马逊根据亚马逊物流丢失和已残损库存赔偿政策对您进行赔偿。'),
	('DEFECTIVE', '有缺陷的', '商品以“不可售”状态退回库存。 商品存在明显残损，或买家声称存在瑕疵。将退款给买家，但商品在库存中将'),
	('EXPIRED', '过期', '剩余保质期不足 50 天的商品可能会被设为“不可售”状态，并最终由亚马逊移除并弃置。被弃置的商品将不'),
	('SELLABLE', '可用', '商品已退回库存并处于可售状态。');

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
