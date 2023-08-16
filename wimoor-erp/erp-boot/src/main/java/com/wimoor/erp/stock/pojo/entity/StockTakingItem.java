package com.wimoor.erp.stock.pojo.entity;
 
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.erp.common.pojo.entity.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_erp_stocktaking_item")
public class StockTakingItem extends BaseEntity{
    /**
	 * 
	 */
	private static final long serialVersionUID = -6798506257200460224L;

	@TableField(value= "stocktakingid")
    private String stocktakingid;

    @TableField(value= "materialid")
    private String materialid;

    @TableField(value= "amount")
    private Integer amount;

    @TableField(value= "warehouseid")
    private String warehouseid;
    
    @TableField(value= "overamount")
    private Integer overamount;

	@TableField(value= "lossamount")
    private Integer lossamount;
     
    
}