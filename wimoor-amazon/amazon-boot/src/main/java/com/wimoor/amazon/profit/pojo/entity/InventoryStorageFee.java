package com.wimoor.amazon.profit.pojo.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@TableName("t_inventoryStorageFee")  
@ApiModel(value="InventoryStorageFee对象", description="产品Storage费用")
public class InventoryStorageFee implements Serializable{
	/**
	 * 
	 */
	@TableField(exist=false)
	private static final long serialVersionUID = 6954478381989393821L;
 
	@TableId(value= "id")
    private Integer id;

	@TableField(value= "month")
    private String month;

	@TableField(value= "price")
    private BigDecimal price;

	@TableField(value= "country")
    private String country;

	@TableField(value= "isStandard")
    private Boolean isstandard;
 
}