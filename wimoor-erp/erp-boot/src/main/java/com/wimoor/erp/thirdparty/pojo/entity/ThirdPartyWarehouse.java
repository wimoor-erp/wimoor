package com.wimoor.erp.thirdparty.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_erp_thirdparty_warehouse")
public class ThirdPartyWarehouse {
    
    @TableId(value= "id")
    private String id;

    @TableField(value= "code")
    private String code;

    @TableField(value= "name")
    private String name;

    @TableField(value= "country")
    private String country;

    @TableField(value= "ext")
    private String ext;

    @TableField(value= "shopid")
    private String shopid;

    @TableField(value= "api")
    private String api;

}