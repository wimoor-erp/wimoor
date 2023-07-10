package com.wimoor.erp.assembly.pojo.entity;
 
 
import java.util.LinkedList;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.erp.common.pojo.entity.ErpBaseForm;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_erp_assembly_form")
public class AssemblyForm extends ErpBaseForm {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2794663947045469220L;

	@TableField(value="warehouseid")
	private  String warehouseid;
	
	@TableField(value="mainmid")
	private String mainmid;
	
	@NotNull(message="数量不能为空")
	@TableField(value="amount")
	private Integer amount;
	
	@TableField(value="amount_handle")
	private Integer amountHandle;
	
	@Size(max=500,message="备注的长度不能超过50个字符")
	@TableField(value="remark")
	private String remark;
    
	@TableField(value="planitem")
	private String planitem;
 
	@TableField(value="ftype")
	private String ftype;
 

 
	public String getAuditstatusName() {
		if(auditstatus==0)return "未提交";
		if(auditstatus==1)return "待组装";
		if(auditstatus==2)return "组装中";
		if(auditstatus==3)return "已完成";
		if(auditstatus==4)return "已终止";
		return "";
	}
 
	public static String getAuditstatusName(int auditstatus) {
    	if(auditstatus==0)return "未提交";
		if(auditstatus==1)return "待组装";
		if(auditstatus==2)return "组装中";
		if(auditstatus==3)return "已完成";
		if(auditstatus==4)return "已终止";
		return "";
	}
 
	public Integer getAmountHandle() {
		if(amountHandle==null)return 0;
		return amountHandle;
	}

	@TableField(exist = false)
    private List<AssemblyFormEntry> myEntrylist=new LinkedList<AssemblyFormEntry>();
    
    public void addEntry(AssemblyFormEntry entry){
    	myEntrylist.add(entry);
    }
    
	
	
}
