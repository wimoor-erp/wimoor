
<template>
  <div class="deepseek-app">
    <el-row v-if="innerType=='deepseek'" class="deepseek-row">
      <!-- 左侧会话列表 -->
      <el-col :span="4" class="sidebar-left">
        <div class="sidebar-header">
          <el-button type="primary" class="new-session-btn" @click="handleAddSession()">
            <el-icon><Plus /></el-icon>
            <span>新建会话</span>
          </el-button>
        </div>
        <el-scrollbar height="calc(100vh - 160px)" class="session-scrollbar">
          <div class="session-list">
            <div v-if="sessions.length === 0" class="empty-state">
              <div class="empty-icon">💬</div>
              <div class="empty-text">暂无会话</div>
              <div class="empty-subtitle">点击上方按钮创建新会话</div>
            </div>
            <div
              v-for="item in sessions"
              :key="item.id"
              class="session-item"
              :class="{ 'active': sessionid === item.id }"
              @click="handleSession(item)"
            >
              <div class="session-item-content">
                <div class="session-title">{{ item.title || '新会话' }}</div>
              </div>
              <div class="session-item-actions">
                <el-button 
                  type="danger" 
                  size="small" 
                  circle 
                  @click.stop="handleDeleteSession(item)"
                  title="删除会话"
                >
                  <el-icon><Delete /></el-icon>
                </el-button>
              </div>
            </div>
          </div>
        </el-scrollbar>
      </el-col>

      <!-- 中间聊天区域 -->
      <el-col :span="16" class="chat-main">
        <div ref="messagesWrapperRef" class="messages-wrapper">
          <template v-for="(item, index) in messages" :key="item.id || item.message_id || index">
            <div class="message-wrapper" :class="item.role=='user' ? 'user-message' : 'assistant-message'">
              <div class="message-avatar">
                <div v-if="item.role=='user'" class="avatar user-avatar">你</div>
                <div v-else class="avatar ai-avatar">AI</div>
              </div>
              <div class="message-content">
                <!-- 思考过程展示 -->
                <div v-if="item.reasoning" class="reasoning-block">
                  <div class="reasoning-header" @click="item.showReasoning = !item.showReasoning">
                    <span class="reasoning-icon">💭</span>
                    <span class="reasoning-title">{{ item.isThinking ? '正在思考...' : '思考完成' }}</span>
                    <span class="reasoning-toggle">{{ item.showReasoning ? '▼' : '▶' }}</span>
                  </div>
                  <div v-if="item.showReasoning" class="reasoning-content">
                    <MarkdownRenderer :content="item.reasoning" :showCodeOnly="true"></MarkdownRenderer>
                  </div>
                </div>
                <!-- 正式内容 -->
                <div class="message-bubble" :class="item.role=='user' ? 'user-bubble' : 'ai-bubble'">
                  <MarkdownRenderer :content="item.content" :showCodeOnly="true"></MarkdownRenderer>
                  <span v-if="item.isStreaming && !item.isThinking" class="streaming-cursor">|</span>
                </div>
              </div>
            </div>
          </template>
          <!-- 流式消息占位 -->
          <div id="streaming-container" class="streaming-container"></div>
        </div>

        <!-- 输入区域 -->
        <div class="input-area">
          <div class="input-wrapper">
            <div class="textarea-container">
              <textarea
                v-model="message"
                placeholder="输入消息... (Enter发送，Shift+Enter换行)"
                @keydown.enter.exact="handleSubmit"
                rows="3"
                ref="textareaRef"
                class="message-textarea"
              />
            </div>
            <div class="input-actions">
              <div class="model-select">
                <el-select size="small" v-model="search_model" placeholder="选择模型" class="model-dropdown">
                  <el-option label="qwen-turbo" value="qwen-turbo">通义千问-Turbo (最快)</el-option>
                  <el-option label="qwen-plus" value="qwen-plus">通义千问-Plus (均衡)</el-option>
                  <el-option label="qwen-max" value="qwen-max">通义千问-Max (最强)</el-option>
                  <el-option label="deepseek-v3.2" value="deepseek-v3.2">DeepSeek-阿里云</el-option>
                  <el-option label="siliconflow/deepseek-v3.2" value="siliconflow/deepseek-v3.2">DeepSeek-硅基流动</el-option>
                  <el-option label="vanchin/deepseek-v3.2-think" value="vanchin/deepseek-v3.2-think">DeepSeek-快手万擎</el-option>
                  <el-option label="kimi-k2-thinking" value="kimi-k2-thinking">Kimi-阿里云</el-option>
                  <el-option label="kimi/kimi-k2.5" value="kimi/kimi-k2.5">Kimi-月之暗面</el-option>
                  <el-option label="MiniMax-M2.5" value="MiniMax-M2.5">MiniMax-阿里云</el-option>
                  <el-option label="MiniMax/MiniMax-M2.7" value="MiniMax/MiniMax-M2.7">MiniMax-稀宇科技</el-option>
                </el-select>
                <el-tooltip content="流式读取：实时显示AI回复，响应更快" placement="top">
                  <el-switch
                    v-model="streamEnabled"
                    class="stream-switch"
                    inline-prompt
                    active-text="流"
                    inactive-text="普通"
                  />
                </el-tooltip>
              </div>
              <el-button
                type="primary"
                :loading="isLoading"
                @click="handleSubmit"
                :disabled="!message.trim()"
                class="send-btn"
              >
                <el-icon v-if="!isLoading"><Promotion /></el-icon>
                <span>{{ isLoading ? '发送中...' : '发送' }}</span>
              </el-button>
            </div>
          </div>
        </div>
      </el-col>

      <!-- 右侧快捷短语 -->
      <el-col :span="4" class="sidebar-right">
        <div class="sidebar-header">
          <div class="sidebar-title">快捷短语</div>
        </div>
        <el-scrollbar height="calc(100vh - 160px)">
          <div class="quick-phrases">
            <div v-if="searchKeys.length === 0" class="empty-state">
              <div class="empty-icon">💡</div>
              <div class="empty-text">暂无短语</div>
            </div>
            <div
              v-for="item in searchKeys"
              :key="item.id"
              class="phrase-item"
              @click="handleContent(item.content)"
            >
              <div class="phrase-text">{{ item.content }}</div>
            </div>
          </div>
        </el-scrollbar>
      </el-col>
    </el-row>
	     <el-col v-if="innerType=='product'" :span="24" style="display: flex; flex-direction: column; height: 100%; overflow: hidden;">
	        <div ref="messagesWrapperRef" class="messages-wrapper product-messages-wrapper">
	          <template v-for="(item, index) in messages" :key="item.id || item.message_id || index">
	            <div class="message-wrapper" :class="item.role=='user' ? 'user-message' : 'assistant-message'">
	              <div class="message-avatar">
	                <div v-if="item.role=='user'" class="avatar user-avatar">你</div>
	                <div v-else class="avatar ai-avatar">AI</div>
	              </div>
	              <div class="message-content">
	                <!-- 思考过程展示 -->
	                <div v-if="item.reasoning" class="reasoning-block">
	                  <div class="reasoning-header" @click="item.showReasoning = !item.showReasoning">
	                    <span class="reasoning-icon">💭</span>
	                    <span class="reasoning-title">{{ item.isThinking ? '正在思考...' : '思考完成' }}</span>
	                    <span class="reasoning-toggle">{{ item.showReasoning ? '▼' : '▶' }}</span>
	                  </div>
	                  <div v-if="item.showReasoning" class="reasoning-content">
	                    <MarkdownRenderer :content="item.reasoning" :showCodeOnly="true"></MarkdownRenderer>
	                  </div>
	                </div>
	                <!-- 正式内容 -->
	                <div class="message-bubble" :class="item.role=='user' ? 'user-bubble' : 'ai-bubble'">
	                  <MarkdownRenderer :content="item.content" :showCodeOnly="true"></MarkdownRenderer>
	                  <span v-if="item.isStreaming && !item.isThinking" class="streaming-cursor">|</span>
	                </div>
	              </div>
	            </div>
	          </template>
	          <!-- 流式消息占位 -->
	          <div id="streaming-container" class="streaming-container"></div>
	          <!-- AI 思考加载动画 -->
	          <div v-if="isLoading && !hasStreamContent" class="ai-thinking-indicator">
	            <div class="message-wrapper assistant-message">
	              <div class="message-avatar">
	                <div class="avatar ai-avatar">AI</div>
	              </div>
	              <div class="message-content">
	                <div class="message-bubble ai-bubble thinking-bubble">
	                <div class="thinking-dots">
	                  <span class="dot"></span>
	                  <span class="dot"></span>
	                  <span class="dot"></span>
	                </div>
	                <span class="thinking-text">AI正在思考中...</span>
	              </div>
	              </div>
	            </div>
	          </div>
	        </div>
	        
	        <!-- 输入区域 -->
	        <div class="input-area product-input-area">
	          <div class="input-wrapper">
	            <div class="textarea-container">
	              <textarea
	                class="message-textarea"
	                v-model="message"
	                :placeholder="isLoading ? 'AI正在思考中...' : '输入你的问题...'"
	                @keydown.enter.exact.prevent="handleSubmit"
	                rows="2"
	                ref="textareaRef"
	                :disabled="isLoading"
	              />
	            </div>
	            <div class="input-actions">
                <el-space>
	              <el-select size="small" style="width:140px" v-model="search_model" placeholder="请选择模型">
	                <el-option label="通义千问-Turbo" value="qwen-turbo">通义千问-Turbo</el-option>
	                <el-option label="通义千问-Plus" value="qwen-plus">通义千问-Plus</el-option>
	                <el-option label="通义千问-Max" value="qwen-max">通义千问-Max</el-option>
	                <el-option label="deepseek-v3.2" value="deepseek-v3.2">DeepSeek-阿里云</el-option>
	                <el-option label="siliconflow/deepseek-v3.2" value="siliconflow/deepseek-v3.2">DeepSeek-硅基流动</el-option>
	              </el-select>
	              <el-button 
	                type="danger" 
	                class="clear-btn"
                  link
	                @click="handleClearHistory" 
	                :disabled="messages.length === 0"
	                title="清空历史消息"
	              >
	                <el-icon><Delete /></el-icon>
	              </el-button>
                </el-space>
	              <el-button 
	                type="primary" 
	                class="send-btn" 
	                @click="handleSubmit" 
	                :disabled="!message.trim() || isLoading"
	                :loading="isLoading"
	              >
	                <el-icon><Promotion /></el-icon>
	                <span>发送</span>
	              </el-button>
	            </div>
	          </div>
	        </div>
	     </el-col>
  </div>
