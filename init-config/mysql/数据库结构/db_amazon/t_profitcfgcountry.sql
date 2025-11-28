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

-- 导出  表 db_amazon.t_profitcfgcountry 结构
CREATE TABLE IF NOT EXISTS `t_profitcfgcountry` (
  `id` bigint unsigned NOT NULL COMMENT 'ID 用于区分每一个方案对应不同国家的方案配置项',
  `profitid` bigint unsigned NOT NULL COMMENT '总方案',
  `country` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '国家',
  `constantw` decimal(10,2) DEFAULT '0.00' COMMENT '重量基数',
  `constantd` decimal(10,2) DEFAULT '0.00' COMMENT '材积比',
  `constantm` decimal(10,2) DEFAULT '0.00' COMMENT '材积基数',
  `fba_month` int DEFAULT NULL COMMENT 'FBA配送月份和亚马逊仓储费计算月份',
  `storagefee` decimal(10,3) DEFAULT '0.000' COMMENT '仓储费',
  `taxRate` decimal(10,2) DEFAULT '0.00' COMMENT '进口关税费率(原名：税率)',
  `lostRate` decimal(10,2) DEFAULT '0.00' COMMENT '汇率损耗比率',
  `sellerRate` decimal(10,2) DEFAULT '0.00' COMMENT '市场营销占比',
  `otherfee` decimal(10,2) DEFAULT '0.00' COMMENT '其他每单销售固定费用',
  `costRate` decimal(10,2) DEFAULT '0.00' COMMENT '其他每单销售成本比率',
  `logistics` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '物流方式',
  `subscriptionfee` decimal(10,2) DEFAULT NULL COMMENT '订阅费',
  `prepservice` bit(1) DEFAULT b'0' COMMENT '计划内的服务费',
  `labelService` bit(1) DEFAULT b'0' COMMENT '亚马逊标签服务费',
  `manualProcessing` decimal(10,2) DEFAULT '0.00' COMMENT '亚马逊手动处理费',
  `unprepservice` decimal(10,2) DEFAULT '0.00' COMMENT '计划外的服务费',
  `invplacefee` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '库存配置费',
  `promotion` decimal(10,2) DEFAULT '0.00' COMMENT 'all store promtion',
  `amonth` int DEFAULT '1' COMMENT '亚马逊仓储费，库存周期',
  `hasAddedSite` bit(1) DEFAULT b'0' COMMENT '当配送方案为Pan EU时，Germany是否添加Poland, Czech Republic仓库站点',
  `warehouse_site` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '当配送方案为EFN时，亚马逊仓库站点',
  `dispatch_type` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '亚马逊配送方案:pan_EU,EFN',
  `vat_rate` decimal(10,2) DEFAULT '0.00' COMMENT 'VAT增值税费率',
  `fba_taxes` decimal(10,2) DEFAULT '0.00' COMMENT 'FBA GST/HST taxes',
  `hasDeclaredValue` bit(1) DEFAULT b'0' COMMENT '是否单独输入申报价值',
  `sipp` bit(1) DEFAULT b'0',
  `declared_value` decimal(10,4) DEFAULT '0.0000' COMMENT '申报价值',
  `gst_rate` decimal(10,2) DEFAULT '0.00' COMMENT '进口GST税率',
  `selling_GSTRate` decimal(10,2) DEFAULT '0.00' COMMENT '销售GST税率',
  `corporate_InRate` decimal(10,2) DEFAULT '0.00' COMMENT '企业所得税率',
  PRIMARY KEY (`id`),
  KEY `country` (`country`),
  KEY `FK_t_profitcfgcountry_t_profitcfg` (`profitid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='利润各国计算方案';

-- 数据导出被取消选择。

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
