# 08. 数据实体与跨模块契约

## 8.1 目标

本篇整理系统分析中最常用的数据实体、主表与跨模块契约，为阅读流程文档和状态机文档提供定位索引。

## 8.2 ERP 关键实体

### 采购

- `t_erp_purchase_form`
- `t_erp_purchase_form_entry`
- `t_erp_purchase_form_receive`

### 库存

- `t_erp_inventory`
- `t_erp_inventory_record`

### 调库

- `t_erp_dispatch_form`
- `t_erp_dispatch_form_entry`
- `t_erp_dispatch_form_record`
- `t_erp_dispatch_oversea_form`
- `t_erp_dispatch_oversea_form_entry`

### 组装

- `t_erp_assembly_form`
- `t_erp_assembly_form_entry`
- `t_erp_assembly_from_instock`

## 8.3 Amazon 关键实体

### 商品与 Listing

- `t_product_info`

### 订单

- `t_amz_order_main`
- `t_amz_order_item`

### 入库与货件

- `t_erp_ship_v2_inboundplan`
- `t_erp_ship_v2_inboundshipment`
- `t_erp_ship_v2_inbounditem`
- `t_erp_ship_v2_inboundshipment_item`
- `t_erp_ship_v2_inbound_record`

### 通知

- `t_amz_notifications_subscriptions`

## 8.4 广告关键实体

- `t_amz_adv_report_request`
- `t_amz_adv_report_request_type`
- `t_amz_adv_snapshot`
- `t_amz_adv_schedule_plan`
- `t_amz_adv_schedule_planitem`
- `t_amz_adv_schedule_plandata`

## 8.5 财务关键实体

- `fin_vouchers`
- `fin_voucher_entries`
- `fin_accounting_periods`
- `fin_general_ledger`
- `fin_report_templates`
- `fin_report_items`
- `fin_code_rule`

## 8.6 报价关键实体

- `t_order`
- `t_order_supplier`
- `t_supplier_quotation_price`
- `t_shipment`

## 8.7 跨模块契约清单

### Gateway 到各业务服务

通过 Gateway 的路由规则决定路径前缀到服务实例的映射。这是所有服务的统一入口契约。

### Admin 到业务服务

通过 `t_sys_quartz_task` + Quartz + HTTP 回调调用业务接口。这是任务驱动执行的统一契约。

### Amazon 到 ERP

通过 ERP Feign 契约调用库存、物料或供应链相关能力，是入库计划和货件执行的重要边界。

### Amazon 到 Quote

通过 Quote Feign 契约写入询价请求，是货件报价流程的入口边界。

### Finance 到 Amazon

通过 Amazon 组织与站点相关接口获得核算边界信息。

## 8.8 实体阅读建议

- 如果分析库存副作用，优先同时阅读单据主表和 `t_erp_inventory_record`。
- 如果分析 Amazon 入库链，优先同时阅读计划表、货件表和状态轨迹表。
- 如果分析广告任务，优先同时阅读主请求表和 Mapper 中的筛选 SQL。
- 如果分析财务闭环，优先同时阅读凭证表、分录表和期间表。

## 8.9 文档结论

Wimoor 的完整业务执行逻辑，最终都可以回溯到三类证据：

1. 业务控制器和核心服务中的动作入口。
2. DDL 中的状态与字段定义。
3. 跨模块契约和运行态配置中的边界规则。

因此，后续如需继续深化某条主线，最有效的方法是沿着“入口 -> 状态主体 -> 副作用 -> 跨模块契约”的顺序继续展开。
