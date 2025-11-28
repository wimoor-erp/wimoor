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

-- 导出  表 db_amazon.t_product_info 结构
CREATE TABLE IF NOT EXISTS `t_product_info` (
  `id` bigint unsigned NOT NULL COMMENT '产品ID',
  `asin` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '唯一码asin',
  `sku` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '用户码sku',
  `marketplaceid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '站点',
  `name` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '产品名称（产品标题）',
  `openDate` datetime DEFAULT NULL COMMENT '创建日期',
  `itemDimensions` bigint unsigned DEFAULT NULL COMMENT '产品尺寸',
  `pageDimensions` bigint unsigned DEFAULT NULL COMMENT '含包装尺寸',
  `fulfillChannel` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '交付渠道',
  `binding` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '装订',
  `totalOfferCount` int DEFAULT NULL COMMENT '卖家数量',
  `brand` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '品牌',
  `manufacturer` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '厂商',
  `pgroup` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '分组',
  `typename` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '分类',
  `price` decimal(14,2) DEFAULT NULL COMMENT '价格',
  `image` bigint unsigned DEFAULT NULL COMMENT '照片',
  `parentMarketplace` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '父商品marketplace',
  `parentAsin` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '父商品asin',
  `isparent` bit(1) DEFAULT b'0' COMMENT '是否副产品（即不是变体）',
  `lastupdate` datetime DEFAULT NULL COMMENT '更新时间',
  `createdate` datetime DEFAULT NULL,
  `amazonAuthId` bigint unsigned DEFAULT NULL COMMENT '授权ID',
  `invalid` bit(1) DEFAULT b'0' COMMENT '是否无效',
  `oldid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `inSnl` bit(1) DEFAULT b'0' COMMENT '是否轻小',
  `fnsku` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `pcondition` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `status` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `disable` bit(1) DEFAULT b'0',
  `refreshtime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `Index 3` (`amazonAuthId`,`marketplaceid`,`sku`),
  KEY `Index 6` (`marketplaceid`,`amazonAuthId`,`isparent`,`invalid`,`disable`),
  KEY `invalid` (`invalid`,`disable`,`isparent`),
  KEY `idx_asin_amazonAuthId` (`asin`),
  KEY `marketplaceid_parentAsin_amazonAuthId` (`parentAsin`,`marketplaceid`,`amazonAuthId`),
  KEY `idx_sku_isparent_invalid` (`sku`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='产品信息';

-- 数据导出被取消选择。

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
