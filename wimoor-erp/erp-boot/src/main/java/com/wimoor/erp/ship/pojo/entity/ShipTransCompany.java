package com.wimoor.erp.ship.pojo.entity;

import java.math.BigInteger;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.erp.common.pojo.entity.ErpBaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_erp_ship_transcompany")
public class ShipTransCompany extends ErpBaseEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6128014311128006099L;

	@NotNull(message="名称不能为空")
	@Size(max=100,message="名称不能超过100个字符")
    @TableField(value= "name")
    private String name;
    
	@NotNull(message="简称不能为空")
	@Size(max=100,message="简称不能超过100个字符")
    @TableField(value= "simplename")
    private String simplename;
    
    @TableField(value= "shopid")
    private String shopid;
 
    @TableField(value= "access_token")
    private String accessToken;
    
    @TableField(value= "api")
    private Integer api;
    
    @TableField(value= "uploadpath")
    private String uploadpath;
    
    @TableField(value= "location")
    private BigInteger location;
    
	@TableField(value="isdelete")
	private Boolean isdelete;
    
    @TableField(exist = false)
    List<ShipTransDetail> detaillist;
    
    public Boolean getIsdelete() {
    	if(isdelete==null) {
    		return false;
    	}
		return isdelete;
	}
 
	
 
}