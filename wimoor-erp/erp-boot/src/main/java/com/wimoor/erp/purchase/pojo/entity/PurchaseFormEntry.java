package com.wimoor.erp.purchase.pojo.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.erp.common.pojo.entity.ErpBaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_erp_purchase_form_entry")
public class PurchaseFormEntry extends ErpBaseEntity{
    /**
	 * 
	 */
	private static final long serialVersionUID = 6151297059089122963L;

	@TableField(value= "formid")
    private String formid;

    @TableField(value= "materialid")
    private String materialid;

    @TableField(value= "supplier")
    private String supplier;

    @TableField(value= "amount")
    private Integer amount;

    @NotNull(message="单价不能为空")
    @TableField(value= "itemprice")
    private BigDecimal itemprice;

    @TableField(value= "audittime")
    private Date audittime;

    @TableField(value= "auditor")
    private String auditor;

    @TableField(value= "orderprice")
    private BigDecimal orderprice;

    @TableField(value= "auditstatus")
    private Integer auditstatus;

    @TableField(value= "planitemid")
    private String planitemid;

    @TableField(value= "totalpay")
    private BigDecimal totalpay;
    
    @TableField(value= "totalre")
    private Integer totalre;
    
    @TableField(value= "totalin")
    private Integer totalin;
    
    @TableField(value= "totalch")
    private Integer totalch;
    
    @TableField(value= "paystatus")
    private Integer paystatus;
    
    @TableField(value= "inwhstatus")
    private Integer inwhstatus;
    
    @TableField(value= "deliverydate")
    private Date deliverydate;
    
    @TableField(value= "closepaydate")
    private Date closepaydate;
    
    @TableField(value= "closerecdate")
    private Date closerecdate;
    
    @TableField(value="remark")
    private String remark;
    
    @TableField(value= "ischange")
    private boolean ischange;
    
    @TableField(exist=false)
    List<PurchaseFormPayment> payList;

	public BigDecimal getTotalpay() {
		if(totalpay==null)return new BigDecimal("0");
		return totalpay;
	}

	 
	public Integer getTotalre() {
		if(totalre==null)return 0;
		return totalre;
	}

 
	public Integer getTotalin() {
		if(totalin==null)return 0;
		return totalin;
	}

 

	public Integer getTotalch() {
		if(totalch==null)return 0;
		return totalch;
	}

	 
 
   

    
 
    public BigDecimal getOrderprice() {
    	if(orderprice==null)return new BigDecimal("0");
        return orderprice;
    }

 
  
    public String getAuditstatusName() {
         if(auditstatus==0)return "作废";
    	 if(auditstatus==1)return "待审核";
         if(auditstatus==2)return "审核通过";
         if(auditstatus==3)return "已完成";
		return "";
    }
 
    public static String getAuditstatusName(PurchaseFormEntry pojo) {
         if(pojo.getAuditstatus()==0)return "作废";
    	 if(pojo.getAuditstatus()==1)return "待审核";
         if(pojo.getAuditstatus()==2)return "审核通过";
         if(pojo.getAuditstatus()==3)return "已完成";
		 return "";
    }
     
    public static String getAuditstatusName(int  auditstatus) {
        if(auditstatus==0)return "作废";
   	    if(auditstatus==1)return "待审核";
        if(auditstatus==2)return "审核通过";
        if(auditstatus==3)return "已完成";
		 return "";
   }
    
    public void setPlanitemid(String planitemid) {
        this.planitemid = planitemid == null ? null : planitemid.trim();
    }

	 

	 
	
    
}