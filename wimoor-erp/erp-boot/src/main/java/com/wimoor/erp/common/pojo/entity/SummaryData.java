package com.wimoor.erp.common.pojo.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_erp_summary_data")
public class SummaryData extends BaseEntity {
   
	/**
	 * 
	 */
	private static final long serialVersionUID = 3807356801546670654L;

	@TableField(value="ftype")
    private String ftype;

	@TableField(value="value")
    private BigDecimal value;

	@TableField(value="shopid")
    private String shopid;
	
	@TableField(value="updatetime")
    private Date updatetime;
	
	@TableField(value="mapdata")
	private String mapdata;
 
    
}