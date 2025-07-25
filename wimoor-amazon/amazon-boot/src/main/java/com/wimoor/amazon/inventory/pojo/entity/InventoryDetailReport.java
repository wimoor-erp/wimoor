package com.wimoor.amazon.inventory.pojo.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.amazon.common.pojo.entity.BaseEntity;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
 

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_amz_rpt_inventory_detail")  
@ApiModel(value="InventoryDetailReport对象", description="库存详细")
public class InventoryDetailReport extends BaseEntity {

	private static final long serialVersionUID = -5642676895596583894L;

	@TableField(value= "byday")
    private Date byday;
	
	@TableField(value= "bytime")
    private Date bytime;
	
	@TableField(value= "authid")
    private String authid;

	@TableField(value= "fnsku")
    private String fnsku;

	@TableField(value= "asin")
    private String asin;
	
	@TableField(value= "msku")
    private String msku;

	@TableField(value= "country")
    private String country;

	@TableField(value= "eventType")
    private String eventType;

	@TableField(value= "referenceID")
    private String referenceID;

	@TableField(value= "fulfillmentCenter")
    private String fulfillmentCenter;
	
	@TableField(value= "disposition")
    private String disposition;
	
	@TableField(value= "reason")
    private String reason;
	
	@TableField(value= "quantity")
    private Integer quantity;
	
	@TableField(value= "reconciledQuantity")
    private Integer reconciledQuantity;
	
	@TableField(value= "unreconciledQuantity")
    private Integer unreconciledQuantity;
	
	@TableField(value= "refreshtime")
    private Date refreshtime;
	
}