</template>

<script setup>
import { ref,reactive, onMounted, toRefs,nextTick, watch } from 'vue';
import deepseekApi from '@/api/sys/tool/deepseekApi.js';
import MarkdownRenderer from "./components/MarkdownRenderer.vue"
import {Top,Plus,Promotion,Delete} from '@element-plus/icons-vue';
const emit = defineEmits(['change']);
const props = defineProps({
  innerType: {
    type: String,
    default: 'deepseek'
  },

});

const { innerType,  } = props;

const textareaRef = ref(null)
const messagesWrapperRef=ref();

    const  state=reactive({
		message:"",
		messages:[],
		sessionid:null,
		search_model:"qwen-turbo",
		search_network:"deepseek-chat",
		isLoading:false,
		sessions:[],
		searchKeys:[],
		response:null,
		streamEnabled: true, // 是否启用流式读取
		hasStreamContent: false, // 是否有流式内容
	})
	const{
		message,
		messages,
		sessionid,
		searchKeys,
		sessions,
		search_model,
		search_network,
		isLoading,
		response,
		streamEnabled,
		hasStreamContent,
	}=toRefs(state);
  function submit(data,callback){
    data.model=state.search_model;
    data.frequencyPenalty=0;
    data.maxTokens=4096;
    data.presencePenalty=0;
    data.responseFormat={"type":"json_object"};
    data.stop=null;
    data.stream=false;
    data.streamOptions=null;
    data.temperature=1;
    data.topP=1;
    data.sessionId=state.sessionid;
    data.tools=null;
    data.toolChoice="none";
    data.logprobs=false;
    data.topLogprobs=null;
    state.isLoading=true;
    deepseekApi.search(data).then(res=>{
      if(res.data){
        state.response=res.data;
        state.isLoading=false;
        // 合并服务器返回的消息（包含用户消息和AI回复），添加 reasoning 相关字段
        state.messages = state.response.messages.map(msg => ({
          ...msg,
          reasoning: msg.reasoning_content || msg.reasoning || '',
          showReasoning: false,
          isStreaming: false,
          isThinking: false
        }));
        state.sessionid=state.response.id;
        emit("change");
        if(callback){
          callback(res.data);
        }
        nextTick(()=>{
          scrollToBottom();
        })
      }

    })
  }

  // 流式提交方法
  function submitStream(data, callback) {
    data.model = state.search_model;
    data.frequencyPenalty = 0;
    data.maxTokens = 4096;
    data.presencePenalty = 0;
    data.responseFormat = {"type": "json_object"};
    data.stop = null;
    data.stream = true;
    data.streamOptions = null;
    data.temperature = 1;
    data.topP = 1;
    data.sessionId = state.sessionid;
    data.tools = null;
    data.toolChoice = "none";
    data.logprobs = false;
    data.topLogprobs = null;
    state.isLoading = true;
    state.hasStreamContent = false; // 重置流式内容状态

    // 存储完整内容用于保存
    let fullReasoning = '';
    let fullContent = '';
    let hasReceivedData = false;
    
    // 获取流式消息容器
    const container = document.getElementById('streaming-container');
    if (!container) {
      console.error('找不到流式消息容器');
      return;
    }
    
    // 清空容器并创建流式消息元素
    container.innerHTML = `
      <div class="message-wrapper assistant-message" id="streaming-message">
        <div class="message-avatar">
          <div class="avatar ai-avatar">AI</div>
        </div>
        <div class="message-content">
          <div class="reasoning-block" id="streaming-reasoning" style="display:none;">
            <div class="reasoning-header">
              <span class="reasoning-icon">💭</span>
              <span class="reasoning-title">正在思考...</span>
            </div>
            <div class="reasoning-content" id="streaming-reasoning-content"></div>
          </div>
          <div class="message-bubble ai-bubble">
            <div id="streaming-content"></div>
            <span class="streaming-cursor">|</span>
          </div>
        </div>
      </div>
    `;

    deepseekApi.searchStream(
      data,
      // onMessage - 接收流式数据
      (parsed) => {
        // 第一次接收到数据时，触发 change 事件以关闭 loading
        if (!hasReceivedData) {
          hasReceivedData = true;
          state.hasStreamContent = true; // 标记有流式内容
          emit("change");
        }
        
        // 处理思考内容 - 直接操作 DOM
        if (parsed.reasoning) {
          fullReasoning += parsed.reasoning;
          const reasoningBlock = document.getElementById('streaming-reasoning');
          const reasoningContent = document.getElementById('streaming-reasoning-content');
          
          if (reasoningBlock && reasoningContent) {
            reasoningBlock.style.display = 'block';
            reasoningContent.innerHTML = fullReasoning.replace(/\n/g, '<br>');
          }
        }
        
        // 处理正式内容
        if (parsed.content && parsed.content !== '[DONE]') {
          fullContent += parsed.content;
          const contentDiv = document.getElementById('streaming-content');
          
          if (contentDiv) {
            contentDiv.innerHTML = fullContent.replace(/\n/g, '<br>');
          }
        }
        
        if (parsed.id) {
          state.sessionid = parsed.id;
        }
        
        // 滚动到底部
        scrollToBottom();
      },
      // onError - 错误处理
      (error) => {
        console.error('Stream error:', error);
        state.isLoading = false;
        // 移除流式消息元素
        const streamingMsg = document.getElementById('streaming-message');
        if (streamingMsg) {
          streamingMsg.remove();
        }
        // 出错时回退到普通请求
        deepseekApi.search(data).then(res => {
          if (res.data) {
            state.response = res.data;
            state.messages = state.response.messages;
            state.sessionid = state.response.id;
            emit("change");
            nextTick(() => {
              scrollToBottom();
            });
          }
        });
      },
      // onComplete - 完成，接收sessionId
      (sessionId) => {
        state.isLoading = false;
        
        // 移除流式消息元素
        const streamingMsg = document.getElementById('streaming-message');
        if (streamingMsg) {
          streamingMsg.remove();
        }
        
        // 更新sessionId（如果是新会话）
        if (sessionId && !state.sessionid) {
          state.sessionid = sessionId;
        }
        
        // 将流式内容添加到消息列表
        const aiMessage = {
          id: Date.now(),
          message_id: Date.now(),
          role: 'assistant',
          content: fullContent,
          reasoning: fullReasoning,
          format_type: 'plain',
          message_type: 'text',
          created_time: new Date(),
          showReasoning: false,
          isStreaming: false,
          isThinking: false
        };
        state.messages = [...state.messages, aiMessage];
        
        emit("change");
        // 重新加载会话列表
        deepseekApi.getSession().then(res => {
          state.sessions = res.data;
        });
        if (callback) {
          callback(aiMessage);
        }
        nextTick(() => {
          scrollToBottom();
        });
      }
    );
  }

	function handleSubmit(){
		var data={};
		var messages=[]; 
		if(state.message) {
			// 删除当前会话（如果有的话）
			const currentSessionId = state.sessionid;
			if (currentSessionId) {
				deepseekApi.deleteSession(currentSessionId).catch(err => {
					console.error('删除会话失败:', err);
				});
			}
			
			// 清空之前的历史消息，只保留当前问题
			const userMessage = {
				message_id: Date.now(),
				role: 'user',
				content: state.message,
				format_type: 'plain',
				message_type: 'text',
				created_time: new Date()
			};
			state.messages = [userMessage];
			state.sessionid = null; // 使用新的会话ID
			
			// 清空输入框
			const currentMessage = state.message;
			state.message = "";
			
			// 清空流式消息容器
			const container = document.getElementById('streaming-container');
			if (container) {
				container.innerHTML = '';
			}
			
			// 滚动到底部
			nextTick(() => {
				scrollToBottom();
			});
			
			// 准备请求数据
		messages.push({"role":"user","content": currentMessage});
      data.messages=messages;
      
      // 根据开关选择流式或普通模式
      if (streamEnabled.value) {
        submitStream(data);
      } else {
        submit(data);
      }
    }

	}
 function handleSession(session){
	 // 加载历史消息时，添加 reasoning 相关字段
	 state.messages = session.messages.map(msg => ({
		 ...msg,
		 reasoning: msg.reasoning_content || msg.reasoning || '',
		 showReasoning: false,
		 isStreaming: false,
		 isThinking: false
	 }));
	 state.sessionid=session.id;
	 nextTick(()=>{
		 scrollToBottom();
	 })
	 
 }
 function handleContent(content){
	 state.message=state.message+" "+content;
 }
 function handleAddSession(){
	 state.messages=messages.value = [
										{
										  message_id: 1,
										  role: 'assistant',
										  content: '你好！我是AI助手，我可以帮助你解答问题。请问有什么可以帮您的？',
										  format_type: 'plain',
										  message_type: 'text',
										  created_time: new Date()
										}
									  ];
	 state.sessionid=null;
 }
 
 // 清空历史消息
 function handleClearHistory(){
   // 删除当前会话（如果有的话）
   const currentSessionId = state.sessionid;
   if (currentSessionId) {
     deepseekApi.deleteSession(currentSessionId).catch(err => {
       console.error('删除会话失败:', err);
     });
   }
   
   state.messages = [];
   state.sessionid = null;
   // 清空流式消息容器
   const container = document.getElementById('streaming-container');
   if (container) {
     container.innerHTML = '';
   }
 }
 
 // 删除单个会话
 function handleDeleteSession(session){
   deepseekApi.deleteSession(session.id).then(() => {
     // 从列表中移除
     state.sessions = state.sessions.filter(s => s.id !== session.id);
     // 如果删除的是当前会话，清空消息
     if (state.sessionid === session.id) {
       state.messages = [];
       state.sessionid = null;
       const container = document.getElementById('streaming-container');
       if (container) {
         container.innerHTML = '';
       }
     }
   }).catch(err => {
     console.error('删除会话失败:', err);
   });
 }
 
