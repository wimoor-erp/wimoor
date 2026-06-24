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

-- 导出  表 db_finance.fin_report_mapping_rules 结构
CREATE TABLE IF NOT EXISTS `fin_report_mapping_rules` (
  `rule_id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '规则ID',
  `groupid` bigint unsigned NOT NULL COMMENT '租户ID',
  `template_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '模板类型：BALANCE_SHEET-资产负债表, INCOME_STATEMENT-利润表, CASH_FLOW-现金流量表',
  `rule_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT 'SUBJECT' COMMENT '规则类型：SUBJECT-科目映射, ITEM_SUM-项目汇总, ITEM_CONDITIONAL-条件汇总',
  `item_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '报表项目编码',
  `rule_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '规则名称',
  `match_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT 'PREFIX' COMMENT '匹配类型：PREFIX-前缀匹配, EXACT-精确匹配, RANGE-范围匹配, PARENT-父级科目匹配, ITEM-报表项目',
  `match_value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '匹配值（前缀/科目编码/范围起始值/报表项目编码）',
  `match_value_end` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '匹配结束值（用于范围匹配）',
  `operator` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT 'ADD' COMMENT '操作符：ADD-加, SUBTRACT-减, EXCLUDE-排除',
  `subject_type` tinyint DEFAULT NULL COMMENT '科目类型：1-资产, 2-负债, 3-所有者权益, 4-成本, 5-损益',
  `direction` tinyint DEFAULT NULL COMMENT '余额方向：1-借方, 2-贷方',
  `is_leaf_only` tinyint DEFAULT '1' COMMENT '是否仅匹配末级科目：0-否, 1-是',
  `priority` int DEFAULT '100' COMMENT '优先级（数值越小优先级越高）',
  `status` tinyint DEFAULT '1' COMMENT '状态：0-停用, 1-启用',
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '规则描述',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `created_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`rule_id`),
  KEY `idx_groupid` (`groupid`),
  KEY `idx_template_type` (`template_type`),
  KEY `idx_item_code` (`item_code`),
  KEY `idx_match_type` (`match_type`),
  KEY `idx_status` (`status`),
  KEY `idx_priority` (`priority`),
  KEY `idx_tenant_template` (`groupid`,`template_type`),
  KEY `idx_tenant_item` (`groupid`,`item_code`)
) ENGINE=InnoDB AUTO_INCREMENT=2315 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='报表映射规则配置表，用于自动维护报表模板的计算公式';

-- 数据导出被取消选择。

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
