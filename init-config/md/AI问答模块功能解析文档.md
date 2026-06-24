# AI问答模块功能解析文档

## 1. 模块架构

### 1.1 前端架构

**技术栈：**
- Vue 3 + Composition API
- Element Plus UI组件库
- Markdown渲染组件
- Axios网络请求

**核心文件结构：**
```
wimoor-ui/src/views/sys/deepseek/
├── index.vue                  # 主页面组件
└── components/
    ├── MarkdownRenderer.vue    # Markdown渲染组件
    ├── markdownParser.js       # Markdown解析工具
    └── languageParser.js       # 语言解析工具

wimoor-ui/src/api/sys/tool/
└── deepseekApi.js              # API调用接口
```

**前端组件结构：**
- **主页面组件** (`index.vue`)：包含会话管理、消息交互和模式切换功能
- **MarkdownRenderer组件**：用于渲染AI返回的Markdown格式回答
- **markdownParser组件**：解析Markdown内容
- **languageParser组件**：解析代码语言，支持代码高亮

### 1.2 后端架构

**技术栈：**
- Spring Boot
- MyBatis-Plus ORM框架
- RESTful API设计
- OkHttp客户端
- DeepSeek AI API

**核心文件结构：**
```
wimoor-admin/admin-boot/src/main/java/com/wimoor/sys/tool/
├── controller/
│   └── SysDeepSeekController.java     # 控制器
├── service/
│   ├── ISysDeepSeekService.java        # 服务接口
│   ├── IDeepseekChatSessionsService.java # 会话服务接口
│   ├── IDeepseekChatMessagesService.java # 消息服务接口
│   └── impl/
│       ├── SysDeepSeekServiceImpl.java  # 服务实现
│       ├── DeepseekChatSessionsServiceImpl.java # 会话服务实现
│       └── DeepseekChatMessagesServiceImpl.java # 消息服务实现
├── mapper/
│   ├── DeepseekChatSessionsMapper.java  # 会话数据访问接口
│   └── DeepseekChatMessagesMapper.java  # 消息数据访问接口
├── pojo/
│   ├── dto/
│   │   └── SysChartCompletionRequestDTO.java # 请求DTO
│   └── entity/
│       ├── DeepseekChatSessions.java    # 会话实体
│       ├── DeepseekChatMessages.java    # 消息实体
│       └── DeepSeekMessage.java         # 消息传输对象
└── ...
```

**数据库表结构：**
- **t_sys_tool_deepseek_chat_sessions**：存储会话信息
- **t_sys_tool_deepseek_chat_messages**：存储消息历史

## 2. 核心功能实现

### 2.1 前端核心功能实现

#### 2.1.1 会话管理

**实现代码：**
```javascript
function handleAddSession(){
    state.messages = messages.value = [
        {
            message_id: 1,
            role: 'assistant',
            content: '你好！我是AI助手，我可以帮助你解答问题。请问有什么可以帮您的？',
            format_type: 'plain',
            message_type: 'text',
            created_time: new Date()
        }
    ];
    state.sessionid = null;
}

function handleSession(item){
    state.messages = item.messages;
    state.sessionid = item.id;
    nextTick(() => {
        scrollToBottom();
    });
}
```

**技术要点：**
- 新建会话时初始化默认欢迎消息
- 会话切换时加载对应会话的消息历史
- 自动滚动到最新消息，提升用户体验

#### 2.1.2 消息交互

**实现代码：**
```javascript
function handleSubmit(){
    var data = {};
    var messages = [];
    messages.push({"role": "user", "content": state.message});
    data.messages = messages;
    data.model = state.isdeep == 'deepseek-reasoner' ? 'deepseek-reasoner' : "deepseek-chat";
    data.frequencyPenalty = 0;
    data.maxTokens = 4096;
    data.presencePenalty = 0;
    data.responseFormat = {"type": "json_object"};
    data.stop = null;
    data.stream = false;
    data.streamOptions = null;
    data.temperature = 1;
    data.topP = 1;
    data.sessionId = state.sessionid;
    data.tools = null;
    data.toolChoice = "none";
    data.logprobs = false;
    data.topLogprobs = null;
    state.isLoading = true;
    deepseekApi.search(data).then(res => {
        if (res.data) {
            state.response = res.data;
            state.isLoading = false;
            state.messages = state.response.messages;
            state.sessionid = state.response.id;
            state.message = "";
            nextTick(() => {
                scrollToBottom();
            });
        }
    });
}
```