function scrollToBottom(){
	if (messagesWrapperRef.value) {
		messagesWrapperRef.value.scrollTop = messagesWrapperRef.value.scrollHeight;
	}
}

// 自动调整文本框高度
watch(state.messages, () => {
  nextTick(() => {
    if (textareaRef.value) {
      textareaRef.value.style.height = 'auto'
      textareaRef.value.style.height = Math.min(textareaRef.value.scrollHeight, 120) + 'px'
    }
  })
})


// 初始化示例消息
onMounted(() => {
  state.messages = [
    {
      message_id: 1,
      role: 'assistant',
      content: '你好！我是AI助手，我可以帮助你解答问题。请问有什么可以帮您的？',
      format_type: 'plain',
      message_type: 'text',
      created_time: new Date()
    }
  ]
  deepseekApi.getSession().then(res=>{
	  state.sessions=res.data;
  });
  deepseekApi.getKey().then(res=>{
  	  state.searchKeys=res.data;
  });


  
})
 function show(dataRow,ftype,callback){
   // 默认使用流式模式
   state.streamEnabled = true;
   
   if(ftype=="addMsg"){
     var data={};
		var messages=[];
     state.message=dataRow;
		if(state.message) {
			messages.push({"role":"system","content":state.message});
       data.messages=messages;
       // 使用流式提交
       submitStream(data,callback);
     }
   }else if(ftype=="initMsg"){
     // 获取会话列表，查找匹配的会话
     deepseekApi.getSession().then(res=>{
       state.sessions=res.data;
       
       // 查找标题匹配且消息数不超过30轮的会话
       const targetTitle = dataRow.title;
       const existingSession = state.sessions.find(session => {
         if (session.title !== targetTitle) return false;
         // 检查消息数量（每轮包含用户消息和AI回复）
         const messageCount = session.messages ? session.messages.length : 0;
         return messageCount < 60; // 30轮 = 60条消息
       });
       
       if (existingSession) {
         // 使用现有会话
         state.sessionid = existingSession.id;
         
         // 将当前信息作为新的用户消息
         const userMessage = {
           message_id: Date.now(),
           role: 'user',
           content: dataRow.content,
           format_type: 'plain',
           message_type: 'text',
           created_time: new Date()
         };
         
         // 一次性更新消息列表，减少重渲染
         state.messages = [
           ...existingSession.messages.map(msg => ({
             ...msg,
             role: msg.role === 'system' ? 'user' : msg.role,
             reasoning: msg.reasoning_content || msg.reasoning || '',
             showReasoning: false,
             isStreaming: false,
             isThinking: false
           })),
           userMessage
         ];
         
         nextTick(() => {
           scrollToBottom();
         });
         
         // 发送给AI继续对话
         var data={};
         var messages=[];
         // 添加历史消息到请求中（使用原始角色）
         existingSession.messages.forEach(msg => {
           messages.push({"role": msg.role, "content": msg.content});
         });
         // 添加当前用户消息
         messages.push({"role":"user","content": dataRow.content});
         data.messages=messages;
         data.sessionId = existingSession.id;
         
         // 使用流式提交
         submitStream(data,callback);
       } else {
         // 新建会话
         state.sessionid=null;
         // 显示系统消息
         const systemMessage = {
           message_id: Date.now(),
           role: 'user',
           content: dataRow.content,
           format_type: 'plain',
           message_type: 'text',
           created_time: new Date()
         };
         state.messages = [systemMessage];
         
         nextTick(() => {
           scrollToBottom();
         });
         
         var data={};
         var messages=[];
         messages.push({"role":"system","content":dataRow.title});
         messages.push({"role":"system","content":dataRow.content});
         data.messages=messages;
         // 使用流式提交
         submitStream(data,callback);
       }
     });
   }
 }
