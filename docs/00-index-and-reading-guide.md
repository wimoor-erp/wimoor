# 00. 索引与阅读指南

## 文档目标

这组文档用于回答五个问题：

1. Wimoor 的业务系统由哪些业务域组成。
2. 一个业务请求如何穿过网关、服务、任务、异步和数据库。
3. 各核心业务对象如何进行状态流转。
4. 各模块之间通过什么契约和配置发生协作。
5. 哪些运行态依赖会影响系统的真实执行行为。

## 阅读建议

- 如果目标是快速理解全系统，先阅读 [01-system-overview.md](01-system-overview.md) 和 [02-runtime-topology-and-execution.md](02-runtime-topology-and-execution.md)。
- 如果目标是分析供应链与履约，继续阅读 [03-erp-supply-chain.md](03-erp-supply-chain.md) 和 [04-amazon-fulfillment.md](04-amazon-fulfillment.md)。
- 如果目标是分析报表、任务和运营自动化，阅读 [05-amazon-advertising.md](05-amazon-advertising.md)。
- 如果目标是分析经营闭环和补齐层，阅读 [06-finance-and-quote.md](06-finance-and-quote.md)。
- 如果目标是建立统一状态字典，阅读 [07-state-machines-and-rules.md](07-state-machines-and-rules.md)。
- 如果目标是回溯关键对象、主表和契约，阅读 [08-data-entities-and-contracts.md](08-data-entities-and-contracts.md)。
- 如果目标是完整梳理商品基础资料、商品主数据与平台商品映射，阅读 [09-product-master-data-analysis.md](09-product-master-data-analysis.md)。

## 分析方法

本目录的内容采用以下方法构建：

1. 从顶层聚合与网关路由识别系统边界。
2. 从 Controller 和任务入口识别业务触发点。
3. 从 Service 主干方法识别执行链和副作用。
4. 从 DDL 注释、实体字段和映射函数提取状态机。
5. 从 Feign、配置中心和调度器识别跨模块协作关系。

## 业务主线清单

### 1. 通用运行骨架

- Gateway 统一入口
- Admin 任务中心与 Quartz 调度
- Amazon 启动事件与 SQS 异步消费
- Nacos、Redis、MySQL、Seata 等运行时依赖

### 2. ERP 供应链

- 商品主数据
- 采购
- 库存
- 仓库
- 调库
- 组装
- 其他出入库

### 3. Amazon 履约

- Listing 与商品信息
- 订单抓取与订单变更
- 入库计划 V2
- 货件执行与物流确认
- 异步通知消费

### 4. Amazon 广告

- 广告活动管理
- 报表请求与读取
- 快照请求与处理
- 计划调度与时间任务

### 5. 财务与报价

- 财务凭证
- 财务期间与结账
- 报价单与供应商报价
- 与 Amazon 入库履约的联动

## 图示约定

- `flowchart` 用于表示业务动作的顺序推进。
- `sequenceDiagram` 用于表示跨服务协作。
- `stateDiagram-v2` 用于表示业务对象状态流转。
- 图中的状态名称优先取自 SQL 注释，其次取实体映射或 Service 中的状态名称函数。

## 注意事项

- 文档中的状态机优先以数据库结构为准，因为系统中存在部分 SQL 与 Java 枚举或注释不一致的情况。
- 文档中的权限和调度部分依赖 Redis、Nacos、Quartz 等运行时组件，静态代码只能说明机制，不能完全替代运行态数据。
- 本文档集面向业务执行逻辑分析，不覆盖全部接口参数细节和全部库表字段字典。
