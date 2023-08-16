package com.wimoor.erp.inventory.pojo.entity;

import javax.servlet.ServletContext;

import com.wimoor.erp.common.pojo.entity.EnumByInventory;
import com.wimoor.erp.common.pojo.entity.ErpBaseEntity;
import com.wimoor.erp.common.pojo.entity.Status;
 

public class InventoryParameter  extends ErpBaseEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1188098156847164282L;

	private ServletContext session;
	
    //仓库ID
    private String warehouse;

    //电商公司
    private String shopid;
    
    //物料，产品ID
    private String sku;
    //物料，产品ID
    private String material;

    //要操作的数量
    private Integer amount;
    
    //结余库存
    private Integer invqty;
    
    //操作来自那个表单
    private  String formid;
    
    //操作来自那个类型的表单，数据来自（t_erp_formtype） 此处直接放入ID
    private  String formtype;
    
    //来自那个number
    private String number;

    private EnumByInventory status;
    
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
	
	public ServletContext getSession() {
		return session;
	}
	public void setSession(ServletContext session) {
		this.session = session;
	}
	
	public InventoryParameter clone() {
		// TODO Auto-generated method stub
		InventoryParameter myclone=new InventoryParameter();
		myclone.setAmount(this.amount);
		myclone.setFormid(this.formid);
		myclone.setFormtype(this.formtype);
		myclone.setId(this.getId());
		myclone.setSession(this.session);
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
	
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
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