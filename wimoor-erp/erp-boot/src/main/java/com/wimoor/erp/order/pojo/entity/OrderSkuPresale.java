package com.wimoor.erp.order.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.erp.common.pojo.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_erp_order_sku_presale")
public class OrderSkuPresale extends BaseEntity {
    /**
	 * 
	 */
	private static final long serialVersionUID = -8401333489880986919L;

	@TableField(value= "sku")
	private String sku;

    @TableField(value= "warehouseid")
    private String warehouseid;

	@TableField(value= "date")
    private Date date;

	@TableField(value= "quantity")
    private Integer quantity;

	@TableField(value= "operator")
    private String operator;

	@TableField(value= "opttime")
    private Date opttime;
	
}