package com.wimoor.feishu.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigInteger;
import java.util.Date;

@Data
@TableName("t_sys_feishu_table")
public class SysFeishuTable {

    @TableId(value = "id", type=IdType.AUTO)
    private BigInteger id;
    
    @TableField(value = "`appid`")
    private String appid;

    @TableField(value = "`name`")
    private String name;
    
    @TableField(value = "`apptoken`")
    private String apptoken;
    
    @TableField(value = "`tableid`")
    private String tableid;

    @TableField(value = "`viewid`")
    private String viewid;
    
    @TableField(value = "`fieldjson`")
    private String fieldjson;

    @TableField(value = "`url`")
    private String url;
    
    @TableField(value = "`feedback`")
    private String feedback;
    
    @TableField(value = "`fieldkey`")
    private String fieldkey;
    
    @TableField(value = "`pagenum`")
    private String pagenum;
    
    @TableField(value = "`pagesize`")
    private String pagesize;

    @TableField(value = "`opttime`")
    private Date opttime;
    
    @TableField(value = "`operator`")
    private BigInteger operator;

    @TableField(value = "`disabled`")
    private Boolean disabled;
    
}