defineExpose({
  show,
})

</script>

<style scoped>
.deepseek-app {
  height: calc(100vh - 40px);
  background: linear-gradient(135deg, #f5f7fa 0%, #e4e8ec 100%);
  overflow: hidden;
}

.deepseek-row {
  height: 100%;
}

/* 左侧边栏 */
.sidebar-left {
  background: #ffffff;
  border-right: 1px solid #e8eaec;
  display: flex;
  flex-direction: column;
  box-shadow: 2px 0 12px rgba(0, 0, 0, 0.04);
}

.sidebar-header {
  padding: 16px;
  border-bottom: 1px solid #f0f2f5;
  background: #fafbfc;
}

.new-session-btn {
  width: 100%;
  height: 44px;
  border-radius: 8px;
  font-weight: 500;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  transition: all 0.3s ease;
}

.new-session-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.4);
}

.session-scrollbar {
  flex: 1;
}

.session-list {
  padding: 8px;
}

.session-item {
  display: flex;
  align-items: center;
  padding: 12px 16px;
  margin: 4px 0;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.2s ease;
  background: #f8f9fb;
}

.session-item:hover {
  background: #eef0f3;
  transform: translateX(4px);
}

.session-item.active {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #ffffff;
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3);
}

.session-item-content {
  flex: 1;
  min-width: 0;
}

