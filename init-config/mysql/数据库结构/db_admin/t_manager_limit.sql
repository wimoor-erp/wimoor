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

-- 导出  表 db_admin.t_manager_limit 结构
CREATE TABLE IF NOT EXISTS `t_manager_limit` (
  `id` bigint unsigned NOT NULL COMMENT 'ID(h)',
  `shopId` bigint unsigned DEFAULT NULL,
  `maxShopCount` int DEFAULT '10' COMMENT '绑定店铺数量',
  `maxMarketCount` int DEFAULT '10' COMMENT '绑定店铺站点数量',
  `maxProductCount` int DEFAULT '50000' COMMENT '商品数量上限',
  `maxOrderCount` int DEFAULT '3600' COMMENT '处理订单上限',
  `maxMember` int DEFAULT '3' COMMENT '子用户数量上限',
  `maxProfitPlanCount` int DEFAULT '1' COMMENT '利润计算方案数量',
  `maxdayOpenAdvCount` int DEFAULT '10' COMMENT '每天开启广告组数量',
  `existShopCount` int DEFAULT '0',
  `existMarketCount` int DEFAULT '0',
  `existProductCount` int DEFAULT '0',
  `existOrderCount` int DEFAULT '0',
  `existMember` int DEFAULT '0',
  `existProfitPlanCount` int DEFAULT '0',
  `existdayOpenAdvCount` int DEFAULT '0',
  `tariffpackage` int DEFAULT '0' COMMENT '0-基础版，1-标准版，2-专业版，3-独享版,4-自定义',
  `losingEffect` date DEFAULT NULL COMMENT '失效时间',
  `remark` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备注',
  `opratetime` datetime DEFAULT NULL COMMENT '修改时间',
  `createtime` datetime DEFAULT NULL COMMENT '创建时间',
  `oprate` bigint unsigned DEFAULT NULL COMMENT '修改人',
  `logicVersion` bigint DEFAULT '0',
  `saleskey` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `neverNoticeShop` bit(1) DEFAULT b'0',
  `afterNnoticeTariff` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `t_manager_limit_ibfk_1` (`shopId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
