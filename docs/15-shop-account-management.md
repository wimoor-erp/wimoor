# 店铺账号管理分析

> 分析日期：2026-05-28  
> 涉及模块：wimoor-admin / wimoor-amazon / wimoor-amazon-adv / wimoor-erp / wimoor-modules/wimoor-finance / wimoor-gateway

---

## 1. 概念层次

Wimoor 的"店铺账号"在代码中由三个层次的概念组成，彼此通过外键关联：

```
租户（t_shop / SysCompany）
  └─── 亚马逊店铺组（t_amazon_group / AmazonGroup）
         └─── 卖家授权（t_amazon_auth / AmazonAuthority）
                └─── 卖家实际站点绑定（t_amazonseller_market / AmazonSellerMarket）
```

| 层次 | 数据库表 | Java 实体 | 核心标识 |
|------|----------|-----------|---------|
| 租户 | `db_admin.t_shop` | `SysCompany` | `id`（即全系统 `shopid` / `companyid`） |
| 店铺组 | `db_amazon.t_amazon_group` | `AmazonGroup` | `id`（即 `groupid`） |
| 卖家授权 | `db_amazon.t_amazon_auth` | `AmazonAuthority` | `id`（即 `amazonauthid`） |
| 实际站点 | `db_amazon.t_amazonseller_market` | `AmazonSellerMarket` | `(sellerid, marketplace_id)` |
| 站点主数据 | `db_amazon.t_marketplace` / `db_erp.t_marketplace` | `Marketplace` | `marketplaceid` |

---

## 2. 核心表结构

### 2.1 `t_shop`（租户表，映射 `SysCompany`）

位于 `db_admin` 库。在代码中被称为"公司"（company），也叫"租户"，其 `id` 在全系统以 `shopid` 或 `companyid` 两个别名传播。

| 字段 | 说明 |
|------|------|
| `id` | 主键，BigInteger，全系统 `shopid/companyid` |
| `name` | 公司/租户名称 |
| `remark` | 备注 |
| `invitecode` | 邀请码（7位唯一） |
| `fromcode` | 受邀请码 |
| `boss_email` | 老板邮箱 |

### 2.2 `t_amazon_group`（亚马逊店铺组，映射 `AmazonGroup`）

位于 `db_amazon` 库。一个租户可以有多个"店铺组"，代表系统里的"店铺"概念（在前端叫"店铺"）。

| 字段 | 说明 |
|------|------|
| `id` | 主键，BigInteger，全系统称 `groupid` |
| `shopid` | 外键 → `t_shop.id`，所属租户 |
| `name` | 店铺名称（在同一 shopid 下唯一） |
| `company` | 店铺对应公司主体名称 |
| `profitcfgid` | 默认利润计算方案 ID |
| `isfinance` | 是否开启财务账套（影响是否初始化财务科目） |
| `tax_number` | 税号 |
| `findex` | 显示排序号 |
| `operator` / `opttime` | 最后操作人/时间 |
| `creator` / `createtime` | 创建人/时间 |
| `isdelete` | 逻辑删除标记 |

### 2.3 `t_amazon_auth`（卖家 SP-API 授权，映射 `AmazonAuthority`）

位于 `db_amazon` 库。一个店铺组（groupid）可以绑定多个区域（region：NA/EU/JP/IN/AU/SG 等）的卖家授权，每个授权对应一个 Amazon SellerID。

| 字段 | 说明 |
|------|------|
| `id` | 主键，BigInteger，全系统称 `amazonauthid` |
| `shop_id` | 外键 → `t_shop.id` |
| `groupid` | 外键 → `t_amazon_group.id` |
| `sellerid` | Amazon 卖家 ID（Seller ID，全局唯一） |
| `region` | 区域代码（NA/EU/JP/IN/AU/SG 等） |
| `aws_region` | AWS 区域（us-east-1/eu-west-1/us-west-2） |
| `refresh_token` | SP-API Refresh Token（敏感字段） |
| `refresh_token_time` | Refresh Token 获取时间 |
| `client_id` | LWA Application Client ID |
| `client_secret` | LWA Application Client Secret |
| `proxy_ip` / `proxy_port` | 代理服务器 |
| `disable` | 是否禁用 |
| `status` / `statusupdate` | 授权状态/更新时间 |
| `MWSAuthToken` | 旧版 MWS Token（历史字段） |
| `pictureId` | 店铺图标 |

