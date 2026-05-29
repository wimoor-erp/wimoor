# 14 SKU 分类属性与平台刊登映射分析

> 本文档基于源码分析（2026-05-28），描述 Wimoor 系统中 ERP 本地 SKU、分类属性与亚马逊平台多店铺刊登之间的映射关系和现有实现。

---

## 1. 总体架构

```
ERP（本地）                        Amazon（平台）
─────────────────                  ─────────────────────────────────────
t_erp_material                     t_product_info
  .sku         ─── msku映射 ──→     .sku  (SellerSKU)
  .categoryid                       .asin (Amazon唯一编码)
  .name                             .fnsku
  .brand                            .marketplaceid
  .specification                    .amazonAuthId (关联到哪个店铺授权)
  ↓
t_erp_material_category            t_product_in_opt (运营配置层)
  .id, .name                         .pid → t_product_info.id
  .shopid                            .msku ← ERP本地SKU（可覆盖）
                                     .fnsku
```

### 核心结论

| 维度 | 现有设计 |
|------|---------|
| ERP本地分类 | 简单标签式（id/name/shopid），无层级，不与Amazon分类做结构性绑定 |
| Amazon分类 | BrowseNode树（`t_amz_adv_browsenode`）+ ProductType（`t_product_type`）两套独立体系 |
| 分类映射 | **缺失**：ERP分类 ↔ Amazon分类之间没有映射表，由用户刊登时手动选择 ProductType |
| 属性映射 | Amazon属性通过 SP-API Definitions API 动态获取 JSON Schema，不在本地持久化属性字段 |
| SKU映射 | `t_product_in_opt.msku` 字段存储"本地ERP SKU"，对应关系为：`t_product_info`（含amazonAuthId+marketplaceid+sku的唯一记录）→ `t_product_in_opt.msku` |
| 重复刊登 | 同一本地SKU可在同一平台不同店铺刊登多次，体现为多条`t_product_info`记录（不同`amazonAuthId`），都指向同一`msku` |

---

## 2. ERP 本地分类（`t_erp_material_category`）

```sql
CREATE TABLE `t_erp_material_category` (
  `id`       bigint unsigned NOT NULL,
  `name`     char(100),        -- 分类名称（如"耳机"、"数码配件"）
  `number`   char(50),         -- 编号
  `color`    char(10),         -- 颜色标识（UI用）
  `shopid`   bigint unsigned,  -- 公司ID（租户隔离）
  `remark`   varchar(500),
  `opttime`  datetime,
  `operator` bigint unsigned,
  PRIMARY KEY (`id`),
  UNIQUE KEY (`name`, `shopid`)
);
```

**特点：**
- 是**平面结构**，没有父子层级（无 `parentId` 字段）。
- 仅作为 ERP 内部物料管理的"分组标签"，不承载任何平台分类语义。
- `t_erp_material.categoryid` 引用此表，用于库存/采购模块按类别筛选。

---

## 3. Amazon 平台分类体系

Amazon 分类在系统中有两套独立表，用途不同：

### 3.1 BrowseNode 树（`t_amz_adv_browsenode`）

```sql
CREATE TABLE `t_amz_adv_browsenode` (
  `id`              bigint unsigned NOT NULL,  -- Amazon BrowseNode ID
  `name`            varchar(200),
  `parentid`        bigint unsigned,           -- 父节点ID（树形结构）
  `country`         char(2),                  -- 国家/站点
  `level`           int,
  `is_category_root` bit(1),
  `refreshtime`     datetime,
  PRIMARY KEY (`id`)
);
```

**用途：** 广告模块（`wimoor-amazon-adv`）用于选择广告定向分类，不直接用于 listing 刊登的分类选择。

### 3.2 ProductType 表（`t_product_type`）

```sql
CREATE TABLE `t_product_type` (
  `id`      int AUTO_INCREMENT,
  `name`    varchar(255),   -- ProductType名称（如 "HEADPHONES", "SHOES"）
  `country` varchar(5),     -- 国家/站点
  PRIMARY KEY (`id`)
);
```

**用途：** Listing 刊登时选择 Amazon ProductType，是刊登属性 Schema 的入口。从 SP-API `DefinitionsApi.searchDefinitionsProductTypes()` 同步获取并缓存在此表。

### 3.3 产品信息分类字段（`t_product_info`）

```sql
-- t_product_info 中的分类相关字段
`pgroup`   varchar(50)    -- 分组（Amazon API返回的pgroup，如 "Sports"）
`typename` varchar(50)    -- 分类名（Amazon API返回的类型名称）
`binding`  varchar(50)    -- 装订/产品绑定形式（如 "Electronics"）
```

