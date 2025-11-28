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

-- 导出  表 db_amazon.t_product_in_sys 结构
CREATE TABLE IF NOT EXISTS `t_product_in_sys` (
  `pid` bigint unsigned NOT NULL COMMENT '产品ID',
  `saleDate` datetime DEFAULT NULL,
  `orderDate` datetime DEFAULT NULL,
  `avgsales` int DEFAULT NULL,
  `oldavgsales` int DEFAULT NULL,
  `daynum` int DEFAULT NULL,
  `maxsales_day_month` int DEFAULT NULL,
  `sales_week` int DEFAULT NULL COMMENT 'sales_week,往前推2天之后的7日销量',
  `price_week` decimal(10,2) DEFAULT NULL COMMENT '销售额',
  `profit_week` decimal(10,2) DEFAULT NULL,
  `margin_week` decimal(10,2) DEFAULT NULL,
  `sales_month` int DEFAULT NULL COMMENT '30日销量',
  `order_week` int DEFAULT NULL,
  `order_month` int DEFAULT NULL,
  `changeRate` decimal(10,2) DEFAULT NULL,
  `lastupdate` datetime DEFAULT NULL,
  `sales_seven` int DEFAULT NULL COMMENT 'sales_seven,7日销量',
  `sales_fifteen` int DEFAULT NULL COMMENT 'sales_fifteen,15日销量',
  `rank` int DEFAULT NULL,
  `buyprice` decimal(10,2) DEFAULT NULL,
  `shipmentfee` decimal(10,2) DEFAULT NULL,
  `othersfee` decimal(10,2) DEFAULT NULL,
  `costDetail` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '',
  PRIMARY KEY (`pid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='产品信息';

-- 数据导出被取消选择。

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
