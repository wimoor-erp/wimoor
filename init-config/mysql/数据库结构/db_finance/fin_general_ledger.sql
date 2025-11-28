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

-- 导出  表 db_finance.fin_general_ledger 结构
CREATE TABLE IF NOT EXISTS `fin_general_ledger` (
  `ledger_id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '总账记录ID',
  `groupid` bigint unsigned NOT NULL COMMENT '租户ID',
  `subject_id` bigint unsigned DEFAULT NULL COMMENT '会计科目ID',
  `period` varchar(6) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '会计期间，格式：YYYYMM',
  `begin_balance` decimal(15,2) DEFAULT '0.00' COMMENT '期初余额',
  `begin_direction` tinyint DEFAULT NULL COMMENT '期初余额方向：1-借，2-贷',
  `debit_total` decimal(15,2) DEFAULT '0.00' COMMENT '本期借方合计',
  `credit_total` decimal(15,2) DEFAULT '0.00' COMMENT '本期贷方合计',
  `end_balance` decimal(15,2) DEFAULT '0.00' COMMENT '期末余额',
  `end_direction` tinyint DEFAULT NULL COMMENT '期末余额方向：1-借，2-贷',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`ledger_id`),
  UNIQUE KEY `uk_tenant_subject_period` (`groupid`,`subject_id`,`period`) USING BTREE,
  KEY `idx_general_ledger_tenant_period` (`groupid`,`period`) USING BTREE COMMENT '租户总账期间索引'
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='总账表，按科目和期间汇总的账务信息';

-- 数据导出被取消选择。

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
