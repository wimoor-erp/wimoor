-- =============================================================
-- 商品媒体管理 — 权限点配置 (db_admin.t_sys_permission)
-- 触发 Gateway Redis hash `system:perm_roles_rule:url:` 的兜底数据。
-- 说明:
--   parent_id 需引用 "商品管理" 菜单 id；这里使用占位 98（与现有 t_sys_permission.sql 中 material 节点一致）。
--   实际部署时建议通过 Admin -> 菜单管理 UI 配置，自动同步 Redis。
--   id 使用雪花占位，请按现有规则替换。
-- =============================================================

INSERT INTO `t_sys_permission`(id, name, parent_id, url, code, create_time, update_time) VALUES
  (1900000000000000001, '媒体-上传', 98, 'POST:/erp/api/v1/material/media/upload',          'erp:material:media:upload', NOW(), NOW()),
  (1900000000000000002, '媒体-批量上传', 98, 'POST:/erp/api/v1/material/media/uploadBatch', 'erp:material:media:upload', NOW(), NOW()),
  (1900000000000000003, '媒体-查询SKU', 98, 'GET:/erp/api/v1/material/media/list',          'erp:material:media:view',   NOW(), NOW()),
  (1900000000000000004, '媒体-查询池', 98, 'GET:/erp/api/v1/material/media/pool',           'erp:material:media:view',   NOW(), NOW()),
  (1900000000000000005, '媒体-分配', 98, 'POST:/erp/api/v1/material/media/assign',          'erp:material:media:assign', NOW(), NOW()),
  (1900000000000000006, '媒体-批量分配', 98, 'POST:/erp/api/v1/material/media/batchAssign', 'erp:material:media:assign', NOW(), NOW()),
  (1900000000000000007, '媒体-取消分配', 98, 'DELETE:/erp/api/v1/material/media/unassign/*','erp:material:media:assign', NOW(), NOW()),
  (1900000000000000008, '媒体-设主图', 98, 'PUT:/erp/api/v1/material/media/setMain/*',      'erp:material:media:assign', NOW(), NOW()),
  (1900000000000000009, '媒体-排序', 98, 'POST:/erp/api/v1/material/media/sort',            'erp:material:media:assign', NOW(), NOW()),
  (1900000000000000010, '媒体-修改用途', 98, 'PUT:/erp/api/v1/material/media/usage/*',      'erp:material:media:assign', NOW(), NOW()),
  (1900000000000000011, '媒体-删除', 98, 'DELETE:/erp/api/v1/material/media/delete/*',      'erp:material:media:delete', NOW(), NOW()),
  (1900000000000000012, '媒体-Amazon同步', 98, 'POST:/erp/api/v1/material/media/syncFromAmazon', 'erp:material:media:sync', NOW(), NOW()),
  (1900000000000000013, '媒体-AI回调', 98, 'POST:/erp/api/v1/material/media/processCallback','erp:material:media:callback', NOW(), NOW());