.session-title {
  font-size: 14px;
  font-weight: 500;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.session-item.active .session-title {
  color: #ffffff;
  font-weight: 600;
}

.session-item-actions {
  margin-left: 8px;
  opacity: 0;
  transition: opacity 0.2s ease;
}

.session-item:hover .session-item-actions {
  opacity: 1;
}

.session-item.active .session-item-actions .el-button {
  background: rgba(255, 255, 255, 0.2);
  border-color: rgba(255, 255, 255, 0.3);
  color: #fff;
}

.session-item.active .session-item-actions .el-button:hover {
  background: rgba(255, 255, 255, 0.3);
}

/* 中间聊天区域 */
.chat-main {
  display: flex;
  flex-direction: column;
  background: #f5f7fa;
}

.messages-scrollbar {
  flex: 1;
}

.message-wrapper {
  display: flex;
  gap: 12px;
  animation: fadeIn 0.3s ease;
}

.user-message {
  flex-direction: row-reverse;
}

.message-avatar {
  flex-shrink: 0;
}

.avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
  font-weight: 600;
  color: #ffffff;
}

.user-avatar {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.ai-avatar {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
}

.message-content {
  max-width: 70%;
}

.message-bubble {
  padding: 16px 20px;
  border-radius: 12px;
  line-height: 1.6;
  font-size: 14px;
  word-wrap: break-word;
}

.user-bubble {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #ffffff;
  border-bottom-right-radius: 4px;
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3);
}

