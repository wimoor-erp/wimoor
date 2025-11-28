package com.wimoor.sys.tool.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * 聊天会话表
 * @TableName t_sys_tool_deepseek_chat_sessions
 */
@TableName(value ="t_sys_tool_deepseek_chat_sessions")
@Data
public class DeepseekChatSessions {
    /**
     * 会话ID
     */
    @TableId
    private String id;

    /**
     * 用户ID
     */
    private String userid;

    /**
     * 会话标题
     */
    private String title;

    /**
     * 模型名称
     */
    private String model;

    /**
     * 系统提示词
     */
    @TableField(value="system_prompt")
    private String systemPrompt;

    /**
     * 总消息数
     */
    @TableField(value="total_messages")
    private Integer totalMessages;

    /**
     * 总token消耗
     */
    @TableField(value="total_tokens")
    private Integer totalTokens;

    /**
     * 创建时间
     */
    private Date createtime;

    /**
     * 更新时间
     */
    private Date opttime;

    /**
     * 是否活跃
     */
    private Integer isActive;

    @TableField(exist = false)
    List<DeepseekChatMessages> messages;
}