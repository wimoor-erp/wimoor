package com.wimoor.erp.purchase.pojo.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("t_erp_purchase_warahouse_status")
public class PurchaseWareHouseStatus {
	
 
	@TableId(value= "warehouseid")
    private String warehouseid;

	@TableField(value= "purchase_status")
    private Integer purchaseStatus;

	@TableField(value= "assbly_status")
    private Integer assblyStatus;

	@TableField(value= "userid")
    private String userid;

	@TableField(value= "opptime")
    private Date opptime;
 
}