### 2.4 `t_amazonseller_market`（卖家实际站点，映射 `AmazonSellerMarket`）

位于 `db_amazon` 库。记录一个卖家 SellerID 实际在哪些站点（Marketplace）经营。

| 字段 | 说明 |
|------|------|
| `sellerid` | 联合主键，卖家 ID |
| `marketplace_id` | 联合主键，站点 ID |
| `country` | 国家代码（如 US/DE/JP） |
| `name` | 站点英文名称 |
| `currency` | 对应币种 |
| `domain` | 站点域名 |
| `amazonauthid` | 外键 → `t_amazon_auth.id` |
| `disable` | 是否对系统隐藏该站点 |

### 2.5 `t_marketplace`（站点主数据）

同时存在于 `db_amazon` 和 `db_erp` 中（数据相同）。存储 Amazon 所有全球站点的静态配置，如 API 端点、汇率单位、区域归属等。

### 2.6 广告专用授权：`t_amz_adv_auth`（`AmzAdvAuth`）

位于 `db_amazon_adv` 库。亚马逊广告 API 使用独立的 OAuth 授权，与 SP-API 分离。

| 字段 | 说明 |
|------|------|
| `id` | 主键 |
| `groupid` | 外键 → `t_amazon_group.id` |
| `shopid` | 外键 → `t_shop.id` |
| `region` | 区域代码 |
| `refresh_token` / `access_token` | 广告 API Token |
| `disable` | 是否禁用 |

---

## 3. 权限与用户关联

### 3.1 用户 → 租户的绑定

```
t_user (用户)
  └── t_user_shop (用户店铺归属，UNIQUE shop_id)
        └── t_shop.id (租户)
```

`t_user_shop` 中 `shop_id` 设有唯一索引，说明一个租户（`t_shop`）只允许有一个"老板账号"（`admin`/`manager` 用户类型）直接拥有它。普通子用户通过 `t_sys_user_group` 获得对某些店铺组（`AmazonGroup`）的访问权。

### 3.2 角色 → 店铺组/站点的授权

| 表 | 用途 |
|----|------|
| `t_sys_user_group` | 普通用户被授权访问哪些店铺组（groupid） |
| `t_role_group` | 角色绑定哪些店铺组 |
| `t_role_marketplace` | 角色绑定哪些站点 |

### 3.3 `UserInfo` 上下文传播

登录时 `SysUserServiceImpl` 将 shopid（= `t_shop.id`）、groups（被授权的 groupid 列表）、roles 等写入 `UserInfo` 对象，存入 Redis（`login_tokens:<jsessionid>`）。

Gateway 的 `SecurityGlobalFilter` 从 Redis 读取后，将整个 `UserInfo` JSON URL-编码后写入请求头 `X-USERINFO`，下游业务服务通过 `UserInfoContext.get()` 反序列化获取：

```
jsessionid → Redis → UserInfo
  ├── id (用户ID)
  ├── companyid (租户shopid)
  ├── roles (角色列表)
  ├── groups (可访问的groupid列表)
  └── datalimits (数据权限类型)
```

---

## 4. 套餐限制（`t_manager_limit`）

每个租户对应一条 `ManagerLimit` 记录，控制该租户的资源上限：

| 字段 | 含义 |
|------|------|
| `shopId` | 外键 → `t_shop.id` |
| `maxShopCount` | 可绑定的店铺组（AmazonGroup）数量上限 |
| `maxMarketCount` | 可绑定的站点数量上限 |
| `maxProductCount` | 商品数量上限 |
| `maxOrderCount` | 处理订单数量上限 |
| `maxMember` | 子用户数量上限 |
| `maxProfitPlanCount` | 利润计算方案数量上限 |
| `maxdayOpenAdvCount` | 每天可开启的广告组数量上限 |
| `losingEffect` | 套餐有效期 |
| `tariffpackage` | 套餐等级（0基础/1标准/2专业/3独享/4自定义） |

---

## 5. 店铺信息在各业务模块的使用

### 5.1 Amazon 产品（wimoor-amazon）

- `shopid`（来自 `userinfo.getCompanyid()`）用于：
  - **Listing 管理**：`MaterialListing.shopid` 区隔租户产品
  - **商品 Catalog 查询**：按 `shopid` 过滤 `selectByShopAndMarket(shopid, marketplaceid)`
  - **产品销售分析**：`ProductInfoController` 按 shopid 查询