**技术要点：**
- 构建符合DeepSeek API格式的请求数据
- 支持多种参数配置，如maxTokens、temperature等
- 异步发送请求，显示加载状态
- 接收并处理AI返回的回答
- 自动清空输入框，准备下一次输入

#### 2.1.3 Markdown渲染

**实现代码：**
```vue
<template>
    <div class="markdown-renderer">
        <template v-if="showCodeOnly && isCodeBlock(content)">
            <el-code :language="getLanguage(content)" class="code-block">
                {{ getCodeContent(content) }}
            </el-code>
        </template>
        <template v-else>
            <div v-html="renderedContent" class="content"></div>
        </template>
    </div>
</template>
```

**技术要点：**
- 支持纯代码块和混合内容的渲染
- 自动检测代码语言，支持代码高亮
- 使用Element Plus的代码高亮组件

### 2.2 后端核心功能实现

#### 2.2.1 控制器实现

**实现代码：**
```java
@Api(tags = "获取deepseek回答")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/deepseek")
public class SysDeepSeekController {
    final ISysDeepSeekService iSysDeepSeekService;
    @PostMapping("/search")
    public Result<?> searchAction(@RequestBody SysChartCompletionRequestDTO dto)  {
        UserInfo userInfo = UserInfoContext.get();
        return Result.success(iSysDeepSeekService.completions(userInfo, dto));
    }
    @GetMapping("/getSession")
    public Result<?> getSessionAction()  {
        UserInfo userInfo = UserInfoContext.get();
        return Result.success(iSysDeepSeekService.getSession(userInfo));
    }

    @GetMapping("/getKey")
    public Result<?> getKeyAction()  {
        UserInfo userInfo = UserInfoContext.get();
        return Result.success(iSysDeepSeekService.getKey(userInfo));
    }
}
```

**技术要点：**
- 使用`UserInfoContext`获取当前用户信息
- 提供三个核心API接口：搜索、获取会话、获取关键词
- 统一的结果返回格式

#### 2.2.2 服务层实现

**实现代码：**
```java
@Override
public Object completions(UserInfo userInfo, SysChartCompletionRequestDTO dto) {
    try {
        MediaType mediaType = MediaType.parse("application/json");
        ObjectMapper objectMapper = new ObjectMapper();

        // 获取会话历史消息
        List<DeepseekChatMessages> messages = iDeepseekChatMessagesService.lambdaQuery()
                .eq(DeepseekChatMessages::getSessionId, dto.getSessionId())
                .orderBy(true, true, DeepseekChatMessages::getCreatetime)
                .list();
        List<DeepseekChatMessages> newMessages = new ArrayList<DeepseekChatMessages>();
        List<DeepSeekMessage> contentList = new ArrayList<DeepSeekMessage>();
        
        // 构建历史消息列表
        for (DeepseekChatMessages message : messages) {
            DeepSeekMessage e = new DeepSeekMessage();
            e.setContent(message.getContent());
            e.setRole(message.getRole());
            contentList.add(e);
        }
        
        // 添加新消息
        for (DeepSeekMessage message : dto.getMessages()) {
            DeepseekChatMessages e = new DeepseekChatMessages();
            e.setSessionId(dto.getSessionId());
            e.setCreatetime(new Date());
            e.setContent(message.getContent());
            e.setRole(message.getRole());
            newMessages.add(e);
            contentList.add(message);
        }
        dto.setMessages(contentList);
        
        // 构建请求体
        String jsonBody = objectMapper.writeValueAsString(dto);
        RequestBody body = RequestBody.create(mediaType, jsonBody);
        
        // 调用DeepSeek API
        Response response = requestDeepSeek(body);
        if (response == null || !response.isSuccessful() || response.body() == null) {
            throw new BizException("请求失败");
        }
        String result = response.body().string();
        JSONObject resultJson = GeneralUtil.getJsonObject(result);
        String sessionId = resultJson.getString("id");
        JSONArray choices = resultJson.getJSONArray("choices");
        
        // 处理AI回答
        for (int i = 0; i < choices.size(); i++) {
            JSONObject message = choices.getJSONObject(i);
            JSONObject messageInfo = message.getJSONObject("message");
            String content = messageInfo.getString("content");
            String role = messageInfo.getString("role");
            DeepseekChatMessages e = new DeepseekChatMessages();
            e.setSessionId(dto.getSessionId());
            e.setCreatetime(new Date());
            e.setContent(content);
            e.setRole(role);
            newMessages.add(e);
        }
        
        // 处理会话信息
        DeepseekChatSessions session = null;
        if (StrUtil.isBlankOrUndefined(dto.getSessionId())) {
            for (DeepseekChatMessages message : newMessages) {
                message.setSessionId(sessionId);
            }
            session = new DeepseekChatSessions();
            session.setId(sessionId);
            session.setUserid(userInfo.getId());
            session.setOpttime(new Date());
            session.setModel(dto.getModel());
            session.setCreatetime(new Date());
            session.setTitle(dto.getMessages().get(0).getContent());
            this.iDeepseekChatSessionsService.save(session);
        } else {
            resultJson.put("id", dto.getSessionId());
            session = iDeepseekChatSessionsService.getById(dto.getSessionId());
        }

        // 保存消息记录
        iDeepseekChatMessagesService.saveBatch(newMessages);
        if (messages != null) {
            messages.addAll(newMessages);
            session.setMessages(messages);
        } else {
            session.setMessages(newMessages);
        }

        return session;
    } catch (IOException e) {
        throw new RuntimeException(e);
    }
}
```

