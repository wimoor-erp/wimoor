package com.wimoor.erp.purchase.pojo.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.NotNull;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_erp_purchase_form_entry_history")
public class PurchaseFormEntryHistory {
    /**
	 * 
	 */
	@TableField(value= "id")
    private String id;

	@TableField(value= "formid")
    private String formid;

    @TableField(value= "materialid")
    private String materialid;

    @TableField(value= "supplier")
    private String supplier;
    
    @TableField(value= "operator")
    private String operator;

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
    
    @TableField(value= "opttime")
    private Date opttime;
    
    @TableField(value= "closerecdate")
    private Date closerecdate;
    
    @TableField(value="remark")
    private String remark;
    
    @TableField(value= "ischange")
    private boolean ischange;
    
   

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
         if(auditstatus==0)return "退回";
    	 if(auditstatus==1)return "待审核";
         if(auditstatus==2)return "审核通过";
         if(auditstatus==3)return "已完成";
         if(totalin==0 && totalpay.floatValue()<0.01 && auditstatus==3) return "已撤销";
		return "";
    }
 
    public static String getAuditstatusName(PurchaseFormEntryHistory pojo) {
         if(pojo.getAuditstatus()==0)return "退回";
    	 if(pojo.getAuditstatus()==1)return "待审核";
         if(pojo.getAuditstatus()==2)return "审核通过";
         if(pojo.getAuditstatus()==3)return "已完成";
         if(pojo.getTotalin()==0 && pojo.getTotalpay().floatValue()<0.01 && pojo.getAuditstatus()==3) return "已撤销";
		 return "";
    }
     
    public void setPlanitemid(String planitemid) {
        this.planitemid = planitemid == null ? null : planitemid.trim();
    }

	 

	 
	
    
}