- `groupid`（店铺组 ID）用于：
  - 获取对应卖家授权（`selectByGroupAndMarket(groupid, marketplaceid)`）
  - 确定默认利润计算方案（`profitcfgid`）
  - 产品优化任务、广告数据下钻

### 5.2 Amazon 订单（wimoor-amazon）

- `AmazonOrdersDTO.groupid`：订单查询必须指定店铺组
- `AmazonOrdersDTO.shopid`：汇总统计时区分租户
- `OrdersFulfillmentDTO.groupid/shopid`：FBM 自发货订单处理
- `AmzOrdersInvoice.groupid`：发票记录绑定店铺组（用于欧洲 VAT 发票）

### 5.3 FBA 入库/发货（wimoor-erp + wimoor-amazon）

- `ShipInboundPlan.shopid`：入库计划所属租户
- `ShipInboundPlan.amazongroupid`：入库计划所属店铺组
- `ShipAddress.groupid/shopid`：发货地址按店铺组区分
- `AmzInboundFbaCycle.shopid`：FBA 仓储周期追踪

### 5.4 Amazon 财务（wimoor-amazon）

- `AmzFinAccount.groupid`：财务账户绑定店铺组
- `AmzSettlementAccStatement.shopid/groupid`：结算对账单归属
- `AmzSettlementSummarySkuMonth.groupid`：按店铺组汇总 SKU 月度销售
- `AmzFinSettlementFormula.shopid`：结算公式按租户定制

### 5.5 ERP 物料/采购（wimoor-erp）

- `MaterialCategory.shopid`：商品类目按租户区隔
- `MaterialListing.shopid`：ERP 中产品的归属租户
- `PurchaseForm.groupid`：采购单关联店铺组（用于确定目标 Amazon 店铺）
- `PurchasePlanItem.groupid`：采购计划项关联店铺组

### 5.6 Amazon 广告（wimoor-amazon-adv）

- `AmzAdvAuth.groupid/shopid`：广告授权关联店铺组和租户
- `getTypeNumber(shopid)` / `getEnableNumber(shopid)`：广告类型/开启数量按租户统计

### 5.7 汇总报表（SummaryOrderReportServiceImpl）

- 按 `amazonauthid` 遍历全部有效授权（`getAllAuth(region)`），拉取 Amazon 报告
- 通过 `auth.getShopId()` 映射回租户，汇总到 `shopid` 维度
- 利润计算：先按 groupid 找默认 `profitcfgid`，再找 `ProfitConfig`

---

## 6. 前端店铺管理（storeAuth 页面）

**路径**：`wimoorui/src/views/amazon/storeAuth/`

### 6.1 页面结构

```
index.vue         ← 左侧：店铺组列表（增删改排序）+ 右侧：授权列表
  └── table.vue   ← 右侧：每个店铺组下的卖家授权、广告授权、站点状态
        ├── authstore.vue   ← 店铺 SP-API 授权对话框
        ├── authadv.vue     ← 广告 API 授权对话框
        ├── taskData.vue    ← 报表拉取进度弹窗
        └── advReport.vue   ← 广告报告申请弹窗
```

### 6.2 关键 API 调用

| 操作 | 前端 API 文件 | 后端接口 |
|------|-------------|---------|
| 获取店铺组列表 | `groupApi.getAmazongroupList()` | `GET /amazon/api/v1/amzgroup/list` |
| 保存/更新店铺组 | `groupApi.AmazonGroupSave(form)` | `PUT /amazon/api/v1/amzgroup/save` |
| 删除店铺组 | `groupApi.deleteAmazongroup(id)` | `DELETE /amazon/api/v1/amzgroup/delete/{id}` |
| 更新店铺组排序/配置 | `groupApi.updateBatch(groups)` | `POST /amazon/api/v1/amzgroup/updateBatch` |
| 获取授权列表（含站点） | `authApi.getBindSeller()` | `GET /amazon/api/v1/amzauthority/getBindSeller` |
| 获取 SP-API 授权 URL | `authApi.getAuthUrl({groupid, marketplaceid})` | `GET /amazon/api/v1/amzauthority/getAuthUrl` |
| SP-API 授权回调 | `authApi.authSeller({state,...})` | `GET /amazon/api/v1/amzauthority/authSeller` |
| 手动保存授权 | `authApi.saveAuth(auth)` | `POST /amazon/api/v1/amzauthority/saveAuth` |
| 停用授权 | `authApi.deleteByLogic({sellerid})` | `GET /amazon/api/v1/amzauthority/deleteByLogic` |
| 刷新站点列表 | `authApi.refreshMarketByAuth({id})` | `GET /amazon/api/v1/amazonSellerMarket/refreshMarketByAuth` |
| 站点启停 | `authApi.statusChange({sellerid, marketplaceid, disable})` | `GET /amazon/api/v1/amazonSellerMarket/statusChange` |

