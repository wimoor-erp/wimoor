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

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_hsa_purchaseproduct 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_hsa_purchaseproduct` (
  `campaignId` bigint unsigned NOT NULL COMMENT '广告活动ID',
  `adGroupId` bigint unsigned NOT NULL COMMENT '广告组ID',
  `bydate` date NOT NULL COMMENT '日期',
  `campaignBudgetCurrencyCode` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '广告活动预算货币代码',
  `campaignName` char(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '广告活动名称',
  `campaignPriceTypeCode` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '广告活动价格类型代码',
  `adGroupName` char(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '广告组名称',
  `attributionType` char(50) DEFAULT NULL COMMENT '归因类型',
  `purchasedAsin` char(50) NOT NULL COMMENT '购买ASIN',
  `ordersClicks14d` int DEFAULT NULL COMMENT '14天内点击产生的订单',
  `productName` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '产品名称',
  `productCategory` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '产品类别',
  `sales14d` decimal(10,2) DEFAULT NULL COMMENT '14天内销售额',
  `salesClicks14d` decimal(10,2) DEFAULT NULL COMMENT '14天内点击产生的销售额',
  `orders14d` int DEFAULT NULL COMMENT '14天内订单量',
  `unitsSold14d` int DEFAULT NULL COMMENT '14天内销量',
  `newToBrandSales14d` decimal(10,2) DEFAULT NULL COMMENT '14天内新品牌销售额',
  `newToBrandPurchases14d` int DEFAULT NULL COMMENT '14天内新品牌购买量',
  `newToBrandUnitsSold14d` int DEFAULT NULL COMMENT '14天内新品牌销量',
  `newToBrandSalesPercentage14d` decimal(10,2) DEFAULT NULL COMMENT '14天内新品牌销售额百分比',
  `newToBrandPurchasesPercentage14d` decimal(10,2) DEFAULT NULL COMMENT '14天内新品牌购买量百分比',
  `newToBrandUnitsSoldPercentage14d` decimal(10,2) DEFAULT NULL COMMENT '14天内新品牌销量百分比',
  `unitsSoldClicks14d` int DEFAULT NULL COMMENT '14天内点击产生的销量',
  `kindleEditionNormalizedPagesRead14d` int DEFAULT NULL COMMENT '14天内Kindle版标准化页阅读量',
  `kindleEditionNormalizedPagesRoyalties14d` decimal(10,2) DEFAULT NULL COMMENT '14天内Kindle版标准化页版税',
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`adGroupId`,`bydate`,`purchasedAsin`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='亚马逊广告HSA购买产品报告表';

-- 数据导出被取消选择。

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
