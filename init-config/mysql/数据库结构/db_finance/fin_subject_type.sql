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

-- 导出  表 db_finance.fin_subject_type 结构
CREATE TABLE IF NOT EXISTS `fin_subject_type` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `code` varchar(32) COLLATE utf8mb4_bin NOT NULL COMMENT '类别编码，唯一',
  `name` varchar(64) COLLATE utf8mb4_bin NOT NULL COMMENT '类别名称',
  `parent_code` varchar(32) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '父级类别编码，顶级类别的父级为NULL',
  `level` tinyint NOT NULL COMMENT '层级：1=一级科目类别，2=二级类别，3=三级类别',
  `category_type` enum('asset','liability','equity','cost','profit_loss') COLLATE utf8mb4_bin NOT NULL COMMENT '科目大类标识，用于业务逻辑判断',
  `sort_order` int DEFAULT '0' COMMENT '排序号，数值越小越靠前',
  `is_fixed` tinyint(1) DEFAULT '1' COMMENT '是否为固定预置项（TRUE=系统固定，不可删除/修改名称）',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `code` (`code`),
  KEY `idx_parent` (`parent_code`),
  KEY `idx_level` (`level`),
  KEY `idx_category` (`category_type`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='科目类别表';

-- 数据导出被取消选择。

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
