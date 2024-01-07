package com.wimoor.erp.ship.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("t_erp_ship_transcompany_api")
public class ShipTransCompayAPI {
    
    @TableId(value= "id")
    private Integer id;

    @TableField(value= "api")
    private String api;

    @TableField(value= "name")
    private String name;

    @TableField(value= "openkey")
    private String openkey;

    @TableField(value= "openaccount")
    private String openaccount;

    @TableField(value= "url")
    private String url;
     
    @TableField(value= "`system`")
    private String system;
}