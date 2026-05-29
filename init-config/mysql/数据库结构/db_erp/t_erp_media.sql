-- --------------------------------------------------------
-- 商品媒体资源元数据表（对标 PDM sku_pic + sku_video）
-- 双表分离架构：media（元数据）+ media_ref（关联）
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

-- 导出  表 db_erp.t_erp_media 结构
CREATE TABLE IF NOT EXISTS `t_erp_media` (
  `id` bigint unsigned NOT NULL COMMENT '主键(Snowflake)',
  `shopid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '企业ID(租户隔离)',
  `media_type` tinyint NOT NULL DEFAULT 0 COMMENT '媒体类型: 0=图片 1=视频',
  `usage_type` tinyint NOT NULL DEFAULT 40 COMMENT '用途类型: 10=参考图 30=原图 40=成品图 60=橱窗图 70=公共图 90=说明图 100=场景图',
  `source` tinyint NOT NULL DEFAULT 1 COMMENT '来源: 1=引用图 2=自拍图 3=白底图 4=Amazon同步 5=批量导入 6=AI生成',
  `url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '外部原始URL(如Amazon CDN)',
  `location` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '对象存储相对路径',
  `thumb_location` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '缩略图/视频封面存储路径',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '原始文件名',
  `width` int DEFAULT NULL COMMENT '宽度px',
  `height` int DEFAULT NULL COMMENT '高度px',
  `file_size` bigint DEFAULT NULL COMMENT '文件大小(bytes)',
  `duration` int DEFAULT NULL COMMENT '视频时长(秒,仅视频)',
  `content_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'MIME类型',
  `md5` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '文件MD5(用于去重)',
  `process_status` tinyint NOT NULL DEFAULT 0 COMMENT 'AI处理状态: 0=无需处理 1=处理中 2=处理完成 3=处理失败',
  `process_task_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'AI处理任务ID(用于回调关联)',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '上传人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_shop` (`shopid`),
  KEY `idx_md5` (`shopid`, `md5`),
  KEY `idx_process` (`process_status`, `shopid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='商品媒体资源元数据表';

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
