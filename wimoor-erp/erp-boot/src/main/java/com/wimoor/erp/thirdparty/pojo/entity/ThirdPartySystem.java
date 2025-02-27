package com.wimoor.erp.thirdparty.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigInteger;
import java.util.Date;

@Data
@TableName("t_erp_thirdparty_system")
public class ThirdPartySystem {
    
    @TableId(value= "id")
    private String id;

    @TableField(value= "name")
    private String name;

    @TableField(value= "cname")
    private String cname;

    @TableField(value= "support")
    private String support;

    @TableField(value= "url")
    private String url;

    @TableField(value= "apidoc")
    private String apidoc;

    @TableField(value= "classz")
    private String classz;

    @TableField(value= "needkey")
    private Boolean needkey;
     
    @TableField(value= "`needtoken`")
    private Boolean needtoken;

}