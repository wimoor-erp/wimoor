package com.wimoor.sys.tool.service;

import com.wimoor.sys.tool.pojo.entity.DeepseekChatMessages;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author liufei
* @description 针对表【t_sys_tool_deepseek_chat_messages】的数据库操作Service
* @createDate 2025-08-30 11:06:36
*/
public interface IDeepseekChatMessagesService extends IService<DeepseekChatMessages> {

    Object getKey(String id);
}