**技术要点：**
- 构建完整的对话历史，保持上下文连贯性
- 调用DeepSeek API获取AI回答
- 处理会话的创建和更新
- 保存消息历史到数据库
- 构建返回给前端的会话对象

#### 2.2.3 DeepSeek API交互

**实现代码：**
```java
public OkHttpClient getClient() {
    OkHttpClient.Builder builder = new OkHttpClient.Builder();
    builder.connectTimeout(1200, TimeUnit.SECONDS);
    builder.callTimeout(1200, TimeUnit.SECONDS);
    builder.readTimeout(1200, TimeUnit.SECONDS);
    builder.writeTimeout(1200, TimeUnit.SECONDS);
    return builder.build();
}

public Response requestDeepSeek(RequestBody body) throws IOException {
    Request request = new Request.Builder()
            .url("https://api.deepseek.com/chat/completions")
            .method("POST", body)
            .addHeader("Content-Type", "application/json")
            .addHeader("Accept", "application/json")
            .addHeader("Authorization", "Bearer " + token)
            .build();
    OkHttpClient client = getClient();
    return client.newCall(request).execute();
}
```

**技术要点：**
- 配置OkHttpClient，设置较长的超时时间以适应AI处理
- 构建符合DeepSeek API要求的HTTP请求
- 添加必要的请求头，包括Authorization token
- 执行请求并返回响应

## 3. API接口设计

### 3.1 前端API调用

| API方法 | 接口路径 | 请求参数 | 响应数据 | 描述 |
|---------|---------|---------|---------|------|
| `search` | `/admin/api/v1/deepseek/search` | `{messages, model, sessionId, maxTokens, temperature}` | 会话对象 | 发送消息给AI，获取回答 |
| `getSession` | `/admin/api/v1/deepseek/getSession` | 无 | 会话列表 | 获取用户的会话列表 |
| `getKey` | `/admin/api/v1/deepseek/getKey` | 无 | 关键词列表 | 获取常用搜索关键词 |

### 3.2 后端API接口

| 接口路径 | 请求方法 | 功能描述 | 请求体 (JSON) | 响应体 (JSON) |
|---------|---------|---------|--------------|--------------|
| `/api/v1/deepseek/search` | POST | 发送消息给AI | `{"messages": [{"role": "user", "content": "问题"}], "model": "deepseek-chat", "sessionId": "session123"}` | `{"code": 0, "msg": "success", "data": {"id": "session123", "title": "问题标题", "messages": [...]}}` |
| `/api/v1/deepseek/getSession` | GET | 获取会话列表 | N/A | `{"code": 0, "msg": "success", "data": [{"id": "session123", "title": "问题标题", "messages": [...]}]}` |
| `/api/v1/deepseek/getKey` | GET | 获取常用关键词 | N/A | `{"code": 0, "msg": "success", "data": [{"id": "1", "content": "关键词"}]}` |