这些字段是从 Amazon SP-API（Catalog Items API / Listings Items API）拉取后**被动存储**的，不是用户主动配置的分类字段。

### 3.4 产品二级分类表（`t_product_category`）

```sql
CREATE TABLE `t_product_category` (
  `CategoryId` char(50),      -- Amazon分类ID
  `pid`        bigint unsigned, -- 公司ID
  `Name`       varchar(200),  -- 分类名
  `parentId`   char(36),      -- 父分类ID
  PRIMARY KEY (`pid`, `CategoryId`)
);
```

从 Amazon Catalog Items API 返回的产品分类树（非 BrowseNode），用于展示产品所属分类路径。

---

## 4. 属性（Attribute）映射机制

### 4.1 设计思路

Wimoor 对亚马逊商品属性**没有本地静态映射表**，而是采用**动态 JSON Schema** 方案：

```
用户选择 ProductType
    ↓
调用 SP-API: DefinitionsApi.getDefinitionsProductType()
    ↓
返回 JSON Schema（包含该类目下所有属性的名称、类型、约束、是否必填）
    ↓
前端动态渲染表单
    ↓
用户填写属性值
    ↓
以 JSON Object 形式通过 attributes 字段提交 putListingsItem / patchListingsItem
```

**关键接口：**

| Controller | 方法 | 说明 |
|-----------|------|------|
| `AmzProductTypeController.searchDefinitionsProductTypes()` | `POST /api/v1/product/amzProductType/searchDefinitionsProductTypes` | 按关键词搜索 ProductType |
| `AmzProductTypeController.getProductDefinitionAction()` | `POST /api/v1/product/amzProductType/getProductDefinition` | 获取指定 ProductType 的完整属性 Schema |
| `AmzProductTypeController.getgetSchemaAction()` | `GET /api/v1/product/amzProductType/getSchema` | 直接按 URL 拉取 Amazon 的 JSON Schema 文件 |

### 4.2 属性提交结构（`ProductListingPushDTO`）

```java
public class ProductListingPushDTO {
    List<String> marketplaceids;  // 目标站点（支持同时提交多站点）
    String amazonauthid;          // 授权店铺ID
    String sku;                   // 平台 SellerSKU
    String productType;           // Amazon ProductType（如 "HEADPHONES"）
    private Object attributes;    // JSON Object，包含所有属性值
    ListingsItemPatchRequest patchBody; // PATCH 方式时使用
}
```

`attributes` 字段是自由 JSON Object，结构由对应 ProductType 的 Schema 决定，系统不做本地校验，直接透传给 SP-API。

### 4.3 公共属性字段（跨站点差异处理）

`ItemAttributesDTO` 封装了多站点通用的运营属性，能为多站点生成差异化 JSON：

```java
public class ItemAttributesDTO {
    String title;                    // 标题（多站点语言不同）
    Money list_price;               // 定价
    Money purchasable_price;        // 可购买价格
    String condition_type;          // 商品状态（new_new等）
    String fulfillment_channel_code; // FBA/自发货
    String fulfillment_availability; // 可用库存
    String merchant_shipping_group; // 运费模板
    Dimensions itemDimensions;      // 产品尺寸
    Dimensions packageDimensions;   // 包装尺寸
    String max_order_quantity;       // 最大购买数量
    // ...
}
```

该对象的 `getJson(List<String> marketplaceids, String asin)` 方法会为每个 `marketplaceid` 生成各自的属性 JSON 数组，适配 Amazon 要求的"按 marketplace 分组"格式。

---

## 5. 本地 SKU 与平台刊登 SKU 映射关系

### 5.1 映射表设计

```
ERP 本地 SKU                     Amazon 平台 SKU
─────────────────────────────    ─────────────────────────────────────────
t_erp_material                   t_product_info
  .sku        （本地唯一码）         .sku    （SellerSKU，卖家自定义）
  .shopid                          .asin   （Amazon唯一ASIN）
                                   .fnsku  （FBA仓储标签码）
                                   .marketplaceid  （站点）
                                   .amazonAuthId   （授权/店铺ID）
                          ↕
                   t_product_in_opt
                     .pid        → t_product_info.id
                     .msku       ← 对应的本地 ERP SKU（可为空）
                     .fnsku      （冗余存储）
```

### 5.2 核心规则：`ifnull(opt.msku, t.sku)`

系统所有涉及"从平台 SKU 反查本地 SKU"的 SQL，均采用以下逻辑：

