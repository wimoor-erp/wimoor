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

-- 导出  表 db_amazon.t_referralfee 结构
CREATE TABLE IF NOT EXISTS `t_referralfee` (
  `id` int NOT NULL AUTO_INCREMENT,
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '类型',
  `isMedia` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '0' COMMENT '是否媒介',
  `name` varchar(100) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL COMMENT '名称',
  `loweast` decimal(18,6) DEFAULT '0.000000' COMMENT '最低值',
  `top1` decimal(18,6) DEFAULT NULL COMMENT '第一最高值',
  `top2` decimal(18,6) DEFAULT NULL COMMENT '第二最高值',
  `top3` decimal(18,6) DEFAULT NULL COMMENT '第三最高值',
  `percent1` decimal(18,6) DEFAULT NULL COMMENT '第一百分比',
  `percent2` decimal(18,6) DEFAULT NULL COMMENT '第二百分比',
  `percent3` decimal(18,6) DEFAULT NULL COMMENT '第三百分比',
  `country` varchar(20) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL COMMENT '国家',
  `parent_id` int DEFAULT NULL COMMENT '父分类',
  PRIMARY KEY (`id`),
  UNIQUE KEY `parent_id` (`country`,`parent_id`)
) ENGINE=InnoDB AUTO_INCREMENT=699 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='成本计算用，的亚马逊成本。采用父分类的方式，将所有国家的分类都放入此表。\r\n以美国的分类作为主要分类。如果美国没有此分类，则该分类依旧为美国添加，使用其他分类 类型的策略。\r\n当用户选择1 号分类，则其他子分类即改分类对应的其他国家的分类。\r\n ';

-- 数据导出被取消选择。

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