## 4. 数据模型

### 4.1 前端数据模型

**状态管理：**
```javascript
const state = reactive({
    message: "",
    messages: [],
    sessionid: null,
    search_model: "deepseek-chat",
    search_network: "deepseek-chat",
    isLoading: false,
    sessions: [],
    searchKeys: [],
    response: null,
});
```

**核心数据结构：**
- **message**：当前输入的消息内容
- **messages**：当前会话的消息列表
- **sessionid**：当前会话ID
- **search_model**：当前使用的AI模型
- **isLoading**：加载状态
- **sessions**：会话列表
- **searchKeys**：常用搜索关键词

### 4.2 后端数据模型

**会话实体：**
```java
@TableName(value = "t_sys_tool_deepseek_chat_sessions")
@Data
public class DeepseekChatSessions {
    private String id;
    private String userid;
    private String title;
    private String model;
    private Date createtime;
    private Date opttime;
    @TableField(exist = false)
    private List<DeepseekChatMessages> messages;
}
```

**消息实体：**
```java
@TableName(value = "t_sys_tool_deepseek_chat_messages")
@Data
public class DeepseekChatMessages {
    private String id;
    private String sessionId;
    private String role;
    private String content;
    private Date createtime;
}
```

**请求DTO：**
```java
@Data
public class SysChartCompletionRequestDTO {
    private List<DeepSeekMessage> messages;
    private String model;
    private String sessionId;
    private Integer maxTokens;
    private Double temperature;
    private Double topP;
    private Double frequencyPenalty;
    private Double presencePenalty;
    private List<String> stop;
    private Boolean stream;
    private Object streamOptions;
    private Object responseFormat;
    private Object tools;
    private String toolChoice;
    private Boolean logprobs;
    private Integer topLogprobs;
}
```

**数据库表结构：**

**t_sys_tool_deepseek_chat_sessions表：**
| 字段名 | 数据类型 | 约束 | 描述 |
|-------|---------|------|------|
| `id` | `varchar(50)` | `PRIMARY KEY` | 会话ID |
| `userid` | `varchar(50)` | `NOT NULL` | 用户ID |
| `title` | `varchar(255)` | `NOT NULL` | 会话标题 |
| `model` | `varchar(50)` | `NOT NULL` | 使用的AI模型 |
| `createtime` | `datetime` | `NOT NULL` | 创建时间 |
| `opttime` | `datetime` | `NOT NULL` | 更新时间 |

**t_sys_tool_deepseek_chat_messages表：**
| 字段名 | 数据类型 | 约束 | 描述 |
|-------|---------|------|------|
| `id` | `varchar(50)` | `PRIMARY KEY` | 消息ID |
| `sessionId` | `varchar(50)` | `NOT NULL` | 会话ID |
| `role` | `varchar(20)` | `NOT NULL` | 角色(user/assistant) |
| `content` | `text` | `NOT NULL` | 消息内容 |
| `createtime` | `datetime` | `NOT NULL` | 创建时间 |

## 5. 技术亮点与创新点

### 5.1 前端技术亮点

1. **会话管理**：实现了多会话管理功能，用户可以创建和切换多个对话，便于分类管理不同类型的问题
2. **Markdown渲染**：支持渲染AI返回的Markdown格式回答，包括代码高亮、格式化文本等
3. **响应式设计**：使用Element Plus的响应式布局，适配不同屏幕尺寸
4. **用户体验**：
   - 自动滚动到最新消息
   - 消息发送后自动清空输入框
   - 加载状态显示
   - 常用关键词推荐
5. **模式切换**：支持普通模式和深度搜索模式的切换，满足不同场景的需求

### 5.2 后端技术亮点

1. **AI集成**：成功集成DeepSeek AI API，实现智能问答功能
2. **会话管理**：实现了完整的会话管理功能，包括会话创建、更新和历史记录
3. **上下文保持**：通过构建完整的对话历史，保持AI对话的上下文连贯性
4. **性能优化**：
   - 配置合理的超时时间，适应AI处理的需要
   - 批量保存消息记录，减少数据库操作
   - 高效的会话和消息查询
5. **安全性**：
   - 使用用户上下文获取用户信息，确保数据隔离
   - 合理的错误处理机制
   - 安全的API调用方式

