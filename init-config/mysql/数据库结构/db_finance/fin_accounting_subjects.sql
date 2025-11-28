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

-- 导出  表 db_finance.fin_accounting_subjects 结构
CREATE TABLE IF NOT EXISTS `fin_accounting_subjects` (
  `subject_id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `subject_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '科目编码，如1001、1002等',
  `groupid` bigint unsigned NOT NULL COMMENT '租户ID',
  `subject_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '科目名称',
  `subject_level` tinyint NOT NULL COMMENT '科目级别：1-一级科目，2-二级科目，3-三级科目',
  `parent_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '父级科目编码，用于构建科目树形结构',
  `subject_type` tinyint NOT NULL COMMENT '科目类型：1-资产，2-负债，3-所有者权益，4-成本，5-损益',
  `direction` tinyint NOT NULL COMMENT '余额方向：1-借方，2-贷方',
  `is_leaf` tinyint DEFAULT '1' COMMENT '是否末级科目：0-否，1-是',
  `status` tinyint DEFAULT '1' COMMENT '状态：0-停用，1-启用',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`subject_id`) USING BTREE,
  UNIQUE KEY `tenant_id` (`groupid`,`subject_code`) USING BTREE,
  KEY `parent_id` (`parent_code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=13133 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='会计科目表，存储企业所有会计科目信息';

-- 数据导出被取消选择。

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
