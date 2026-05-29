## 1. 数据库层

- [x] 1.1 创建 `init-config/mysql/数据库结构/db_erp/t_erp_material_listing.sql` 建表 SQL（字段：id BIGINT PK、materialid BIGINT NOT NULL、shopid BIGINT NOT NULL、lang VARCHAR(10) NOT NULL、title VARCHAR(500)、description MEDIUMTEXT、create_time DATETIME、update_time DATETIME；唯一索引 `uk_material_lang(materialid, lang)`）
- [x] 1.2 在 `deploy/mysql-init/` 下新增或追加 Listing 表初始化 SQL（对齐 Docker 自动初始化流程）

## 2. 后端 Entity & Mapper

- [x] 2.1 创建实体类 `wimoor-erp/erp-boot/src/main/java/com/wimoor/erp/material/pojo/entity/MaterialListing.java`（MyBatis Plus 注解，`@TableName("t_erp_material_listing")`，字段映射，雪花 ID）
- [x] 2.2 创建 Mapper 接口 `wimoor-erp/erp-boot/src/main/java/com/wimoor/erp/material/mapper/MaterialListingMapper.java`（继承 BaseMapper）
- [x] 2.3 创建 Mapper XML `wimoor-erp/erp-boot/src/main/resources/mapper/material/MaterialListingMapper.xml`（如需自定义 SQL）

## 3. 后端 Service

- [x] 3.1 创建 Service 接口 `IMaterialListingService.java`（方法：listByMaterialId、getByMaterialIdAndLang、saveOrUpdate、deleteById）
- [x] 3.2 创建 Service 实现 `MaterialListingServiceImpl.java`（含 HTML XSS 过滤逻辑，使用 jsoup Cleaner 白名单方式清洗 description）
- [x] 3.3 在 `wimoor-erp/erp-boot/pom.xml` 中添加 jsoup 依赖（如尚未存在）

## 4. 后端 Controller

- [x] 4.1 创建 `MaterialListingController.java`（RequestMapping `/api/v1/material/listing`）
- [x] 4.2 实现 GET `/list` 接口（返回产品所有语言 Listing 概要：id、lang、title、update_time）
- [x] 4.3 实现 GET `/get` 接口（返回产品指定语言完整 Listing 记录含 description）
- [x] 4.4 实现 POST `/save` 接口（接收 materialid、lang、title、description，执行 UPSERT）
- [x] 4.5 实现 DELETE `/delete` 接口（按 id 删除，幂等）
- [x] 4.6 添加 `@SystemControllerLog` 注解用于操作日志记录

## 5. 前端 API

- [x] 5.1 创建 `wimoorui/src/api/erp/material/materialListingApi.js`（封装 list、get、save、delete 四个请求方法）

## 6. 前端编辑页 Listing Tab

- [x] 6.1 创建组件 `wimoorui/src/views/erp/baseinfo/material/editinfo/components/listing.vue`（含语言 tab 切换、title 输入框带 500 字符校验、TinyMCE 富文本编辑器、独立保存按钮）
- [x] 6.2 在 `editinfo/index.vue` 中导入 Listing 组件，添加左侧 tab 项（"标题描述"），在内容区对应位置渲染 Listing section（位于物流/辅料之后、图片视频之前）
- [x] 6.3 确保 tab 锚点 id 与 href 对应，滚动同步正常工作
- [x] 6.4 保存按钮与其他模块保持一致（无需独立权限控制，与保存基本信息/保存组合信息等按钮一致）

## 7. 前端详情页 Listing 展示

- [x] 7.1 在 `details/components/listinfo.vue` 或新建独立组件，增加 标题描述展示区域（语言 tab + title 展示 + description HTML 渲染）
- [x] 7.2 在 `details/index.vue` 中添加对应 tab 锚点项
- [x] 7.3 无数据时显示"暂无 标题描述"空状态

## 8. 权限配置

- [x] 8.1 在 `init-config/mysql/数据/` 中补充菜单按钮权限数据 `erp:material:listing:edit`（INSERT 到 `t_sys_menu` 表）

## 9. 验证与测试

- [x] 9.1 编译验证：`mvn -pl wimoor-erp/erp-boot -am -DskipTests package` 确保后端编译通过
- [x] 9.2 前端验证：`cd wimoorui && npm run build` 确保无编译错误
- [x] 9.3 Docker 容器中部署 ERP 服务，API 接口联调验证（CRUD 全流程）
- [x] 9.4 验证 XSS 过滤：提交含 script/onclick 的 HTML，确认被清洗
- [x] 9.5 验证前端编辑页：语言切换、保存、加载功能正常
- [x] 9.6 验证前端详情页：HTML 描述正确渲染
