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
@TableName("t_inventory_reserved_report")  
@ApiModel(value="InventoryReservedReport对象", description="预留库存报表")
public class InventoryReservedReport extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 959115192670449285L;

	@TableField(value= "sku")
    private String sku;

	@TableField(value= "marketplaceid")
    private String marketplaceid;

    @TableField(value= "byday")
    private Date byday;

    @TableField(value= "fnsku")
    private String fnsku;

	@TableField(value= "asin")
    private String asin;

	@TableField(value= "reserved_qty")
    private Integer reservedQty;

	@TableField(value= "reserved_customerorders")
    private Integer reservedCustomerorders;

	@TableField(value= "reserved_fc_transfers")
    private Integer reservedFcTransfers;

	@TableField(value= "reserved_fc_processing")
    private Integer reservedFcProcessing;

	@TableField(value= "amazonAuthId")
    private String amazonAuthId;
 
}