.ai-bubble {
  background: #ffffff;
  color: #303133;
  border-bottom-left-radius: 4px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
}

/* 输入区域 */
.input-area {
  padding: 16px 24px;
  background: #ffffff;
  border-top: 1px solid #e8eaec;
  box-shadow: 0 -4px 12px rgba(0, 0, 0, 0.04);
}

.input-wrapper {
  background: #f8f9fb;
  border-radius: 12px;
  overflow: hidden;
  border: 2px solid #e8eaec;
  transition: border-color 0.2s;
}

.input-wrapper:focus-within {
  border-color: #667eea;
  box-shadow: 0 0 0 4px rgba(102, 126, 234, 0.1);
}

.textarea-container {
  padding: 12px 16px 0;
}

.message-textarea {
  width: 100%;
  border: none;
  background: transparent;
  resize: none;
  font-family: inherit;
  font-size: 14px;
  line-height: 1.6;
  min-height: 60px;
  max-height: 120px;
  outline: none;
  color: #303133;
}

.message-textarea::placeholder {
  color: #909399;
}

.input-actions {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 8px 12px;
}

.model-select {
  flex: 1;
}

.model-dropdown {
  width: 200px;
}

.model-dropdown :deep(.el-input__wrapper) {
  border-radius: 6px;
  background: #ffffff;
}

