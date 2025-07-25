package com.wimoor.erp.common.pojo.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.TableField;

import lombok.Data;
import lombok.EqualsAndHashCode;
 
 

@Data
@EqualsAndHashCode(callSuper = true)
public class ErpBaseForm extends BaseEntity{
	 
    /**
	 * 
	 */
	private static final long serialVersionUID = 6260547880376601946L;

	@TableField(value= "number")
    private String number;

    @TableField(value= "auditor")
    private String auditor;

    @TableField(value= "creator")
    private String creator;

    @TableField(value= "audittime")
    private Date audittime;

    @TableField(value= "shopid")
    private String shopid;
    
    @TableField(value= "auditstatus")
	protected Integer auditstatus;

    @TableField(value= "opttime")
    private Date opttime;

    @TableField(value= "operator")
    private String operator;
    
    @TableField(value="createdate")
	private Date createdate;
    
    
    @TableField(exist = false)
    List<ErpBaseFormEntry> entryList=new ArrayList<ErpBaseFormEntry>();
    public void addEntry(ErpBaseFormEntry entry){
    	entryList.add(entry);
    }
    
    public void addEntryItem( String warehouseid, String materialid,Integer amount){
    	ErpBaseFormEntry entry = new ErpBaseFormEntry();
    	entry.setWarehouseid(warehouseid);
    	entry.setMaterialid(materialid);
    	entry.setAmount(amount);
    	entryList.add(entry);
    }

	 
    
}
