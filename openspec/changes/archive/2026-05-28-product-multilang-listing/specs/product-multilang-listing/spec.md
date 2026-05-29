## ADDED Requirements

### Requirement: Multi-language Listing data storage

系统 SHALL 为每个 ERP 产品（`t_erp_material`）提供按语言独立存储 Listing 标题和 HTML 描述的能力。每条记录由 `(materialid, lang)` 唯一标识，支持的语言包括：en、de、fr、es、it、nl、pl、sv、tr、ja、ru。

#### Scenario: Create listing for a new language
- **WHEN** 用户为一个产品首次保存某语言的 标题描述（如 lang=en, title="2m stainless steel tape measure", description="<p>Compact and Portable...</p>"）
- **THEN** 系统创建一条新记录，包含 materialid、lang、title、description、shopid、create_time

#### Scenario: Update existing listing
- **WHEN** 用户修改已存在的某语言 标题描述并保存
- **THEN** 系统更新对应记录的 title 和 description 字段，更新 update_time

#### Scenario: Unique constraint enforcement
- **WHEN** 尝试为同一产品的同一语言创建第二条记录
- **THEN** 系统执行 UPSERT（INSERT ON DUPLICATE KEY UPDATE），而非报错

### Requirement: Query listing information

系统 SHALL 提供按产品查询所有语言 Listing 和按产品+语言查询单条 Listing 的 API。

#### Scenario: Query all languages for a product
- **WHEN** 前端请求 `GET /erp/api/v1/material/listing/list?materialid={id}`
- **THEN** 系统返回该产品所有语言的 Listing 记录列表（不含 description 全文，仅返回 id、lang、title、update_time）

#### Scenario: Query single language listing
- **WHEN** 前端请求 `GET /erp/api/v1/material/listing/get?materialid={id}&lang={lang}`
- **THEN** 系统返回该产品指定语言的完整 Listing 记录（包含 description HTML 全文）

#### Scenario: Product has no listing data
- **WHEN** 前端查询一个尚未配置任何 Listing 的产品
- **THEN** 系统返回空列表（list 接口）或 null（get 接口），HTTP 200

### Requirement: Delete listing record

系统 SHALL 允许删除指定的 Listing 记录。

#### Scenario: Delete a specific language listing
- **WHEN** 用户删除某产品的某语言 Listing 记录（DELETE /erp/api/v1/material/listing/delete?id={id}）
- **THEN** 系统物理删除对应记录并返回成功

#### Scenario: Delete non-existent record
- **WHEN** 删除一条不存在的记录
- **THEN** 系统返回成功（幂等）

### Requirement: HTML content security

系统 SHALL 对保存的 HTML description 内容进行 XSS 安全过滤。

#### Scenario: Script tag removal
- **WHEN** 用户提交的 description 包含 `<script>alert('xss')</script>`
- **THEN** 系统在存储前移除所有 `<script>` 标签及其内容

#### Scenario: Event handler removal
- **WHEN** 用户提交的 description 包含 `<div onclick="alert(1)">text</div>`
- **THEN** 系统在存储前移除所有 `on*` 事件处理属性

#### Scenario: Safe HTML preserved
- **WHEN** 用户提交的 description 仅包含安全标签（p、h1-h6、ul、ol、li、table、tr、td、th、img、a、br、strong、em、span、div）和安全属性（style、class、src、href、alt、width、height）
- **THEN** 系统保留这些内容不做修改

### Requirement: Frontend listing tab in edit page

系统 SHALL 在产品编辑页新增"标题描述"选项卡，提供多语言标题和 HTML 描述的编辑能力。

#### Scenario: Tab visibility
- **WHEN** 用户打开任意产品的编辑页（已保存的产品，有 materialid）
- **THEN** 左侧 tab 列表中显示"标题描述"选项，位于"物流信息"/"辅料关联"之后、"图片视频"之前

#### Scenario: Language switching
- **WHEN** 用户在 标题描述区域切换语言 tab（如从"英文"切换到"德语"）
- **THEN** 系统加载对应语言的 title 和 description（如无数据则显示空表单）

#### Scenario: Save listing from edit page
- **WHEN** 用户在某语言 tab 下编辑标题和描述后点击保存
- **THEN** 系统调用 `POST /erp/api/v1/material/listing/save` 保存当前语言数据，保存成功后显示成功提示

#### Scenario: Title length validation
- **WHEN** 用户输入超过 500 字符的标题
- **THEN** 前端阻止提交并提示标题长度超限

### Requirement: Frontend listing tab in detail page

系统 SHALL 在产品详情页新增"标题描述"展示区域，以只读方式展示多语言标题和 HTML 描述。

#### Scenario: Detail tab visibility
- **WHEN** 用户打开任意产品的详情页
- **THEN** 左侧 tab 列表中显示"标题描述"选项

#### Scenario: View listing content
- **WHEN** 用户在详情页滚动到 标题描述区域并选择某语言
- **THEN** 系统展示该语言的标题（纯文本）和描述（HTML 渲染展示）

#### Scenario: No listing data display
- **WHEN** 产品没有任何 Listing 数据
- **THEN** 区域显示"暂无 标题描述"的空状态提示

### Requirement: Permission control

系统 SHALL 对 标题描述的编辑操作进行权限控制。

#### Scenario: User with edit permission
- **WHEN** 具有 `erp:material:listing:edit` 权限的用户访问编辑页
- **THEN** 标题描述 tab 显示编辑表单和保存按钮

#### Scenario: User without edit permission
- **WHEN** 不具有 `erp:material:listing:edit` 权限的用户访问编辑页
- **THEN** 标题描述 tab 的保存按钮不显示（或 tab 仅展示只读内容）
