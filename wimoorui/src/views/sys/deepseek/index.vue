
<template>
  <el-row  >
	  <el-col :span="4">
		  <el-scrollbar height="calc(100vh - 50px)">
		  	<el-card style="height:100%">
				   <p class="scrollbar-demo-item pointer titleadd" @click="handleAddSession()"><el-icon><Plus></Plus></el-icon>
				   <span style="padding-left:20px">新建会话</span> </p>
				   <p v-for="item in sessions" @click="handleSession(item)" :key="item.id" class="scrollbar-demo-item ">{{ item.title }}</p>
			</el-card>
		   </el-scrollbar>
	  </el-col>
	 <el-col :span="16">
	   <div>
		    <el-scrollbar ref="scrollbarRef" height="calc(100vh - 200px)"  >
				<div ref="innerRef"  v-if="messages" >
					<template  v-for="item in messages" :key="item.id">
							<el-card 
								style="min-height:100px;margin-top:20px;margin-left:20px;margin-right:20px"
								:class="item.role=='user'?'text-right user-content content':'text-left content'"
								>
									<MarkdownRenderer :content="item.content"  :showCodeOnly="true"></MarkdownRenderer>
							</el-card>
			     </template>
				</div>
	      </el-scrollbar>
	<el-card style="margin-top:2px;margin-left:20px;margin-right:20px">
	  <div class="input-container">
	    <textarea
	      v-model="message"
	      placeholder="输入消息..."
	      @keydown.enter="handleSubmit"
	      rows="3"
	      ref="textareaRef"
	    />
	    <el-button type="primary" :loading="isLoading" @click="handleSubmit" :disabled="!message.trim()">
	     提交
	    </el-button>
	  </div>
	  <div class="input-container-footer">
	  	<div>
			<el-button :type="search_model=='deepseek-reasoner'?'primary':''" 
			@click="search_model=(search_model=='deepseek-reasoner'?'deepseek-chat':'deepseek-reasoner')"
			size="small">深度搜索</el-button>
	  	 </div>
	  </div>
	</el-card>
	</div>
	</el-col>
	 <el-col :span="4">
	 		  <el-scrollbar height="calc(100vh - 50px)">
	 		  	<el-card style="height:100%">
	 				   <p v-for="item in searchKeys" :key="item.id"
						    @click="handleContent(item.content )" 
								class="scrollbar-demo-item">{{ item.content }}
							</p>
	 			</el-card>
	 		   </el-scrollbar>
	 </el-col>
   </el-row>
</template>

<script setup>
import { ref,reactive, onMounted, toRefs,nextTick, watch } from 'vue';
import deepseekApi from '@/api/sys/tool/deepseekApi.js';
import MarkdownRenderer from "./components/MarkdownRenderer.vue"
import {Top,Plus} from '@element-plus/icons-vue';

const textareaRef = ref(null)
const scrollbarRef=ref();
const innerRef=ref();
    const  state=reactive({
		message:"",
		messages:[],
		sessionid:null,
		search_model:"deepseek-chat",
		search_network:"deepseek-chat",
		isLoading:false,
		sessions:[],
		searchKeys:[],
		response:null,
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
	}=toRefs(state);
	function handleSubmit(){
		var data={};
		var messages=[]; 
		messages.push({"role":"user","content":state.message});
		data.messages=messages;
		data.model=state.isdeep=='deepseek-reasoner'?'deepseek-reasoner':"deepseek-chat";
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
				state.messages=state.response.messages;
				state.sessionid=state.response.id;
				state.message="";
				nextTick(()=>{
						 scrollToBottom();
				})
			}
			
		})
	}
 function handleSession(session){
	 state.messages=session.messages;
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
function scrollToBottom(){
	var height=innerRef.value.clientHeight  ;
	scrollbarRef.value.setScrollTop(height);
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
</script>

<style scoped>
.chat-container {
  height: 100vh;
  display: flex;
  flex-direction: column;
  background: #f8f9fa;
}

.messages-container {
  flex: 1;
  overflow-y: auto;
  padding: 20px;
  scroll-behavior: smooth;
}

.loading-indicator {
  display: flex;
  justify-content: center;
  padding: 20px;
}

.typing-animation {
  display: flex;
  gap: 4px;
}

.typing-animation span {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: #666;
  animation: typing 1.4s infinite ease-in-out;
}

.typing-animation span:nth-child(1) { animation-delay: 0s; }
.typing-animation span:nth-child(2) { animation-delay: 0.2s; }
.typing-animation span:nth-child(3) { animation-delay: 0.4s; }

@keyframes typing {
  0%, 60%, 100% { transform: translateY(0); }
  30% { transform: translateY(-10px); }
}

.input-container {
  display: flex;
  gap: 12px;
  padding: 16px 20%;
  background: white;
  align-items: end;
}
.input-container-footer {
  display: flex;
  gap: 12px;
  margin-top:-45px;
  margin-left:10px;
  padding: 0px 20%;
  background: white;
  align-items: end;
}


.input-container textarea {
  flex: 1;
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 12px 16px;
  resize: none;
  font-family: inherit;
  font-size: 14px;
  line-height: 1.4;
  min-height: 44px;
  max-height: 120px;
}

.input-container textarea:focus {
  outline: none;
  border-color: #667eea;
}

.input-container button {
  border: none;
  border-radius: 8px;
  padding-top:43px;
  padding-bottom:43px;
  padding-left:20px;
  padding-right:20px;
  cursor: pointer;
  color: white;
  transition: background-color 0.2s;
}

 
.messageinput{
	padding-bottom:10px;padding-left:20%;padding-right:20%;
}
.user-content{
	color: var(--el-color-primary);
	font-weight:600;
	background: var(--el-bg-color);
}
.scrollbar-demo-item {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 50px;
  margin: 10px;
  padding:10px;
 display: block; /* 或者 inline-block，根据需要选择 */
 width: 260px; /* 设置一个宽度限制 */
 white-space: nowrap;
 overflow: hidden;
 text-overflow: ellipsis;
  cursor: pointer;
  text-align: center;
  border-radius: 4px;
  background: var(--el-bg-color);
  font-weight:600;
}

.titleadd{
	color:var(--el-color-blue);
}
.user-content .el-card__body{
	padding:0px !important;
}
.content{
	padding-left:20px;
	padding-right:20px;
}
</style>