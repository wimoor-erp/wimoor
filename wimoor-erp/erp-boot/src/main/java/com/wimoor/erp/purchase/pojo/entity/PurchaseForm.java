package com.wimoor.erp.purchase.pojo.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.erp.common.pojo.entity.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_erp_purchase_form")
public class PurchaseForm extends BaseEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = -387575535893537435L;

	@Pattern(regexp="\\w+",message="编码只能含字符数字下划线")
    @TableField(value= "number")
    private String number;

    @TableField(value="warehouseid")
    private String warehouseid;
    
    @Size(max=500,message="备注不能超过500个字符")
    @TableField(value= "remark")
    private String remark;

    @TableField(value= "creator")
    private String creator;

    @TableField(value= "createdate")
    private Date createdate;
    
    @TableField(value= "shopid")
    private String shopid;
    
    @TableField(value= "purchaser")
    private String purchaser;
    
    @TableField(value= "batch")
    private String batch;
    
    @TableField(value= "groupid")
    private String groupid;
    
    
    @TableField(exist = false) 
    private List<PurchaseFormEntry> entrylist=new ArrayList<PurchaseFormEntry>();
    
    
 
    public  void addEntry(PurchaseFormEntry entry) {
    	    entrylist.add(entry);
	}
 
    
}