```sql
-- ProductInfoMapper.xml - selectByMSku
SELECT t.* FROM t_product_info t
LEFT JOIN t_product_in_opt o ON o.pid = t.id
WHERE ifnull(o.msku, t.sku) = #{msku}  -- 优先用 opt.msku，没有则用平台 sku 本身
```

```sql
-- selectByAuth（用于同步ERP库存数据）
SELECT a.id, a.shop_id shopid, a.groupid,
       ifnull(opt.msku, t.sku) msku,   -- 本地SKU
       t.id pid, t.asin, t.sku,        -- 平台SKU和ASIN
       ...
FROM t_product_info t
LEFT JOIN t_product_in_opt opt ON opt.pid = t.id
LEFT JOIN t_amazon_auth a ON a.id = t.amazonAuthId
```

**含义：**
- 若用户在 `t_product_in_opt.msku` 中手动配置了本地 SKU 映射 → 使用配置的本地 SKU。
- 若未配置（`msku IS NULL`） → 默认认为平台 SellerSKU 与本地 SKU 相同。

### 5.3 msku 的设置入口

`ProductInOptController` 提供接口供运营人员手动绑定：

```java
// POST /api/v1/product/opt/bindmsku (大致路径，见实际路由)
public Result<?> bindMskuAction(String pid, String msku, String ownerid) {
    ProductInOpt opt = iProductInOptService.getById(pid);
    if (opt != null) {
        opt.setMsku(msku);   // 设置本地ERP SKU映射
        iProductInOptService.updateById(opt);
    } else {
        opt = new ProductInOpt();
        opt.setPid(new BigInteger(pid));
        opt.setMsku(msku);
        iProductInOptService.save(opt);
    }
}
```

### 5.4 t_product_info 的唯一性约束

```sql
UNIQUE KEY `Index 3` (`amazonAuthId`, `marketplaceid`, `sku`)
```

即：**同一授权（店铺）+ 同一站点 + 同一 SellerSKU = 唯一记录**。

---

## 6. 一个本地 SKU 在同一平台不同店铺的重复刊登

### 6.1 数据结构支撑

Amazon 授权（店铺）通过 `t_amazon_auth` 管理：

```
t_amazon_auth
  .id         → 授权ID（一个店铺账号 = 一个授权记录）
  .shop_id    → 公司ID（租户）
  .sellerid   → Amazon Seller ID
  .groupid    → 店铺分组
```

一个本地 SKU 可在多个店铺中刊登，产生多条 `t_product_info` 记录：

```
本地 SKU: "ABC-001"
  │
  ├── t_product_info (amazonAuthId=店铺A, marketplaceid=US, sku="ABC-001")
  │     └── t_product_in_opt.msku = "ABC-001"
  │
  ├── t_product_info (amazonAuthId=店铺B, marketplaceid=US, sku="ABC-US-001")
  │     └── t_product_in_opt.msku = "ABC-001"  ← 手动映射
  │
  └── t_product_info (amazonAuthId=店铺A, marketplaceid=UK, sku="ABC-UK")
        └── t_product_in_opt.msku = "ABC-001"  ← 手动映射
```

### 6.2 查询多平台刊登数量

```sql
-- selectByMSku: 通过本地SKU反查所有平台刊登记录
SELECT t.* FROM t_product_info t
LEFT JOIN t_product_in_opt o ON o.pid = t.id
LEFT JOIN t_amazon_auth auth ON auth.id = t.amazonAuthId
LEFT JOIN t_amazon_group g ON g.id = auth.groupid
WHERE ifnull(o.msku, t.sku) = #{msku}
  AND auth.shop_id = #{shopid}
  -- 可选：AND t.marketplaceid = #{marketplaceid}
  -- 可选：AND auth.groupid = #{groupid}
```

### 6.3 入库计划中的重复刊登处理

`t_erp_ship_inbounditem` 中同时存储 `SellerSKU`（平台 SKU）和 `msku`（本地 SKU），支持发货计划中按本地 SKU 汇总多个平台 SKU 的发货数量：

```sql
-- AmzProductSalesPlanMapper.xml
SELECT IFNULL(o.msku, t.SellerSKU) msku,   -- 本地SKU
       MAX(t.SellerSKU) sku,                -- 平台SKU（多条时取一条代表）
       SUM(t.Quantity) qty                  -- 汇总数量
FROM t_erp_ship_inbounditem t
LEFT JOIN t_product_in_opt o ON ...        -- 通过 pid 关联
GROUP BY IFNULL(o.msku, t.SellerSKU)
```

---

## 7. 刊登流程全链路