.send-btn {
  height: 40px;
  padding: 0 24px;
  border-radius: 8px;
  font-weight: 500;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  gap: 8px;
}

.send-btn:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.4);
}

.send-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

/* 右侧边栏 */
.sidebar-right {
  background: #ffffff;
  border-left: 1px solid #e8eaec;
  display: flex;
  flex-direction: column;
  box-shadow: -2px 0 12px rgba(0, 0, 0, 0.04);
}

.sidebar-title {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

.quick-phrases {
  padding: 12px;
}

.phrase-item {
  padding: 12px 16px;
  margin: 6px 0;
  background: #f8f9fb;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.2s ease;
}

.phrase-item:hover {
  background: #eef0f3;
  transform: translateX(-4px);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.phrase-text {
  font-size: 13px;
  color: #606266;
  line-height: 1.5;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

/* 空状态 */
.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 40px 20px;
  text-align: center;
}

.empty-icon {
  font-size: 48px;
  margin-bottom: 16px;
}

.empty-text {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 8px;
}

.empty-subtitle {
  font-size: 13px;
  color: #909399;
}

/* 动画 */
@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.message-wrapper:nth-child(1) { animation-delay: 0.05s; }
.message-wrapper:nth-child(2) { animation-delay: 0.1s; }
.message-wrapper:nth-child(3) { animation-delay: 0.15s; }
.message-wrapper:nth-child(4) { animation-delay: 0.2s; }
.message-wrapper:nth-child(5) { animation-delay: 0.25s; }

/* 流式光标效果 */
.streaming-cursor {
  display: inline-block;
  color: #667eea;
  font-weight: bold;
  animation: cursorBlink 0.8s infinite;
  margin-left: 2px;
}

@keyframes cursorBlink {
  0%, 100% {
    opacity: 1;
  }
  50% {
    opacity: 0;
  }
}

/* 思考过程样式 */
.reasoning-block {
  margin-bottom: 12px;
  border-radius: 8px;
  overflow: hidden;
  background: #f8f9fb;
  border: 1px solid #e8eaec;
}

.reasoning-header {
  display: flex;
  align-items: center;
  padding: 10px 14px;
  cursor: pointer;
  user-select: none;
  transition: background-color 0.2s;
}

.reasoning-header:hover {
  background: #eef0f3;
}

.reasoning-icon {
  margin-right: 8px;
  font-size: 16px;
}

.reasoning-title {
  flex: 1;
  font-size: 13px;
  color: #606266;
  font-weight: 500;
}

.reasoning-toggle {
  font-size: 12px;
  color: #909399;
  transition: transform 0.2s;
}

.reasoning-content {
  padding: 12px 14px;
  border-top: 1px solid #e8eaec;
  color: #909399;
  font-size: 13px;
  line-height: 1.6;
  max-height: 300px;
  overflow-y: auto;
}

.reasoning-content :deep(p) {
  margin-bottom: 8px;
}

.reasoning-content :deep(p:last-child) {
  margin-bottom: 0;
}

/* 思考中动画 */
@keyframes thinkingPulse {
  0%, 100% {
    opacity: 0.6;
  }
  50% {
    opacity: 1;
  }
}

.reasoning-header:has(.reasoning-title:contains('正在思考')) .reasoning-icon {
  animation: thinkingPulse 1.5s infinite;
}

/* 消息区域样式 */
.messages-wrapper {
  height: calc(100vh - 220px);
  overflow-y: auto;
  padding: 24px;
}

.product-messages-wrapper {
  flex: 1;
  overflow-y: auto;
  padding: 16px;
  background: #f5f7fa;
  border-radius: 8px 8px 0 0;
  border: 1px solid #e4e7ed;
  border-bottom: none;
}

.product-input-area {
  padding: 12px 16px;
  background: #fff;
  border-radius: 0 0 8px 8px;
  border: 1px solid #e4e7ed;
  border-top: 1px solid #f0f2f5;
}

.product-input-area .input-wrapper {
  border-radius: 8px;
}

.product-input-area .send-btn {
  height: 36px;
  padding: 0 20px;
  border-radius: 6px;
}

.message-wrapper {
  margin-bottom: 20px;
}

.streaming-container {
  min-height: 50px;
  padding: 16px;
  background: #f5f7fa;
  border-radius: 8px;
  margin-top: 16px;
}
.stream-switch {
  margin-left: 12px;
}

.stream-switch :deep(.el-switch__core) {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.stream-switch :deep(.el-switch.is-checked .el-switch__core) {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
}

/* AI 思考加载动画 */
.ai-thinking-indicator {
  margin-bottom: 20px;
}

.thinking-bubble {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 16px 20px;
}

.thinking-dots {
  display: flex;
  gap: 6px;
}

.thinking-dots .dot {
  width: 10px;
  height: 10px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 50%;
  animation: dotPulse 1.4s infinite ease-in-out both;
}

.thinking-dots .dot:nth-child(1) {
  animation-delay: -0.32s;
}

.thinking-dots .dot:nth-child(2) {
  animation-delay: -0.16s;
}

.thinking-dots .dot:nth-child(3) {
  animation-delay: 0s;
}

@keyframes dotPulse {
  0%, 80%, 100% {
    transform: scale(0.6);
    opacity: 0.5;
  }
  40% {
    transform: scale(1);
    opacity: 1;
  }
}

.thinking-text {
  color: #909399;
  font-size: 14px;
  animation: fadeInOut 2s infinite;
}

@keyframes fadeInOut {
  0%, 100% {
    opacity: 0.5;
  }
  50% {
    opacity: 1;
  }
}

/* 响应式调整 */
@media (max-width: 1200px) {
  .sidebar-left,
  .sidebar-right {
    display: none;
  }

  .chat-main {
    width: 100%;
  }
}

/* 暗黑模式 */
:global(html.dark) .deepseek-app {
  background: linear-gradient(135deg, #1a1a2e 0%, #16213e 100%);
}

:global(html.dark) .sidebar-left,
:global(html.dark) .sidebar-right {
  background: #1e1e2e;
  border-color: #2a2a3e;
}

:global(html.dark) .sidebar-header {
  background: #252538;
  border-color: #2a2a3e;
}

:global(html.dark) .session-item {
  background: #252538;
}

:global(html.dark) .session-item:hover {
  background: #2e2e42;
}

:global(html.dark) .session-title {
  color: #e0e0e0;
}

:global(html.dark) .session-item.active .session-title {
  color: #ffffff;
}

:global(html.dark) .sidebar-title {
  color: #e0e0e0;
}

:global(html.dark) .phrase-item {
  background: #252538;
}

:global(html.dark) .phrase-item:hover {
  background: #2e2e42;
}

:global(html.dark) .phrase-text {
  color: #c0c0c0;
}

:global(html.dark) .chat-main {
  background: #1a1a2e;
}

:global(html.dark) .messages-wrapper {
  background: transparent;
}

:global(html.dark) .ai-bubble {
  background: #252538;
  color: #e0e0e0;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.2);
}

:global(html.dark) .input-area {
  background: #1e1e2e;
  border-color: #2a2a3e;
}

:global(html.dark) .input-wrapper {
  background: #252538;
  border-color: #2a2a3e;
}

:global(html.dark) .input-wrapper:focus-within {
  border-color: #667eea;
  box-shadow: 0 0 0 4px rgba(102, 126, 234, 0.2);
}

:global(html.dark) .message-textarea {
  color: #e0e0e0;
}

:global(html.dark) .message-textarea::placeholder {
  color: #808090;
}

:global(html.dark) .model-dropdown :deep(.el-input__wrapper) {
  background: #1e1e2e;
}

:global(html.dark) .reasoning-block {
  background: #1e1e2e;
  border-color: #2a2a3e;
}

:global(html.dark) .reasoning-header:hover {
  background: #2e2e42;
}

:global(html.dark) .reasoning-title {
  color: #c0c0c0;
}

:global(html.dark) .reasoning-content {
  border-color: #2a2a3e;
  color: #a0a0b0;
}

:global(html.dark) .empty-icon {
  opacity: 0.7;
}

:global(html.dark) .empty-text {
  color: #e0e0e0;
}

:global(html.dark) .empty-subtitle {
  color: #808090;
}

:global(html.dark) .thinking-text {
  color: #808090;
}

:global(html.dark) .product-messages-wrapper {
  background: #1a1a2e;
  border-color: #2a2a3e;
}

:global(html.dark) .product-input-area {
  background: #1e1e2e;
  border-color: #2a2a3e;
}

:global(html.dark) .streaming-container {
  background: #252538;
}
</style>