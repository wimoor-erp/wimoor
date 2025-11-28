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

-- 导出  表 db_amazon.t_erp_estimated_sales 结构
CREATE TABLE IF NOT EXISTS `t_erp_estimated_sales` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `sku` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `marketplaceid` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `groupid` bigint unsigned DEFAULT NULL,
  `presales` int DEFAULT NULL COMMENT '手动输入日均销量',
  `startTime` date DEFAULT NULL COMMENT '开始生效时间 为null则是不限制',
  `endTime` date DEFAULT NULL COMMENT '结束生效时间 为null则是不限制',
  `conditions` int DEFAULT '0' COMMENT '失效条件：0 = 不限制；1 = 指定>默认 ; 2 = 指定<默认',
  `conditionNum` decimal(10,2) DEFAULT NULL COMMENT '超过失效条件数值（百分比）',
  `isInvalid` bit(1) DEFAULT NULL COMMENT '当前输入销量是否有效 1表示有效，0表示无效',
  `operator` bigint unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `sku_marketplaceid_groupid` (`groupid`,`sku`,`marketplaceid`)
) ENGINE=InnoDB AUTO_INCREMENT=14123 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='用户维护日均销量表';

-- 数据导出被取消选择。

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
