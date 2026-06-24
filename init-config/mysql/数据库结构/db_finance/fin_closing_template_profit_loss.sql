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

-- 导出  表 db_finance.fin_closing_template_profit_loss 结构
CREATE TABLE IF NOT EXISTS `fin_closing_template_profit_loss` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `template_id` bigint unsigned NOT NULL COMMENT '结账模板ID，关联fin_closing_template.id',
  `groupid` bigint unsigned DEFAULT NULL COMMENT '账套ID',
  `transfer_cycle` tinyint unsigned DEFAULT '0' COMMENT '结转周期：0-按月结转损益，1-按年结转损益',
  `transfer_method` tinyint unsigned DEFAULT '0' COMMENT '结转方式：0-追加结转，1-重新结转',
  `voucher_date` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '凭证日期，格式YYYY-MM-DD',
  `summary` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '结转本期损益' COMMENT '凭证摘要',
  `direction_handling` bit(1) DEFAULT b'0' COMMENT '余额与方向不一致时的处理方式：0-按科目方向反向结转，1-按金额正数结转',
  `profit_loss_subject_code` char(10) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '损益类科目的结转科目ID，关联fin_accounting_subjects.subject_code',
  `current_year_profit_subject_code` char(10) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '本年利润的结转科目ID，关联fin_accounting_subjects.subject_code',
  `prior_year_adjustment_subject_code` char(10) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '以前年度损益调整科目ID，关联fin_accounting_subjects.subject_code',
  `prior_year_adjust_transfer_subject_code` char(10) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '以前年度损益调整科目的结转科目ID，关联fin_accounting_subjects.subject_code',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_time` datetime DEFAULT NULL COMMENT '修改时间',
  `create_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '创建人',
  `modify_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_template_id` (`template_id`) COMMENT '模板ID唯一索引',
  KEY `idx_groupid` (`groupid`) COMMENT '账套ID索引',
  KEY `idx_profit_loss_subject` (`profit_loss_subject_code`) USING BTREE COMMENT '损益科目索引',
  KEY `idx_current_year_profit_subject` (`current_year_profit_subject_code`) USING BTREE COMMENT '本年利润科目索引',
  KEY `idx_prior_year_adjustment_subject` (`prior_year_adjustment_subject_code`) USING BTREE COMMENT '以前年度损益调整科目索引',
  KEY `idx_prior_year_adjust_transfer_subject` (`prior_year_adjust_transfer_subject_code`) USING BTREE COMMENT '以前年度损益调整结转科目索引'
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='结转损益凭证模板配置表';

-- 数据导出被取消选择。

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
