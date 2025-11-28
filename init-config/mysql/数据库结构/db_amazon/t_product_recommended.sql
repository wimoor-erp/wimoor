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

-- 导出  表 db_amazon.t_product_recommended 结构
CREATE TABLE IF NOT EXISTS `t_product_recommended` (
  `id` bigint unsigned NOT NULL,
  `amazonAuthId` bigint unsigned DEFAULT NULL,
  `marketplaceid` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `refreshtime` datetime DEFAULT NULL,
  `asin` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `name` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `link` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '产品购买链接',
  `brand` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '品牌',
  `category` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '分类',
  `subcategory` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '子分类',
  `lowestprice` decimal(10,2) DEFAULT NULL COMMENT '上周最低价格',
  `fbaoffer` bit(1) DEFAULT b'0' COMMENT 'fba提供',
  `amzoffer` bit(1) DEFAULT b'0' COMMENT '亚马逊提供',
  `offers` int DEFAULT NULL COMMENT '优惠数量',
  `reviews` int DEFAULT NULL COMMENT '评论数量',
  `rank` int DEFAULT NULL COMMENT '销量排名',
  `sales_rank_growth` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '业务销售额排名增长 评级',
  `page_views` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '页面浏览量 评级',
  `manufacturer_part_number` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '制造商零件编号',
  `EAN` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'EAN码',
  `UPC` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'UPC码',
  `model_number` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '型号编号',
  `ISBN` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '图书编码',
  `brandoffer` bit(1) DEFAULT NULL COMMENT '是否 自己提供的品牌',
  `categoryoffer` bit(1) DEFAULT NULL COMMENT '是否 自己提供的类别',
  `performance` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '产品性能',
  `istoprank` bit(1) DEFAULT b'0' COMMENT '是否最高销售排名',
  `islowprice` bit(1) DEFAULT b'0' COMMENT '是否最低价格',
  `onAmazon` bit(1) DEFAULT b'0' COMMENT '产品尚未在亚马逊上',
  `isrefresh` bit(1) DEFAULT b'0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `amazonAuthId_marketplaceid_asin` (`amazonAuthId`,`marketplaceid`,`asin`),
  KEY `asin` (`amazonAuthId`,`sales_rank_growth`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
