package com.wimoor.sys.tool.mapper;

import com.wimoor.sys.tool.pojo.entity.DeepseekChatMessages;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
* @author liufei
* @description 针对表【t_sys_tool_deepseek_chat_messages】的数据库操作Mapper
* @createDate 2025-08-30 11:06:36
* @Entity com.wimoor.sys.tool.pojo.entity.DeepseekChatMessages
*/
@Mapper
public interface DeepseekChatMessagesMapper extends BaseMapper<DeepseekChatMessages> {

    List<Map<String,Object>> getSearchKey(@Param("userid") String  userid);
}