```
用户操作（前端）
    │
    ├─ 1. 选择授权店铺（amazonAuthId）和站点（marketplaceid）
    │
    ├─ 2. 选择/搜索 Amazon ProductType
    │       → GET /api/v1/product/amzProductType/searchDefinitionsProductTypes
    │       → 调用 SP-API DefinitionsApi.searchDefinitionsProductTypes()
    │
    ├─ 3. 获取该 ProductType 的属性 Schema
    │       → POST /api/v1/product/amzProductType/getProductDefinition
    │       → 调用 SP-API DefinitionsApi.getDefinitionsProductType()
    │       → 返回 JSON Schema（包含 browseNodeId、variationTheme、所有属性）
    │
    ├─ 4. 用户填写属性（标题、描述、关键词、bullet points、尺寸重量等）
    │
    ├─ 5. 提交刊登
    │       → POST /api/v1/report/product/listing/putListingsItem
    │       → ProductListingPushDTO { sku, productType, attributes, marketplaceids }
    │       → 调用 SP-API ListingsApi.putListingsItem()
    │
    └─ 6. 绑定本地 SKU（可选）
            → 在 t_product_in_opt.msku 中设置本地 ERP SKU 映射
```

### 7.1 Feed 刊登（旧方式）

系统同时保留了 Feeds API（XML）方式：
- `FeedFileProductXML`：生成 `POST_PRODUCT_DATA` 类型的 XML，提交 MerchantShippingGroup 等基础信息。
- `FeedFileInventoryXML`：提交库存信息。
- `FeedFileProductPriceXML`：提交价格信息。
- 通过 `ISubmitfeedService` 管理提交队列，结果存入 `t_amz_submitfeed`。

---

## 8. 现有实现的问题与差距

### 8.1 分类映射缺失（关键问题）

| 问题 | 影响 |
|------|------|
| ERP 本地分类（`t_erp_material_category`）与 Amazon ProductType / BrowseNode 之间**没有映射关系** | 每次刊登需要用户手动选择 ProductType，不能根据本地分类自动推荐 |
| Amazon ProductType 缓存在 `t_product_type` 但无层级 | 搜索 ProductType 只能通过关键词，用户体验差 |
| BrowseNode 树（`t_amz_adv_browsenode`）只用于广告，未用于刊登辅助 | 刊登时无法按分类树导航选择 |

### 8.2 属性无本地持久化

- 刊登属性（属性名/属性值）完全依赖 Amazon SP-API 实时获取，没有缓存。
- 不同站点（US/UK/DE）同一 ProductType 的属性 Schema 可能不同，系统未做差异化管理。
- 没有"属性模板"机制，用户每次刊登都需从头填写。

### 8.3 SKU 映射依赖手动操作

- `msku` 字段需要运营人员手动在界面绑定，没有自动匹配规则。
- 当平台 SellerSKU 与本地 SKU 一致时不需要配置，但当不一致时（多店铺、SKU 规则不同）必须手动绑定。

### 8.4 多平台支持现状

目前系统**仅支持亚马逊**一个平台，所有刊登逻辑都针对 Amazon SP-API 设计。没有抽象的"平台适配层"，若要扩展到其他平台（Shopify、速卖通等），需要较大重构。

---

## 9. 最佳实践建议

### 9.1 建立 ERP 分类 ↔ Amazon ProductType 映射表

```sql
CREATE TABLE `t_erp_category_amazon_producttype` (
  `id`              bigint unsigned NOT NULL,
  `erp_categoryid`  bigint unsigned NOT NULL,  -- ERP本地分类ID
  `marketplaceid`   char(15) NOT NULL,          -- Amazon站点
  `product_type`    varchar(255) NOT NULL,      -- Amazon ProductType名称
  `shopid`          bigint unsigned NOT NULL,   -- 租户
  `operator`        bigint unsigned,
  `opttime`         datetime,
  PRIMARY KEY (`id`),
  UNIQUE KEY (`erp_categoryid`, `marketplaceid`, `shopid`)
);
```

**收益：** 用户在 ERP 中维护好"本地分类 → 某站点用哪个 ProductType"，新建刊登时自动预填 ProductType，减少重复操作。

### 9.2 属性模板持久化

```sql
CREATE TABLE `t_amazon_listing_template` (
  `id`            bigint unsigned NOT NULL,
  `shopid`        bigint unsigned,
  `product_type`  varchar(255),     -- ProductType
  `marketplaceid` char(15),         -- 站点
  `name`          varchar(100),     -- 模板名称
  `attributes`    json,             -- 预设属性 JSON
  `operator`      bigint unsigned,
  `opttime`       datetime,
  PRIMARY KEY (`id`)
);
```

