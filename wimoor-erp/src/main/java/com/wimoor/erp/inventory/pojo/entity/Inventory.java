package com.wimoor.erp.inventory.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.erp.common.pojo.entity.ErpBaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_erp_inventory")
public class Inventory  extends ErpBaseEntity{
 
    /**
	 * 
	 */
	private static final long serialVersionUID = -1868209780900073010L;

	@TableField(value= "warehouseid")
    private String warehouseid;

    @TableField(value= "shopid")
    private String shopid;

    @TableField(value= "materialid")
    private String materialid;

    @TableField(value= "quantity")
    private Integer quantity;
 
    @TableField(value="status")
    private String status;
    
}