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

-- 导出  表 db_quote.t_order 结构
CREATE TABLE IF NOT EXISTS `t_order` (
  `id` bigint unsigned NOT NULL,
  `buyerid` bigint unsigned DEFAULT NULL,
  `ftype` tinyint DEFAULT NULL COMMENT '0：单个报价，2 批量报价 ，3 地址报价',
  `isgroupbuy` bit(1) DEFAULT NULL COMMENT '是否拼团',
  `isbidding` bit(1) DEFAULT NULL,
  `weight` decimal(20,6) DEFAULT NULL,
  `volume` decimal(20,6) DEFAULT NULL,
  `days` int DEFAULT NULL,
  `transchannel` bigint unsigned DEFAULT NULL,
  `number` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `status` int unsigned DEFAULT NULL COMMENT '1,等待询价，2等待拼团 3等待报价，4已产生报价， 5结束',
  `remark` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `closetime` datetime DEFAULT NULL COMMENT '结束时间',
  `createtime` datetime DEFAULT NULL COMMENT '创建时间',
  `pricetime` datetime DEFAULT NULL COMMENT '报价结束时间',
  PRIMARY KEY (`id`),
  KEY `createtime` (`createtime`),
  KEY `buyerid` (`buyerid`,`createtime`) USING BTREE,
  KEY `buyerid_number` (`buyerid`,`number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
