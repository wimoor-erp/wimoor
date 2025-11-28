package com.wimoor.sys.tool.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName t_sys_tool_deepseek_chat_messages
 */
@TableName(value ="t_sys_tool_deepseek_chat_messages")
@Data
public class DeepseekChatMessages {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private String id;

    /**
     * 
     */
    @TableField(value="session_id")
    private String sessionId;

    /**
     * 
     */
    private String role;

    /**
     * 支持HTML格式的富文本内容
     */
    private String content;

    /**
     * 原始文本内容（用于编辑）
     */
    @TableField(value="reasoning_content")
    private String reasoningContent;

    /**
     * 
     */
    @TableField(value="message_type")
    private String messageType;

    /**
     * 
     */
    @TableField(value="format_type")
    private String formatType;

    /**
     * 代码语言类型
     */
    @TableField(value="code_language")
    private String codeLanguage;

    /**
     * 
     */
    private Integer tokens;

    /**
     * 
     */
    @TableField(value="createtime")
    private Date createtime;

    /**
     * 附加元数据，如代码高亮信息、文件信息等
     */
    private Object metadata;
}