### 6.3 全局店铺/站点选择器组件

`wimoorui/src/components/header/group.vue` 是全系统公用的店铺+国家联动下拉组件，被所有列表页面复用：

1. 初始化时调用 `getAmazonGroup()` 获取当前用户有权限的店铺组列表
2. 切换店铺组后，调用 `getMarketByGroup({groupid})` 加载对应站点
3. 将 `{groupid, marketplaceid}` 作为参数 `emit("change", data)` 传递给父页面刷新数据

---

## 7. 完整业务关系流程

### 7.1 注册与初始化

```
用户注册（SysUserServiceImpl.register）
  │
  ├─ 创建 t_user 记录
  ├─ 创建 t_userinfo（姓名、邮箱等扩展信息）
  ├─ 创建 t_shop（租户，即公司）
  ├─ 创建 t_user_shop（用户 ↔ 租户绑定，UNIQUE）
  ├─ 创建 t_manager_limit（套餐限制，绑定到租户）
  └─ 分配默认角色（t_user_role）
```

### 7.2 店铺组管理（前端"店铺账号列表"）

```
管理员在 storeAuth 页面创建店铺组
  │
  ├─ PUT /amazon/api/v1/amzgroup/save
  ├─ AmazonGroupController.saveAmazonGroupAction
  │   └─ group.shopid = userinfo.getCompanyid()  ← 自动绑定当前租户
  └─ 存入 t_amazon_group（shopid, name, profitcfgid, isfinance 等）
```

### 7.3 SP-API 授权流程

```
方式一：OAuth 重定向授权
  │
  ├─ getAuthUrl(groupid, marketplaceid)
  │   └─ 拼接 Amazon 授权 URL，state = "groupid@aws_region"
  │
  ├─ 用户跳转至 Amazon Seller Central 授权
  │
  ├─ Amazon 回调 authSeller(state, selling_partner_id, spapi_oauth_code)
  │   ├─ 解析 state 得到 groupid + aws_region
  │   ├─ 用 spapi_oauth_code 换取 refresh_token
  │   ├─ 从 t_amazon_group.shopid 获取租户 shopid
  │   ├─ 存入/更新 t_amazon_auth
  │   ├─ 调用 refreshMarketByAuth → 更新 t_amazonseller_market（从 SP-API 拉取实际站点）
  │   └─ 主动拉取初始报告（OrdersByOrderDateReport / ProductListings / InventoryReport）
  │
方式二：手动填写 Token（saveAuth）
  │
  ├─ POST /amazon/api/v1/amzauthority/saveAuth
  ├─ 校验 sellerid / client_id / client_secret / refresh_token
  ├─ 存入/更新 t_amazon_auth
  └─ 同上 refreshMarketByAuth + 初始报告
```

### 7.4 广告授权流程（独立）

```
管理员在 storeAuth 页面点击"广告授权"
  │
  ├─ 跳转至 Amazon Advertising 授权页面
  ├─ 回调时存入 t_amz_adv_auth（groupid, shopid, region, refresh_token）
  └─ 后续定时任务用 refresh_token 换 access_token 调用广告 API
```

### 7.5 子用户对店铺的权限绑定

```
管理员添加子用户时
  │
  ├─ 在 t_user 创建子用户（deptid, ftype=normal）
  ├─ 分配角色（t_user_role）
  ├─ 绑定可访问的店铺组（t_sys_user_group: userid → groupid）
  └─ 绑定数据权限（t_sys_user_datalimit）

子用户登录后：
  UserInfo.companyid = 租户 shopid
  UserInfo.groups    = 被授权的 groupid 列表（若为空则默认可访问同租户所有店铺组）
```

