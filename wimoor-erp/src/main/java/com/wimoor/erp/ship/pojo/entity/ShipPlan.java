package com.wimoor.erp.ship.pojo.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.erp.common.pojo.entity.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_erp_ship_plan")
public class ShipPlan  extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7565063652788755001L;

	@TableField(value="shopid")
	private String shopid;
	
    @TableField(value= "warehouseid")
    private String warehouseid;

    @TableField(value= "amazongroupid")
    private String amazongroupid;

    @TableField(value= "totalnum")
    private Integer totalnum;

    @TableField(value= "totalamount")
    private Integer totalamount;
    
    @TableField(value= "goodsworth")
    private BigDecimal goodsworth;

    @TableField(value= "totalweight")
    private BigDecimal totalweight;
    
    @TableField(value= "opttime")
    private Date opttime;

    @TableField(value= "operator")
    private String operator;

    @TableField(value= "status")
    private int status;
   
    @TableField(exist = false)
    List<ShipPlanSub> sublist = new ArrayList<ShipPlanSub>();
    
    @TableField(exist = false)
	List<ShipPlanItem> sublistitem = new ArrayList<ShipPlanItem>();
 
    
}