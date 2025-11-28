package com.wimoor.sys.tool.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wimoor.admin.common.exception.BizException;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.HttpClientUtil;
import com.wimoor.common.user.UserInfo;
import com.wimoor.sys.tool.pojo.dto.SysChartCompletionRequestDTO;
import com.wimoor.sys.tool.pojo.entity.DeepSeekMessage;
import com.wimoor.sys.tool.pojo.entity.DeepseekChatMessages;
import com.wimoor.sys.tool.pojo.entity.DeepseekChatSessions;
import com.wimoor.sys.tool.service.IDeepseekChatMessagesService;
import com.wimoor.sys.tool.service.IDeepseekChatSessionsService;
import com.wimoor.sys.tool.service.ISysDeepSeekService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import okhttp3.*;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class SysDeepSeekServiceImpl implements ISysDeepSeekService {
    final IDeepseekChatSessionsService iDeepseekChatSessionsService;
    final IDeepseekChatMessagesService iDeepseekChatMessagesService;
    @Value("${deepseek.token}")
    private String token;

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
                .url("https://api.deepseek.com/chat/completions")
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
    public Object getKey(UserInfo userInfo) {
        return iDeepseekChatMessagesService.getKey(userInfo.getId());
    }
}
