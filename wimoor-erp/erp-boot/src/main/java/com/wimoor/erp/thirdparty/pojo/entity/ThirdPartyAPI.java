package com.wimoor.erp.thirdparty.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

import java.math.BigInteger;
import java.util.Date;

@Data
@TableName("t_erp_thirdparty_api")
public class ThirdPartyAPI {
    
    @TableId(value= "id")
    private String id;

    @TableField(value= "api")
    private String api;

    @TableField(value= "name")
    private String name;

    @TableField(value= "appsecret")
    private String appsecret;

    @TableField(value= "appkey")
    private String appkey;

    @TableField(value= "token")
    private String token;

    @TableField(value= "shopid")
    private String shopid;

    @TableField(value= "url")
    private String url;
     
    @TableField(value= "`system`")
    private String system;

    @TableField(exist = false)
    private String systemName;

    @TableField(exist = false)
    private ThirdPartySystem systemEntity;

    @TableField(value= "`operator`")
    private BigInteger operator;

    @TableField(value= "`opttime`")
    private Date opttime;

    @TableField(value= "`isdelete`")
    private Boolean isdelete;

}