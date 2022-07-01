package com.wimoor.erp.purchase.pojo.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.erp.common.pojo.entity.ErpBaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_erp_purchase_plan")
public class PurchasePlan  extends ErpBaseEntity{
 
    /**
	 * 
	 */
	private static final long serialVersionUID = -1405555393100029595L;

	@TableField(value= "number")
    private String number;

    @TableField(value= "status")
    private Byte status;

    @TableField(value= "creator")
    private String creator;

    @TableField(value= "shopid")
    private String shopid;

    @TableField(value= "createtime")
    private Date createtime;
    
    @TableField(value= "totalbuyqty")
    Integer totalbuyqty;
    
    @TableField(value= "totalpayprice")
    BigDecimal totalpayprice;
    
    @TableField(value= "totalnum")
    Integer totalnum;
    
    @TableField(exist = false)
    PurchasePlanSub po=null;
    
    @TableField(exist = false)
    PurchasePlanSub ao=null;
    
    @TableField(exist = false)
    Date caltime=null;
    
    @TableField(exist = false)
    Boolean isrun=false;

	 

	public Integer getTotalbuyqty() {
		if(totalbuyqty==null)return 0;
		return totalbuyqty;
	}

	 
	public BigDecimal getTotalpayprice() {
		if(totalpayprice==null)return new BigDecimal("0");
		return totalpayprice;
	}

	 
 

}