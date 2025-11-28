package com.wimoor.sys.tool.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName t_sys_tool_notepad
 */
@TableName(value ="t_sys_tool_notepad")
@Data
public class Notepad {
    /**
     * 
     */
    private String id;

    /**
     * 
     */
    private String shopid;

    /**
     * 
     */
    private String title;

    /**
     * 
     */
    private String sku;

    /**
     * 
     */
    private String content;

    /**
     * 
     */
    private String operator;

    /**
     * 
     */
    private Date opttime;
}