## 6. 安全考虑

### 6.1 前端安全

1. **输入验证**：对用户输入进行基本验证，防止恶意输入
2. **权限控制**：前端路由权限控制，确保只有授权用户可访问
3. **数据传输**：使用HTTPS协议传输数据，加密敏感信息
4. **错误处理**：避免在前端暴露详细错误信息
5. **内容安全**：提醒用户不要输入敏感信息

### 6.2 后端安全

1. **权限验证**：基于UserInfoContext的权限控制，确保用户只能访问自己的会话数据
2. **输入验证**：后端对所有输入参数进行验证，防止恶意输入
3. **API密钥保护**：DeepSeek API token存储在配置文件中，避免硬编码
4. **异常处理**：统一的异常处理机制，避免敏感信息泄露
5. **数据隔离**：通过userid字段确保数据按用户隔离
6. **请求限制**：可以考虑添加请求频率限制，防止滥用AI服务

## 7. 性能优化

### 7.1 前端性能优化

1. **消息渲染**：
   - 使用虚拟列表渲染大量消息，避免DOM节点过多
   - 按需渲染消息内容，减少初始加载时间

2. **数据加载**：
   - 会话列表分页加载
   - 消息历史按需加载

3. **渲染优化**：
   - 使用Vue 3的Composition API，减少组件实例开销
   - 合理使用v-if和v-show，优化条件渲染
   - 避免不必要的计算属性和监听器

### 7.2 后端性能优化

1. **API调用**：
   - 配置合理的连接池参数
   - 缓存常用的AI回答，减少重复请求
   - 批量处理消息记录，减少数据库操作

2. **数据库优化**：
   - 合理的索引设计（sessionId、userid、createtime等字段）
   - 使用MyBatis-Plus的缓存机制
   - 批量操作优化

3. **代码优化**：
   - 减少数据库查询次数
   - 合理使用缓存
   - 异步处理非关键操作

4. **AI调用优化**：
   - 合理设置maxTokens，避免返回过长的回答
   - 根据问题类型选择合适的模型
   - 实现请求重试机制，提高可靠性

## 8. 总结与建议

### 8.1 模块优势

1. **功能完整**：提供了完整的AI问答功能，包括会话管理、消息交互等
2. **用户友好**：直观的界面设计和流畅的用户体验
3. **技术先进**：集成了最新的AI模型，提供智能问答服务
4. **安全可靠**：完善的权限控制和数据验证
5. **性能良好**：优化的API调用和数据处理
6. **扩展性强**：模块化设计，便于后续功能扩展

### 8.2 改进建议

1. **功能扩展**：
   - 添加消息编辑和删除功能
   - 支持消息导出（如PDF、Markdown等格式）
   - 实现消息标签和分类功能
   - 添加AI模型参数自定义设置

2. **性能优化**：
   - 实现前端本地缓存，减少重复请求
   - 后端添加Redis缓存，提高查询性能
   - 优化Markdown渲染性能

3. **用户体验**：
   - 添加消息发送状态指示
   - 实现消息撤回功能
   - 支持表情符号和图片输入
   - 优化移动端适配

4. **安全增强**：
   - 实现内容审核，过滤不良内容
   - 添加操作审计日志
   - 增强输入内容的安全过滤

5. **集成建议**：
   - 与其他模块集成，如记事本、文档管理等
   - 支持从其他模块直接发起AI查询
   - 实现AI助手的个性化设置

### 8.3 未来发展方向

AI问答模块作为智能助手工具，具有很大的发展潜力。未来可以考虑：

1. **多模型支持**：集成多种AI模型，如GPT、Claude等，提供更多选择
2. **领域专精**：针对不同领域（如技术、营销、财务等）训练专门的模型
3. **知识库集成**：整合公司内部知识库，提供更准确的专业回答
4. **语音交互**：添加语音输入和语音合成功能，实现语音交互
5. **智能推荐**：基于用户历史对话，智能推荐相关问题和回答
6. **协作功能**：支持多人共享会话，实现团队协作

通过持续优化和功能扩展，AI问答模块可以成为公司内部的智能助手平台，为提高工作效率和知识管理做出更大贡献。

---

**技术文档版本：** 1.0.0
**更新时间：** 2026-02-03