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

-- 导出  表 db_amazon.t_product_in_opt 结构
CREATE TABLE IF NOT EXISTS `t_product_in_opt` (
  `pid` bigint unsigned NOT NULL COMMENT '产品ID',
  `remark` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备注',
  `priceremark` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '价格公告',
  `buyprice` decimal(10,2) DEFAULT NULL COMMENT '采购单价',
  `businessprice` decimal(10,2) DEFAULT NULL COMMENT '销售价格',
  `businesstype` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '价格',
  `businesslist` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '隐藏',
  `disable` bit(1) DEFAULT b'0' COMMENT '手动输入的预估销量',
  `presales` int DEFAULT NULL COMMENT '手动输入的预估销量',
  `lastupdate` datetime DEFAULT NULL COMMENT '更新时间',
  `remark_analysis` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备注',
  `msku` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '本地SKU',
  `fnsku` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'FNSKU',
  `review_daily_refresh` int DEFAULT NULL COMMENT '评论刷新时间',
  `fulfillment_availability` int DEFAULT NULL COMMENT '自发货可用库存',
  `merchant_shipping_group` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '自发货运费模板',
  `profitid` bigint unsigned DEFAULT NULL COMMENT '对应利润计算方案',
  `status` int unsigned DEFAULT NULL COMMENT '产品状态 0备货 1维持 2提升 3促销  4停售 5清仓 6删除',
  `owner` bigint unsigned DEFAULT NULL COMMENT '运营负责人',
  `operator` bigint unsigned DEFAULT NULL COMMENT '操作人',
  `lowestprice` decimal(20,6) DEFAULT NULL,
  PRIMARY KEY (`pid`),
  KEY `idx_msku_disable_status` (`msku`,`disable`,`status`),
  KEY `msku` (`msku`),
  KEY `Index 5` (`disable`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='产品信息';

-- 数据导出被取消选择。

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