**收益：** 用户可以保存常用属性组合为模板（如"耳机标准属性"），刊登时一键应用，只需修改差异字段。

### 9.3 SKU 自动映射规则

在 `t_product_in_opt` 中增加"SKU 映射规则"字段，或增加一张映射规则表，支持基于前缀/后缀/正则的自动映射：

```sql
CREATE TABLE `t_amazon_sku_mapping_rule` (
  `id`           bigint unsigned NOT NULL,
  `shopid`       bigint unsigned,
  `amazonauthid` bigint unsigned,  -- 针对特定店铺的规则
  `rule_type`    char(20),         -- 'exact'/'prefix'/'suffix'/'regex'
  `platform_sku_pattern` varchar(200),  -- 平台SKU模式
  `local_sku_template`   varchar(200),  -- 本地SKU生成模板（支持变量替换）
  `priority`     int,
  PRIMARY KEY (`id`)
);
```

### 9.4 多平台抽象层（长期）

当前所有服务都直接依赖 Amazon SP-API 模型，建议在 `wimoor-api` 中抽象通用 listing 接口：

```java
// wimoor-api/src/main/java/com/wimoor/api/listing/
public interface IPlatformListingService {
    String putListing(PlatformListingDTO dto);         // 创建/更新listing
    String deleteListing(PlatformListingItemDTO dto);  // 删除listing
    Object getProductTypeSchema(String platformCode, String marketplaceId, String productType);
}
```

各平台实现自己的适配器，避免业务代码直接依赖平台 SDK 类型。

---

## 10. 数据流图（SKU 全链路）

```
[ERP本地]                    [Amazon平台]                [库存/财务]
t_erp_material               t_product_info              t_erp_inventory
  sku="ABC-001"  ─────────── sku="ABC-001"(US,店铺A)
  shopid=100       msku映射    sku="ABC-US-001"(US,店铺B)
  categoryid=5                sku="ABC-UK"(UK,店铺A)
                              asin="B0XXXXXX"             ← 由ASIN汇总销量
                              fnsku="X001XXXXX"           ← FBA仓储
                              amazonAuthId=店铺A/B
                                     ↕
                            t_product_in_opt
                              pid → t_product_info.id
                              msku = "ABC-001"  ← 指向ERP本地SKU
                              fnsku (冗余)
```

**关键 SQL 等式：**
```sql
ifnull(opt.msku, product_info.sku) = erp_material.sku
```
这是系统中 ERP SKU ↔ 平台 SKU 映射的核心等式。

---

## 11. 相关源文件索引

| 文件 | 路径 | 说明 |
|------|------|------|
| ERP物料实体 | `wimoor-erp/erp-boot/src/main/.../material/pojo/entity/Material.java` | 本地SKU主体 |
| ERP分类表 | `wimoor-erp/erp-boot/src/main/.../material/mapper/MaterialCategoryMapper.java` | 本地分类 |
| Amazon产品信息 | `wimoor-amazon/amazon-boot/src/main/.../product/pojo/entity/ProductInfo.java` | 平台产品记录 |
| 运营配置（含msku） | `wimoor-amazon/amazon-boot/src/main/.../product/pojo/entity/ProductInOpt.java` | 本地-平台SKU映射 |
| msku绑定接口 | `wimoor-amazon/amazon-boot/src/main/.../product/controller/ProductInOptController.java` | 手动绑定msku |
| Listing推送 | `wimoor-amazon/amazon-boot/src/main/.../product/service/impl/ProductListingsItemServiceImpl.java` | 核心刊登逻辑 |
| ProductType服务 | `wimoor-amazon/amazon-boot/src/main/.../product/service/impl/ProductProductTypeServiceImpl.java` | Amazon分类类型管理 |
| 属性DTO | `wimoor-amazon/amazon-boot/src/main/.../product/pojo/dto/ItemAttributesDTO.java` | 多站点属性构建 |
| Listing推送DTO | `wimoor-amazon/amazon-boot/src/main/.../product/pojo/dto/ProductListingPushDTO.java` | 刊登请求体 |
| DB: ERP分类 | `init-config/mysql/数据库结构/db_erp/t_erp_material_category.sql` | 本地分类DDL |
| DB: Amazon产品 | `init-config/mysql/数据库结构/db_amazon/t_product_info.sql` | 平台产品DDL |
| DB: BrowseNode | `init-config/mysql/数据库结构/db_amazon/t_amz_adv_browsenode.sql` | Amazon分类树DDL |
| Mapper XML | `wimoor-amazon/amazon-boot/src/main/resources/mapper/product/ProductInfoMapper.xml` | msku查询SQL |
