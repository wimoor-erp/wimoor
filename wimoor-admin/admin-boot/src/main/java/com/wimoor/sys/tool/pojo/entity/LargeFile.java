package com.wimoor.sys.tool.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 用于存放Image
 * @TableName t_sys_tool_large_file
 */
@TableName(value ="t_sys_tool_large_file")
@Data
public class LargeFile {
    /**
     * 图片ID
     */
    @TableId
    private String id;

    /**
     * 图片本地位置
     */
    private String location;

    private String shopid;

    /**
     * 
     */
    private Date opttime;

    private String ftype;
}