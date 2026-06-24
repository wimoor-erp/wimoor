package com.wimoor.sys.tool.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 用于存放用户文件关联
 * @TableName t_sys_tool_large_file_user
 */
@TableName(value ="t_sys_tool_large_file_user")
@Data
public class LargeFileUser {
    /**
     * 主键ID
     */
    @TableId
    private String id;

    /**
     * 用户ID
     */
    private String userid;

    /**
     * 店铺ID
     */
    private String shopid;

    /**
     * 文件ID
     */
    private String fileid;

    /**
     * 文件名称
     */
    private String name;

    /**
     * 创建时间
     */
    private Date createtime;
}