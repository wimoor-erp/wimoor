package com.wimoor.erp.material.pojo.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.erp.common.pojo.entity.ErpBaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_erp_stockCycle")
public class StockCycle extends ErpBaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2965474223987230250L;

	@TableField(value= "warehouseid")
    private String warehouseid;

	@TableField(value= "materialid")
    private String materialid;

	@TableField(value= "stockingCycle")
    private Integer stockingcycle;
	
	@TableField(value="min_cycle")
	private Integer mincycle;
	
	@TableField(value="operator")
	private String operator;
	
	@TableField(value="opttime")
	private Date opttime;
	 

    public Integer getStockingcycle() {
        return stockingcycle == null ? 0 : stockingcycle;
    }
 
}