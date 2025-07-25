package com.wimoor.erp.thirdparty.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("t_erp_thirdparty_warehouse_bind")
public class ThirdPartyWarehouseBind {
    
    @TableId(value= "thirdparty_warehouse_id")
    private String thirdpartyWarehouseId;

    @TableField(value= "local_warehouse_id")
    private String localWarehouseId;

    @TableField(value= "operator")
    private String operator;

    @TableField(value= "opttime")
    private Date opttime;
}