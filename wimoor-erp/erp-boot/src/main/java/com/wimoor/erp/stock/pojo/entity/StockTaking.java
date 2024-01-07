package com.wimoor.erp.stock.pojo.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.Size;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.erp.common.pojo.entity.ErpBaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_erp_stocktaking")
public class StockTaking extends ErpBaseEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7922906052422128069L;

	@TableField(value= "number")
    private String number;

	@TableField(value= "warehouseid")
    private String warehouseid;

	@TableField(value= "whtotalamount")
    private Integer whtotalamount;

	@TableField(value= "ftype")
    private Integer ftype;
	
	@TableField(value= "whtotalprice")
    private BigDecimal whtotalprice;

	@TableField(value= "overamount")
    private Integer overamount;

	@TableField(value= "lossamount")
    private Integer lossamount;

	@TableField(value= "overprice")
    private BigDecimal overprice;

	@TableField(value= "lossprice")
    private BigDecimal lossprice;

	@Size(max=500,message="备注不能超过500个字符")
	@TableField(value= "remark")
    private String remark;

	@TableField(value= "creator")
    private String creator;

	@TableField(value= "createdate")
    private Date createdate;
	
	@TableField(value= "shopid")
    private String shopid;
	
	@TableField(value= "isworking")
    private boolean isworking;
 
	@TableField(exist=false)
    private boolean isNew;
	
	@TableField(exist=false)
    private List<StocktakingShelf> shelflist;
	
	@TableField(exist=false)
	List<StocktakingWarehouse> warehouselist;
}