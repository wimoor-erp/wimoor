package com.wimoor.erp.ship.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("t_erp_ship_transcompany_services_zhihui")
public class ShipTransCompayServicesZhihui {
    
    @TableId(value= "code")
    private String code;

    @TableField(value= "name")
    private String name;

    @TableField(value= "ftype")
    private String ftype;

    @TableField(value= "apiid")
    private Integer apiid;
     
}