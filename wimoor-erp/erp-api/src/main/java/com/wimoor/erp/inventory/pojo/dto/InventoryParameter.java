package com.wimoor.erp.inventory.pojo.dto;

import java.io.Serializable;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.wimoor.util.UUIDUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
 
@Data
public class InventoryParameter   implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1188098156847164282L;
 
    //仓库ID
    private String warehouse;

    //电商公司
    private String shopid;
    
    private String sku;

    //物料，产品ID
    private String material;

    //要操作的数量
    private Integer amount;
    
    //要操作的数量
    private Integer invqty;
    
    //操作来自那个表单
    private  String formid;
    
    //操作来自那个类型的表单，数据来自（t_erp_formtype） 此处直接放入ID
    private  String formtype;
    
    //来自那个number
    private String number;

    private EnumByInventory status;
    
	@TableId(value = "id" )
	@ApiModelProperty(value = "ID")
    String id;
	
	public String getId() {
		if(id==null) {
			id=UUIDUtil.getUUIDshort();
		}
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	public boolean idIsNULL() {
	   return StrUtil.isBlank(id);
	} 
   
	@TableField(value = "operator")
    private String operator;
 
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@TableField(fill = FieldFill.INSERT_UPDATE,value = "opttime")
    private Date opttime;
    
    public String getInvStatus(Status status){
    	if(status.equals(Status.inbound) ||status.equals(Status.outbound) )
    	return status.getValue()+"_"+formtype;
    	else return status.getValue();
    }
	public String getFormid() {
		return formid;
	}

	public void setFormid(String formid) {
		this.formid = formid;
	}

	public String getFormtype() {
		return formtype;
	}

	public void setFormtype(String formtype) {
		this.formtype = formtype;
	}

	public EnumByInventory getStatus() {
		return status;
	}

	public void setStatus(EnumByInventory status) {
		this.status = status;
	}

	public String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse == null ? null : warehouse.trim();
    }

    public String getShopid() {
        return shopid;
    }

    public void setShopid(String shopid) {
        this.shopid = shopid == null ? null : shopid.trim();
    }
    

    public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material == null ? null : material.trim();
    }

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	
	public InventoryParameter clone() {
		// TODO Auto-generated method stub
		InventoryParameter myclone=new InventoryParameter();
		myclone.setAmount(this.amount);
		myclone.setFormid(this.formid);
		myclone.setFormtype(this.formtype);
		myclone.setId(this.getId());
		myclone.setMaterial(this.material);
		myclone.setNumber(this.number);
		myclone.setOperator(this.getOperator());
		myclone.setOpttime(this.getOpttime());
		myclone.setStatus(this.status);
		myclone.setWarehouse(this.warehouse);
		myclone.setShopid(this.shopid);
		myclone.setInvqty(this.invqty);
		return  myclone;
	}
	/**
	 * @return the invqty
	 */
	public Integer getInvqty() {
		return invqty;
	}
	/**
	 * @param invqty the invqty to set
	 */
	public void setInvqty(Integer invqty) {
		this.invqty = invqty;
	}
	
 
}