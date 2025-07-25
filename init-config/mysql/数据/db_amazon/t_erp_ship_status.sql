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

-- 正在导出表  db_amazon.t_erp_ship_status 的数据：~11 rows (大约)
DELETE FROM `t_erp_ship_status`;
INSERT INTO `t_erp_ship_status` (`status`, `content`, `name`) VALUES
	('CANCELLED', '卖家在货件发送至亚马逊配送中心后取消了货件', '已取消'),
	('CHECKED_IN', '货件已在亚马逊配送中心的收货装卸地点登记', '已登记'),
	('CLOSED', '货件已到达亚马逊配送中心，但有部分商品尚未标记为已收到', '已完成'),
	('DELETED', '卖家在将货件发送到亚马逊配送中心之前取消了货件', '已删除'),
	('DELIVERED', '承运人已将货件配送至亚马逊配送中心', '已接收'),
	('ERROR', '货件出错，但不是由亚马逊处理的', '货件出错'),
	('IN_TRANSIT', '承运人已通知亚马逊配送中心其已知晓货件的存在', '在途'),
	('READY_TO_SHIP', '货件准备发货', '准备发货'),
	('RECEIVING', '货件已到达亚马逊配送中心，但有部分商品尚未标记为已收到', '正在接收'),
	('SHIPPED', '承运人已取件', '已发货'),
	('WORKING', '卖家已创建货件，但未发货', '正在配货装箱');

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
