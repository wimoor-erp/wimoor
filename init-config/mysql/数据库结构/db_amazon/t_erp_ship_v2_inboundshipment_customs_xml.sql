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

-- 导出  表 db_amazon.t_erp_ship_v2_inboundshipment_customs_xml 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_v2_inboundshipment_customs_xml` (
  `guid` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `number` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '',
  `order_number` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `sequence` varchar(4) COLLATE utf8mb4_bin NOT NULL DEFAULT '0',
  `xml_type` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '电子订单 CEB303,物流运单 CEB505,物流运抵单 CEB507,收款单 CEB403,出口清单 CEB603 \r\n',
  `groupid` bigint unsigned DEFAULT NULL,
  `app_time` datetime DEFAULT NULL,
  `total_price` decimal(20,6) DEFAULT NULL,
  `total_quantity` int DEFAULT NULL,
  `net_weight` decimal(20,6) DEFAULT NULL,
  `gross_weight` decimal(20,6) unsigned DEFAULT NULL,
  `app_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `app_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `content` text COLLATE utf8mb4_bin,
  `logistics_company_code` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '物流企业代码',
  `logistics_company_name` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '物流企业名称',
  `logistics_no` varchar(30) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '物流运单号',
  `port_code` varchar(30) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '出口口岸海关代码',
  `pod` varchar(30) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '指运港代码',
  `file_path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `file_name` varchar(200) COLLATE utf8mb4_bin DEFAULT NULL,
  `return_status` char(10) COLLATE utf8mb4_bin DEFAULT NULL,
  `return_time` datetime DEFAULT NULL,
  `return_info` varchar(500) COLLATE utf8mb4_bin DEFAULT NULL,
  `disabled` bit(1) DEFAULT b'0',
  `opttime` datetime DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  PRIMARY KEY (`guid`) USING BTREE,
  UNIQUE KEY `number` (`groupid`,`xml_type`,`number`) USING BTREE,
  KEY `order_number` (`order_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- 数据导出被取消选择。

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
