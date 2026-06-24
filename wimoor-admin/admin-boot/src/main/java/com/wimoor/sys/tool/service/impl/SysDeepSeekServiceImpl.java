package com.wimoor.sys.tool.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wimoor.admin.common.exception.BizException;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.user.UserInfo;
import com.wimoor.sys.tool.pojo.dto.SysChartCompletionRequestDTO;
import com.wimoor.sys.tool.pojo.entity.DeepSeekMessage;
import com.wimoor.sys.tool.pojo.entity.DeepseekChatMessages;
import com.wimoor.sys.tool.pojo.entity.DeepseekChatSessions;
import com.wimoor.sys.tool.service.IDeepseekChatMessagesService;
import com.wimoor.sys.tool.service.IDeepseekChatSessionsService;
import com.wimoor.sys.tool.service.ISysDeepSeekService;
import io.reactivex.Flowable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class SysDeepSeekServiceImpl implements ISysDeepSeekService {
    final IDeepseekChatSessionsService iDeepseekChatSessionsService;
    final IDeepseekChatMessagesService iDeepseekChatMessagesService;
    
    @Value("${deepseek.token}")
    private String token;
    
    // 线程池用于异步处理流式请求
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    public OkHttpClient getClient(){
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(1200, TimeUnit.SECONDS);
        builder.callTimeout(1200, TimeUnit.SECONDS);
        builder.readTimeout(1200, TimeUnit.SECONDS);
        builder.writeTimeout(1200, TimeUnit.SECONDS);
        return builder.build();
    }
    public Response requestDeepSeek( RequestBody body ) throws IOException {
        Request request = new Request.Builder()
                .url("https://dashscope.aliyuncs.com/compatible-mode/v1/chat/completions")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", "Bearer "+token)
                .build();
        OkHttpClient client=getClient();
        return client.newCall(request).execute();
    }

   public Object completions(UserInfo userInfo ,SysChartCompletionRequestDTO dto){
       try {
           Generation gen = new Generation();
           List<DeepseekChatMessages> messages = iDeepseekChatMessagesService.lambdaQuery()
                   .eq(DeepseekChatMessages::getSessionId, dto.getSessionId())
                   .orderBy(true,true,DeepseekChatMessages::getCreatetime)
                   .list();
           List<DeepseekChatMessages> newMessages = new ArrayList<DeepseekChatMessages>();
           List<Message> messageList=new ArrayList<Message>();
           for(DeepseekChatMessages message:messages){
               Message e=Message.builder()
                       .role(message.getRole())
                       .content(message.getContent())
                       .build();
               messageList.add(e);
           }
           for(DeepSeekMessage message:dto.getMessages()){
               DeepseekChatMessages e=new DeepseekChatMessages();
               e.setSessionId(dto.getSessionId());
               e.setCreatetime(new Date());
               e.setContent(message.getContent());
               e.setRole(message.getRole());
               newMessages.add(e);
               Message msg=Message.builder()
                       .role(message.getRole())
                       .content(message.getContent())
                       .build();
               messageList.add(msg);
           }
           GenerationParam param = GenerationParam.builder()
                   .apiKey(token)
                   .model(dto.getModel())
                   .enableThinking(true)
                   .incrementalOutput(false)
                   .resultFormat("message")
                   .messages(messageList)
                   .build();
           GenerationResult result = gen.call(param);
           String content = result.getOutput().getChoices().get(0).getMessage().getContent();
           String role = result.getOutput().getChoices().get(0).getMessage().getRole();
           DeepseekChatMessages e=new DeepseekChatMessages();
           e.setSessionId(dto.getSessionId());
           e.setCreatetime(new Date());
           e.setContent(content);
           e.setRole(role);
           newMessages.add(e);
           DeepseekChatSessions session=null;
           String sessionId = dto.getSessionId();
           if(StrUtil.isBlankOrUndefined(sessionId)){
               sessionId = UUID.randomUUID().toString();
               for(DeepseekChatMessages message:newMessages){
                   message.setSessionId(sessionId);
               }
               session=new DeepseekChatSessions();
               session.setId(sessionId);
               session.setUserid(userInfo.getId());
               session.setOpttime(new Date());
               session.setModel(dto.getModel());
               session.setCreatetime(new Date());
               session.setTitle(dto.getMessages().get(0).getContent());
               this.iDeepseekChatSessionsService.save(session);
           }else{
               session=iDeepseekChatSessionsService.getById(sessionId);
           }
           iDeepseekChatMessagesService.saveBatch(newMessages);
           if(messages!=null){
               messages.addAll(newMessages);
               session.setMessages(messages);
           }else{
               session.setMessages(newMessages);
           }
           return session;
       } catch (ApiException | NoApiKeyException | InputRequiredException e) {
           System.err.println("An exception occurred: " + e.getMessage());
           throw new BizException("请求失败: " + e.getMessage());
       }
   }

    @Override
    public SseEmitter completionsStream(UserInfo userInfo, SysChartCompletionRequestDTO dto) {
        // 创建SseEmitter，设置超时时间
        SseEmitter emitter = new SseEmitter(300000L); // 5分钟超时
        
        // 异步处理流式请求
        executorService.execute(() -> {
            try {
                // 构建消息列表
                List<Message> messageList = new ArrayList<>();
                
                // 获取历史消息
                if (StrUtil.isNotBlank(dto.getSessionId())) {
                    List<DeepseekChatMessages> historyMessages = iDeepseekChatMessagesService.lambdaQuery()
                            .eq(DeepseekChatMessages::getSessionId, dto.getSessionId())
                            .orderBy(true, true, DeepseekChatMessages::getCreatetime)
                            .list();
                    for (DeepseekChatMessages message : historyMessages) {
                        Message e = Message.builder()
                                .role(message.getRole())
                                .content(message.getContent())
                                .build();
                        messageList.add(e);
                    }
                }
                
                // 添加新消息
                List<DeepseekChatMessages> newMessages = new ArrayList<>();
                for (DeepSeekMessage message : dto.getMessages()) {
                    DeepseekChatMessages e = new DeepseekChatMessages();
                    e.setSessionId(dto.getSessionId());
                    e.setCreatetime(new Date());
                    e.setContent(message.getContent());
                    e.setRole(message.getRole());
                    newMessages.add(e);
                    
                    Message msg = Message.builder()
                            .role(message.getRole())
                            .content(message.getContent())
                            .build();
                    messageList.add(msg);
                }
                
                // 构建流式请求参数
                Generation gen = new Generation();
                GenerationParam param = GenerationParam.builder()
                        .apiKey(token)
                        .model(dto.getModel())
                        .enableThinking(false) // 关闭思考模式以提高响应速度
                        .incrementalOutput(true) // 启用增量输出
                        .resultFormat("message")
                        .messages(messageList)
                        .build();
                
                // 用于存储完整的AI回复和思考内容
                StringBuilder fullContent = new StringBuilder();
                StringBuilder fullReasoning = new StringBuilder();
                String sessionId = dto.getSessionId();
                
                // 调用流式API
                Flowable<GenerationResult> result = gen.streamCall(param);
                result.blockingForEach(message -> {
                    try {
                        String content = message.getOutput().getChoices().get(0).getMessage().getContent();
                        String reasoning = message.getOutput().getChoices().get(0).getMessage().getReasoningContent();
                        
                        boolean hasData = false;
                        JSONObject data = new JSONObject();
                        
                        // 发送思考内容
                        if (reasoning != null && !reasoning.isEmpty()) {
                            fullReasoning.append(reasoning);
                            data.put("reasoning", reasoning);
                            hasData = true;
                        }
                        
                        // 发送正式内容
                        if (content != null && !content.isEmpty()) {
                            fullContent.append(content);
                            data.put("content", content);
                            hasData = true;
                        }
                        
                        // 只在有数据时发送
                        if (hasData) {
                            String jsonData = data.toJSONString();
                            emitter.send(SseEmitter.event()
                                .id(String.valueOf(System.currentTimeMillis()))
                                .data(jsonData, org.springframework.http.MediaType.APPLICATION_JSON));
                        }
                    } catch (IOException e) {
                        log.error("发送SSE数据失败", e);
                    }
                });
                
                // 保存会话和消息
                DeepseekChatSessions session = null;
                if (StrUtil.isBlankOrUndefined(sessionId)) {
                    sessionId = UUID.randomUUID().toString();
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
                    session = iDeepseekChatSessionsService.getById(sessionId);
                }
                
                // 保存AI回复消息
                DeepseekChatMessages aiMessage = new DeepseekChatMessages();
                aiMessage.setSessionId(sessionId);
                aiMessage.setCreatetime(new Date());
                aiMessage.setContent(fullContent.toString());
                aiMessage.setReasoningContent(fullReasoning.toString()); // 保存思考内容
                aiMessage.setRole("assistant");
                newMessages.add(aiMessage);
                iDeepseekChatMessagesService.saveBatch(newMessages);
                
                // 发送完成事件，包含sessionId
                JSONObject completeData = new JSONObject();
                completeData.put("id", sessionId);
                completeData.put("content", "[DONE]");
                log.info("发送SSE完成事件: {}", completeData.toJSONString());
                emitter.send(SseEmitter.event()
                    .id("complete")
                    .data(completeData.toJSONString(), org.springframework.http.MediaType.APPLICATION_JSON));
                
                emitter.complete();
            } catch (Exception e) {
                log.error("流式请求失败", e);
                try {
                    // 发送错误信息给前端
                    JSONObject errorData = new JSONObject();
                    errorData.put("error", e.getMessage());
                    emitter.send(SseEmitter.event().data(errorData.toJSONString(), org.springframework.http.MediaType.APPLICATION_JSON));
                } catch (IOException ex) {
                    log.error("发送错误信息失败", ex);
                }
                emitter.completeWithError(e);
            }
        });
        
        // 设置超时和错误回调
        emitter.onTimeout(() -> {
            log.warn("SSE连接超时");
            emitter.complete();
        });
        
        emitter.onError(e -> {
            log.error("SSE连接错误", e);
        });
        
        return emitter;
    }

    public Object completionsOld(UserInfo userInfo ,SysChartCompletionRequestDTO dto){
        try {
            MediaType mediaType = MediaType.parse("application/json");
            ObjectMapper objectMapper = new ObjectMapper();
            List<DeepseekChatMessages> messages = iDeepseekChatMessagesService.lambdaQuery()
                    .eq(DeepseekChatMessages::getSessionId, dto.getSessionId())
                    .orderBy(true,true,DeepseekChatMessages::getCreatetime)
                    .list();
            List<DeepseekChatMessages> newMessages = new ArrayList<DeepseekChatMessages>();
            List<DeepSeekMessage> contentList=new ArrayList<DeepSeekMessage>();
            for(DeepseekChatMessages message:messages){
                DeepSeekMessage e=new DeepSeekMessage();
                e.setContent(message.getContent());
                e.setRole(message.getRole());
                contentList.add(e);
            }
            for(DeepSeekMessage message:dto.getMessages()){
                DeepseekChatMessages e=new DeepseekChatMessages();
                e.setSessionId(dto.getSessionId());
                e.setCreatetime(new Date());
                e.setContent(message.getContent());
                e.setRole(message.getRole());
                newMessages.add(e);
                contentList.add(message);
            }
            dto.setMessages(contentList);
            String jsonBody = objectMapper.writeValueAsString(dto);
            RequestBody body = RequestBody.create(mediaType, jsonBody);
            Response response = requestDeepSeek(body);
            if(response==null||!response.isSuccessful()||response.body()==null){
                throw new BizException("请求失败");
            }
            String result= response.body().string();
            JSONObject resultJson = GeneralUtil.getJsonObject(result);
            String sessionId = resultJson.getString("id");
            JSONArray choices = resultJson.getJSONArray("choices");
            for(int i=0;i<choices.size();i++){
                JSONObject message = choices.getJSONObject(i);
                JSONObject messageInfo = message.getJSONObject("message");
                String content= messageInfo.getString("content");
                String role= messageInfo.getString("role");
                DeepseekChatMessages e=new DeepseekChatMessages();
                e.setSessionId(dto.getSessionId());
                e.setCreatetime(new Date());
                e.setContent(content);
                e.setRole(role);
                newMessages.add(e);
            }
            DeepseekChatSessions session=null;
            if(StrUtil.isBlankOrUndefined(dto.getSessionId())){
                for(DeepseekChatMessages message:newMessages){
                    message.setSessionId(sessionId);
                }
                session=new DeepseekChatSessions();
                session.setId(sessionId);
                session.setUserid(userInfo.getId());
                session.setOpttime(new Date());
                session.setModel(dto.getModel());
                session.setCreatetime(new Date());
                session.setTitle(dto.getMessages().get(0).getContent());
                this.iDeepseekChatSessionsService.save(session);
            }else{
                resultJson.put("id", dto.getSessionId());
                session=iDeepseekChatSessionsService.getById(dto.getSessionId());
            }
            iDeepseekChatMessagesService.saveBatch(newMessages);
            if(messages!=null){
                messages.addAll(newMessages);
                session.setMessages(messages);
            }else{
                session.setMessages(newMessages);
            }
            return session;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object getSession(UserInfo userInfo) {
        LambdaQueryChainWrapper<DeepseekChatSessions> query = iDeepseekChatSessionsService.lambdaQuery();
        query.eq(DeepseekChatSessions::getUserid, userInfo.getId());
        query.orderBy(true,false,DeepseekChatSessions::getCreatetime);
        IPage<DeepseekChatSessions> page=new Page<DeepseekChatSessions>();
        page.setCurrent(0);
        page.setSize(1000);
        query.page(page);
        List<DeepseekChatSessions> list = query.list();
        for(DeepseekChatSessions item:list){
            List<DeepseekChatMessages> messages = iDeepseekChatMessagesService.lambdaQuery().eq(DeepseekChatMessages::getSessionId, item.getId()).list();
            item.setMessages(messages);
        }
        return list;
    }

    @Override
    public void deleteSession(UserInfo userInfo, String sessionId) {
        // 删除会话下的所有消息
        iDeepseekChatMessagesService.lambdaUpdate()
                .eq(DeepseekChatMessages::getSessionId, sessionId)
                .remove();
        // 删除会话
        iDeepseekChatSessionsService.removeById(sessionId);
    }

    @Override
    public Object getKey(UserInfo userInfo) {
        return iDeepseekChatMessagesService.getKey(userInfo.getId());
    }

    private static StringBuilder reasoningContent = new StringBuilder();
    private static StringBuilder finalContent = new StringBuilder();
    private static boolean isFirstPrint = true;
    private  void handleGenerationResult(GenerationResult message) {
        String reasoning = message.getOutput().getChoices().get(0).getMessage().getReasoningContent();
        String content = message.getOutput().getChoices().get(0).getMessage().getContent();
        if (reasoning != null && !reasoning.isEmpty()) {
            reasoningContent.append(reasoning);
            if (isFirstPrint) {
                System.out.println("====================思考过程====================");
                isFirstPrint = false;
            }
            System.out.print(reasoning);
        }
        if (content != null && !content.isEmpty()) {
            finalContent.append(content);
            if (!isFirstPrint) {
                System.out.println("\n====================完整回复====================");
                isFirstPrint = true;
            }
            System.out.print(content);
        }
    }
    private GenerationParam buildGenerationParam(Message userMsg) {
        return GenerationParam.builder()
                // 若没有配置环境变量，请用阿里云百炼API Key将下行替换为：.apiKey("sk-xxx")
                .apiKey(token)
                .model("deepseek-v3.2")
                .enableThinking(true)
                .incrementalOutput(true)
                .resultFormat("message")
                .messages(Arrays.asList(userMsg))
                .build();
    }
    public   void streamCallWithMessage(Generation gen, Message userMsg)
            throws NoApiKeyException, ApiException, InputRequiredException {
        GenerationParam param = buildGenerationParam(userMsg);
        Flowable<GenerationResult> result = gen.streamCall(param);
        result.blockingForEach(message -> handleGenerationResult(message));
    }

}
