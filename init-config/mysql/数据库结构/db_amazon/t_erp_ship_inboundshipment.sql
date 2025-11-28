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

-- 导出  表 db_amazon.t_erp_ship_inboundshipment 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_inboundshipment` (
  `ShipmentId` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '货件编号',
  `DestinationFulfillmentCenterId` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '	亚马逊创建的亚马逊配送中心标识。',
  `ShipToAddressID` bigint unsigned DEFAULT NULL COMMENT '目的地',
  `LabelPrepType` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `ShipmentStatus` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'ShipmentStatus 值：WORKING - 卖家已创建货件，但未发货。 SHIPPED - 承运人已取件。',
  `inboundplanid` bigint unsigned DEFAULT NULL COMMENT 'planId',
  `name` char(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'shipment_name',
  `TotalUnits` int DEFAULT NULL COMMENT '装运单位数',
  `FeePerUnit` decimal(10,2) DEFAULT NULL COMMENT '单位手工加工费',
  `status` int DEFAULT NULL COMMENT '-1,已驳回；0取消货件；1,待审核；2，配货（已确认货件）；3，装箱；4，物流信息确认；5已发货；6，已完成发货',
  `status0date` datetime DEFAULT NULL,
  `status1date` datetime DEFAULT NULL,
  `status2date` datetime DEFAULT NULL,
  `status3date` datetime DEFAULT NULL,
  `status4date` datetime DEFAULT NULL,
  `status5date` datetime DEFAULT NULL COMMENT '客户可以修改的发货日期',
  `status6date` datetime DEFAULT NULL,
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `TotalFee` decimal(10,2) DEFAULT NULL COMMENT '装运的总手工加工费',
  `currency` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '货币',
  `carrier` char(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '承运人',
  `boxnum` int DEFAULT NULL,
  `oldboxnum` int DEFAULT NULL,
  `transtyle` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `refreshtime` datetime DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `createdate` datetime DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `shiped_date` datetime DEFAULT NULL,
  `start_receive_date` datetime DEFAULT NULL,
  `creator` bigint unsigned DEFAULT NULL,
  `submissionid_excel` bigint unsigned DEFAULT NULL,
  `submissionid` bigint unsigned DEFAULT NULL,
  `feedstatus` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `pro_number` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `referenceid` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `transport_status` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `box_contents_source` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `sync_inv` tinyint DEFAULT '0' COMMENT '1代表没有扣库存，2代表已经扣库存',
  `check_inv` bigint unsigned DEFAULT NULL COMMENT '下架ID',
  `ignorerec` bit(1) DEFAULT b'0' COMMENT '忽略收货异常',
  PRIMARY KEY (`ShipmentId`),
  KEY `idx_inboundplanid_status_refreshtime` (`inboundplanid`,`status`,`refreshtime`),
  KEY `Index 2` (`inboundplanid`),
  KEY `Index 3` (`status`),
  KEY `idx_inboundplanid_status_shipeddate_refreshtime` (`inboundplanid`,`status`,`shiped_date`,`refreshtime`),
  KEY `status5date` (`status5date`),
  KEY `DestinationFulfillmentCenterId` (`DestinationFulfillmentCenterId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
