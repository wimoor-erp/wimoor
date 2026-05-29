-- --------------------------------------------------------
-- 商品媒体-商品关联表（对标 PDM sku_pic_pack）
-- 实现一图多用、SPU池→SKU分配、Listing绑定
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

-- 导出  表 db_erp.t_erp_media_ref 结构
CREATE TABLE IF NOT EXISTS `t_erp_media_ref` (
  `id` bigint unsigned NOT NULL COMMENT '主键(Snowflake)',
  `media_id` bigint unsigned NOT NULL COMMENT 'FK → t_erp_media.id',
  `material_id` bigint unsigned NOT NULL COMMENT 'FK → t_erp_material.id',
  `shopid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '企业ID',
  `ref_type` tinyint NOT NULL DEFAULT 0 COMMENT '关联类型: 0=SPU级图片池 1=SKU级展示图',
  `pic_class` tinyint NOT NULL DEFAULT 1 COMMENT '分配角色: 1=成品图 2=橱窗图 3=公共图 4=说明图 5=场景图',
  `sort_order` int NOT NULL DEFAULT 0 COMMENT '排序(越小越靠前)',
  `is_main` tinyint NOT NULL DEFAULT 0 COMMENT '是否主图 0=否 1=是',
  `platform` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '平台标识(amazon/tiktok/ebay) NULL=通用',
  `marketplace_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '站点ID(用于多站点差异图)',
  `slot_position` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '图片位(MAIN/PT01~PT08) 用于Listing绑定',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_media_material_slot` (`media_id`, `material_id`, `platform`, `marketplace_id`, `slot_position`),
  KEY `idx_material` (`material_id`, `ref_type`),
  KEY `idx_shop_material` (`shopid`, `material_id`),
  KEY `idx_media` (`media_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='媒体-商品关联表';

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
