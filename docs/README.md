# Wimoor 业务执行逻辑分析文档

本目录用于从业务视角说明 Wimoor 系统的完整执行逻辑，包括系统架构、运行骨架、核心业务流程、状态机、跨模块调用和关键数据实体。

## 阅读顺序

1. [00-index-and-reading-guide.md](00-index-and-reading-guide.md)
2. [01-system-overview.md](01-system-overview.md)
3. [02-runtime-topology-and-execution.md](02-runtime-topology-and-execution.md)
4. [03-erp-supply-chain.md](03-erp-supply-chain.md)
5. [04-amazon-fulfillment.md](04-amazon-fulfillment.md)
6. [05-amazon-advertising.md](05-amazon-advertising.md)
7. [06-finance-and-quote.md](06-finance-and-quote.md)
8. [07-state-machines-and-rules.md](07-state-machines-and-rules.md)
9. [08-data-entities-and-contracts.md](08-data-entities-and-contracts.md)

## 文档范围

- 系统总体业务边界与模块职责
- 请求驱动、任务驱动、事件驱动三类执行模型
- ERP、Amazon 履约、Amazon 广告、财务、报价五条业务主线
- 核心业务对象及其状态流转
- 关键跨模块调用与运行时依赖
- Mermaid 图表达的流程图、时序图、状态图

## 使用说明

- 本目录优先表达业务执行逻辑，而不是逐个代码包说明。
- 每篇文档都尽量回溯到真实代码、配置和 SQL 结构。
- 涉及运行态配置的部分会显式标注依赖 Nacos、Redis、Quartz 或其他外部基础设施。
