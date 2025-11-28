package com.wimoor.sys.tool.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.sys.tool.pojo.entity.DeepseekChatMessages;
import com.wimoor.sys.tool.service.IDeepseekChatMessagesService;
import com.wimoor.sys.tool.mapper.DeepseekChatMessagesMapper;
import org.springframework.stereotype.Service;

/**
* @author liufei
* @description 针对表【t_sys_tool_deepseek_chat_messages】的数据库操作Service实现
* @createDate 2025-08-30 11:06:36
*/
@Service
public class DeepseekChatMessagesServiceImpl extends ServiceImpl<DeepseekChatMessagesMapper, DeepseekChatMessages>
    implements IDeepseekChatMessagesService {

    @Override
    public Object getKey(String userid) {
        return this.baseMapper.getSearchKey(userid);
    }
}