### 7.6 业务数据查询时的店铺过滤链路

```
前端请求携带 jsessionid
  │
  Gateway SecurityGlobalFilter
    ├─ 从 Redis 读取 UserInfo（含 companyid, groups）
    └─ 写入请求头 X-USERINFO
           │
  业务服务（如 wimoor-amazon）
    ├─ UserInfoContext.get() 获取 UserInfo
    │
    ├─ 方式 A（按租户过滤）：
    │   userinfo.getCompanyid() → 查询 t_amazon_group.shopid = ?
    │
    └─ 方式 B（按授权的店铺组过滤）：
        userinfo.getGroups() → 直接限定 groupid IN (...)
```

### 7.7 批处理任务的店铺遍历

```
定时任务（Quartz）触发
  │
  AmazonAuthorityService.executTask(apiService)
    ├─ getAllAuth(region) → 查询所有 disable=false 的 t_amazon_auth
    └─ 对每条授权启动线程：apiService.runApi(auth)
         ├─ auth.getId()    → amazonauthid，用于 API 调用
         ├─ auth.getShopId() → 结果写入时区分租户
         └─ auth.getGroupid() → 细化到店铺组维度
```

---

## 8. 关键约束与注意事项

1. **shopid = companyid 是同一个值**：`t_shop.id` 在不同代码中被称为 `shopid`（Amazon 侧）或 `companyid`（Admin 侧），含义完全相同。

2. **一个租户可有多个店铺组（AmazonGroup）**：用于区分不同品牌、不同运营主体的 Amazon 账号。

3. **一个店铺组可以有多个区域授权（AmazonAuthority）**：如同一品牌在 NA + EU 同时经营。

4. **一个授权（sellerid）对应多个站点**：通过 `t_amazonseller_market` 记录具体经营的 Marketplace。

5. **广告授权与卖家授权独立**：`t_amz_adv_auth` 和 `t_amazon_auth` 是两套独立的 OAuth Token，需要分别授权。

6. **isfinance 标记触发财务账套初始化**：当 `AmazonGroup.isfinance = true` 时，前端会同步调用 `initFinAccountingSubjects(groupid)` 初始化财务科目。

7. **子用户权限两级控制**：
   - `t_sys_user_group` 控制可操作哪些 AmazonGroup（店铺组级别）
   - `t_role_marketplace` 控制可访问哪些 Marketplace（站点级别）

8. **站点可单独禁用**：`t_amazonseller_market.disable = true` 可将某个站点从系统列表隐藏，不影响授权本身。

9. **缓存说明**：`AmazonAuthority` 查询有 Redis 缓存（`AmazonAuthorityCache#6000`），`t_amazon_group` 相关查询有 `defaultProfitCfgCache` 和 `profitCfgCache` 缓存。写操作时需 `@CacheEvict` 清除。

---

## 9. 数据流全景图

```
注册
  └→ t_shop (租户) ←─────────────────────────── companyid 全系统传播
        └→ t_user_shop (用户绑定)
        └→ t_manager_limit (套餐限制)
        │
        └→ t_amazon_group (店铺组, groupid)
              ├→ t_role_group (角色店铺权限)
              ├→ t_sys_user_group (子用户店铺权限)
              ├→ t_amz_adv_auth (广告授权)
              │
              └→ t_amazon_auth (SP-API 授权, amazonauthid)
                    ├→ t_amazonseller_market (实际站点)
                    │     └→ t_marketplace (站点主数据)
                    ├→ t_amazon_auth_market_performance (账户表现)
                    │
                    │  以 amazonauthid 为主键的业务数据：
                    ├→ t_amz_report_request (报告请求记录)
                    ├→ t_ship_inboundplan (FBA 入库计划)
                    └→ t_amz_orders_* (订单数据)
                    
              以 groupid 为维度的业务数据：
              ├→ t_erp_purchase_form (采购单)
              ├→ t_amz_fin_account (财务账户)
              ├→ t_amz_settlement_acc_statement (结算对账)
              └→ t_amz_settlement_summary_sku_month (SKU 月度汇总)
              
        以 shopid 为维度的业务数据：
        ├→ t_material_category (商品类目)
        ├→ t_amz_declare_rate (申报汇率)
        ├→ t_amz_fin_settlement_formula (结算公式)
        └→ t_amz_sum_* (订单汇总统计)
```
