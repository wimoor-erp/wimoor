-- --------------------------------------------------------
-- t_erp_material 表新增 has_media 字段
-- 用于标记商品是否拥有多媒体资源，便于列表页快速判断
-- --------------------------------------------------------

ALTER TABLE `t_erp_material`
  ADD COLUMN `has_media` tinyint NOT NULL DEFAULT 0 COMMENT '是否有多媒体 0=否 1=是' AFTER `image`;
