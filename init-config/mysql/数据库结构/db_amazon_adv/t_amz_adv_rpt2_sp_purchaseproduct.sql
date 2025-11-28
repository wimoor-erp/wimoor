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

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_sp_purchaseproduct 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sp_purchaseproduct` (
  `keywordId` bigint unsigned NOT NULL COMMENT '关键词ID',
  `adGroupId` bigint unsigned DEFAULT NULL COMMENT '广告组ID',
  `campaignId` bigint unsigned DEFAULT NULL COMMENT '广告活动ID',
  `bydate` date NOT NULL,
  `addToList` int DEFAULT NULL COMMENT '添加到列表',
  `addToListFromClicks` int DEFAULT NULL COMMENT '从点击添加到列表',
  `qualifiedBorrows` int DEFAULT NULL COMMENT '合格借阅',
  `qualifiedBorrowsFromClicks` int DEFAULT NULL COMMENT '从点击产生的合格借阅',
  `royaltyQualifiedBorrows` int DEFAULT NULL COMMENT '版税合格借阅',
  `royaltyQualifiedBorrowsFromClicks` int DEFAULT NULL COMMENT '从点击产生的版税合格借阅',
  `portfolioId` bigint unsigned DEFAULT NULL COMMENT '组合ID',
  `campaignName` char(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '广告活动名称',
  `adGroupName` char(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '广告组名称',
  `keyword` char(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '关键词',
  `keywordType` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '关键词类型',
  `advertisedAsin` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '广告ASIN',
  `purchasedAsin` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '购买ASIN',
  `advertisedSku` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '广告SKU',
  `campaignBudgetCurrencyCode` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '广告活动预算货币代码',
  `matchType` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '匹配类型',
  `unitsSoldClicks1d` int DEFAULT NULL COMMENT '1天内点击产生的销量',
  `unitsSoldClicks7d` int DEFAULT NULL COMMENT '7天内点击产生的销量',
  `unitsSoldClicks14d` int DEFAULT NULL COMMENT '14天内点击产生的销量',
  `unitsSoldClicks30d` int DEFAULT NULL COMMENT '30天内点击产生的销量',
  `sales1d` decimal(12,2) DEFAULT NULL COMMENT '1天内销售额',
  `sales7d` decimal(12,2) DEFAULT NULL COMMENT '7天内销售额',
  `sales14d` decimal(12,2) DEFAULT NULL COMMENT '14天内销售额',
  `sales30d` decimal(12,2) DEFAULT NULL COMMENT '30天内销售额',
  `purchases1d` int DEFAULT NULL COMMENT '1天内购买量',
  `purchases7d` int DEFAULT NULL COMMENT '7天内购买量',
  `purchases14d` int DEFAULT NULL COMMENT '14天内购买量',
  `purchases30d` int DEFAULT NULL COMMENT '30天内购买量',
  `unitsSoldOtherSku1d` int DEFAULT NULL COMMENT '1天内其他SKU销量',
  `unitsSoldOtherSku7d` int DEFAULT NULL COMMENT '7天内其他SKU销量',
  `unitsSoldOtherSku14d` int DEFAULT NULL COMMENT '14天内其他SKU销量',
  `unitsSoldOtherSku30d` int DEFAULT NULL COMMENT '30天内其他SKU销量',
  `salesOtherSku1d` decimal(12,2) DEFAULT NULL COMMENT '1天内其他SKU销售额',
  `salesOtherSku7d` decimal(12,2) DEFAULT NULL COMMENT '7天内其他SKU销售额',
  `salesOtherSku14d` decimal(12,2) DEFAULT NULL COMMENT '14天内其他SKU销售额',
  `salesOtherSku30d` decimal(12,2) DEFAULT NULL COMMENT '30天内其他SKU销售额',
  `purchasesOtherSku1d` int DEFAULT NULL COMMENT '1天内其他SKU购买量',
  `purchasesOtherSku7d` int DEFAULT NULL COMMENT '7天内其他SKU购买量',
  `purchasesOtherSku14d` int DEFAULT NULL COMMENT '14天内其他SKU购买量',
  `purchasesOtherSku30d` int DEFAULT NULL COMMENT '30天内其他SKU购买量',
  `kindleEditionNormalizedPagesRead14d` int DEFAULT NULL COMMENT '14天内Kindle版标准化页阅读量',
  `kindleEditionNormalizedPagesRoyalties14d` decimal(12,2) DEFAULT NULL COMMENT '14天内Kindle版标准化页版税',
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`bydate`,`keywordId`,`purchasedAsin`) USING BTREE,
  KEY `adGroupId_campaignId` (`campaignId`,`adGroupId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='亚马逊广告报告SD购买产品数据表';

-- 数据导出被取消选择。

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
